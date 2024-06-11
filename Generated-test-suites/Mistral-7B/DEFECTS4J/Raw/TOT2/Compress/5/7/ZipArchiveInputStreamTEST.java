import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

@RunWith(JUnit4.class)
public class ZipArchiveInputStreamTest {

    private InputStream inputStream;
    private ZipArchiveInputStream zipArchiveInputStream;
    private ZipFile zipFile;

    @Before
    public void setUp() throws Exception {
        // Create a test zip file
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipFile zipFile = new ZipFile("test.zip");
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.putNextEntry(new ZipEntry("test.txt"));
        zipOutputStream.write("Test content".getBytes());
        zipOutputStream.closeEntry();
        zipFile.close();
        this.zipFile = zipFile;

        // Create an input stream for the test zip file
        this.inputStream = new FileInputStream("test.zip");
        this.zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextZipEntry() throws Exception {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        zipArchiveInputStream.closeEntry();
        entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryAfterClosingInputStream() throws Exception {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test
    public void testReadWithStoredMethod() throws Exception {
        byte[] buffer = new byte[1024];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertGreaterEquals(bytesRead, 0);
        Assert.assertEquals("Test content".getBytes().length, zipArchiveInputStream.getNextEntry().getSize());
        zipArchiveInputStream.closeEntry();
    }

    @Test
    public void testReadWithDeflatedMethod() throws Exception {
        zipArchiveInputStream.getNextEntry();
        byte[] buffer = new byte[1024];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertGreaterEquals(bytesRead, 0);
        zipArchiveInputStream.closeEntry();

        InputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer2 = new byte[1024];
        int bytesRead2;
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Inflater inflater = new Inflater(true);
        inflater.setInput(buffer, 0, bytesRead);

        while (!inflater.finished()) {
            bytesRead2 = testInputStream.read(buffer2, 0, buffer2.length);
            Assert.assertGreaterEquals(bytesRead2, 0);
            Assert.assertArrayEquals(new byte[]{"Test content".getBytes()[0]}, buffer2, 0, bytesRead2);
            inflater.add(buffer2, 0, bytesRead2);
        }
    }

    @Test(expected = IOException.class)
    public void testReadAfterClosingInputStream() throws Exception {
        zipArchiveInputStream.close();
        byte[] buffer = new byte[1024];
        zipArchiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test(expected = EOFException.class)
    public void testReadWithEmptyZipFile() throws Exception {
        // Create an empty zip file
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipFile zipFile = new ZipFile("test2.zip");
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.putNextEntry(new ZipEntry(""));
        zipOutputStream.closeEntry();
        zipFile.close();
        this.zipFile = zipFile;

        // Create an input stream for the empty test zip file
        this.inputStream = new FileInputStream("test2.zip");
        this.zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[1024];
        zipArchiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test(expected = ZipException.class)
    public void testReadWithInvalidSignature() throws Exception {
        // Create a byte array with an invalid signature
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length + 1];
        System.arraycopy(ZipArchiveOutputStream.LFH_SIG, 0, signature, 0, ZipArchiveOutputStream.LFH_SIG.length);
        signature[ZipArchiveOutputStream.LFH_SIG.length] = (byte) 0x01;

        // Create an input stream with the byte array
        InputStream inputStream = new ByteArrayInputStream(signature);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testReadWithNullBuffer() throws Exception {
        byte[] buffer = null;
        zipArchiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeOffset() throws Exception {
        zipArchiveInputStream.read(-1, 0, 1024);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeLength() throws Exception {
        zipArchiveInputStream.read(buffer, -1, 1024);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeStart() throws Exception {
        zipArchiveInputStream.read(-1, 0, -1024);
    }

    @Test(expected = IOException.class)
    public void testReadWithInvalidEntry() throws Exception {
        ZipArchiveInputStream zipArchiveInputStream2 = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        byte[] buffer = new byte[1024];
        zipArchiveInputStream2.read(buffer, 0, buffer.length);
    }

    @Test
    public void testClose() throws Exception {
        zipArchiveInputStream.close();
        Assert.assertTrue(zipArchiveInputStream.isClosed());
    }

    @Test
    public void testSkip() throws Exception {
        long value = zipArchiveInputStream.getNextEntry().getSize();
        zipArchiveInputStream.skip(value);
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.getNextEntry();
    }

    @After
    public void tearDown() throws Exception {
        zipArchiveInputStream.close();
        this.zipFile.close();
        deleteFile("test.zip");
        deleteFile("test2.zip");
    }

    private void deleteFile(String fileName) {
        if (fileName != null) {
            java.io.File file = new java.io.File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}