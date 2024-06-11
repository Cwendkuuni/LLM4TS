import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal(){
        byte[] buffer = {'1','2','3','4','5','\0'};
        long result = TarUtils.parseOctal(buffer, 0, 5);
        assertEquals(12345, result);
    }

    @Test
    public void testParseName(){
        byte[] buffer = {'t','e','s','t','\0','b','y','t','e'};
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes(){
        byte[] buf = new byte[5];
        int offset = TarUtils.formatNameBytes("test", buf, 0, 5);
        byte[] expected = {'t','e','s','t',0};
        assertArrayEquals(expected, buf);
        assertEquals(5, offset);
    }

    @Test
    public void testFormatUnsignedOctalString(){
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(12345, buffer, 0, 5);
        byte[] expected = {'3','0','1','1','1'};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatOctalBytes(){
        byte[] buf = new byte[5];
        int offset = TarUtils.formatOctalBytes(12345, buf, 0, 5);
        byte[] expected = {'3','0','1','1', ' '};
        assertArrayEquals(expected, buf);
        assertEquals(5, offset);
    }

    @Test
    public void testComputeCheckSum(){
        byte[] buffer = {'t','e','s','t','\0'};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(429, result); // ASCII values of 't','e','s','t'
    }

}