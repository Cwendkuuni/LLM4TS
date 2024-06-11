package org.apache.commons.compress.test;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    private static final String TEST_FILE = "test.zip";

    @Test
    public void testReadFromFile() throws IOException {
        try (ZipFile zipFile = new ZipFile(TEST_FILE);
             ZipArchiveInputStream zis = new ZipArchiveInputStream(new FileInputStream(TEST_FILE))) {
            ZipArchiveEntry entry = zis.getNextZipEntry();
            assertNotNull(entry);
            byte[] buffer = new byte[1024];
            int bytesRead = zis.read(buffer);
            assertNotEquals(-1, bytesRead);
            assertEquals(entry.getCrc(), new CRC32().getValue(buffer, 0, bytesRead));
            IOUtils.closeQuietly(zis);
            IOUtils.closeQuietly(zipFile);
        }
    }

    @Test
    public void testReadFromByteArray() throws IOException {
        byte[] zipData = createTestZipFile();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(zipData);
             ZipArchiveInputStream zis = new ZipArchiveInputStream(bis)) {
            ZipArchiveEntry entry = zis.getNextZipEntry();
            assertNotNull(entry);
            byte[] buffer = new byte[1024];
            int bytesRead = zis.read(buffer);
            assertNotEquals(-1, bytesRead);
            assertEquals(entry.getCrc(), new CRC32().getValue(buffer, 0, bytesRead));
            IOUtils.closeQuietly(zis);
        }
    }

    @Test
    public void testReadFromNullInputStream() {
        assertThrows(NullPointerException.class, () -> new ZipArchiveInputStream(null));
    }

    @Test
    public void testReadFromEmptyInputStream() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ZipArchiveInputStream zis = new ZipArchiveInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
            assertNull(zis.getNextZipEntry());
            int bytesRead = zis.read(new byte[1024]);
            assertEquals(-1, bytesRead);
            IOUtils.closeQuietly(zis);
        }
    }

    @Test
    public void testReadFromEOF() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             FileOutputStream fos = new FileOutputStream(TEST_FILE);
             FileChannel fc = fos.getChannel();
             ZipArchiveInputStream zis = new ZipArchiveInputStream(new RandomAccessFile(TEST_FILE, "r"))) {
            writeTestZipFile(bos, fc);
            ZipArchiveEntry entry = zis.getNextZipEntry();
            assertNotNull(entry);
            byte[] buffer = new byte[1024];
            int bytesRead = zis.read(buffer);
            assertNotEquals(-1, bytesRead);
            assertEquals(entry.getCrc(), new CRC32().getValue(buffer, 0, bytesRead));
            // move past the central directory
            for (int i = 0; i < (int) (fc.size() / ZipArchiveEntry.SIZE); i++) {
                zis.getNextZipEntry();
            }
            assertEquals(-1, zis.read(new byte[1024]));
            IOUtils.closeQuietly(zis);
            IOUtils.closeQuietly(fos);
            deleteFile(TEST_FILE);
        }
    }

    @Test
    public void testReadFromInvalidFile() throws IOException {
        try (ZipArchiveInputStream zis = new ZipArchiveInputStream(new FileInputStream("does_not_exist.zip"))) {
            assertNull(zis.getNextZipEntry());
            int bytesRead = zis.read(new byte[1024]);
            assertEquals(-1, bytesRead);
            IOUtils.closeQuietly(zis);
        }
    }

    private byte[] createTestZipFile() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileOutputStream fos = new FileOutputStream(TEST_FILE);
        FileChannel fc = fos.getChannel();
        try (ZipOutputStream zos = new ZipOutputStream(fos)) {
            ZipEntry entry = new ZipEntry("test.txt");
            ZipEntry comment = new ZipEntry("test.txt");
            comment.setComment("This is a test comment");
            zos.putNextEntry(entry);
            zos.putNextEntry(comment);
            zos.write("Hello, World!".getBytes(StandardCharsets.UTF_8), 0, 13);
            zos.closeEntry();
            zos.finish();
            writeTo(bos, zos.getBuffer(), zos.size());
        } finally {
            IOUtils.closeQuietly(fc);
            IOUtils.closeQuietly(fos);
            deleteFile(TEST_FILE);
        }
        return bos.toByteArray();
    }

    private void writeTestZipFile(ByteArrayOutputStream bos, FileChannel fc) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(TEST_FILE))) {
            ZipEntry entry = new ZipEntry("test.txt");
            ZipEntry comment = new ZipEntry("test.txt");
            comment.setComment("This is a test comment");
            zos.putNextEntry(entry);
            zos.putNextEntry(comment);
            zos.write(bos.toByteArray(), 0, bos.size());
            zos.closeEntry();
            zos.finish();
        }
        writeTo(fc, bos.toByteArray(), bos.size());
    }

    private void writeTo(FileChannel fc, byte[] data, long size) throws IOException {
        if (size > 0) {
            fc.write(data, 0, (int) size);
        }
    }

    private void writeTo(ByteArrayOutputStream bos, byte[] data, int size) {
        bos.write(data, 0, size);
    }

    private void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }
}