import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String num1 = "101";
        String num2 = "11";
        String expected = "1000";
        
        assertEquals(expected, binaryCalculate.addBinary(num1, num2));
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String num1 = "10101";
        String num2 = "11";
        String expected = "10010";
        
        assertEquals(expected, binaryCalculate.subBinary(num1, num2));
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String IP = "192.168.0.1";
        String IPPrefix = "24";
        String expected = "Total Range: 192.168.0.1 -- 192.168.0.255\n" +
                "Usable Range: 192.168.0.1 -- 192.168.0.254\n" +
                "\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 192.168.0.1\n" +
                "Binary Subnet: 11000000.10101000.00000000.00000001\n" +
                "Broadcast Address: 192.168.0.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";
        
        assertEquals(expected, binaryCalculate.IPCalculate(IP, IPPrefix));
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String binaryIP = "11000000.10101000.00000000.00000001";
        String expected = "11000000101010000000000000000001";
        
        assertEquals(expected, binaryCalculate.getBinaryIP(binaryIP));
    }
    
    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String binaryIP = "11000000.10101000.00000000.00000001";
        String expected = "00111111.01010111.11111111.11111110";
        
        assertEquals(expected, binaryCalculate.getNetmask(binaryIP));
    }
    
    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String subnet = "11000000.10101000.00000000.00000001";
        String noOfIPAddresses = "254";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        
        assertEquals("192.168.0.1", binaryCalculate.getStartIP());
        assertEquals("254", binaryCalculate.totalIPAddresses.toString());
        assertEquals("/24", binaryCalculate.prefix);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String IP = "192.168.0.1";
        String IPPrefix = "24";
        int noPrefix = 3;
        String endPrefix = "/26";
        
        binaryCalculate.prefixInPrefixCalculate(IP, IPPrefix, noPrefix, endPrefix);
        // Test assertions for output
    }
    
    // Add more test cases for other methods
    
}