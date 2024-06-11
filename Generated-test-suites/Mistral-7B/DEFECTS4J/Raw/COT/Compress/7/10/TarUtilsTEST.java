import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(017777L, result);

        byte[] invalidBuffer = new byte[]{(byte) '0', (byte) '8'};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] trailingNullBuffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) 0};
        long resultTrailingNull = TarUtils.parseOctal(trailingNullBuffer, 0, trailingNullBuffer.length);
        assertEquals(017777L, resultTrailingNull);

        byte[] trailingSpaceBuffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) ' '};
        long resultTrailingSpace = TarUtils.parseOctal(trailingSpaceBuffer, 0, trailingSpaceBuffer.length);
        assertEquals(017777L, resultTrailingSpace);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'T', (byte) 'a', (byte) 'r', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Tar.gz", result);

        byte[] emptyBuffer = new byte[]{0};
        String resultEmpty = TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length);
        assertEquals("", resultEmpty);

        byte[] nullBuffer = new byte[]{0};
        try {
            TarUtils.parseName(nullBuffer, 0, nullBuffer.length);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Tar.gz";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) 'T', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) ' ', (byte) 0, 0, 0, 0}, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[8];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) ' ', (byte) '0', (byte) '1', (byte) '2', (byte) '3'}, buffer, 0, offset);

        long tooLargeValue = Long.parseUnsignedLong("1234567890123456789012345678901234567890L");
        try {
            TarUtils.formatUnsignedOctalString(tooLargeValue, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[6];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) ' ', (byte) '0'}, buffer, 0, offset);

        long tooLargeValue = Long.parseUnsignedLong("1234567890123456789012345678901234567890L");
        try {
            TarUtils.formatOctalBytes(tooLargeValue, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3'}, buffer, 0, offset);

        long tooLargeValue = Long.parseUnsignedLong("1234567890123456789012345678901234567890L");
        try {
            TarUtils.formatCheckSumOctalBytes(tooLargeValue, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0, (byte) 0x70, (byte) 0x4c, (byte) 0x69, (byte) 0x62, (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x53, (byte) 0x61, (byte) 0x6d, (byte) 0x79};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(0x6572746562704c75L, result);
    }
}