import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    private InputStream inputStream;
    private ZipArchiveInputStream zipArchiveInputStream;
    private ZipEncoding zipEncoding;

    @Before
    public void setUp() throws IOException {
        // Prepare test data
        byte[] testData = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4b, (byte) 0x50, // Local File Header Signature
                (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Version Made By
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Flags
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Compression Method
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Last Mod File Time
                (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Last Mod File Date
                (byte) 0x9c, (byte) 0x3f, (byte) 0x64, (byte) 0x65, (byte) 0x66, (byte) 0x74, (byte) 0x20, // CRCSignature
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Compressed Size
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Uncompressed Size
                (byte) 0x00, (byte) 0x00, // File Name Length
                (byte) 0x02, (byte) 0x00, // Extra Field Length
                (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, // Extra Field (for testing)
                0x00};

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(testData);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        this.inputStream = byteArrayInputStream;

        this.zipEncoding = ZipEncodingHelper.UTF8;
        this.zipArchiveInputStream = new ZipArchiveInputStream(this.inputStream);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = this.zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("TestEntry", entry.getName());
        this.zipArchiveInputStream.closeEntry();
        entry = this.zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_IOException() throws IOException {
        this.zipArchiveInputStream.getNextZipEntry();
        this.zipArchiveInputStream.getNextZipEntry();
        this.zipArchiveInputStream.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[5];
        int length = this.zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(testData[0], buffer[0]);
        Assert.assertEquals(testData[1], buffer[1]);
        Assert.assertEquals(testData[2], buffer[2]);
        Assert.assertEquals(testData[3], buffer[3]);
        Assert.assertEquals(testData[4], buffer[4]);
    }

    @Test(expected = IOException.class)
    public void testRead_IOException() throws IOException {
        byte[] buffer = new byte[5];
        this.zipArchiveInputStream.read(buffer, 0, buffer.length);
        this.zipArchiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testClose() throws IOException {
        this.zipArchiveInputStream.close();
        ZipArchiveEntry entry = this.zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testClose_IOException() throws IOException {
        this.zipArchiveInputStream.close();
        this.zipArchiveInputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testSkip() throws IOException {
        this.zipArchiveInputStream.skip(Long.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_IllegalArgumentException() throws IOException {
        this.zipArchiveInputStream.skip(-1);
    }

    @Test
    public void testMatches() throws IOException {
        Assert.assertTrue(ZipArchiveInputStream.matches(testData, testData.length));
    }

    @Test
    public void testCloseEntry() throws IOException {
        ZipArchiveEntry entry = this.zipArchiveInputStream.getNextZipEntry();
        this.zipArchiveInputStream.closeEntry();
        entry = this.zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testCloseEntry_IOException() throws IOException {
        ZipArchiveEntry entry = this.zipArchiveInputStream.getNextZipEntry();
        this.zipArchiveInputStream.closeEntry();
        this.zipArchiveInputStream.closeEntry();
    }

    @Test
    public void testFill() throws IOException {
        this.zipArchiveInputStream.fill();
    }

    @Test(expected = IOException.class)
    public void testFill_IOException() throws IOException {
        this.zipArchiveInputStream.fill();
        this.zipArchiveInputStream.fill();
    }
}