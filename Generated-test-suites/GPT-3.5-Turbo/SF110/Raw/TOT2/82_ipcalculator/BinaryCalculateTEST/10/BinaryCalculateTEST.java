import static org.junit.Assert.*;
import org.junit.Test;

public class BinaryCalculateTest {
    
    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        
        assertEquals("11", bc.addBinary("1", "10"));
        assertEquals("1111", bc.addBinary("110", "101"));
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        
        assertEquals("10", bc.subBinary("111", "101"));
        assertEquals("1", bc.subBinary("110", "101"));
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        
        String expectedOutput = "Total Range: 000 - 111\nUsable Range: 000 - 111\nTotal usable IP Addresses : 8\nSubnet: 000\nBinary Subnet: 000\nBroadcast Address: 111\nPrefix: /3\nNetmask: 111\nBinary Netmask: 111";
        assertEquals(expectedOutput, bc.IPCalculate("000", "3"));
    }
    
    @Test
    public void testGetBinaryIP() {
        BinaryCalculate bc = new BinaryCalculate();
        
        assertEquals("000", bc.getBinaryIP("000"));
        assertEquals("111", bc.getBinaryIP("111"));
    }
    
    @Test
    public void testGetNetmask() {
        BinaryCalculate bc = new BinaryCalculate();
        
        assertEquals("111", bc.getNetmask("000"));
        assertEquals("000", bc.getNetmask("111"));
    }
    
    @Test
    public void testCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        
        bc.calculate("000", "3");
        assertEquals("000", bc.getStartIP());
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        
        bc.prefixInPrefixCalculate("000", "3", 2, "00");
        // Assert more based on expected outputs
    }
    
    // Add more tests for edge cases and exceptions
    
}