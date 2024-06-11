import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ByteArrayInputStream;

/**
 * JUnit test cases for ArArchiveInputStream class
 */
public class ArArchiveInputStreamTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        Assert.assertNotNull(arArchiveInputStream);
    }

    /**
     * Test getNextArEntry method with valid input
     */
    @Test
    public void testGetNextArEntryWithValidInput() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER.getBytes());
        outputStream.write(new byte[16]); // entry name
        outputStream.write(new byte[12]); // last modified
        outputStream.write(new byte[6]); // user id
        outputStream.write(new byte[6]); // group id
        outputStream.write(new byte[8]); // file mode
        outputStream.write(new byte[10]); // entry size
        outputStream.write(ArArchiveEntry.TRAILER.getBytes());
        InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry nextEntry = arArchiveInputStream.getNextArEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals("name", nextEntry.getName().trim(), new String(nextEntry.getName().getData()));
        Assert.assertEquals("size", nextEntry.getSize(), nextEntry.getSizeInBytes());
    }

    /**
     * Test getNextArEntry method with invalid input
     */
    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.getNextArEntry();
    }

    /**
     * Test getNextEntry method
     */
    @Test
    public void testGetNextEntry() throws IOException {
        testGetNextArEntryWithValidInput();
    }

    /**
     * Test close method
     */
    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER.getBytes());
        outputStream.write(new byte[16]); // entry name
        outputStream.write(new byte[12]); // last modified
        outputStream.write(new byte[6]); // user id
        outputStream.write(new byte[6]); // group id
        outputStream.write(new byte[8]); // file mode
        outputStream.write(new byte[10]); // entry size
        outputStream.write(ArArchiveEntry.TRAILER.getBytes());
        InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.close();
    }

    /**
     * Test read method
     */
    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[10]); // data
        InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        int ret = arArchiveInputStream.read();
        Assert.assertEquals("read", ret, outputStream.read());
    }

    /**
     * Test read method with EOF
     */
    @Test
    public void testReadWithEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        int ret = arArchiveInputStream.read();
        Assert.assertEquals("read with EOF", -1, ret);
    }

    /**
     * Test matches method
     */
    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        signature[0] = 0x21;
        signature[1] = 0x3c;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;
        Assert.assertTrue("matches", ArArchiveInputStream.matches(signature, 8));
    }

    /**
     * Test matches method with invalid signature length
     */
    @Test
    public void testMatchesWithInvalidSignatureLength() {
        byte[] signature = new byte[5];
        Assert.assertFalse("matches with invalid signature length", ArArchiveInputStream.matches(signature, 5));
    }
}