import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    // Test case for parseOctal method
    
    @Test
    public void testParseOctal() {
        byte[] buffer = "123456".getBytes();
        int offset = 0;
        int length = buffer.length;
        
        long result = TarUtils.parseOctal(buffer, offset, length);
        
        assertEquals(3423910, result);
    }
    
    // Test case for parseName method
    
    @Test
    public void testParseName() {
        byte[] buffer = "testFileName".getBytes();
        int offset = 0;
        int length = buffer.length;
        
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals("testFileName", result);
    }
    
    // Test case for formatNameBytes method
    
    @Test
    public void testFormatNameBytes() {
        String name = "testFileName";
        byte[] buf = new byte[50];
        int offset = 0;
        int length = 50;
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertEquals(50, result);
        assertEquals("testFileName" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0", new String(buf));
    }
    
    // Test case for formatUnsignedOctalString method
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 3423910;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
        
        assertEquals("000123456", new String(buf));
    }
    
    // Test case for formatOctalBytes method
    
    @Test
    public void testFormatOctalBytes() {
        long value = 3423910;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        assertEquals(8, result);
        assertEquals("00012345 ", new String(buf));
    }
    
    // Test case for formatLongOctalBytes method
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 3423910;
        byte[] buf = new byte[9];
        int offset = 0;
        int length = 9;
        
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        assertEquals(9, result);
        assertEquals("00012345 ", new String(buf));
    }
    
    // Test case for formatCheckSumOctalBytes method
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 3423910;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        assertEquals(8, result);
        assertEquals("00012345 ", new String(buf));
    }
    
    // Test case for computeCheckSum method
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = "testFileName".getBytes();
        
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(1152, result);
    }
}