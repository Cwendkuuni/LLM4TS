package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipIn;
    private ZipInputStream zipInputStream;
    private InputStream input;
    private byte[] buffer;

    @Before
    public void setUp() {
        // Mock input data
        buffer = new byte[1024];
        input = new ByteArrayInputStream(buffer);
    }

    @After
    public void tearDown() throws IOException {
        if (zipInputStream != null) {
            zipInputStream.close();
        }
        if (zipIn != null) {
            zipIn.close();
        }
        if (input != null) {
            input.close();
        }
    }

    @Test
    public void testGetNextZipEntry_whenFileIsNotEmpty_returnEntry() throws IOException {
        zipInputStream = new ZipInputStream(input);
        zipIn = new ZipArchiveInputStream(zipInputStream);
        ZipEntry expectedEntry = new ZipEntry("example.txt");
        zipInputStream.putNextEntry(expectedEntry);

        ZipArchiveEntry zipEntry = zipIn.getNextZipEntry();

        assertEquals(expectedEntry.getName(), zipEntry.getName());
    }

    @Test
    public void testGetNextZipEntry_whenFileIsEmpty_returnNull() throws IOException {
        zipInputStream = new ZipInputStream(input);
        zipIn = new ZipArchiveInputStream(zipInputStream);

        ZipArchiveEntry zipEntry = zipIn.getNextZipEntry();

        assertNull(zipEntry);
    }

    @Test
    public void testGetNextZipEntry_whenCentralDirectoryHit_returnNull() throws IOException {
        zipInputStream = new ZipInputStream(input);
        zipIn = new ZipArchiveInputStream(zipInputStream);
        zipIn.getNextZipEntry();

        ZipArchiveEntry zipEntry = zipIn.getNextZipEntry();

        assertNull(zipEntry);
    }

    @Test
    public void testRead_whenCompressedMethodIsStored_returnBytes() throws IOException {
        zipInputStream = new ZipInputStream(input);
        zipIn = new ZipArchiveInputStream(zipInputStream);
        ZipEntry entry = new ZipEntry("example.txt");
        entry.setMethod(ZipEntry.STORED);
        entry.setSize(10);
        byte[] expectedBytes = "test data".getBytes();
        entry.setCrc(computeCRC(expectedBytes));
        zipInputStream.putNextEntry(entry);
        zipInputStream.write(expectedBytes);

        byte[] actualBytes = new byte[10];
        int bytesRead = zipIn.read(actualBytes, 0, actualBytes.length);

        assertEquals(actualBytes.length, bytesRead);
        assertEquals(new String(expectedBytes), new String(actualBytes));
    }

    @Test
    public void testRead_whenCompressedMethodIsDeflated_returnBytes() throws IOException {
        zipInputStream = new ZipInputStream(input);
        zipIn = new ZipArchiveInputStream(zipInputStream);
        ZipEntry entry = new ZipEntry("example.txt");
        entry.setMethod(ZipEntry.DEFLATED);
        entry.setSize(10);
        byte[] expectedBytes = "test data".getBytes();
        zipInputStream.putNextEntry(entry);
        zipInputStream.write(expectedBytes);

        byte[] actualBytes = new byte[10];
        int bytesRead = zipIn.read(actualBytes, 0, actualBytes.length);

        assertEquals(actualBytes.length, bytesRead);
        assertEquals(new String(expectedBytes), new String(actualBytes));
    }

    @Test(expected = IOException.class)
    public void testRead_whenStreamIsClosed_throwIOException() throws IOException {
        zipInputStream = new ZipInputStream(input);
        zipIn = new ZipArchiveInputStream(zipInputStream);
        zipInputStream.close();

        zipIn.read(new byte[10], 0, 10);
    }

    @Test
    public void testClose_whenStreamIsClosed_return() throws IOException {
        zipInputStream = new ZipInputStream(input);
        zipIn = new ZipArchiveInputStream(zipInputStream);

        zipIn.close();
    }

    @Test
    public void testMatches_whenSignatureMatches_returnTrue() {
        byte[] signature = {0x50, 0x4B, 0x03, 0x04};

        boolean result = ZipArchiveInputStream.matches(signature, 4);

        assertTrue(result);
    }

    @Test
    public void testMatches_whenSignatureDoesNotMatch_returnFalse() {
        byte[] signature = {0x50, 0x4B, 0x03, 0x05};

        boolean result = ZipArchiveInputStream.matches(signature, 4);

        assertFalse(result);
    }

    @Test
    public void testSkip_whenValueIsPositive_skipBytes() throws IOException {
        zipInputStream = new ZipInputStream(input);
        zipIn = new ZipArchiveInputStream(zipInputStream);

        long skippedBytes = zipIn.skip(10);

        assertEquals(10, skippedBytes);
    }

    @Test
    public void testSkip_whenValueIsZero_returnZero() throws IOException {
        zipInputStream = new ZipInputStream(input);
        zipIn = new ZipArchiveInputStream(zipInputStream);

        long skippedBytes = zipIn.skip(0);

        assertEquals(0, skippedBytes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_whenValueIsNegative_throwIllegalArgumentException() throws IOException {
        zipInputStream = new ZipInputStream(input);
        zipIn = new ZipArchiveInputStream(zipInputStream);

        zipIn.skip(-1);
    }

    private long computeCRC(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }
}