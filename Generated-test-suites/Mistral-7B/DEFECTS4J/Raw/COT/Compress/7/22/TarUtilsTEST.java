import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
        for (int i = 0; i <= 7; i++) {
            int offset = 0;
            int length = i + 1;
            long expected = (long) Math.pow(8, i);
            long result = TarUtils.parseOctal(buffer, offset, length);
            assertEquals(expected, result);
        }

        byte[] invalidBuffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '8', (byte) '3', (byte) '4', (byte) '5', (byte) '6'};
        int invalidOffset = 0;
        int invalidLength = 8;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidBuffer, invalidOffset, invalidLength));

        byte[] nullBuffer = null;
        assertThrows(NullPointerException.class, () -> TarUtils.parseOctal(nullBuffer, 0, 1));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'};
        String expected = "abcdef";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);

        byte[] nullBuffer = null;
        assertThrows(NullPointerException.class, () -> TarUtils.parseName(nullBuffer, 0, 1));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdef";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 6;
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(offset + length, result);
        for (int i = 0; i < length; i++) {
            assertEquals(name.charAt(i), (char) buffer[i + offset]);
        }
        assertEquals(0, buffer[offset + length]);
        for (int i = length + 1; i < buffer.length; i++) {
            assertEquals(0, buffer[i]);
        }

        name = "abcdefghijklmnopqrstuvwxyz";
        length = 11;
        result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(offset + length, result);
        for (int i = 0; i < 10; i++) {
            assertEquals(name.charAt(i), (char) buffer[i + offset]);
        }
        assertEquals(0, buffer[offset + 10]);
        for (int i = 11; i < buffer.length; i++) {
            assertEquals(0, buffer[i]);
        }

        name = "abcdef";
        length = 5;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatNameBytes(name, buffer, offset, length));

        name = null;
        assertThrows(NullPointerException.class, () -> TarUtils.formatNameBytes(name, buffer, offset, length));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals(0, buffer[offset]);
        for (int i = 1; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = 255;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals((byte) '7', buffer[offset]);
        for (int i = 1; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = 1234;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals((byte) '1', buffer[offset]);
        assertEquals((byte) '1', buffer[offset + 1]);
        assertEquals((byte) '2', buffer[offset + 2]);
        assertEquals((byte) '3', buffer[offset + 3]);
        for (int i = 4; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = 177777;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals((byte) '0', buffer[offset]);
        assertEquals((byte) '0', buffer[offset + 1]);
        assertEquals((byte) '1', buffer[offset + 2]);
        assertEquals((byte) '7', buffer[offset + 3]);
        assertEquals((byte) '7', buffer[offset + 4]);
        assertEquals((byte) '7', buffer[offset + 5]);
        for (int i = 6; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = Long.MAX_VALUE;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, offset, length));

        value = Long.MIN_VALUE;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, offset, length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, TarUtils.formatNameBytes(Long.toOctalString(value), buffer, offset, length-2).offset + length);
        assertEquals((byte) '0', buffer[offset]);
        for (int i = 1; i < length-1; i++) {
            assertEquals(0, buffer[offset + i]);
        }
        assertEquals((byte) '0', buffer[offset + length-1]);
        assertEquals((byte) ' ', buffer[offset + length-2]);

        value = 255;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals((byte) '077', buffer[offset]);
        assertEquals((byte) ' ', buffer[offset + 1]);
        assertEquals((byte) '0', buffer[offset + 2]);
        for (int i = 3; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = 1234;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals((byte) '011', buffer[offset]);
        assertEquals((byte) '012', buffer[offset + 1]);
        assertEquals((byte) '013', buffer[offset + 2]);
        assertEquals((byte) '014', buffer[offset + 3]);
        for (int i = 4; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = 177777;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals((byte) '000', buffer[offset]);
        assertEquals((byte) '001', buffer[offset + 1]);
        assertEquals((byte) '011', buffer[offset + 2]);
        assertEquals((byte) '012', buffer[offset + 3]);
        assertEquals((byte) '013', buffer[offset + 4]);
        assertEquals((byte) '014', buffer[offset + 5]);
        assertEquals((byte) '015', buffer[offset + 6]);
        assertEquals((byte) '017', buffer[offset + 7]);
        for (int i = 8; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = Long.MAX_VALUE;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, offset, length));

        value = Long.MIN_VALUE;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, offset, length));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 9;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, TarUtils.formatNameBytes(Long.toOctalString(value), buffer, offset, length-1).offset + length);
        assertEquals((byte) '0', buffer[offset]);
        for (int i = 1; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = 255;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals((byte) '077', buffer[offset]);
        for (int i = 1; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = 1234;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals((byte) '011', buffer[offset]);
        assertEquals((byte) '012', buffer[offset + 1]);
        assertEquals((byte) '013', buffer[offset + 2]);
        assertEquals((byte) '014', buffer[offset + 3]);
        for (int i = 4; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = 177777;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals((byte) '000', buffer[offset]);
        assertEquals((byte) '001', buffer[offset + 1]);
        assertEquals((byte) '011', buffer[offset + 2]);
        assertEquals((byte) '012', buffer[offset + 3]);
        assertEquals((byte) '013', buffer[offset + 4]);
        assertEquals((byte) '014', buffer[offset + 5]);
        for (int i = 6; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = Long.MAX_VALUE;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalBytes(value, buffer, offset, length));

        value = Long.MIN_VALUE;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalBytes(value, buffer, offset, length));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, TarUtils.formatNameBytes(Long.toOctalString(value), buffer, offset, length-2).offset + length);
        assertEquals((byte) '0', buffer[offset]);
        for (int i = 1; i < length-1; i++) {
            assertEquals(0, buffer[offset + i]);
        }
        assertEquals((byte) '0', buffer[offset + length-1]);
        assertEquals((byte) ' ', buffer[offset + length-2]);

        value = 255;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals((byte) '077', buffer[offset]);
        for (int i = 1; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = 1234;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals((byte) '011', buffer[offset]);
        assertEquals((byte) '012', buffer[offset + 1]);
        assertEquals((byte) '013', buffer[offset + 2]);
        assertEquals((byte) '014', buffer[offset + 3]);
        for (int i = 4; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = 177777;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals((byte) '000', buffer[offset]);
        assertEquals((byte) '001', buffer[offset + 1]);
        assertEquals((byte) '011', buffer[offset + 2]);
        assertEquals((byte) '012', buffer[offset + 3]);
        assertEquals((byte) '013', buffer[offset + 4]);
        assertEquals((byte) '014', buffer[offset + 5]);
        assertEquals((byte) '015', buffer[offset + 6]);
        assertEquals((byte) '017', buffer[offset + 7]);
        for (int i = 8; i < length; i++) {
            assertEquals(0, buffer[offset + i]);
        }

        value = Long.MAX_VALUE;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length));

        value = Long.MIN_VALUE;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0, 1, 2, 3, 4, 5};
        long expected = 192;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSumWithAllZeroes() {
        byte[] buffer = new byte[0];
        long expected = 0;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSumWithNullBuffer() {
        byte[] buffer = null;
        assertThrows(NullPointerException.class, () -> TarUtils.computeCheckSum(buffer));
    }
}