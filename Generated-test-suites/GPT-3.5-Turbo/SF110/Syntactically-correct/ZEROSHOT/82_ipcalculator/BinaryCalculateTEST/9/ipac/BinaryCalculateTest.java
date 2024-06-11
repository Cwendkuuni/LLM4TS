import static org.junit.Assert.*;
import org.junit.Test;

public class BinaryCalculateTest {
    
    @Test
    public void testAddBinary() {
        BinaryCalculate calc = new BinaryCalculate();
        
        // Basic test cases
        assertEquals("10", calc.addBinary("1", "1"));
        assertEquals("100", calc.addBinary("11", "1"));
        assertEquals("100", calc.addBinary("1", "11"));
        assertEquals("110", calc.addBinary("101", "1"));
        assertEquals("10110", calc.addBinary("1011", "1011"));
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate calc = new BinaryCalculate();
        
        // Basic test cases
        assertEquals("0", calc.subBinary("1", "1"));
        assertEquals("10", calc.subBinary("11", "1"));
        assertEquals("10", calc.subBinary("100", "10"));
        assertEquals("11101", calc.subBinary("110011", "11"));
        assertEquals("110", calc.subBinary("1010", "1000"));
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate calc = new BinaryCalculate();
        
        // Test IP address calculation
        String IP = "192.168.0.1";
        String IPPrefix = "/24";
        String expectedOutput = "Total Range: 192.168.0.1 -- 192.168.0.255\n" +
                                "Usable Range: 192.168.0.2 -- 192.168.0.254\n" +
                                "\n" +
                                "Total usable IP Addresses : " +
                                "254\n" +
                                "Subnet: 192.168.0.0\n" +
                                "Binary Subnet: 11000000.10101000.00000000\n" +
                                "Broadcast Address: 192.168.0.255\n" +
                                "Prefix: /24\n" +
                                "Netmask: 255.255.255.0\n" +
                                "Binary Netmask: 11111111.11111111.11111111.00000000";
        assertEquals(expectedOutput, calc.IPCalculate(IP, IPPrefix));
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate calc = new BinaryCalculate();
        
        // Test prefix in prefix calculation
        String IP = "192.168.0.1";
        String IPPrefix = "/24";
        int noPrefix = 3;
        String endPrefix = "/27";
        
        // No assertions, just run the method to check for any errors
        calc.prefixInPrefixCalculate(IP, IPPrefix, noPrefix, endPrefix);
    }
}