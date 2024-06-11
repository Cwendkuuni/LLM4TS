import org.junit.Test;

import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("file1", false, 1000);
        assertEquals("-    1000 file1", ArchiveUtils.toString(entry1));
        
        ArchiveEntry entry2 = new ArchiveEntry("dir1", true, 0);
        assertEquals("d          0 dir1", ArchiveUtils.toString(entry2));
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        String asciiString = "Hello World!";
        byte[] buffer = asciiString.getBytes();
        
        assertTrue(ArchiveUtils.matchAsciiBuffer(asciiString, buffer));
        
        String asciiString2 = "Hello World";
        assertFalse(ArchiveUtils.matchAsciiBuffer(asciiString2, buffer));
    }
    
    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello";
        byte[] expectedBytes = {(byte)72, (byte)101, (byte)108, (byte)108, (byte)111};
        
        assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));
    }
    
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {(byte)72, (byte)101, (byte)108, (byte)108, (byte)111};
        String expectedString = "Hello";
        
        assertEquals(expectedString, ArchiveUtils.toAsciiString(inputBytes));
    }
    
    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = {(byte)72, (byte)101, (byte)108, (byte)108, (byte)111};
        int offset = 1;
        int length = 3;
        String expectedString = "ell";
        
        assertEquals(expectedString, ArchiveUtils.toAsciiString(inputBytes, offset, length));
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = {(byte)1, (byte)2, (byte)3};
        byte[] buffer2 = {(byte)1, (byte)2, (byte)3};
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        
        byte[] buffer3 = {(byte)4, (byte)5};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }
    
    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {(byte)1, (byte)2, (byte)3, (byte)0};
        byte[] buffer2 = {(byte)1, (byte)2, (byte)3};
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
        
        byte[] buffer3 = {(byte)4, (byte)5};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, true));
    }
    
    @Test
    public void testIsEqualWithOffsetsAndLengths() {
        byte[] buffer1 = {(byte)1, (byte)2, (byte)3};
        int offset1 = 0;
        int length1 = 2;
        
        byte[] buffer2 = {(byte)1, (byte)2};
        int offset2 = 0;
        int length2 = 2;
        
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2));
        
        byte[] buffer3 = {(byte)4, (byte)5};
        assertFalse(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer3, offset2, length2));
    }
    
    @Test
    public void testIsEqualWithOffsetsAndLengthsWithIgnoreTrailingNulls() {
        byte[] buffer1 = {(byte)1, (byte)2, (byte)3, (byte)0};
        int offset1 = 0;
        int length1 = 3;
        
        byte[] buffer2 = {(byte)1, (byte)2, (byte)3};
        int offset2 = 0;
        int length2 = 3;
        
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2, true));
        
        byte[] buffer3 = {(byte)4, (byte)5};
        assertFalse(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer3, offset2, length2, true));
    }
    
    @Test
    public void testIsEqualWithOffsetsAndLengthsAndIgnoreTrailingNulls() {
        byte[] buffer1 = {(byte)1, (byte)2, (byte)3, (byte)0};
        int offset1 = 0;
        int length1 = 3;
        
        byte[] buffer2 = {(byte)1, (byte)2, (byte)3};
        int offset2 = 0;
        int length2 = 3;
        
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2, true));
        
        byte[] buffer3 = {(byte)4, (byte)5};
        assertFalse(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer3, offset2, length2, true));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] inputArray = {(byte)0, (byte)0, (byte)0, (byte)0};
        int size = 4;
        
        assertTrue(ArchiveUtils.isArrayZero(inputArray, size));
        
        byte[] nonZeroArray = {(byte)0, (byte)0, (byte)0, (byte)1};
        assertFalse(ArchiveUtils.isArrayZero(nonZeroArray, size));
    }
    
    @Test
    public void testSanitize() {
        String inputString = "Hello\fWorld";
        String expectedString = "Hello?World";
        
        assertEquals(expectedString, ArchiveUtils.sanitize(inputString));
    }

}