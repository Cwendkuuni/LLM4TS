import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    private BinaryCalculate calculate;

    @Before
    public void setup() {
        calculate = new BinaryCalculate();
    }

    @Test
    public void testAddBinary() {
        String num1 = "101";
        String num2 = "11";
        String expected = "1000";
        String result = calculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        String num1 = "1101";
        String num2 = "101";
        String expected = "1000";
        String result = calculate.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        String IP = "192.168.0.1";
        String IPPrefix = "24";
        String expected = "Total Range: 192.168.0.1 -- 192.168.0.255\n" +
                          "Usable Range: 192.168.0.0 -- 192.168.0.254\n" +
                          "\n" +
                          "Total usable IP Addresses : 254\n" +
                          "Subnet: 192.168.0.1\n" +
                          "Binary Subnet: 11000000.10101000.00000000.00000001\n" +
                          "Broadcast Address: \n" +
                          "Prefix: /24\n" +
                          "Netmask: 255.255.255.0\n" +
                          "Binary Netmask: 11111111.11111111.11111111.00000000";
        String result = calculate.IPCalculate(IP, IPPrefix);
        assertEquals(expected, result);
    }

    // Add more test cases for other methods

}