import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String result = binaryCalculate.addBinary("1010", "1011");

        assertEquals("10101", result);
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String result = binaryCalculate.subBinary("1010", "1011");

        assertEquals("1111", result);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String result = binaryCalculate.IPCalculate("192.168.0.1", "24");

        assertEquals("Total Range: 192.168.0.1 -- 192.168.0.255\nUsable Range: 192.168.0.2 -- 192.168.0.254\n\nTotal usable IP Addresses : 253\nSubnet: 192.168.0.0\nBinary Subnet: 11000000.10101000.00000000\nBroadcast Address: 192.168.0.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.0", result);
    }
    
    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String result = binaryCalculate.getBinaryIP("11000000.10101000.00000000");

        assertEquals("110000001010100000000000", result);
    }
    
    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        String result = binaryCalculate.getNetmask("110000001010100000000000");

        assertEquals("001111110101011111111111", result);
    }
    
    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        binaryCalculate.calculate("192.168.0.1", "24");

        assertEquals("192.168.0.0", binaryCalculate.getStartIP());
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        binaryCalculate.prefixInPrefixCalculate("192.168.0.1", "24", 3, "/30");

        // No assert as it will only print output
    }
}