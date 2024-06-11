import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testGetNextEntry() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(bos);
        zipOut.putNextEntry(new ZipEntry("test.txt"), new byte[0]);
        zipOut.finish();
        zipOut.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ZipArchiveInputStream zipIn = new ZipArchiveInputStream(bis);

        ZipArchiveEntry entry = zipIn.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());

        zipIn.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryAfterClosed() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(bos);
        zipOut.putNextEntry(new ZipEntry("test.txt"), new byte[0]);
        zipOut.finish();
        zipOut.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ZipArchiveInputStream zipIn = new ZipArchiveInputStream(bis);

        zipIn.getNextZipEntry();
        zipIn.close();

        zipIn.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(bos);
        zipOut.putNextEntry(new ZipEntry("test.txt"), "Test content".getBytes());
        zipOut.finish();
        zipOut.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ZipArchiveInputStream zipIn = new ZipArchiveInputStream(bis);
        ZipArchiveEntry entry = zipIn.getNextZipEntry();
        Assert.assertNotNull(entry);

        byte[] buffer = new byte[10];
        int read = zipIn.read(buffer, 0, buffer.length);
        Assert.assertEquals(read, 10);
        Assert.assertArrayEquals("Test content".getBytes(), buffer);

        read = zipIn.read(buffer, 0, buffer.length);
        Assert.assertEquals(read, -1);

        zipIn.close();
    }

    @Test(expected = IOException.class)
    public void testReadAfterClosed() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(bos);
        zipOut.putNextEntry(new ZipEntry("test.txt"), new byte[0]);
        zipOut.finish();
        zipOut.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ZipArchiveInputStream zipIn = new ZipArchiveInputStream(bis);
        ZipArchiveEntry entry = zipIn.getNextZipEntry();
        Assert.assertNotNull(entry);

        byte[] buffer = new byte[10];
        zipIn.read(buffer, 0, buffer.length);

        zipIn.close();

        zipIn.read(buffer, 0, buffer.length);
    }

    @Test(expected = IOException.class)
    public void testReadInvalidSignature() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write("Invalid signature".getBytes());
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        ZipArchiveInputStream zipIn = new ZipArchiveInputStream(bis);
        zipIn.getNextZipEntry();

        byte[] buffer = new byte[10];
        zipIn.read(buffer, 0, buffer.length);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(bos);
        zipOut.putNextEntry(new ZipEntry("test.txt"), "Test content".getBytes());
        zipOut.finish();
        zipOut.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ZipArchiveInputStream zipIn = new ZipArchiveInputStream(bis);
        ZipArchiveEntry entry = zipIn.getNextZipEntry();
        Assert.assertNotNull(entry);

        long size = entry.getSize();
        zipIn.skip(size);
        Assert.assertEquals(-1, zipIn.read());

        zipIn.close();
    }

    @Test(expected = IOException.class)
    public void testSkipAfterClosed() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(bos);
        zipOut.putNextEntry(new ZipEntry("test.txt"), new byte[0]);
        zipOut.finish();
        zipOut.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ZipArchiveInputStream zipIn = new ZipArchiveInputStream(bis);
        ZipArchiveEntry entry = zipIn.getNextZipEntry();
        Assert.assertNotNull(entry);

        zipIn.skip(entry.getSize());
        zipIn.close();

        zipIn.read();
    }
}