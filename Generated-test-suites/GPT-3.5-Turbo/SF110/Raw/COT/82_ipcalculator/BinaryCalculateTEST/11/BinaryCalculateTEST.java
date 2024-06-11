import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1010", "1101");
        assertEquals("11011", result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("1101", "1010");
        assertEquals("11", result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate("192.168.0.1", "24");
        assertTrue(result.contains("Total Range: "));
        assertTrue(result.contains("Usable Range: "));
        assertTrue(result.contains("Total usable IP Addresses : "));
        assertTrue(result.contains("Subnet: "));
        assertTrue(result.contains("Binary Subnet: "));
        assertTrue(result.contains("Broadcast Address: "));
        assertTrue(result.contains("Prefix: "));
        assertTrue(result.contains("Netmask: "));
        assertTrue(result.contains("Binary Netmask: "));
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.prefixInPrefixCalculate("192.168.0.1", "24", 3, "27");
        // Verify the output
        // ...
    }

    @Test
    public void testConvert() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.convert("11000000101010000000000000000001");
        assertEquals("192.168.0.1", result);
    }

    @Test
    public void testConvertToBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.convertToBinary("192.168.0.1");
        assertEquals("11000000101010000000000000000001", result);
    }

    @Test
    public void testCheckIPType() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.checkIPType(31);
        assertTrue(binaryCalculate.isIPv4);
        assertFalse(binaryCalculate.isIPv6);
        assertEquals(8, binaryCalculate.lengthOfToken);
        assertEquals(".", binaryCalculate.DELIMITER);
        assertEquals(32, binaryCalculate.MAXPREFIX);

        binaryCalculate.checkIPType(127);
        assertFalse(binaryCalculate.isIPv4);
        assertTrue(binaryCalculate.isIPv6);
        assertEquals(16, binaryCalculate.lengthOfToken);
        assertEquals(":", binaryCalculate.DELIMITER);
        assertEquals(128, binaryCalculate.MAXPREFIX);
    }

    @Test
    public void testGetStartIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.IPRangeAddress = "192.168.0.1 -- 192.168.0.50";
        String result = binaryCalculate.getStartIP();
        assertEquals("192.168.0.1", result);
    }
}