import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctalValid() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '7'};
        long expected = 0127L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseOctalLeadingSpaces() {
        byte[] buffer = new byte[]{(byte) ' ', (byte) '0', (byte) '1', (byte) '2', (byte) '7', (byte) ' '};
        long expected = 0127L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidDigit() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '8', (byte) '7'}; // '8' is not a valid octal digit
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalTrailingSpaces() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '7', (byte) ' ', (byte) ' '};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalLengthExceeded() {
        byte[] buffer = new byte[10]; // buffer is longer than the expected maximum number of bytes to parse
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 'n', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '\u0000'};
        String expected = "tar.gz.plan.tx.tar.gz";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseNameLeadingSpaces() {
        byte[] buffer = new byte[]{(byte) ' ', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 'n', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '\u0000'};
        String expected = "tar.gz.plan.tx.tar.gz";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseNameTrailingSpaces() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 'n', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '\u0000', (byte) ' '};
        String expected = "tar.gz.plan.tx.tar.gz";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNameLengthExceeded() {
        byte[] buffer = new byte[100]; // buffer is longer than the expected maximum number of bytes to parse
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    @Test
    public void testFormatNameBytesValid() {
        String name = "tar.gz.plan.tx.tar.gz";
        byte[] expected = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 'n', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
        byte[] buffer = new byte[expected.length];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, expected.length);
        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(offset, expected.length);
    }

    @Test
    public void testFormatNameBytesTrailingSpaces() {
        String name = "tar.gz.plan.tx.tar.gz";
        byte[] expected = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 'n', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) ' ', (byte) ' '};
        byte[] buffer = new byte[expected.length];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, expected.length);
        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(offset, expected.length);
    }

    @Test
    public void testFormatNameBytesLengthExceeded() {
        String name = "tar.gz.plan.tx.tar.gz";
        byte[] buffer = new byte[name.length()+10]; // buffer is longer than the expected maximum number of bytes to copy
        int offset = TarUtils.formatNameBytes(name, buffer, 0, name.length());
        Assert.assertEquals(offset, name.length());
    }

    @Test
    public void testFormatUnsignedOctalStringValid() {
        long value = 42;
        byte[] expected = new byte[]{(byte) '0', (byte) '5', (byte) '2'};
        byte[] buffer = new byte[expected.length];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, expected.length);
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatUnsignedOctalStringLengthExceeded() {
        long value = 123456789;
        byte[] buffer = new byte[8]; // buffer is longer than the expected maximum number of bytes to fill
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        Assert.fail("Expected an IllegalArgumentException, but none was thrown");
    }

    @Test
    public void testFormatOctalBytesValid() {
        long value = 42;
        byte[] expected = new byte[]{(byte) '0', (byte) '5', (byte) '2', (byte) ' ', (byte) '\u0000'};
        byte[] buffer = new byte[expected.length];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, expected.length);
        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(offset, expected.length);
    }

    @Test
    public void testFormatOctalBytesLengthExceeded() {
        long value = 123456789;
        byte[] buffer = new byte[8]; // buffer is longer than the expected maximum number of bytes to fill
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        Assert.fail("Expected an IllegalArgumentException, but none was thrown");
    }

    @Test
    public void testFormatCheckSumOctalBytesValid() {
        long value = 42;
        byte[] expected = new byte[]{(byte) '0', (byte) '5', (byte) '2', (byte) '\u0000', (byte) ' '};
        byte[] buffer = new byte[expected.length];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, expected.length);
        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(offset, expected.length);
    }

    @Test
    public void testFormatCheckSumOctalBytesLengthExceeded() {
        long value = 123456789;
        byte[] buffer = new byte[8]; // buffer is longer than the expected maximum number of bytes to fill
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        Assert.fail("Expected an IllegalArgumentException, but none was thrown");
    }

    @Test
    public void testComputeCheckSumValid() {
        byte[] buffer = new byte[]{(byte) 0x78, (byte) 0x9A, (byte) 0x2B, (byte) 0xEE, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0x2B, (byte) 0xEE, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0x2B, (byte) 0xEE, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};
        long expected = 0x09358B54D8D9202B52789A789A2BEE123456789A2BEE123456789A2BEE12345656789A2BEE12345656789A2BEE1234565678;
        long result = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, result);
    }
}