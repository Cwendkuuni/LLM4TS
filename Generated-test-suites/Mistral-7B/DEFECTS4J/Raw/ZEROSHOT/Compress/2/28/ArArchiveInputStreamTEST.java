import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArArchiveInputStreamTest {

    private InputStream input;
    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] header = Arrays.copyOfRange(ArArchiveEntry.HEADER.getBytes(), 0, 20);
        byte[] name = "test.txt".getBytes();
        byte[] length = Long.toString(10).getBytes();
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(header);
        byteArrayOutputStream.write(name);
        byteArrayOutputStream.write(lastModified);
        byteArrayOutputStream.write(userid);
        byteArrayOutputStream.write(groupid);
        byteArrayOutputStream.write(filemode);
        byteArrayOutputStream.write(length);
        byteArrayOutputStream.write(trailer);
        input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        arInputStream = new ArArchiveInputStream(input);
    }

    @After
    public void tearDown() throws IOException {
        input.close();
        arInputStream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry arArchiveEntry = arInputStream.getNextArEntry();
        assertEquals("test.txt", arArchiveEntry.getName());
        assertEquals(10, arArchiveEntry.getSize());

        arArchiveEntry = arInputStream.getNextArEntry();
        assertNull(arArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryFail() throws IOException {
        arInputStream.getNextArEntry();
        arInputStream.getNextArEntry();
        arInputStream.getNextArEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        arInputStream.read(buffer, 0, buffer.length);
        Arrays.fill(buffer, (byte) 0);
        assertEquals(10, arInputStream.read(buffer, 0, buffer.length));
        for (int i = 0; i < buffer.length; i++) {
            assertEquals(buffer[i], (byte) i);
        }
    }

    @Test
    public void testClose() throws IOException {
        arInputStream.close();
        assertNull(arInputStream.getNextArEntry());
    }
}