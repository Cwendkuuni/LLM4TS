package ipac;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        assertEquals("110", bc.addBinary("11", "11"));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        assertEquals("10", bc.subBinary("101", "11"));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String expectedOutput = "Total Range: 192.168.0.0 -- 192.168.45.255\n"
                + "Usable Range: 192.168.0.1 -- 192.168.45.254\n"
                + "\n"
                + "Total usable IP Addresses : 11,776\n"
                + "Subnet: 192.168.45.0\n"
                + "Binary Subnet: 11000000.10101000.00101101.00000000\n"
                + "Broadcast Address: 192.168.45.255\n"
                + "Prefix: /16\n"
                + "Netmask: 255.255.255.0\n"
                + "Binary Netmask: 11111111.11111111.11111111.00000000";
        assertEquals(expectedOutput, bc.IPCalculate("192.168.0.0", "24"));
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        // Add test cases for prefix calculation in here
    }

}