package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    @Test
    public void testBasicUsage() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray())) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getClass(), ZipArchiveEntry.class);
            zis.close();
        }
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getClass(), ZipArchiveEntry.class);
            assertEquals(entry.getName(), "META-INF/MANIFEST.MF");
            zis.close();

            entry = zis.getNextEntry();
            assertEquals(entry.getName(), "test.txt");
            zis.close();
        }
    }

    @Test(expected = IOException.class)
    public void testExceptionWhenStreamClosed() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            zis.close();
            zis.getNextEntry();
        }
    }

    @Test(expected = IOException.class)
    public void testExceptionWhenNoMoreEntries() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            ArchiveEntry entry = zis.getNextEntry();
            zis.getNextEntry();
            zis.close();
        }
    }

    @Test
    public void testRead() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray())) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            ArchiveEntry entry = zis.getNextEntry();
            byte[] buffer = new byte[10];
            int length = zis.read(buffer);
            assertTrue(length > 0);
            zis.close();
        }
    }

    @Test
    public void testSkip() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray())) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            ArchiveEntry entry = zis.getNextEntry();
            long size = entry.getSize();
            zis.skip(size);
            zis.close();
        }
    }

    @Test
    public void testCloseEntry() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray())) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            ArchiveEntry entry = zis.getNextEntry();
            zis.closeEntry();
            zis.getNextEntry();
            zis.closeEntry();
            zis.close();
        }
    }

    @Test
    public void testFill() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray())) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            boolean firstCall = true;
            while (zis.getNextEntry() != null) {
                if (firstCall) {
                    zis.fill();
                    firstCall = false;
                }
            }
            zis.close();
        }
    }

    @Test
    public void testReadFully() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray())) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            byte[] buffer = new byte[5];
            zis.readFully(buffer);
            assertEquals(buffer.length, zis.available());
            zis.close();
        }
    }

    @Test
    public void testMatchesSignature() {
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length + 1];
        System.arraycopy(ZipArchiveOutputStream.LFH_SIG, 0, signature, 0, ZipArchiveOutputStream.LFH_SIG.length);
        signature[ZipArchiveOutputStream.LFH_SIG.length] = 1;
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));

        assertTrue(ZipArchiveInputStream.matches(ZipArchiveOutputStream.LFH_SIG, ZipArchiveOutputStream.LFH_SIG.length));
        assertTrue(ZipArchiveInputStream.matches(ZipArchiveOutputStream.EOCD_SIG, ZipArchiveOutputStream.EOCD_SIG.length));

        assertFalse(ZipArchiveInputStream.matches(new byte[]{1}, 1));
        assertFalse(ZipArchiveInputStream.matches(new byte[]{1, 2}, 2));
        assertFalse(ZipArchiveInputStream.matches(new byte[]{3, 4, 5}, 3));
    }

    @Test
    public void testGetNextEntryWithUnicodeExtraFields() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_unicode.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, null, false);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getName(), "Test Unicode.txt");
            zis.close();
        }
    }

    @Test
    public void testGetNextEntryWithUTF8Encoding() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_unicode.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, ZipEncoding.UTF8, false);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getName(), "Test Unicode.txt");
            zis.close();
        }
    }

    @Test
    public void testGetNextEntryWithUTF8ExtraFields() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_unicode_extra.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, ZipEncoding.UTF8, true);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getName(), "Test Unicode.txt");
            zis.close();
        }
    }

    @Test
    public void testGetNextEntryWithDefaultEncoding() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_unicode.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, null, true);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getName(), "Test Unicode.txt");
            zis.close();
        }
    }

    @Test
    public void testGetNextEntryWithUTF8ZipEncoding() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_unicode_extra.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, ZipEncodingHelper.UTF8_ZIP_ENCODING, true);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getName(), "Test Unicode.txt");
            zis.close();
        }
    }

    @Test
    public void testGetNextEntryWithPlatformEncoding() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_platform.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, null, true);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getName(), "test.txt");
            zis.close();
        }
    }

    @Test
    public void testGetNextEntryWithUTF8PlatformEncoding() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_platform_unicode.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, ZipEncoding.UTF8, true);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getName(), "test.txt");
            zis.close();
        }
    }

    @Test
    public void testGetNextEntryWithUTF16LEPlatformEncoding() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_platform_unicode_le.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, ZipEncoding.UTF16LE, true);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getName(), "test.txt");
            zis.close();
        }
    }

    @Test
    public void testGetNextEntryWithUTF16BEPlatformEncoding() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_platform_unicode_be.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, ZipEncoding.UTF16BE, true);
            ArchiveEntry entry = zis.getNextEntry();
            assertEquals(entry.getName(), "test.txt");
            zis.close();
        }
    }

    @Test
    public void testHasDataDescriptor() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_data_descriptor.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            boolean hasDataDescriptor = zis.hasDataDescriptor();
            assertTrue(hasDataDescriptor);
            zis.close();
        }
    }

    @Test
    public void testHasDataDescriptorFalse() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input);
            boolean hasDataDescriptor = zis.hasDataDescriptor();
            assertFalse(hasDataDescriptor);
            zis.close();
        }
    }

    @Test
    public void testGetNameAndCommentFromExtraFields() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_unicode_extra.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, ZipEncodingHelper.UTF8, true);
            ArchiveEntry entry = zis.getNextEntry();
            ZipArchiveEntry zipEntry = (ZipArchiveEntry) entry;
            String name = zipEntry.getName();
            String comment = zipEntry.getComment();
            assertEquals(name, "Test Unicode.txt");
            assertEquals(comment, "A test file with a Unicode comment.");
            zis.close();
        }
    }

    @Test
    public void testGetNameAndCommentFromExtraFieldsEmpty() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, ZipEncodingHelper.UTF8, true);
            ArchiveEntry entry = zis.getNextEntry();
            ZipArchiveEntry zipEntry = (ZipArchiveEntry) entry;
            String name = zipEntry.getName();
            String comment = zipEntry.getComment();
            assertEquals(name, "test.txt");
            assertNull(comment);
            zis.close();
        }
    }

    @Test(expected = ZipException.class)
    public void testGetNameAndCommentFromExtraFieldsWithInvalidFormat() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/test_invalid_extra.zip")) {
            ZipArchiveInputStream zis = new ZipArchiveInputStream(input, ZipEncodingHelper.UTF8, true);
            ArchiveEntry entry = zis.getNextEntry();
            zis.getNextEntry();
            zis.close();
        }
    }
}