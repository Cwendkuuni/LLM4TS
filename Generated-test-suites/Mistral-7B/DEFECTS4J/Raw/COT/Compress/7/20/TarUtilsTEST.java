import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7'};
        int offset = 0;
        int length = buffer.length;
        long expectedResult = 017557L;

        long actualResult = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '8'}; // '8' is not a valid octal digit
        int offset = 0;
        int length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '\u0000'};
        int offset = 0;
        int length = buffer.length;

        String expectedResult = "tar.file.";
        String actualResult = TarUtils.parseName(buffer, offset, length);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalidByte() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'f', 'i', 'l', 'e', '\u0001'}; // '\u0001' is not a valid file name character
        int offset = 0;
        int length = buffer.length;

        TarUtils.parseName(buffer, offset, length);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.file.";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;

        int actualResult = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(offset+length, actualResult);

        String result = new String(buf, 0, offset);
        assertEquals(name, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 012345L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        String expectedResult = "002023405";
        String actualResult = new String(buffer, 0, offset);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringValueTooLarge() {
        long value = 12345678912345L; // too large to fit in the buffer
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '\u0000'};
        long expectedResult = (0x00 & 0xFF) << 24 |
                (0x61 & 0xFF) << 16 |
                (0x72 & 0xFF) << 8 |
                (0x74 & 0xFF);

        long actualResult = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedResult, actualResult);
    }

}