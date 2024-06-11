import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        // Typical use case
        String result = binaryCalculate.addBinary("1010", "1100");
        assertEquals("10110", result);

        // Edge case
        result = binaryCalculate.addBinary("", "");
        assertEquals("", result);

        // Error scenario
        result = binaryCalculate.addBinary("1010", "111b");
        assertEquals("", result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        // Typical use case
        String result = binaryCalculate.subBinary("1100", "1010");
        assertEquals("10", result);

        // Edge case
        result = binaryCalculate.subBinary("", "");
        assertEquals("", result);

        // Error scenario
        result = binaryCalculate.subBinary("1100", "111b");
        assertEquals("", result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        // Typical use case
        String result = binaryCalculate.IPCalculate("192.168.0.1", "24");
        assertEquals("Total Range: 192.168.0.0 -- 192.168.0.255\nUsable Range: 192.168.0.1 -- 192.168.0.254\n\nTotal usable IP Addresses : 254\nSubnet: 192.168.0.0\nBinary Subnet: 11000000.10101000.00000000.00000000\nBroadcast Address: 192.168.0.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000", result);

        // Edge case
        result = binaryCalculate.IPCalculate("", "");
        assertEquals("Total Range: \nUsable Range: \n\nTotal usable IP Addresses : 1\nSubnet: \nBinary Subnet: \nBroadcast Address: \nPrefix: \nNetmask: \nBinary Netmask: ", result);

        // Error scenario
        result = binaryCalculate.IPCalculate("192.168.0.b", "24");
        assertEquals("", result);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        // Typical use case
        binaryCalculate.prefixInPrefixCalculate("192.168.0.1", "24", 2, "/26");
        // Validate the output in the Output class or assert the values in the output array

        // Edge case
        binaryCalculate.prefixInPrefixCalculate("", "", 0, "");
        // Validate the output in the Output class or assert the values in the output array

        // Error scenario
        binaryCalculate.prefixInPrefixCalculate("192.168.0.b", "24", 2, "/26");
        // Validate the output in the Output class or assert the values in the output array
    }
}