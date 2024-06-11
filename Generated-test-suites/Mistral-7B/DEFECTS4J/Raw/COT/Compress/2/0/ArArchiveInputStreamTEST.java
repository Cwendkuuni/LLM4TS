import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] header = ArArchiveInputStream.HEADER.getBytes();
        byte[] trailer = ArArchiveInputStream.TRAILER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] userid = "user".getBytes();
        byte[] groupid = "group".getBytes();
        byte[] filemode = new byte[]{0666};
        byte[] length = new byte[]{0x0A, 0x01, 0x00, 0x03, 0x00, 0x00, 0x00, 0x01};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(header);
        outputStream.write(name);
        outputStream.write(userid);
        outputStream.write(groupid);
        outputStream.write(filemode);
        outputStream.write(length);
        outputStream.write(trailer);

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (arArchiveInputStream != null) {
            arArchiveInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals(3L, entry.getSize());
    }

    @Test
    public void testGetNextArEntry_whenEndOfStreamReached_shouldReturnNull() throws IOException {
        ArArchiveEntry entry = null;
        while ((entry = arArchiveInputStream.getNextArEntry()) != null) {
            // do nothing
        }
        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_whenInvalidHeader_shouldThrowIOException() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_whenInvalidTrailer_shouldThrowIOException() throws IOException {
        byte[] header = ArArchiveInputStream.HEADER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] userid = "user".getBytes();
        byte[] groupid = "group".getBytes();
        byte[] filemode = new byte[]{0666};
        byte[] length = new byte[]{0x0A, 0x01, 0x00, 0x03, 0x00, 0x00, 0x00, 0x01, 0x01}; // invalid trailer

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(header);
        outputStream.write(name);
        outputStream.write(userid);
        outputStream.write(groupid);
        outputStream.write(filemode);
        outputStream.write(length);

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arArchiveInputStream.getNextEntry();
        assertEquals(ArArchiveEntry.class, entry.getClass());
        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        assertEquals("test.txt", arEntry.getName());
        assertEquals(3L, arEntry.getSize());
    }

    @Test
    public void testGetNextEntry_whenEndOfStreamReached_shouldReturnNull() throws IOException {
        ArchiveEntry entry = null;
        while ((entry = arArchiveInputStream.getNextEntry()) != null) {
            // do nothing
        }
        assertNull(arArchiveInputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_whenInvalidHeader_shouldThrowIOException() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_whenInvalidTrailer_shouldThrowIOException() throws IOException {
        byte[] header = ArArchiveInputStream.HEADER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] userid = "user".getBytes();
        byte[] groupid = "group".getBytes();
        byte[] filemode = new byte[]{0666};
        byte[] length = new byte[]{0x0A, 0x01, 0x00, 0x03, 0x00, 0x00, 0x00, 0x01, 0x01}; // invalid trailer

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(header);
        outputStream.write(name);
        outputStream.write(userid);
        outputStream.write(groupid);
        outputStream.write(filemode);
        outputStream.write(length);

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextEntry();
    }

    @Test
    public void testClose() throws IOException {
        arArchiveInputStream.close();
        assertNull(arArchiveInputStream.getNextArEntry());
        assertNull(arArchiveInputStream.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        int read = arArchiveInputStream.read();
        assertEquals(-1, read);
    }

    @Test
    public void testRead_whenDataAvailable_shouldReturnData() throws IOException {
        byte[] buffer = new byte[3];
        int read = arArchiveInputStream.read(buffer);
        assertEquals(3, read);
        for (int i = 0; i < 3; i++) {
            assertEquals(buffer[i], (byte) 't');
        }
    }

    @Test
    public void testRead_whenNoDataAvailable_shouldReturnMinusOne() throws IOException {
        arArchiveInputStream.getNextArEntry(); // consume an entry
        int read = arArchiveInputStream.read();
        assertEquals(-1, read);
    }

    @Test
    public void testRead_whenMultipleBytesAvailable_shouldReadAllBytes() throws IOException {
        byte[] buffer = new byte[5];
        int read = arArchiveInputStream.read(buffer);
        assertEquals(3, read);
        for (int i = 0; i < 3; i++) {
            assertEquals(buffer[i], (byte) 't');
        }

        read = arArchiveInputStream.read(buffer, 3, 2);
        assertEquals(2, read);
        assertEquals(buffer[5], (byte) 'e');
        assertEquals(buffer[6], (byte) 's');
    }

    @Test(expected = IOException.class)
    public void testRead_whenStreamClosed_shouldThrowIOException() throws IOException {
        arArchiveInputStream.close();
        arArchiveInputStream.read();
    }

    @Test
    public void testMatches() throws IOException {
        byte[] signature = new byte[8];
        assertTrue(ArArchiveInputStream.matches(signature, 8));

        signature[1] = (byte) 0x61;
        assertFalse(ArArchiveInputStream.matches(signature, 8));
    }
}