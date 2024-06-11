
package org.apache.commons.compress.archivers.zip;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipException;

import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInput;

    @Before
    public void setUp() {
        byte[] zipData = new byte[]{0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x50, 0x4B, 0x01, 0x02};
        ByteArrayInputStream in = new ByteArrayInputStream(zipData);
        zipInput = new ZipArchiveInputStream(in);
    }

    @After
    public void tearDown() throws IOException {
        zipInput.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipInput.getNextZipEntry();
        assertNotNull(entry);
        assertNull(zipInput.getNextZipEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = zipInput.getNextEntry();
        assertNotNull(entry);
        assertNull(zipInput.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        ZipArchiveEntry entry = zipInput.getNextZipEntry();
        assertNotNull(entry);
        byte[] buffer = new byte[1024];
        int bytesRead = zipInput.read(buffer, 0, buffer.length);
        assertEquals(0, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        zipInput.close();
        zipInput.getNextZipEntry();
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = zipInput.skip(10);
        assertEquals(10, skipped);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeValue() throws IOException {
        zipInput.skip(-1);
    }

    @Test
    public void testMatches() {
        byte[] sig = new byte[]{0x50, 0x4B, 0x03, 0x04};
        assertTrue(ZipArchiveInputStream.matches(sig, sig.length));
    }
}

