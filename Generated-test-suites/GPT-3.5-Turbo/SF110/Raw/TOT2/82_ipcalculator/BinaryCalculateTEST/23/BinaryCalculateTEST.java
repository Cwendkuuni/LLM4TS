import static org.junit.Assert.*;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        assertEquals("10", binaryCalculate.addBinary("1", "1"));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        assertEquals("1", binaryCalculate.subBinary("10", "1"));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String expectedResult = "Total Range: 192.168.1.0 -- 192.168.1.255\nUsable Range: 192.168.1.1 -- 192.168.1.254\nTotal usable IP Addresses : 254\nSubnet: 11000000101010000000000100000000\nBinary Subnet: 11000000.10101000.00000001.00000000\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
        assertEquals(expectedResult, binaryCalculate.IPCalculate("192.168.1.0", "24"));
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        assertEquals("11000000.10101000.00000001.00000000", binaryCalculate.getBinaryIP("11000000101010000000000100000000"));
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        assertEquals("00000000.00000000.00000000.11111111", binaryCalculate.getNetmask("11111111111111111111111100000000"));
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.calculate("192.168.1.0", "24");
        assertNotNull(binaryCalculate.prefix);
    }

    @Test
    public void testCheckIPType() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.checkIPType(31);
        assertTrue(binaryCalculate.isIPv4);
    }
}