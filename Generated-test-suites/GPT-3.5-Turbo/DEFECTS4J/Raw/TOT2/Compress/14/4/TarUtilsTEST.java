import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = { '1', '2', '3' };
        assertEquals(83, TarUtils.parseOctal(buffer, 0, 3));
        byte[] allNULBuffer = { 0, 0, 0 };
        assertEquals(0, TarUtils.parseOctal(allNULBuffer, 0, 3));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] octalBuffer = { '0', '1', '0' };
        assertEquals(64, TarUtils.parseOctalOrBinary(octalBuffer, 0, 3));
        byte[] binaryBuffer = { (byte) 0x80, 0x01, 0x00 };
        assertEquals(32768, TarUtils.parseOctalOrBinary(binaryBuffer, 0, 3));
    }

    @Test
    public void testParseBoolean() {
        byte[] trueBuffer = { 1 };
        assertTrue(TarUtils.parseBoolean(trueBuffer, 0));
        byte[] falseBuffer = { 0 };
        assertFalse(TarUtils.parseBoolean(falseBuffer, 0));
    }

    @Test
    public void testParseName() {
        byte[] nameBuffer = { 'T', 'e', 's', 't', 0, 'T', 'e', 's', 't' };
        assertEquals("Test", TarUtils.parseName(nameBuffer, 0, 9));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buf = new byte[5];
        int offset = TarUtils.formatNameBytes(name, buf, 0, 5);
        assertEquals(5, offset);
        assertEquals('T', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('s', buf[2]);
        assertEquals('t', buf[3]);
        assertEquals(0, buf[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buf = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buf, 0, 5);
        assertEquals('1', buf[0]);
        assertEquals('7', buf[1]);
        assertEquals('3', buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(0, buf[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = { 'T', 'e', 's', 't' };
        assertEquals(416, TarUtils.computeCheckSum(buffer));
    }
}