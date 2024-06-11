import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {
    
    @Test
    public void parseOctal_WithValidData_ReturnsLongValue() {
        // Arrange
        byte[] buffer = "123".getBytes();
        int offset = 0;
        int length = buffer.length;

        // Act
        long result = TarUtils.parseOctal(buffer, offset, length);

        // Assert
        Assert.assertEquals(123, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void parseOctal_WithInvalidByte_ThrowsException() {
        // Arrange
        byte[] buffer = "123a".getBytes();
        int offset = 0;
        int length = buffer.length;

        // Act
        TarUtils.parseOctal(buffer, offset, length);
    }
    
    @Test
    public void parseName_WithValidData_ReturnsString() {
        // Arrange
        byte[] buffer = "Test File\0".getBytes();
        int offset = 0;
        int length = buffer.length;

        // Act
        String result = TarUtils.parseName(buffer, offset, length);

        // Assert
        Assert.assertEquals("Test File", result);
    }
    
    @Test
    public void formatNameBytes_WithValidData_ReturnsUpdatedOffset() {
        // Arrange
        String name = "Test File";
        byte[] buf = new byte[20];
        int offset = 0;
        int length = buf.length;

        // Act
        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        // Assert
        Assert.assertEquals(20, result);
        Assert.assertArrayEquals("Test File\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0", buf);
    }
    
    @Test
    public void formatUnsignedOctalString_WithValidData_FormatsOctalString() {
        // Arrange
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        // Act
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        // Assert
        Assert.assertArrayEquals("0000000017".getBytes(), buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void formatUnsignedOctalString_WithValueTooBig_ThrowsException() {
        // Arrange
        long value = 999999999999L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        // Act
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }
    
    @Test
    public void formatOctalBytes_WithValidData_ReturnsUpdatedOffset() {
        // Arrange
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;

        // Act
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        // Assert
        Assert.assertEquals(10, result);
        Assert.assertArrayEquals("0000000017 ".getBytes(), buf);
    }
    
    @Test
    public void formatLongOctalBytes_WithValidData_ReturnsUpdatedOffset() {
        // Arrange
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;

        // Act
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        // Assert
        Assert.assertEquals(10, result);
        Assert.assertArrayEquals("0000000017".getBytes(), buf);
    }
    
    @Test
    public void formatCheckSumOctalBytes_WithValidData_ReturnsUpdatedOffset() {
        // Arrange
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;

        // Act
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        // Assert
        Assert.assertEquals(10, result);
        Assert.assertArrayEquals("0000000017\0 ".getBytes(), buf);
    }
    
    @Test
    public void computeCheckSum_WithValidData_ReturnsChecksum() {
        // Arrange
        byte[] buf = "Test".getBytes();

        // Act
        long result = TarUtils.computeCheckSum(buf);

        // Assert
        Assert.assertEquals(436, result);
    }
}