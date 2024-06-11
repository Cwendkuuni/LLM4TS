import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ByteArrayInputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream testee;
    private ByteArrayOutputStream outputStream;
    private InputStream input;

    @Before
    public void setup() throws IOException {
        byte[] header = new byte[]{"3c21", "7261", "6863", "0a3e"};
        byte[] name = "testFile.txt".getBytes(StandardCharsets.UTF_8);
        byte[] length = new byte[]{"00", "00", "00", "08", "03", "03", "00", "01", "00", "00", "00", "00"};
        byte[] trailer = new byte[]{"3e0a"};

        byte[] fullEntry = new byte[name.length + length.length + header.length + trailer.length];
        System.arraycopy(name, 0, fullEntry, 0, name.length);
        System.arraycopy(length, 0, fullEntry, name.length, length.length);
        System.arraycopy(header, 0, fullEntry, name.length + length.length, header.length);
        System.arraycopy(trailer, 0, fullEntry, name.length + length.length + header.length, trailer.length);

        outputStream = new ByteArrayOutputStream();

        input = new ByteArrayInputStream(fullEntry);
        testee = new ArArchiveInputStream(input);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry arEntry = testee.getNextArEntry();
        assertEquals(new String(ArArchiveEntry.HEADER), new String(testee.readNBytes(16)));
        ArchiveEntry expectedEntry = new ArArchiveEntry("testFile.txt", 8);
        assertEquals(expectedEntry, arEntry);
        assertEquals(8, testee.read(new byte[8]));
        assertEquals(new String(ArArchiveEntry.TRAILER), new String(testee.readNBytes(4)));
        assertEquals(null, testee.getNextArEntry());
    }

    @Test(expected = IOException.class)
    public void testFailToReadHeader() throws IOException {
        when(input.read(Mockito.anyByteArray())).thenReturn(-1);
        testee = new ArArchiveInputStream(input);
        testee.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testInvalidHeader() throws IOException {
        byte[] invalidHeader = new byte[]{"3c21", "7261", "6863", "0a3f"};
        input = new ByteArrayInputStream(invalidHeader);
        testee = new ArArchiveInputStream(input);
        testee.getNextArEntry();
    }

    @Test
    public void testClose() throws IOException {
        testee.close();
        assertEquals(-1, testee.read());
    }

    @Test
    public void testReadByte() throws IOException {
        assertEquals(0x21, testee.read());
        assertEquals(0x3c, testee.read());
        //...
        assertEquals(0x3e, testee.read());
        assertEquals(-1, testee.read());
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] readBytes = new byte[16];
        testee.read(readBytes);
        assertArrayEquals(ArArchiveEntry.HEADER, readBytes);
    }

    @Test
    public void testReadBytesWithOffset() throws IOException {
        byte[] readBytes = new byte[4];
        testee.read(readBytes, 4);
        assertArrayEquals(ArArchiveEntry.TRAILER, readBytes);
    }

    @Test
    public void testMatches() throws IOException {
        byte[] signature = new byte[8];
        assertTrue(ArArchiveInputStream.matches(signature, 8));
        assertFalse(ArArchiveInputStream.matches(signature, 7));
    }

    private byte[] readNBytes(int length) throws IOException {
        byte[] result = new byte[length];
        testee.read(result);
        return result;
    }
}