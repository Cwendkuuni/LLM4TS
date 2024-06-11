import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '5', (byte) '5', (byte) '5'};
        long expected = 0177555L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseOctal_InvalidInput() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '8'}; // Invalid octal digit
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctal_BufferLengthReached() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7'};
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length - 1));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) '.', (byte) 'w', (byte) 'o', (byte) 'r', (byte) 'd', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) '.', (byte) 'm', (byte) 'y', (byte) '.', (byte) 'p', (byte) 'a', (byte) 'c', (byte) 'k', (byte) 'g', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'a', (byte) 'n', (byte) 'd', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
        String expected = "hello.word.tar.gz.zip.tar.gzip.gz.zip.tar.gzip.gz.zip.tar.gzip.gz.tar.gzip.gz.and.tar";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseName_BufferLengthReached() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) '.', (byte) 'w', (byte) 'o', (byte) 'r', (byte) 'd', (byte) '.'};
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello.word.tar.gz.zip.tar.gzip.gz.zip.tar.gzip.gz.zip.tar.gzip.gz.tar.gzip.gz.and.tar";
        byte[] buffer = new byte[name.length() + 1];
        int expectedLength = name.length() + 1;
        int actualLength = TarUtils.formatNameBytes(name, buffer, 0, expectedLength);
        assertEquals(expectedLength, actualLength);
        for (int i = 0; i < expectedLength; ++i) {
            assertEquals(name.charAt(i), (char)buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x12345678L;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "01234567";
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < buffer.length; ++i) {
            actual.append((char)buffer[i]);
        }
        assertEquals(expected, actual.toString());
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01234567  ";
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < buffer.length; ++i) {
            actual.append((char)buffer[i]);
        }
        assertEquals(expected, actual.substring(0, offset));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        long expected = TarUtils.computeCheckSum(buffer);
        long actual = 0x0123456789abcdfL;
        assertEquals(expected, actual);
    }
}