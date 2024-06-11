import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {
    
    @Test
    public void testParseOctal() {
        byte[] buffer = {48, 55, 53}; //(075 in octal)
        int offset = 0;
        int length = 3;
        long expected = 61;
        
        long result = TarUtils.parseOctal(buffer, offset, length);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = {48, 55, 56}; //(078 contains invalid byte)
        int offset = 0;
        int length = 3;
        
        TarUtils.parseOctal(buffer, offset, length);
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = {103, 111, 111, 100, 46, 116, 120, 116, 0, 0, 0}; //(good.txt with trailing null)
        int offset = 0;
        int length = 11;
        String expected = "good.txt";
        
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "test.txt";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        byte[] expected = {116, 101, 115, 116, 46, 116, 120, 116, 0, 0};
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 61; //(075 in octal)
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = 3;
        byte[] expected = {55, 57, 53};
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        assertArrayEquals(expected, buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithInvalidValue() {
        long value = 512; //(1100 in octal, will not fit in buffer)
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = 3;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 61; //(075 in octal)
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        byte[] expected = {55, 57, 53, 32, 0};
        
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 61; //(075 in octal)
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        byte[] expected = {55, 57, 53, 32};
        
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 61; //(075 in octal)
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        byte[] expected = {55, 57, 53, 0, 32};
        
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = {116, 101, 115, 116, 46, 116, 120, 116, 0, 0, 0}; //(good.txt with trailing nulls)
        long expected = 1101;
        
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(expected, result);
    }
}