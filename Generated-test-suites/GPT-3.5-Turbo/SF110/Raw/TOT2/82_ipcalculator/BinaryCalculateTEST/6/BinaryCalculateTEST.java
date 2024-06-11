import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        assertEquals("100", bc.addBinary("11", "01"));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        assertEquals("01", bc.subBinary("100", "011"));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String expectedOutput = "Total Range: 192.168.0.0\nUsable Range: 192.168.0.1\nTotal usable IP Addresses : 2\nSubnet: 192.168.0.0\nBinary Subnet: 11000000.10101000.00000000.00000000\nBroadcast Address: 192.168.0.1\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
        assertEquals(expectedOutput, bc.IPCalculate("192.168.0.0", "24"));
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate bc = new BinaryCalculate();
        assertEquals("11000000.10101000", bc.getBinaryIP("1100000010101000"));
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate bc = new BinaryCalculate();
        assertEquals("00000011", bc.getNetmask("11111100"));
    }

    @Test
    public void testCheckIPType() {
        BinaryCalculate bc = new BinaryCalculate();
        bc.checkIPType(31);
        assertTrue(bc.isIPv4);
        assertEquals(32, bc.MAXPREFIX);
    }

}