import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {
    
    private ArArchiveInputStream arInputStream;
    private InputStream mockInputStream;
    
    @Before
    public void setUp() {
        mockInputStream = mock(InputStream.class);
        arInputStream = new ArArchiveInputStream(mockInputStream);
    }

    @Test
    public void testGetNextArEntry_WhenOffsetIsZero() throws IOException {
        String expectedHeader = "!<arch>\n";
        byte[] headerBytes = expectedHeader.getBytes();
        when(mockInputStream.read(any(byte[].class))).thenReturn(headerBytes.length);
        when(mockInputStream.read(any(byte[].class))).thenReturn(-1);
        
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        
        assertNotNull(entry);
        assertEquals(expectedHeader, new String(entry.getName()));
        assertEquals(0, entry.getSize());
    }
    
    @Test(expected = IOException.class)
    public void testGetNextArEntry_WhenHeaderReadFails() throws IOException {
        byte[] headerBytes = new byte[7];
        when(mockInputStream.read(any(byte[].class))).thenReturn(headerBytes.length);
        
        arInputStream.getNextArEntry();
    }
    
    @Test(expected = IOException.class)
    public void testGetNextArEntry_WhenInvalidHeader() throws IOException {
        String invalidHeader = "!<arc>\n";
        byte[] invalidHeaderBytes = invalidHeader.getBytes();
        when(mockInputStream.read(any(byte[].class))).thenReturn(invalidHeaderBytes.length);
        
        arInputStream.getNextArEntry();
    }
    
    @Test
    public void testGetNextArEntry_WhenInputIsEOF() throws IOException {
        when(mockInputStream.available()).thenReturn(0);
        
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        
        assertNull(entry);
    }
    
    @Test
    public void testClose() throws IOException {
        arInputStream.close();
        
        verify(mockInputStream).close();
    }
    
    @Test
    public void testRead() throws IOException {
        int expectedRet = 65;
        when(mockInputStream.read()).thenReturn(expectedRet);
        
        int ret = arInputStream.read();
        
        assertEquals(expectedRet, ret);
        verify(mockInputStream).read();
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] expectedBytes = new byte[] { 65, 66, 67 };
        when(mockInputStream.read(any(byte[].class))).thenReturn(expectedBytes.length);
        byte[] b = new byte[expectedBytes.length];
        
        int ret = arInputStream.read(b);
        
        assertEquals(expectedBytes.length, ret);
        assertArrayEquals(expectedBytes, b);
        verify(mockInputStream).read(any(byte[].class));
    }
    
    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] expectedBytes = new byte[] { 65, 66, 67 };
        int offset = 1;
        int length = 2;
        ByteArrayInputStream bytearrayInputStream = new ByteArrayInputStream(expectedBytes);
        when(mockInputStream.read(any(byte[].class), eq(offset), eq(length))).thenReturn(length);
        byte[] b = new byte[length];
        
        int ret = arInputStream.read(b, offset, length);
        
        assertEquals(length, ret);
        assertArrayEquals(expectedBytes, b);
        verify(mockInputStream).read(any(byte[].class), eq(offset), eq(length));
    }
    
    @Test
    public void testMatches_WhenSignatureLengthIsLessThan8() {
        byte[] signature = new byte[] { 0x21, 0x3c };
        int length = signature.length;
        
        boolean result = ArArchiveInputStream.matches(signature, length);
        
        assertFalse(result);
    }
    
    @Test
    public void testMatches_WhenSignatureIsNotValid() {
        byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x69, 0x63, 0x68, 0x3e, 0x0a };
        int length = signature.length;
        
        boolean result = ArArchiveInputStream.matches(signature, length);
        
        assertFalse(result);
    }
    
    @Test
    public void testMatches_WhenSignatureIsValid() {
        byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        int length = signature.length;
        
        boolean result = ArArchiveInputStream.matches(signature, length);
        
        assertTrue(result);
    }
}