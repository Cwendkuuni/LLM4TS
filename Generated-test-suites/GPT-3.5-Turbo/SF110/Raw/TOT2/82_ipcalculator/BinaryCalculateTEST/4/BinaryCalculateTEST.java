import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

    BinaryCalculate binaryCalculate;

    @Test
    public void testAddBinary() {
        binaryCalculate = new BinaryCalculate();
        assertEquals("101", binaryCalculate.addBinary("11", "10"));
    }

    @Test
    public void testSubBinary() {
        binaryCalculate = new BinaryCalculate();
        assertEquals("11", binaryCalculate.subBinary("101", "10"));
    }

    @Test
    public void testIPCalculate() {
        binaryCalculate = new BinaryCalculate();
        String expectedOutput = "Total Range: 192.168.0.0 - 192.168.0.255\n" +
                                "Usable Range: 192.168.0.1 - 192.168.0.254\n" +
                                "Total usable IP Addresses : 254\n" +
                                "Subnet: 192.168.0.0\n" +
                                "Binary Subnet: 11000000101010000000000000000000\n" +
                                "Broadcast Address: 192.168.0.255\n" +
                                "Prefix: /24\n" +
                                "Netmask: 11111111.11111111.11111111.00000000\n" +
                                "Binary Netmask: 11111111111111111111111100000000";
        assertEquals(expectedOutput, binaryCalculate.IPCalculate("192.168.0.0", "/24"));
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        binaryCalculate = new BinaryCalculate();
        // test case for prefixInPrefixCalculate method
    }
}