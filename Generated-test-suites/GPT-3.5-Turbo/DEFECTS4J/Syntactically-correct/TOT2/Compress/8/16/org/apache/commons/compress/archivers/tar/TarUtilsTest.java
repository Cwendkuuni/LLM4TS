import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = "1234".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(1234L, result);
    }

    @Test
    public void testParseOctal_InvalidByte() {
        byte[] buffer = "12$4".getBytes();
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = "TestName\0".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("TestName", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "TestFormat";
        byte[] buffer = new byte[10];
        int updatedOffset = TarUtils.formatNameBytes(name, buffer, 0, 5);
        Assert.assertEquals(5, updatedOffset);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 123456;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        Assert.assertEquals("0000017650", new String(buffer).trim());
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "TestString".getBytes();
        long result = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(1072L, result);
    }

}