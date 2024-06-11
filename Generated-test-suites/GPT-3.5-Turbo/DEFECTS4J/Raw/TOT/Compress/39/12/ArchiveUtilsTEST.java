import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    private ArchiveEntry entry;
    
    @Before
    public void setup() {
        entry = new ArchiveEntry() {
            
            @Override
            public String getName() {
                return "test.txt";
            }
            
            @Override
            public long getSize() {
                return 100;
            }
            
            @Override
            public boolean isDirectory() {
                return false;
            }
        };
    }
    
    @Test
    public void toString_DirectoryEntry_ReturnsStringWithDAndSize() {
        entry = new ArchiveEntry() {
            
            @Override
            public String getName() {
                return "test";
            }
            
            @Override
            public long getSize() {
                return 100;
            }
            
            @Override
            public boolean isDirectory() {
                return true;
            }
        };
        
        String expected = "d     100 test";
        String result = ArchiveUtils.toString(entry);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void toString_FileEntry_ReturnsStringWithDashAndSize() {
        String expected = "-     100 test.txt";
        String result = ArchiveUtils.toString(entry);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void matchAsciiBuffer_MatchingBuffer_ReturnsTrue() {
        String expected = "Hello";
        byte[] buffer = expected.getBytes();
        
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        
        assertTrue(result);
    }
    
    @Test
    public void matchAsciiBuffer_NonMatchingBuffer_ReturnsFalse() {
        String expected = "Hello";
        byte[] buffer = "World".getBytes();
        
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        
        assertFalse(result);
    }
    
    @Test
    public void matchAsciiBuffer_BufferWithOffsetAndLength_ReturnsTrue() {
        String expected = "Hello";
        byte[] buffer = "WorldHello".getBytes();
        
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 5, 5);
        
        assertTrue(result);
    }
    
    @Test
    public void toAsciiBytes_ConvertString_ReturnsByteArray() {
        String inputString = "Hello";
        byte[] expected = inputString.getBytes();
        
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void toAsciiString_ConvertByteArray_ReturnsString() {
        byte[] inputBytes = "Hello".getBytes();
        String expected = "Hello";
        
        String result = ArchiveUtils.toAsciiString(inputBytes);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void toAsciiString_ConvertByteArrayWithOffsetAndLength_ReturnsString() {
        byte[] inputBytes = "HelloWorld".getBytes();
        String expected = "World";
        
        String result = ArchiveUtils.toAsciiString(inputBytes, 5, 5);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void isEqual_BuffersWithSameContent_ReturnsTrue() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        
        assertTrue(result);
    }
    
    @Test
    public void isEqual_BuffersWithDifferentContent_ReturnsFalse() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "World".getBytes();
        
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        
        assertFalse(result);
    }
    
    @Test
    public void isEqualWithNull_IgnoreTrailingNulls_ReturnsTrue() {
        byte[] buffer1 = {65, 66, 67, 0, 0, 0};
        byte[] buffer2 = {65, 66, 67};
        
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 3);
        
        assertTrue(result);
    }
    
    @Test
    public void isArrayZero_FirstNBytesAreZero_ReturnsTrue() {
        byte[] array = {0, 0, 0, 1, 2, 3};
        
        boolean result = ArchiveUtils.isArrayZero(array, 3);
        
        assertTrue(result);
    }
    
    @Test
    public void isArrayZero_FirstNBytesAreNonZero_ReturnsFalse() {
        byte[] array = {1, 2, 3};
        
        boolean result = ArchiveUtils.isArrayZero(array, 3);
        
        assertFalse(result);
    }
    
    @Test
    public void sanitize_InputStringWithPrintableCharacters_ReturnsSameString() {
        String input = "Hello World!";
        String expected = "Hello World!";
        
        String result = ArchiveUtils.sanitize(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void sanitize_InputStringWithNonPrintableCharacters_ReturnsSanitizedString() {
        String input = "Hello\tWorld\n";
        String expected = "Hello?World?";
        
        String result = ArchiveUtils.sanitize(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void sanitize_InputStringExceeding255Chars_ReturnsSanitizedStringWithMaxLength() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            sb.append("a");
        }
        String input = sb.toString();
        String expected = input.substring(0, 255);
        
        String result = ArchiveUtils.sanitize(input);
        
        assertEquals(expected, result);
    }
    
}