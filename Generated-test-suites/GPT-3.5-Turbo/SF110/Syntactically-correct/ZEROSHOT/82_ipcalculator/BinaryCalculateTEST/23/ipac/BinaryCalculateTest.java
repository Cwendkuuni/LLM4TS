import org.junit.*;
import static org.junit.Assert.*;

public class BinaryCalculateTest {
    
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Test case 1: 0 + 0 = 0
        String result1 = binaryCalculate.addBinary("0", "0");
        assertEquals("0", result1);
        
        // Test case 2: 1 + 0 = 1
        String result2 = binaryCalculate.addBinary("1", "0");
        assertEquals("1", result2);
        
        // Test case 3: 0 + 1 = 1
        String result3 = binaryCalculate.addBinary("0", "1");
        assertEquals("1", result3);
        
        // Test case 4: 1 + 1 = 10
        String result4 = binaryCalculate.addBinary("1", "1");
        assertEquals("10", result4);
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Test case 1: 0 - 0 = 0
        String result1 = binaryCalculate.subBinary("0", "0");
        assertEquals("0", result1);
        
        // Test case 2: 1 - 0 = 1
        String result2 = binaryCalculate.subBinary("1", "0");
        assertEquals("1", result2);
        
        // Test case 3: 0 - 1 = 1
        String result3 = binaryCalculate.subBinary("0", "1");
        assertEquals("1", result3);
        
        // Test case 4: 1 - 1 = 0
        String result4 = binaryCalculate.subBinary("1", "1");
        assertEquals("0", result4);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Test case 1: Empty IP and IPPrefix
        String result1 = binaryCalculate.IPCalculate("", "");
        assertEquals("Total Range: \nUsable Range: \n\nTotal usable IP Addresses : 0\nSubnet: \nBinary Subnet: \nBroadcast Address: \nPrefix: \nNetmask: \nBinary Netmask: ", result1);
        
        // Test case 2: Non-empty IP and IPPrefix
        String result2 = binaryCalculate.IPCalculate("192.168.0.1", "24");
        assertTrue(result2.contains("Total Range: "));
        assertTrue(result2.contains("Usable Range: "));
        assertTrue(result2.contains("Total usable IP Addresses : "));
        assertTrue(result2.contains("Subnet: "));
        assertTrue(result2.contains("Binary Subnet: "));
        assertTrue(result2.contains("Broadcast Address: "));
        assertTrue(result2.contains("Prefix: "));
        assertTrue(result2.contains("Netmask: "));
        assertTrue(result2.contains("Binary Netmask: "));
    }
    
    // More test cases for other methods
    
}