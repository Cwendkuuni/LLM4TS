package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ZipArchiveInputStreamTest {
    
    private ZipArchiveInputStream zipArchiveInputStream;
    
    @Before
    public void setUp() throws IOException {
        byte[] fileData = new byte[] { 80, 75, 3, 4, 20, 0, 10, 0, 8, 0, 0, 0, 12, 97, 97, 98, 97, 98, 97, 45, 97, 114, 99, 104, 105, 118, 101, 46, 106, 97, 118, 97, 143, 8 };
        InputStream inputStream = new ByteArrayInputStream(fileData);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        assertNull(zipArchiveInputStream.getNextEntry());
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[4];
        assertEquals(-1, zipArchiveInputStream.read(buffer, 0, 4));
    }
    
    @Test(expected = IOException.class)
    public void testReadWhenClosed() throws IOException {
        zipArchiveInputStream.close();
        byte[] buffer = new byte[4];
        zipArchiveInputStream.read(buffer, 0, 4);
    }
    
    @Test
    public void testSkip() throws IOException {
        assertEquals(0, zipArchiveInputStream.skip(10));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeValue() throws IOException {
        zipArchiveInputStream.skip(-10);
    }
    
    @Test
    public void testMatches() {
        byte[] signature = { 80, 75, 3, 4 };
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }
    
    @Test
    public void testMatchesWithInvalidSignature() {
        byte[] signature = { 80, 74, 3, 4 };
        assertFalse(ZipArchiveInputStream.matches(signature, signature.length));
    }
    
    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextZipEntry();
    }

}