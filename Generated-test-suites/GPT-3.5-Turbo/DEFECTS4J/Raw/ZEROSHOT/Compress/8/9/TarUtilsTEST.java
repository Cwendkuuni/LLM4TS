import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {
    
    private byte[] buffer;
    
    @Before
    public void setup() {
        buffer = new byte[10];
    }
    
    @Test
    public void testParseOctal_validInput() {
        buffer[0] = (byte) '1';
        buffer[1] = (byte) '2';
        buffer[2] = (byte) '3';
        buffer[3] = (byte) '4';
        
        long result = TarUtils.parseOctal(buffer, 0, 4);
        
        assertEquals(1234L, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParseOctal_invalidInput() {
        buffer[0] = (byte) '1';
        buffer[1] = (byte) '2';
        buffer[2] = (byte) 'a';
        buffer[3] = (byte) '4';
        
        TarUtils.parseOctal(buffer, 0, 4);
    }
    
    @Test
    public void testParseName_validInput() {
        buffer[0] = (byte) 't';
        buffer[1] = (byte) 'e';
        buffer[2] = (byte) 's';
        buffer[3] = (byte) 't';
        buffer[4] = (byte) 0;
        
        String result = TarUtils.parseName(buffer, 0, 5);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testParseName_noTrailingNull() {
        buffer[0] = (byte) 't';
        buffer[1] = (byte) 'e';
        buffer[2] = (byte) 's';
        buffer[3] = (byte) 't';
        
        String result = TarUtils.parseName(buffer, 0, 4);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testFormatNameBytes_inputWithinLength() {
        String name = "test";
        
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 5);
        
        assertEquals(5, offset);
        assertEquals((byte) 't', buffer[0]);
        assertEquals((byte) 'e', buffer[1]);
        assertEquals((byte) 's', buffer[2]);
        assertEquals((byte) 't', buffer[3]);
        assertEquals((byte) 0, buffer[4]);
    }
    
    @Test
    public void testFormatNameBytes_inputLargerThanLength() {
        String name = "test";
        
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 3);
        
        assertEquals(3, offset);
        assertEquals((byte) 't', buffer[0]);
        assertEquals((byte) 'e', buffer[1]);
        assertEquals((byte) 's', buffer[2]);
        assertEquals((byte) 0, buffer[3]);
        assertEquals((byte) 0, buffer[4]);
    }
    
    @Test
    public void testFormatUnsignedOctalString_validInput() {
        long value = 1234L;
        
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 5);
        
        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) '4', buffer[3]);
        assertEquals((byte) 0, buffer[4]);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_invalidInput() {
        long value = 1234567890123456789L;
        
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 3);
    }
    
    @Test
    public void testFormatOctalBytes_validInput() {
        long value = 1234L;
        
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, 5);
        
        assertEquals(5, offset);
        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) '4', buffer[3]);
        assertEquals((byte) ' ', buffer[4]);
        assertEquals((byte) 0, buffer[5]);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFormatOctalBytes_invalidInput() {
        long value = 1234567890123456789L;
        
        TarUtils.formatOctalBytes(value, buffer, 0, 5);
    }
    
    @Test
    public void testFormatLongOctalBytes_validInput() {
        long value = 1234L;
        
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, 5);
        
        assertEquals(5, offset);
        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) '4', buffer[3]);
        assertEquals((byte) ' ', buffer[4]);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFormatLongOctalBytes_invalidInput() {
        long value = 1234567890123456789L;
        
        TarUtils.formatLongOctalBytes(value, buffer, 0, 5);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes_validInput() {
        long value = 1234L;
        
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 6);
        
        assertEquals(6, offset);
        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) '4', buffer[3]);
        assertEquals((byte) 0, buffer[4]);
        assertEquals((byte) ' ', buffer[5]);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_invalidInput() {
        long value = 1234567890123456789L;
        
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 6);
    }
    
    @Test
    public void testComputeCheckSum_validInput() {
        buffer[0] = (byte) '1';
        buffer[1] = (byte) '2';
        buffer[2] = (byte) '3';
        buffer[3] = (byte) '4';
        
        long result = TarUtils.computeCheckSum(buffer);
        
        assertEquals(346L, result);
    }
    
}