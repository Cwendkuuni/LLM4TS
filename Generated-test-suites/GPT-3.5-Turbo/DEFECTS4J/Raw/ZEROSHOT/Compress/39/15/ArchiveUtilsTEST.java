import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");
        
        String expected = "-    2000 main.c";
        String actual = ArchiveUtils.toString(entry);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello World";
        byte[] buffer = expected.getBytes(StandardCharsets.US_ASCII);
        
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }
    
    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello World";
        byte[] expected = inputString.getBytes(StandardCharsets.US_ASCII);
        
        byte[] actual = ArchiveUtils.toAsciiBytes(inputString);
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "Hello World".getBytes(StandardCharsets.US_ASCII);
        String expected = "Hello World";
        
        String actual = ArchiveUtils.toAsciiString(inputBytes);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToAsciiStringOffsetLength() {
        byte[] inputBytes = "Hello World".getBytes(StandardCharsets.US_ASCII);
        int offset = 0;
        int length = 5;
        String expected = "Hello";
        
        String actual = ArchiveUtils.toAsciiString(inputBytes, offset, length);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = "Hello".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "Hello".getBytes(StandardCharsets.US_ASCII);
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "Hello".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "Hello".getBytes(StandardCharsets.US_ASCII);
        
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[5];
        int size = 5;
        
        assertTrue(ArchiveUtils.isArrayZero(array, size));
    }
    
    @Test
    public void testSanitize() {
        String input = "\u0000Hello\u0000World\u0000";
        String expected = "?Hello?World?";
        
        String actual = ArchiveUtils.sanitize(input);
        
        assertEquals(expected, actual);
    }
}