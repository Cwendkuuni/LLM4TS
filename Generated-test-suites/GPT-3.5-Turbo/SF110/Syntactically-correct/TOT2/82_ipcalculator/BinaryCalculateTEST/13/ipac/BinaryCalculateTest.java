package ipac;
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
        assertEquals("11", bc.subBinary("100", "01"));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String expectedOutput = "Total Range: 192.168.0.1 -- 192.168.0.255\n" +
                                "Usable Range: 192.168.0.0 -- 192.168.0.254\n" +
                                "Total usable IP Addresses : 254\n" +
                                "Subnet: 192.168.0.0\n" +
                                "Binary Subnet: 11000000.10101000.00000000.00000000\n" +
                                "Broadcast Address: 192.168.0.255\n" +
                                "Prefix: /24\n" +
                                "Netmask: 255.255.255.0\n" +
                                "Binary Netmask: 11111111.11111111.11111111.00000000";
        assertEquals(expectedOutput, bc.IPCalculate("192.168.0.1", "24"));
    }
}