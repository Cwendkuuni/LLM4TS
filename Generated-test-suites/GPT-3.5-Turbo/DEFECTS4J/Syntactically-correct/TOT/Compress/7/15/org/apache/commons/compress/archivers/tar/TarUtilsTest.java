import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_emptyBuffer() {
        byte[] buffer = new byte[0];
        int offset = 0;
        int length = 0;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(0, result);
    }

    @Test
    public void testParseOctal_validOctalString() {
        byte[] buffer = "123\0".getBytes();
        int offset = 0;
        int length = buffer.length;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(83, result);
    }

    @Test
    public void testParseOctal_invalidOctalDigit() {
        byte[] buffer = "12a\0".getBytes();
        int offset = 0;
        int length = buffer.length;

        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid octal digit at position 2 in '12a'", e.getMessage());
        }

    }

    @Test
    public void testParseName_emptyBuffer() {
        byte[] buffer = new byte[0];
        int offset = 0;
        int length = 0;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("", result);
    }

    @Test
    public void testParseName_validName() {
        byte[] buffer = "HelloWorld\0".getBytes();
        int offset = 0;
        int length = buffer.length;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("HelloWorld", result);
    }

    @Test
    public void testFormatNameBytes_shortName() {
        String name = "Hello";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;

        int updatedOffset = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals("Hello\0\0\0\0\0\0\0", new String(buf));
        assertEquals(10, updatedOffset);
    }

    @Test
    public void testFormatNameBytes_longName() {
        String name = "VeryLongName";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = buf.length;

        int updatedOffset = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals("VeryL", new String(buf));
        assertEquals(5, updatedOffset);
    }

    @Test
    public void testFormatUnsignedOctalString_zeroValue() {
        long value = 0;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertEquals("00000", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalString_nonZeroValue() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertEquals("00173", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = buf.length;

        int updatedOffset = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertEquals("00173 ", new String(buf));
        assertEquals(5, updatedOffset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = buf.length;

        int updatedOffset = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        assertEquals("00173", new String(buf));
        assertEquals(5, updatedOffset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = buf.length;

        int updatedOffset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        assertEquals("00173 ", new String(buf));
        assertEquals(5, updatedOffset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = "Hello".getBytes();

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(532, result);
    }

}