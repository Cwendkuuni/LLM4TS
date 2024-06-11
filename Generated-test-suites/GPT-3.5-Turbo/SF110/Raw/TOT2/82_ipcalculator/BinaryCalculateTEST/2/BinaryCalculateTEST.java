import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1010";
        String num2 = "1100";
        assertEquals("10110", binaryCalculate.addBinary(num1, num2));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1010";
        String num2 = "1001";
        assertEquals("1", binaryCalculate.subBinary(num1, num2));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String expected = "Total Range: 192.168.1.0 -- 192.168.1.255\nUsable Range: 192.168.1.1 -- 192.168.1.254\nTotal usable IP Addresses : 254\nSubnet: 192.168.1\nBinary Subnet: 11000000.10101000.00000001\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 11111111.11111111.11111111.00000000\nBinary Netmask: 11111111.11111111.11111111.00000000";
        assertEquals(expected, binaryCalculate.IPCalculate("192.168.1.0", "24"));
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        // Assuming edge cases and error scenario testing here
    }

}