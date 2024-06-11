import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6'};
        long expectedResult = 0123456L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput_MissingTrailingSpace() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6'};
        TarUtils.parseOctal(buffer, 0, buffer.length - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput_InvalidByte() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) 'X', (byte) '5', (byte) '6'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) '.', (byte) 'w', (byte) 'o', (byte) 'r', (byte) 'd', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'm', (byte) 'a', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'o', (byte) 'i', (byte) 'n', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '\u0000'};
        String expectedResult = "hello.world.gz.zip.tar.gz";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "hello.world.gz.zip.tar.gz";
        byte[] buffer = new byte[25];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(25, offset);
        for (int i = 0; i < buffer.length; i++) {
            System.out.print(Integer.toHexString(Byte.toUnsignedInt(buffer[i])));
            System.out.print(" ");
        }
        System.out.println();
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 123456L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expectedResult = "0123456 ";
        for (int i = 0; i < buffer.length; i++) {
            String hex = Integer.toHexString(Byte.toUnsignedInt(buffer[i]));
            assertEquals(String.format("%s ", expectedResult.substring(i, i+1)), hex);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_InvalidInput() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) '.', (byte) 'w', (byte) 'o', (byte) 'r', (byte) 'd', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'm', (byte) 'a', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'o', (byte) 'i', (byte) 'n', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '\u0000'};
        long expectedResult = 1862622916;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 123456L;
        byte[] buffer = new byte[10];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expectedResult = "0123456 ";
        for (int i = 0; i < buffer.length; i++) {
            String hex = Integer.toHexString(Byte.toUnsignedInt(buffer[i]));
            assertEquals(String.format("%s ", expectedResult.substring(i, i+1)), hex);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_InvalidInput() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidInput() {
        long value = 123456L;
        byte[] buffer = new byte[10];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expectedResult = "0123456\u0000 ";
        for (int i = 0; i < buffer.length; i++) {
            String hex = Integer.toHexString(Byte.toUnsignedInt(buffer[i]));
            assertEquals(String.format("%s ", expectedResult.substring(i, i+1)), hex);
        }
    }
}