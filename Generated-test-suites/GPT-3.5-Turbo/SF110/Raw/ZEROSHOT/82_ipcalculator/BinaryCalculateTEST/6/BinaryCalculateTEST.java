package ipac;

import org.junit.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("110", "11");
        Assert.assertEquals("1001", result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("110", "11");
        Assert.assertEquals("11", result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate("192.168.0.1", "24");
        // Assert the result
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.getBinaryIP("11000000101010000000000000000001");
        Assert.assertEquals("11000000.10101000.00000000.00000001", result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.getNetmask("11000000101010000000000000000001");
        Assert.assertEquals("00111111.01010111.11111111.11111110", result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.calculate("11000000101010000000000000000001", "24");
        // Assert the calculations
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.prefixInPrefixCalculate("11000000101010000000000000000001", "24", 2, "30");
        // Assert the calculations
    }

    @Test
    public void testCheckIPType() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.checkIPType(31);
        Assert.assertTrue(binaryCalculate.isIPv4);
        binaryCalculate.checkIPType(127);
        Assert.assertTrue(binaryCalculate.isIPv6);
    }

    @Test
    public void testConvert() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.convert("11000000101010000000000000000001");
        Assert.assertEquals("192.168.0.1", result);
    }

    @Test
    public void testConvertToBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.convertToBinary("192.168.0.1");
        Assert.assertEquals("11000000101010000000000000000001", result);
    }

    @Test
    public void testGetStartIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.IPRangeAddress = "192.168.0.1 - 192.168.0.255";
        String result = binaryCalculate.getStartIP();
        Assert.assertEquals("192.168.0.1", result);
    }
}