import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validInput_returnsLongValue() {
        // Given
        byte[] buffer = {49, 50, 51, 0, 52, 53}; // bytes representing octal string "123"
        int offset = 0;
        int length = 6;
        
        // When
        long result = TarUtils.parseOctal(buffer, offset, length);
        
        // Then
        assertEquals(83, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidInput_throwsIllegalArgumentException() {
        // Given
        byte[] buffer = {49, 50, 54, 0, 52, 53}; // bytes representing invalid octal string "126"
        int offset = 0;
        int length = 6;
        
        // When
        TarUtils.parseOctal(buffer, offset, length);
        
        // Then
        // expect IllegalArgumentException to be thrown
    }
    
    @Test
    public void testParseName_validInput_returnsString() {
        // Given
        byte[] buffer = {65, 66, 67, 0, 68}; // bytes representing name "ABC"
        int offset = 0;
        int length = 6;
        
        // When
        String result = TarUtils.parseName(buffer, offset, length);
        
        // Then
        assertEquals("ABC", result);
    }

    @Test
    public void testParseName_inputWithTrailingNull_returnsStringWithNullRemoved() {
        // Given
        byte[] buffer = {65, 66, 67, 0, 68, 0}; // bytes representing name "ABC\0D\0"
        int offset = 0;
        int length = 7;
        
        // When
        String result = TarUtils.parseName(buffer, offset, length);
        
        // Then
        assertEquals("ABC", result);
    }
    
    @Test
    public void testFormatNameBytes_shortName_returnsBufferWithNameAndTrailingNulls() {
        // Given
        String name = "ABC";
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        
        // When
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        // Then
        assertEquals(6, result);
        assertArrayEquals(new byte[] {65, 66, 67, 0, 0, 0}, buf);
    }
    
    @Test
    public void testFormatNameBytes_longName_truncatesOutputBuffer() {
        // Given
        String name = "ABCDEFGHIJKLMN";
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        
        // When
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        // Then
        assertEquals(6, result);
        assertArrayEquals(new byte[] {65, 66, 67, 68, 69, 70}, buf);
    }
    
    @Test
    public void testFormatUnsignedOctalString_validInput_returnsBufferWithUnsignedOctalString() {
        // Given
        long value = 123456;
        byte[] buffer = new byte[6];
        int offset = 0;
        int length = 6;
        
        // When
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        // Then
        assertArrayEquals(new byte[] {50, 52, 70, 60, 0, 0}, buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_invalidInput_throwsIllegalArgumentException() {
        // Given
        long value = 999999;
        byte[] buffer = new byte[6];
        int offset = 0;
        int length = 6;
        
        // When
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        // Then
        // expect IllegalArgumentException to be thrown
    }
    
    @Test
    public void testFormatOctalBytes_validInput_returnsBufferWithOctalBytes() {
        // Given
        long value = 123456;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        // When
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        // Then
        assertEquals(8, result);
        assertArrayEquals(new byte[] {50, 52, 70, 60, 32, 0, 0, 0}, buf);
    }
    
    @Test
    public void testFormatLongOctalBytes_validInput_returnsBufferWithOctalBytes() {
        // Given
        long value = 123456;
        byte[] buf = new byte[7];
        int offset = 0;
        int length = 7;
        
        // When
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        // Then
        assertEquals(7, result);
        assertArrayEquals(new byte[] {50, 52, 70, 60, 32, 0, 0}, buf);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes_validInput_returnsBufferWithOctalBytes() {
        // Given
        long value = 123456;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        // When
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        // Then
        assertEquals(8, result);
        assertArrayEquals(new byte[] {50, 52, 70, 60, 0, 32, 0, 0}, buf);
    }
    
    @Test
    public void testComputeCheckSum_validInput_returnsLongValue() {
        // Given
        byte[] buf = {49, 50, 51, 0, 52, 53}; // bytes representing octal string "123"
        
        // When
        long result = TarUtils.computeCheckSum(buf);
        
        // Then
        assertEquals(246, result);
    }
}