
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipArchiveInputStreamTest {
    private ZipArchiveInputStream zipInputStream;
    private File zipFile;

    @Before
    public void setUp() {
        zipFile = new File("test.zip");
        try {
            zipFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry entry = new ZipEntry("test.txt");
            zos.putNextEntry(entry);
            byte[] data = new byte[1024];
            new Random().nextBytes(data);
            zos.write(data, 0, data.length);
            zos.closeEntry();
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream fis;
        try {
            fis = new FileInputStream(zipFile);
            zipInputStream = new ZipArchiveInputStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetNextZipEntry() {
        try {
            ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("test.txt", entry.getName());
            Assert.assertEquals(1024, entry.getSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRead() {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead = zipInputStream.read(buffer, 0, 1024);
            Assert.assertEquals(1024, bytesRead);
            byte[] expectedData = new byte[1024];
            FileInputStream fis = new FileInputStream(zipFile);
            fis.read(expectedData);
            fis.close();
            Assert.assertArrayEquals(expectedData, buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClose() {
        try {
            zipInputStream.close();
            FileInputStream fis = new FileInputStream(zipFile);
            fis.read();
        } catch (IOException e) {
            Assert.assertTrue(e instanceof FileNotFoundException);
        }
    }

    @Test
    public void testSkip() {
        try {
            long bytesSkipped = zipInputStream.skip(512);
            Assert.assertEquals(512, bytesSkipped);
            ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("test.txt", entry.getName());
            Assert.assertEquals(512, entry.getSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMatches() {
        try {
            FileInputStream fis = new FileInputStream(zipFile);
            byte[] signature = new byte[4];
            fis.read(signature);
            boolean matches = ZipArchiveInputStream.matches(signature, 4);
            Assert.assertTrue(matches);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
