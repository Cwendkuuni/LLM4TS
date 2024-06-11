import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    // Test constructor
    @Test
    public void testArArchiveInputStreamConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        Assert.assertFalse(arArchiveInputStream.hasNextEntry());
    }

    // Test getNextArEntry()

    @Test
    public void testGetNextArEntry_ValidEntry() throws IOException {
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] length = "10".getBytes();
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        byte[] inputData = new byte[header.length + name.length + length.length + trailer.length];
        System.arraycopy(header, 0, inputData, 0, header.length);
        System.arraycopy(name, 0, inputData, header.length, name.length);
        System.arraycopy(length, 0, inputData, header.length + name.length, length.length);
        System.arraycopy(trailer, 0, inputData, header.length + name.length + length.length, trailer.length);

        InputStream input = new ByteArrayInputStream(inputData);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();

        Assert.assertNotNull(arArchiveEntry);
        Assert.assertEquals("test.txt", arArchiveEntry.getName());
        Assert.assertEquals(10L, arArchiveEntry.getSize());
    }

    @Test
    public void testGetNextArEntry_InvalidHeader() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(ArArchiveEntry.HEADER.getBytes());
        output.write(new byte[]{0x01, 0x02, 0x03});
        output.write(ArArchiveEntry.TRAILER.getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();

        Assert.assertNull(arArchiveEntry);
    }

    @Test
    public void testGetNextArEntry_EndOfStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        ArArchiveEntry arArchiveEntry1 = arArchiveInputStream.getNextArEntry();

        Assert.assertFalse(arArchiveInputStream.hasNextEntry());
        Assert.assertNull(arArchiveEntry1);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_UnexpectedEOF() throws IOException {
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] length = "10".getBytes();
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        byte[] inputData = new byte[header.length + name.length + length.length];
        System.arraycopy(header, 0, inputData, 0, header.length);
        System.arraycopy(name, 0, inputData, header.length, name.length);
        System.arraycopy(length, 0, inputData, header.length + name.length, length.length);

        InputStream input = new ByteArrayInputStream(inputData);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.read(); // consume the first byte of the trailer

        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();

        // This test should throw an IOException
    }

    // Test getNextEntry()
    @Test
    public void testGetNextEntry() throws IOException {
        testGetNextArEntry_ValidEntry();
    }

    // Test close()
    @Test
    public void testClose() throws IOException {
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        arArchiveInputStream.close();
        Assert.assertFalse(arArchiveInputStream.hasNextEntry());
    }

    // Test read()
    @Test
    public void testRead() throws IOException {
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));

        byte[] buffer = new byte[1];
        int read = arArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testRead_ValidData() throws IOException {
        byte[] inputData = "Hello World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);

        byte[] buffer = new byte[5];
        int read = arArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("Hello", new String(buffer));
        read = arArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("World!", new String(buffer));
        Assert.assertEquals(-1, arArchiveInputStream.read(buffer, 0, buffer.length));
    }

    // Test read(byte[], int, int)
    @Test
    public void testRead_ValidData_Length() throws IOException {
        byte[] inputData = "Hello World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);

        byte[] buffer = new byte[10];
        int read = arArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(10, read);
        Assert.assertEquals("Hello World!", new String(buffer));
        Assert.assertEquals(-1, arArchiveInputStream.read(buffer, 0, buffer.length));
    }

    // Test matches()
    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        Assert.assertFalse(ArArchiveInputStream.matches(signature, 0));

        signature[0] = 0x21;
        signature[1] = 0x3c;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;

        Assert.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }
}