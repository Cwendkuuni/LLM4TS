import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'0', '1', '2', '3'};
        int offset = 0;
        int length = 4;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(123L, result);
        
        byte[] buffer2 = {'1', '2', '3', '4', '5'};
        int offset2 = 1;
        int length2 = 3;
        long result2 = TarUtils.parseOctal(buffer2, offset2, length2);
        assertEquals(234L, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidInput() {
        byte[] buffer = {'A', 'B', 'C', 'D'};
        int offset = 0;
        int length = 4;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        int offset = 0;
        int length = 11;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("Hello World", result);
        
        byte[] buffer2 = {'J', 'a', 'v', 'a', ' ', 'T', 'e', 's', 't'};
        int offset2 = 0;
        int length2 = 9;
        String result2 = TarUtils.parseName(buffer2, offset2, length2);
        assertEquals("Java Test", result2);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 6;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals("Test\u0000\u0000\u0000\u0000", new String(buf));
        assertEquals(6, result);
        
        String name2 = "Java";
        byte[] buf2 = new byte[10];
        int offset2 = 0;
        int length2 = 5;
        int result2 = TarUtils.formatNameBytes(name2, buf2, offset2, length2);
        assertEquals("Java\u0000\u0000\u0000\u0000\u0000", new String(buf2));
        assertEquals(5, result2);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123L;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 3;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals("123", new String(buffer));
        
        long value2 = 456L;
        byte[] buffer2 = new byte[6];
        int offset2 = 0;
        int length2 = 5;
        TarUtils.formatUnsignedOctalString(value2, buffer2, offset2, length2);
        assertEquals("4560", new String(buffer2));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123L;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals("00123 ", new String(buf));
        assertEquals(4, result);
        
        long value2 = 456L;
        byte[] buf2 = new byte[6];
        int offset2 = 0;
        int length2 = 5;
        int result2 = TarUtils.formatOctalBytes(value2, buf2, offset2, length2);
        assertEquals("004560", new String(buf2));
        assertEquals(5, result2);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123L;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals("00123", new String(buf));
        assertEquals(4, result);
        
        long value2 = 456L;
        byte[] buf2 = new byte[6];
        int offset2 = 0;
        int length2 = 5;
        int result2 = TarUtils.formatLongOctalBytes(value2, buf2, offset2, length2);
        assertEquals("00456", new String(buf2));
        assertEquals(5, result2);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123L;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals("00123 ", new String(buf));
        assertEquals(4, result);
        
        long value2 = 456L;
        byte[] buf2 = new byte[6];
        int offset2 = 0;
        int length2 = 5;
        int result2 = TarUtils.formatCheckSumOctalBytes(value2, buf2, offset2, length2);
        assertEquals("004560", new String(buf2));
        assertEquals(5, result2);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {1, 2, 3, 4, 5};
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(15L, result);
        
        byte[] buf2 = {10, 20, 30, 40, 50};
        long result2 = TarUtils.computeCheckSum(buf2);
        assertEquals(150L, result2);
    }
}