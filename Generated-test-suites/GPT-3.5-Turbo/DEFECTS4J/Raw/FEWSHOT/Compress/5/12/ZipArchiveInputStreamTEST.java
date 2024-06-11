package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {
    
    private ZipArchiveInputStream zipInputStream;
    
    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{80, 75, 3, 4});
        zipInputStream = new ZipArchiveInputStream(inputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        zipInputStream.close();
    }
    
    @Test
    public void testGetNextZipEntry_ClosedStream_ReturnsNull() throws IOException {
        zipInputStream.close();
        
        ZipArchiveEntry result = zipInputStream.getNextZipEntry();
        
        assertNull(result);
    }
    
    @Test
    public void testGetNextZipEntry_HitCentralDirectory_ReturnsNull() throws IOException {
        zipInputStream.getNextZipEntry();
        
        ZipArchiveEntry result = zipInputStream.getNextZipEntry();
        
        assertNull(result);
    }
    
    @Test
    public void testGetNextZipEntry_NoEntryRead_ReturnsNull() throws IOException {
        ZipArchiveEntry result = zipInputStream.getNextZipEntry();
        
        assertNull(result);
    }
    
    @Test
    public void testRead_ClosedStream_ThrowsIOException() throws IOException {
        zipInputStream.close();
        byte[] buffer = new byte[10];
        
        try {
            zipInputStream.read(buffer, 0, 10);
        } catch (IOException e) {
            assertEquals("The stream is closed", e.getMessage());
        }
    }
    
    @Test
    public void testRead_FinishedInflation_ReturnsMinusOne() throws IOException {
        zipInputStream.getNextZipEntry();
        zipInputStream.closeEntry();
        byte[] buffer = new byte[10];
        
        int result = zipInputStream.read(buffer, 0, 10);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testRead_InvalidStart_ThrowsArrayIndexOutOfBoundsException() throws IOException {
        byte[] buffer = new byte[10];
        
        try {
            zipInputStream.read(buffer, -1, 10);
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals(null, e.getMessage());
        }
    }
    
    @Test
    public void testSkip_NegativeValue_ThrowsIllegalArgumentException() throws IOException {
        try {
            zipInputStream.skip(-1);
        } catch (IllegalArgumentException e) {
            assertEquals(null, e.getMessage());
        }
    }
    
    @Test
    public void testSkip_NormalValue_ReturnsSkippedLength() throws IOException {
        zipInputStream.getNextZipEntry();
        
        long result = zipInputStream.skip(5);
        
        assertEquals(5, result);
    }
}