package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {
    
    private InputStream input;
    private ArArchiveInputStream arInput;
    
    @Before
    public void setUp() {
        byte[] data = "abcdefghijkl".getBytes();
        input = new ByteArrayInputStream(data);
        arInput = new ArArchiveInputStream(input);
    }
    
    @After
    public void tearDown() throws IOException {
        arInput.close();
    }

    @Test
    public void testGetNextArEntry_withValidData_returnsCorrectEntry() throws IOException {
        ArArchiveEntry entry = arInput.getNextArEntry();
        assertNotNull(entry);
        assertEquals("abcdefg", entry.getName());
        assertEquals(7, entry.getLength());
    }
    
    @Test
    public void testGetNextArEntry_withInvalidData_throwsIOException() {
        // Mock data to simulate invalid header
        byte[] data = "invalid".getBytes();
        input = new ByteArrayInputStream(data);
        arInput = new ArArchiveInputStream(input);

        try {
            arInput.getNextArEntry();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("failed to read header", e.getMessage());
        }
    }
    
    @Test
    public void testGetNextArEntry_withEmptyData_returnsNull() throws IOException {
        // Mock empty data
        byte[] data = new byte[0];
        input = new ByteArrayInputStream(data);
        arInput = new ArArchiveInputStream(input);
        
        ArArchiveEntry entry = arInput.getNextArEntry();
        assertNull(entry);
    }
    
    @Test
    public void testRead_withData_available_returnsCorrectData() throws IOException {
        byte[] data = new byte[5];
        int bytesRead = arInput.read(data);
        
        assertEquals(5, bytesRead);
        assertArrayEquals("abcde".getBytes(), data);
    }
    
    @Test
    public void testRead_withData_unavailable_returnsMinusOne() throws IOException {
        // Consume all data
        arInput.getNextArEntry();
        
        byte[] data = new byte[5];
        int bytesRead = arInput.read(data);
        
        assertEquals(-1, bytesRead);
    }
    
    @Test
    public void testMatches_withValidSignature_returnsTrue() {
        byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }
    
    @Test
    public void testMatches_withInvalidSignature_returnsFalse() {
        byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x00};
        
        assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }
}