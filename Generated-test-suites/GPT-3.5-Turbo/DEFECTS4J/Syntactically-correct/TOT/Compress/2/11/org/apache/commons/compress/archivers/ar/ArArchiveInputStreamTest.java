import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        arInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() {
        try {
            arInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("Test data", entry.getName());
        Assert.assertEquals(9, entry.getSize());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidHeader() throws IOException {
        byte[] invalidHeader = "InvalidHeader".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidHeader);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        arInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextArEntry_NoEntry() throws IOException {
        byte[] emptyData = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(emptyData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInputStream.getNextArEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("Test data", entry.getName());
        Assert.assertEquals(9, entry.getSize());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_InvalidHeader() throws IOException {
        byte[] invalidHeader = "InvalidHeader".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidHeader);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        arInputStream.getNextEntry();
    }

    @Test
    public void testGetNextEntry_NoEntry() throws IOException {
        byte[] emptyData = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(emptyData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry = arInputStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        arInputStream.close();
        // Verify that the input stream is closed
        Assert.assertTrue(arInputStream.isClosed());
        // Try to read from the closed input stream
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test
    public void testRead_SingleByte() throws IOException {
        int byteRead = arInputStream.read();
        Assert.assertEquals(84, byteRead);
    }

    @Test
    public void testRead_ByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer);
        Assert.assertEquals(9, bytesRead);
        Assert.assertEquals("Test data", new String(buffer, StandardCharsets.UTF_8).trim());
    }

    @Test
    public void testRead_ByteArray_Offset() throws IOException {
        byte[] buffer = new byte[20];
        int bytesRead = arInputStream.read(buffer, 5, 10);
        Assert.assertEquals(9, bytesRead);
        Assert.assertEquals(0, buffer[0]);
        Assert.assertEquals(0, buffer[1]);
        Assert.assertEquals(0, buffer[2]);
        Assert.assertEquals(0, buffer[3]);
        Assert.assertEquals(0, buffer[4]);
        Assert.assertEquals(84, buffer[5]);
        Assert.assertEquals(101, buffer[6]);
        Assert.assertEquals(115, buffer[7]);
        Assert.assertEquals(116, buffer[8]);
        Assert.assertEquals(32, buffer[9]);
    }

    @Test
    public void testMatches_ValidSignature() {
        byte[] validSignature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean matches = ArArchiveInputStream.matches(validSignature, 8);
        Assert.assertTrue(matches);
    }

    @Test
    public void testMatches_InvalidSignature() {
        byte[] invalidSignature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};
        boolean matches = ArArchiveInputStream.matches(invalidSignature, 7);
        Assert.assertFalse(matches);
    }

}