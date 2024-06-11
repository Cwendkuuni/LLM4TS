import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.test.TestArArchiveEntry;
import org.apache.commons.compress.archivers.test.TestArInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Test for ArArchiveInputStream class.
 */
public class ArArchiveInputStreamTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{-65, 0, 1, 0, 3, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        Assert.assertNotNull(test);
    }

    /**
     * Test getNextArEntry method with valid input.
     */
    @Test
    public void testGetNextArEntryNormal() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        ArchiveEntry entry = test.getNextArEntry();
        Assert.assertNotNull(entry);
        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        Assert.assertEquals("entry1.txt", arEntry.getName());
        Assert.assertEquals(5L, arEntry.getSize());
    }

    /**
     * Test getNextArEntry method with invalid input (EOF).
     */
    @Test(expected = IOException.class)
    public void testGetNextArEntryEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        test.getNextArEntry();
    }

    /**
     * Test getNextArEntry method with invalid input (header mismatch).
     */
    @Test(expected = IOException.class)
    public void testGetNextArEntryInvalidHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        test.getNextArEntry();
    }

    /**
     * Test getNextArEntry method with invalid input (incomplete header).
     */
    @Test(expected = IOException.class)
    public void testGetNextArEntryIncompleteHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        test.read(new byte[1]);
        test.getNextArEntry();
    }

    /**
     * Test getNextEntry method with valid input.
     */
    @Test
    public void testGetNextEntryNormal() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        ArchiveEntry entry = test.getNextEntry();
        Assert.assertNotNull(entry);
        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        Assert.assertEquals("entry1.txt", arEntry.getName());
        Assert.assertEquals(5L, arEntry.getSize());
    }

    /**
     * Test getNextEntry method with invalid input (EOF).
     */
    @Test
    public void testGetNextEntryEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        test.getNextEntry();
        test.close();
        ArchiveEntry entry = test.getNextEntry();
        Assert.assertNull(entry);
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        test.close();
        ArchiveEntry entry = test.getNextEntry();
        Assert.assertNull(entry);
    }

    /**
     * Test read method with valid input.
     */
    @Test
    public void testReadNormal() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = test.read(buffer, 0, buffer.length)) > 0) {
            output.write(buffer, 0, read);
        }
        input.close();
        test.close();
        byte[] expected = new TestArInputStream().getBytes("test.ar");
        byte[] realized = output.toByteArray();
        Assert.assertArrayEquals(expected, realized);
    }

    /**
     * Test read method with invalid input (EOF).
     */
    @Test
    public void testReadEOF() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = test.read(buffer, 0, buffer.length)) > 0) {
            output.write(buffer, 0, read);
        }
        test.read(buffer, 0, buffer.length);
        input.close();
        test.close();
        Assert.assertEquals(-1, read);
    }

    /**
     * Test read method with invalid input (compressed data).
     */
    @Test
    public void testReadCompressed() throws IOException {
        InputStream input = new GZIPInputStream(new FileInputStream("test.gz"));
        ArArchiveInputStream test = new ArArchiveInputStream(input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = test.read(buffer, 0, buffer.length)) > 0) {
            output.write(buffer, 0, read);
        }
        input.close();
        test.close();
        byte[] expected = new TestArInputStream().getBytes("test.ar");
        byte[] realized = output.toByteArray();
        Assert.assertArrayEquals(expected, realized);
    }

    /**
     * Test matches method.
     */
    @Test
    public void testMatches() {
        byte[] signature = new byte[]{3, 0xC2, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0A, 0x3E};
        Assert.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }
}