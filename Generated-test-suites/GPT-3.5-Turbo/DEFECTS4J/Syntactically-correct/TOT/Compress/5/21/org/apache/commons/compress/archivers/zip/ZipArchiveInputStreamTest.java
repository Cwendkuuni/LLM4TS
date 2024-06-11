import static org.junit.Assert.*;

import java.io.*;
import java.util.zip.*;

import org.apache.commons.compress.archivers.zip.*;
import org.junit.*;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zais;
    private File zipFile;

    @Before
    public void setUp() throws IOException {
        zipFile = createTestZipFile();
        FileInputStream fis = new FileInputStream(zipFile);
        zais = new ZipArchiveInputStream(fis);
    }

    @After
    public void tearDown() throws IOException {
        zais.close();
        zipFile.delete();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry1 = zais.getNextZipEntry();
        ZipArchiveEntry entry2 = zais.getNextZipEntry();

        assertNotNull(entry1);
        assertNotNull(entry2);
        assertEquals("file1.txt", entry1.getName());
        assertEquals("file2.txt", entry2.getName());
        assertEquals(10, entry1.getSize());
        assertEquals(20, entry2.getSize());
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryAfterClose() throws IOException {
        zais.close();
        zais.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        zais.getNextZipEntry();
        byte[] buffer = new byte[10];
        int bytesRead = zais.read(buffer);

        assertEquals(10, bytesRead);
        assertArrayEquals(new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74 }, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        zais.getNextZipEntry();
        long bytesSkipped = zais.skip(5);

        assertEquals(5, bytesSkipped);

        byte[] buffer = new byte[5];
        int bytesRead = zais.read(buffer);

        assertEquals(5, bytesRead);
        assertArrayEquals(new byte[] { 70, 71, 72, 73, 74 }, buffer);
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(zais.closed);
        zais.close();
        assertTrue(zais.closed);
    }

    @Test(expected = IOException.class)
    public void testReadAfterClose() throws IOException {
        zais.close();
        byte[] buffer = new byte[10];
        zais.read(buffer);
    }

    private File createTestZipFile() throws IOException {
        File zipFile = File.createTempFile("test", ".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));

        ZipEntry entry1 = new ZipEntry("file1.txt");
        zos.putNextEntry(entry1);
        zos.write("abcdefghij".getBytes());
        zos.closeEntry();

        ZipEntry entry2 = new ZipEntry("file2.txt");
        zos.putNextEntry(entry2);
        zos.write("klmnopqrstabcdefghijklmnopqrst".getBytes());
        zos.closeEntry();

        zos.close();

        return zipFile;
    }
}