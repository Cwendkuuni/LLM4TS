package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

/**
 * Test class for ZipArchiveInputStream
 */
public class ZipArchiveInputStreamTest {

    private static final String TEST_ZIP_FILE = "test-zip.zip";

    @Test
    public void testMatches() {
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
        try (InputStream in = ZipFile.open(TEST_ZIP_FILE)) {
            in.read(signature);
        }
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testGetNextEntry() throws IOException {
        try (ZipArchiveInputStream zip = new ZipArchiveInputStream(new ByteArrayInputStream(TEST_ZIP_FILE.getBytes()))) {
            ArchiveEntry entry = zip.getNextEntry();
            assertEquals("entry1.txt", entry.getName());
            ArchiveEntry nextEntry = zip.getNextEntry();
            assertEquals("entry2.txt", nextEntry.getName());
            nextEntry = zip.getNextEntry();
            assertNull(nextEntry);
        }
    }

    @Test
    public void testRead() throws IOException {
        try (ZipArchiveInputStream zip = new ZipArchiveInputStream(new ByteArrayInputStream(TEST_ZIP_FILE.getBytes()))) {
            byte[] buffer = new byte[1024];
            int bytesRead = zip.read(buffer, 0, buffer.length);
            assertEquals(10, bytesRead);
            assertArrayEquals("test data".getBytes(), Arrays.copyOfRange(buffer, 0, 10));
        }
    }

    @Test
    public void testCloseEntry() throws IOException {
        try (ZipArchiveInputStream zip = new ZipArchiveInputStream(new ByteArrayInputStream(TEST_ZIP_FILE.getBytes()))) {
            zip.getNextEntry();
            zip.closeEntry();
            zip.getNextEntry();
            zip.closeEntry();
            assertNull(zip.getNextEntry());
        }
    }

    @Test(expected = EOFException.class)
    public void testReadPastEndOfStream() throws IOException {
        try (ZipArchiveInputStream zip = new ZipArchiveInputStream(new ByteArrayInputStream(TEST_ZIP_FILE.getBytes()))) {
            ArchiveEntry entry = zip.getNextEntry();
            zip.getNextEntry(); // Should throw an exception
            zip.read(new byte[1024]);
        }
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedStream() throws IOException {
        try (ZipArchiveInputStream zip = new ZipArchiveInputStream(new ByteArrayInputStream(TEST_ZIP_FILE.getBytes()))) {
            zip.close();
            zip.read(new byte[1024]);
        }
    }

    @Test(expected = ZipException.class)
    public void testInvalidSignature() throws IOException {
        byte[] invalidSignature = new byte[]{"\u0004", "\u0003", "\u004b", "\u0050", "\u0001", "\u0002", "\u0000", "\u0000"}; // Invalid signature
        try (ByteArrayInputStream input = new ByteArrayInputStream(invalidSignature)) {
            new ZipArchiveInputStream(input);
        }
    }
}