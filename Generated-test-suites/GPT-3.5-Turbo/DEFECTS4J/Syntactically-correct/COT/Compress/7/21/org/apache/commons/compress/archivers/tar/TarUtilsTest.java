import static org.junit.Assert.*;
import org.junit.Test;


public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49,50,53}; 
        assertEquals(125L, TarUtils.parseOctal(buffer, 0, buffer.length));
        
        byte[] buffer2 = {48,48}; 
        assertEquals(0L, TarUtils.parseOctal(buffer2, 0, buffer2.length));
        
        byte[] buffer3 = {49,49,53,56}; 
        assertEquals(135L, TarUtils.parseOctal(buffer3, 0, buffer3.length));
        
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = {104,101,108,108,111,32,116,101,115,116,0}; 
        assertEquals("hello test", TarUtils.parseName(buffer, 0, buffer.length));
        
        byte[] buffer2 = {72,101,108,108,111}; 
        assertEquals("Hello", TarUtils.parseName(buffer2, 0, buffer2.length));
        
        byte[] buffer3 = {116,101,115,116,105,110,103}; 
        assertEquals("testing", TarUtils.parseName(buffer3, 0, buffer3.length));
        
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "hello test";
        byte[] buf = new byte[20];
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertArrayEquals(new byte[]{104,101,108,108,111,32,116,101,115,116,0,0,0,0,0,0,0,0,0,0}, buf);
        
        String name2 = "Hello";
        byte[] buf2 = new byte[10];
        int offset2 = 1;
        int length2 = 5;
        offset2 = TarUtils.formatNameBytes(name2, buf2, offset2, length2);
        assertArrayEquals(new byte[]{0,72,101,108,108,111,0,0,0,0}, buf2);
        
        String name3 = "testing";
        byte[] buf3 = new byte[15];
        int offset3 = 0;
        int length3 = 15;
        offset3 = TarUtils.formatNameBytes(name3, buf3, offset3, length3);
        assertArrayEquals(new byte[]{116,101,115,116,105,110,103,0,0,0,0,0,0,0,0}, buf3);
        
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 125L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{0,0,0,0,0,0,0,48,48,52}, buffer);
        
        long value2 = 0L;
        byte[] buffer2 = new byte[8];
        int offset2 = 3;
        int length2 = 4;
        TarUtils.formatUnsignedOctalString(value2, buffer2, offset2, length2);
        assertArrayEquals(new byte[]{0,0,0,48,0,0,0,0}, buffer2);
        
        long value3 = 135L;
        byte[] buffer3 = new byte[10];
        int offset3 = 0;
        int length3 = 8;
        TarUtils.formatUnsignedOctalString(value3, buffer3, offset3, length3);
        assertArrayEquals(new byte[]{0,0,0,0,50,53,54,0,0,0}, buffer3);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 125L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{0,0,0,0,48,48,52,32,0,0}, buffer);
        
        long value2 = 0L;
        byte[] buffer2 = new byte[8];
        int offset2 = 3;
        int length2 = 4;
        offset2 = TarUtils.formatOctalBytes(value2, buffer2, offset2, length2);
        assertArrayEquals(new byte[]{0,0,0,48,32,0,0,0}, buffer2);
        
        long value3 = 135L;
        byte[] buffer3 = new byte[10];
        int offset3 = 0;
        int length3 = 8;
        offset3 = TarUtils.formatOctalBytes(value3, buffer3, offset3, length3);
        assertArrayEquals(new byte[]{0,0,0,0,50,53,54,32,0,0}, buffer3);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 125L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{0,0,0,0,48,48,52,32,0,0}, buffer);
        
        long value2 = 0L;
        byte[] buffer2 = new byte[8];
        int offset2 = 3;
        int length2 = 4;
        offset2 = TarUtils.formatLongOctalBytes(value2, buffer2, offset2, length2);
        assertArrayEquals(new byte[]{0,0,0,48,32,0,0,0}, buffer2);
        
        long value3 = 135L;
        byte[] buffer3 = new byte[10];
        int offset3 = 0;
        int length3 = 8;
        offset3 = TarUtils.formatLongOctalBytes(value3, buffer3, offset3, length3);
        assertArrayEquals(new byte[]{0,0,0,0,50,53,54,32,0,0}, buffer3);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 125L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{0,0,0,0,48,48,52,0,32,0}, buffer);
        
        long value2 = 0L;
        byte[] buffer2 = new byte[8];
        int offset2 = 3;
        int length2 = 4;
        offset2 = TarUtils.formatCheckSumOctalBytes(value2, buffer2, offset2, length2);
        assertArrayEquals(new byte[]{0,0,0,48,0,0,32,0}, buffer2);
        
        long value3 = 135L;
        byte[] buffer3 = new byte[10];
        int offset3 = 0;
        int length3 = 8;
        offset3 = TarUtils.formatCheckSumOctalBytes(value3, buffer3, offset3, length3);
        assertArrayEquals(new byte[]{0,0,0,0,50,53,54,0,32,0}, buffer3);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = {49,50,53}; 
        assertEquals(147L, TarUtils.computeCheckSum(buf));
        
        byte[] buf2 = {48,48}; 
        assertEquals(48L, TarUtils.computeCheckSum(buf2));
        
        byte[] buf3 = {49,49,53,56}; 
        assertEquals(276L, TarUtils.computeCheckSum(buf3));
    }
}