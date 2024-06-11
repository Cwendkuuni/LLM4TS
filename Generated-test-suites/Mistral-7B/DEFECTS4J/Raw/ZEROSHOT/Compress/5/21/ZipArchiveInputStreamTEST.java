package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.InflaterCompressor;
import org.apache.commons.compress.utils.IoUtils;
import org.apache.commons.compress.utils.ZipEncoding;
import org.apache.commons.compress.utils.ZipEncodingHelper;
import org.apache.commons.compress.utils.ZipLong;
import org.apache.commons.compress.utils.ZipShort;

public class ZipArchiveInputStreamTest {

    private static final int BUFFER_SIZE = ZipArchiveOutputStream.BUFFER_SIZE;

    @org.junit.Test
    public void testZipArchiveInputStream() throws IOException, ArchiveException {
        byte[] zipData = createTestZipData();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);) {

            ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            assertNotNull("Expected first entry", entry);
            assertEquals("Unexpected entry name", "entry1.txt", entry.getName());

            byte[] buffer = new byte[1024];
            int len;
            while ((len = zipArchiveInputStream.read(buffer)) > 0) {
                System.out.write(buffer, 0, len);
            }

            assertEquals("Unexpected number of bytes read", BUFFER_SIZE, zipArchiveInputStream.read(buffer));
            assertTrue("Expected end of stream", zipArchiveInputStream.read() == -1);

            assertEquals("Unexpected CRC32 checksum", new CRC32().getValue(zipData), zipArchiveInputStream.getCrc());
            assertFalse("Expected no more entries", zipArchiveInputStream.getNextZipEntry() != null);
            zipArchiveInputStream.close();

            // Test reusability
            zipArchiveInputStream.reset();
            entry = zipArchiveInputStream.getNextZipEntry();
            assertNotNull("Expected first entry after reset", entry);
            assertEquals("Unexpected entry name after reset", "entry1.txt", entry.getName());
            zipArchiveInputStream.close();
        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testZipArchiveInputStreamWithNoEntries() throws IOException {
        byte[] zipData = createTestZipDataWithoutEntry();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);) {
            zipArchiveInputStream.getNextZipEntry();
        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testZipArchiveInputStreamWithInvalidSignature() throws IOException {
        byte[] zipData = createTestZipDataWithInvalidSignature();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream)) {
            zipArchiveInputStream.getNextZipEntry();
        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testZipArchiveInputStreamWithUnexpectedEOF() throws IOException {
        byte[] zipData = createTestZipDataWithUnexpectedEOF();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);) {
            zipArchiveInputStream.getNextZipEntry();
            zipArchiveInputStream.read(new byte[1]);
            zipArchiveInputStream.read(); // should throw an exception
        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testZipArchiveInputStreamWithMissingDataDescriptor() throws IOException, ArchiveException {
        byte[] zipData = createTestZipDataWithoutDataDescriptor();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);) {
            ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
            assertNotNull("Expected first entry", entry);
            assertEquals("Unexpected entry name", "entry1.txt", entry.getName());
            zipArchiveInputStream.getNextZipEntry(); // should throw an exception
        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testZipArchiveInputStreamWithInvalidCentralDirectory() throws IOException {
        byte[] zipData = createTestZipDataWithInvalidCentralDirectory();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);) {
            zipArchiveInputStream.getNextZipEntry(); // should return an entry
            zipArchiveInputStream.getNextZipEntry(); // should throw an exception
        }
    }

    private byte[] createTestZipData() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
             ZipArchiveOutputStream archiveOutputStream = new ZipArchiveOutputStream(zipOutputStream);) {
            archiveOutputStream.putNextEntry("entry1.txt");
            archiveOutputStream.write("Hello, World!".getBytes());
            archiveOutputStream.closeEntry();
            archiveOutputStream.finish();
            zipOutputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] createTestZipDataWithoutEntry() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
             InflaterCompressor compressor = new InflaterCompressor()) {
            zipOutputStream.putNextEntry("entry1.txt");
            compressor.setInput(new ByteArrayInputStream("Hello, World!".getBytes()));
            compressor.finish();
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = compressor.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }
            zipOutputStream.closeEntry();
            zipOutputStream.finish();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] createTestZipDataWithInvalidSignature() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);) {
            zipOutputStream.putNextEntry("entry1.txt");
            zipOutputStream.write("Hello, World!".getBytes());
            zipOutputStream.closeEntry();
            zipOutputStream.write("INVALID".getBytes()); // write an invalid signature
            zipOutputStream.finish();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] createTestZipDataWithUnexpectedEOF() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);) {
            zipOutputStream.putNextEntry("entry1.txt");
            zipOutputStream.write("Hello, World!".getBytes());
            zipOutputStream.closeEntry();
            zipOutputStream.write("INVALID".getBytes()); // write an invalid signature
            zipOutputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] createTestZipDataWithoutDataDescriptor() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
             ZipArchiveOutputStream archiveOutputStream = new ZipArchiveOutputStream(zipOutputStream);) {
            archiveOutputStream.putNextEntry("entry1.txt");
            archiveOutputStream.write("Hello, World!".getBytes());
            archiveOutputStream.closeEntry();
            archiveOutputStream.setMethod(ZipArchiveOutputStream.STORED);
            archiveOutputStream.finish();
            zipOutputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] createTestZipDataWithInvalidCentralDirectory() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);) {
            ZipArchiveOutputStream archiveOutputStream = new ZipArchiveOutputStream(zipOutputStream);
            archiveOutputStream.putNextEntry("entry1.txt");
            archiveOutputStream.write("Hello, World!".getBytes());
            archiveOutputStream.closeEntry();
            archiveOutputStream.setMethod(ZipArchiveOutputStream.DEFLATED);
            archiveOutputStream.finish();
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = zipOutputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }
            zipOutputStream.putNextEntry("entry2.txt");
            archiveOutputStream.closeEntry();
            zipOutputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}