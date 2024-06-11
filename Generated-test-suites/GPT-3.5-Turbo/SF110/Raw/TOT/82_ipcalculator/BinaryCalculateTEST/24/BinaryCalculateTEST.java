import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {
    
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Test typical use case
        String result1 = binaryCalculate.addBinary("1010", "11");
        assertEquals("1111", result1);
        
        // Test edge case with carry
        String result2 = binaryCalculate.addBinary("1111", "1");
        assertEquals("10000", result2);
        
        // Test edge case without carry
        String result3 = binaryCalculate.addBinary("1111", "0");
        assertEquals("1111", result3);
        
        // Test error scenario with invalid input
        String result4 = binaryCalculate.addBinary("abc", "11");
        assertNull(result4);
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Test typical use case
        String result1 = binaryCalculate.subBinary("1100", "10");
        assertEquals("1010", result1);
        
        // Test edge case with carry
        String result2 = binaryCalculate.subBinary("1000", "1");
        assertEquals("111", result2);
        
        // Test edge case without carry
        String result3 = binaryCalculate.subBinary("1000", "0");
        assertEquals("1000", result3);
        
        // Test error scenario with invalid input
        String result4 = binaryCalculate.subBinary("abc", "10");
        assertNull(result4);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Test typical use case for IPv4
        String result1 = binaryCalculate.IPCalculate("192.168.1.1", "24");
        assertNotNull(result1);
        
        // Test typical use case for IPv6
        String result2 = binaryCalculate.IPCalculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "64");
        assertNotNull(result2);
        
        // Test edge case with maximum prefix for IPv4
        String result3 = binaryCalculate.IPCalculate("0.0.0.0", "32");
        assertNotNull(result3);
        
        // Test edge case with maximum prefix for IPv6
        String result4 = binaryCalculate.IPCalculate("::", "128");
        assertNotNull(result4);
        
        // Test error scenario with invalid input
        String result5 = binaryCalculate.IPCalculate("abc", "24");
        assertNull(result5);
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        
        // Test typical use case
        binaryCalculate.prefixInPrefixCalculate("192.168.0.0", "16", 4, "24");
        
        // Test edge case with maximum prefixes
        binaryCalculate.prefixInPrefixCalculate("192.168.0.0", "16", 65535, "24");
        
        // Test error scenario with invalid number of prefixes
        binaryCalculate.prefixInPrefixCalculate("192.168.0.0", "16", -1, "24");
    }
}