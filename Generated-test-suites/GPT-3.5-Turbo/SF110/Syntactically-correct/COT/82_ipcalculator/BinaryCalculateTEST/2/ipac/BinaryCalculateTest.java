import org.junit.*;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        
        String result = bc.addBinary("0", "0");
        assertEquals("0", result);
        
        result = bc.addBinary("1", "0");
        assertEquals("1", result);
        
        result = bc.addBinary("1", "1");
        assertEquals("10", result);
        
        result = bc.addBinary("110", "11");
        assertEquals("1001", result);
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        
        String result = bc.subBinary("1010", "110");
        assertEquals("1000", result);
        
        result = bc.subBinary("11110", "11");
        assertEquals("11101", result);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        
        String result = bc.IPCalculate("192.168.0.1", "24");
        assertEquals("Total Range: 192.168.0.1 -- 192.168.0.254\nUsable Range: 192.168.0.0 -- 192.168.0.253\n\nTotal usable IP Addresses : 254\nSubnet: 192.168.0.0\nBinary Subnet: 11000000.10101000.00000000.00000000\nBroadcast Address: 192.168.0.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000", result);
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        
        bc.prefixInPrefixCalculate("192.168.0.1", "24", 3, "26");
        // Add assertion here
    }

}