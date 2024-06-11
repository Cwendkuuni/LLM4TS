import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {
    
    @Test
    public void testGetNextArEntry() throws IOException {
        // Typical use case
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(new ByteArrayInputStream("headerfile1\ndata1".getBytes()));
        ArArchiveEntry expectedEntry = new ArArchiveEntry("file1", "data1".length());
        ArArchiveEntry actualEntry = arInputStream.getNextArEntry();
        assertEquals(expectedEntry.getName(), actualEntry.getName());
        assertEquals(expectedEntry.getSize(), actualEntry.getSize());
        
        // Edge case - length of entry name is at the maximum limit
        ArArchiveInputStream arInputStream2 = new ArArchiveInputStream(new ByteArrayInputStream("headerentrName1234567890".getBytes()));
        ArArchiveEntry expectedEntry2 = new ArArchiveEntry("entrName12345678", 0);
        ArArchiveEntry actualEntry2 = arInputStream2.getNextArEntry();
        assertEquals(expectedEntry2.getName(), actualEntry2.getName());
        assertEquals(expectedEntry2.getSize(), actualEntry2.getSize());
        
        // Error scenario - invalid header
        ArArchiveInputStream arInputStream3 = new ArArchiveInputStream(new ByteArrayInputStream("invalidheaderfile\ndata3".getBytes()));
        try {
            arInputStream3.getNextArEntry();
            fail("IOException not thrown for invalid header");
        } catch (IOException e) {
            assertEquals("invalid header invalidheaderfile", e.getMessage());
        }
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        // Typical use case
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(new ByteArrayInputStream("headerfile\ndata".getBytes()));
        ArArchiveEntry expectedEntry = new ArArchiveEntry("file", "data".length());
        ArchiveEntry actualEntry = arInputStream.getNextEntry();
        assertEquals(expectedEntry.getName(), actualEntry.getName());
    }
    
    @Test
    public void testClose() throws IOException {
        // Typical use case
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(new ByteArrayInputStream("headerfile\ndata".getBytes()));
        arInputStream.close();
        try {
            arInputStream.getNextArEntry();
            fail("IOException not thrown for closed input stream");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }
    
    @Test
    public void testRead() throws IOException {
        // Typical use case - read single byte
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(new ByteArrayInputStream("headerfile\n".getBytes()));
        int expectedByte = 'h';
        assertEquals(expectedByte, arInputStream.read());
        
        // Typical use case - read array of bytes
        byte[] expectedBytes = "eaderfi".getBytes();
        byte[] actualBytes = new byte[expectedBytes.length];
        arInputStream.read(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
        
        // Edge case - read until end of stream
        int totalExpectedBytes = 11;
        byte[] expectedBytes2 = "headerfile\n".getBytes();
        byte[] actualBytes2 = new byte[totalExpectedBytes];
        int totalBytesRead = arInputStream.read(actualBytes2);
        assertArrayEquals(expectedBytes2, actualBytes2);
        assertEquals(totalExpectedBytes, totalBytesRead);
        
        // Error scenario - read after end of stream
        ArArchiveInputStream arInputStream2 = new ArArchiveInputStream(new ByteArrayInputStream("".getBytes()));
        int expectedByte2 = -1;
        assertEquals(expectedByte2, arInputStream2.read());
    }
    
    @Test
    public void testMatches() {
        // Typical use case - valid signature
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        int length = 8;
        assertTrue(ArArchiveInputStream.matches(signature, length));
        
        // Typical use case - invalid signature
        byte[] signature2 = {0x21, 0x3c, 0x61, 0x72, 0x0a};
        int length2 = 5;
        assertFalse(ArArchiveInputStream.matches(signature2, length2));
        
        // Edge case - empty signature
        byte[] signature3 = {};
        int length3 = 0;
        assertFalse(ArArchiveInputStream.matches(signature3, length3));
        
        // Edge case - invalid length for valid signature
        byte[] signature4 = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        int length4 = 7;
        assertFalse(ArArchiveInputStream.matches(signature4, length4));
        
        // Error scenario - invalid signature
        byte[] signature5 = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b};
        int length5 = 8;
        assertFalse(ArArchiveInputStream.matches(signature5, length5));
    }
}