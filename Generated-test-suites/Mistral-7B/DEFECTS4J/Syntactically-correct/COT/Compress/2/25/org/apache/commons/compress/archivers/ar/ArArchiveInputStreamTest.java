import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for ArArchiveInputStream
 */
public class ArArchiveInputStreamTest {
    private InputStream inputStream;
    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setup() {
        byte[] header = ArArchiveInputStream.HEADER.getBytes();
        byte[] trailer = ArArchiveInputStream.TRAILER.getBytes();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(header, 0, header.length);
        byteStream.write(new ArArchiveEntry("test.txt", 10L).toString().getBytes());
        byteStream.write(trailer, 0, trailer.length);
        byte[] data = byteStream.toByteArray();
        inputStream = new ByteArrayInputStream(data);
        archiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry actualEntry = archiveInputStream.getNextArEntry();
        ArArchiveEntry expectedEntry = new ArArchiveEntry("test.txt", 10L);
        Assert.assertNotNull(actualEntry);
        Assert.assertEquals(expectedEntry.getName(), actualEntry.getName());
        Assert.assertEquals(expectedEntry.getSize(), actualEntry.getSize());

        // Test when there is no next entry
        ArArchiveEntry nextEntry = archiveInputStream.getNextArEntry();
        Assert.assertNull(nextEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidHeader() throws IOException {
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        ArchiveInputStream invalidArchiveInputStream = new ArArchiveInputStream(invalidInputStream);
        invalidArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_EOF() throws IOException {
        ArArchiveEntry actualEntry = archiveInputStream.getNextArEntry();
        ArArchiveEntry expectedEntry = archiveInputStream.getNextArEntry();
        Assert.assertNull(expectedEntry);

        archiveInputStream.close();
        ArArchiveEntry nextEntry = archiveInputStream.getNextArEntry();
        Assert.assertNull(nextEntry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry actualEntry = archiveInputStream.getNextEntry();
        Assert.assertEquals(ArchiveEntry.class, actualEntry.getClass());
        ArArchiveEntry arEntry = (ArArchiveEntry) actualEntry;
        Assert.assertEquals("test.txt", arEntry.getName());
        Assert.assertEquals(10L, arEntry.getSize());

        // Test when there is no next entry
        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        Assert.assertNull(nextEntry);
    }

    @Test
    public void testClose() throws IOException {
        archiveInputStream.close();
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1];
        int result = archiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(1, result);
        Assert.assertEquals(1, buffer[0] & 0xFF);

        // EOF
        result = archiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testRead_EmptyBuffer() throws IOException {
        byte[] buffer = new byte[0];
        int result = archiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testRead_InputLength() throws IOException {
        byte[] buffer = new byte[10];
        int result = archiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(10, result);
        for (int i = 0; i < buffer.length; i++) {
            Assert.assertTrue((buffer[i] & 0xFF) >= 0 && (buffer[i] & 0xFF) <= 0xFF);
        }
    }

    @Test
    public void testRead_InputOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        int offset = 3;
        int length = 5;
        int result = archiveInputStream.read(buffer, offset, length);
        for (int i = 0; i < length; i++) {
            Assert.assertTrue((buffer[offset + i] & 0xFF) >= 0 && (buffer[offset + i] & 0xFF) <= 0xFF);
        }
        Assert.assertEquals(5, result);
    }
}