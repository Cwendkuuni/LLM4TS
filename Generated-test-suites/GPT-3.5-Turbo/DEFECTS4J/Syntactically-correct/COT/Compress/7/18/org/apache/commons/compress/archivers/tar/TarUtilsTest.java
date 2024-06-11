
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal(){
        byte[] buffer = new byte[] {0x30, 0x31, 0x32, 0x33}; // "0123"
        int offset = 0;
        int length = 4;
        
        long result = TarUtils.parseOctal(buffer, offset, length);
        
        assertEquals(83, result);
    }
    
    @Test
    public void testParseName(){
        byte[] buffer = new byte[] {0x41, 0x42, 0x43}; // "ABC"
        int offset = 0;
        int length = 3;
        
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals("ABC", result);
    }
    
    @Test
    public void testFormatNameBytes(){
        String name = "ABC";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 6;
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertEquals(6, result);
        assertArrayEquals(new byte[]{0x41, 0x42, 0x43, 0x00, 0x00, 0x00}, buf);
    }
    
    @Test
    public void testFormatUnsignedOctalString(){
        long value1 = 123;
        byte[] buf1 = new byte[5];
        int offset1 = 0;
        int length1 = 5;
        
        TarUtils.formatUnsignedOctalString(value1, buf1, offset1, length1);
        
        assertArrayEquals(new byte[]{0x31, 0x37, 0x33, 0x00, 0x00}, buf1);
        
        long value2 = 12345;
        byte[] buf2 = new byte[6];
        int offset2 = 0;
        int length2 = 6;
        
        TarUtils.formatUnsignedOctalString(value2, buf2, offset2, length2);
        
        assertArrayEquals(new byte[]{0x35, 0x32, 0x71, 0x01, 0x00, 0x00}, buf2);
    }
    
    @Test
    public void testFormatOctalBytes(){
        long value = 123;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        assertEquals(6, result);
        assertArrayEquals(new byte[]{0x31, 0x37, 0x33, 0x20, 0x00, 0x00}, buf);
    }
    
    @Test
    public void testFormatLongOctalBytes(){
        long value = 12345;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        assertEquals(6, result);
        assertArrayEquals(new byte[]{0x32, 0x71, 0x01, 0x20, 0x00, 0x00}, buf);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes(){
        long value = 123;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        assertEquals(6, result);
        assertArrayEquals(new byte[]{0x31, 0x37, 0x33, 0x00, 0x20, 0x00}, buf);
    }
    
    @Test
    public void testComputeCheckSum(){
        byte[] buf = new byte[]{0x41, 0x42, 0x43};
        
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(9, result);
    }
}

