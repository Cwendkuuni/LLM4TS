import org.junit.Assert;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Typical Use Case
        String result1 = binaryCalculate.addBinary("1010", "0101");
        Assert.assertEquals("1111", result1);
        
        // Edge Case: Adding binary numbers with leading zeros
        String result2 = binaryCalculate.addBinary("00011", "00110");
        Assert.assertEquals("01001", result2);
        
        // Error Scenario: Adding binary numbers with different lengths
        String result3 = binaryCalculate.addBinary("1001", "10");
        Assert.assertEquals("Error", result3);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Typical Use Case
        String result1 = binaryCalculate.subBinary("1000", "0010");
        Assert.assertEquals("0110", result1);
        
        // Edge Case: Subtracting binary numbers resulting in a negative difference
        String result2 = binaryCalculate.subBinary("1010", "1100");
        Assert.assertEquals("Error", result2);
        
        // Error Scenario: Subtracting binary numbers with different lengths
        String result3 = binaryCalculate.subBinary("1001", "10");
        Assert.assertEquals("Error", result3);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Typical Use Case
        String result1 = binaryCalculate.IPCalculate("192.168.0.1", "24");
        Assert.assertEquals("Expected output text", result1);
        
        // Edge Case: IP with maximum number of characters
        String result2 = binaryCalculate.IPCalculate("255.255.255.255", "32");
        Assert.assertEquals("Expected output text", result2);
        
        // Error Scenario: Invalid IP address format
        String result3 = binaryCalculate.IPCalculate("256.0.0.0", "24");
        Assert.assertEquals("Error", result3);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Typical Use Case
        binaryCalculate.prefixInPrefixCalculate("192.168.0.0", "24", 3, "27");
        // Assert expected results
        
        // Edge Case: Maximum number of prefixes
        binaryCalculate.prefixInPrefixCalculate("192.168.0.0", "24", 7, "26");
        // Assert expected results
        
        // Error Scenario: Invalid IP address format
        binaryCalculate.prefixInPrefixCalculate("256.0.0.0", "24", 2, "25");
        // Assert expected results
    }
}