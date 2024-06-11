package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;
    
    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        zipInputStream = new ZipArchiveInputStream(inputStream);
    }
    
    @Test
    public void testGetNextZipEntry() throws IOException {
        assertNull(zipInputStream.getNextZipEntry());
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        assertNull(zipInputStream.getNextEntry());
    }
    
    @Test
    public void testRead() throws IOException {
        assertEquals(-1, zipInputStream.read());
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[10];
        assertEquals(-1, zipInputStream.read(buffer));
    }
    
    @Test
    public void testReadWithBufferAndOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        assertEquals(-1, zipInputStream.read(buffer, 0, 5));
    }
    
    @Test
    public void testSkip() throws IOException {
        assertEquals(0, zipInputStream.skip(10));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeValueThrowsException() throws IOException {
       zipInputStream.skip(-10);
    }
    
    @Test
    public void testMatchesWithShorterLength() {
        byte[] signature = { 0x50, 0x4B };
        assertFalse(ZipArchiveInputStream.matches(signature, 1));
    }
    
    @Test
    public void testMatchesWithNormalFileSignature() {
        byte[] signature = { 0x50, 0x4B, 0x03, 0x04 };
        assertTrue(ZipArchiveInputStream.matches(signature, 4));
    }
    
    @Test
    public void testMatchesWithEmptyZipSignature() {
        byte[] signature = { 0x50, 0x4B, 0x05, 0x06 };
        assertTrue(ZipArchiveInputStream.matches(signature, 4));
    }
    
    @Test
    public void testMatchesWithDifferentSignature() {
        byte[] signature = { 0x50, 0x4B, 0x03, 0x05 };
        assertFalse(ZipArchiveInputStream.matches(signature, 4));
    }
    
    @Test
    public void testClose() throws IOException {
        zipInputStream.close();
        try {
            zipInputStream.getNextZipEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}