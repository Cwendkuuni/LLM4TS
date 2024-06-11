import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        
        // Typical use case
        String result1 = bc.addBinary("110", "101");
        assertEquals("1011", result1);
        
        // Edge case
        String result2 = bc.addBinary("0", "1");
        assertEquals("1", result2);
        
        // Error scenario
        String result3 = bc.addBinary("a", "101");
        assertEquals("", result3);
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        
        // Typical use case
        String result1 = bc.subBinary("1010", "110");
        assertEquals("100", result1);
        
        // Edge case
        String result2 = bc.subBinary("0", "1");
        assertEquals("1", result2);
        
        // Error scenario
        String result3 = bc.subBinary("a", "101");
        assertEquals("", result3);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        
        // Typical use case
        String result1 = bc.IPCalculate("192.168.1.1", "24");
        // TODO: assertion
        
        // Edge case
        String result2 = bc.IPCalculate("0.0.0.0", "32");
        // TODO: assertion
        
        // Error scenario
        String result3 = bc.IPCalculate("12345", "24");
        // TODO: assertion
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        
        // Typical use case
        bc.prefixInPrefixCalculate("192.168.1.1", "24", 2, "28");
        // TODO: assertion
        
        // Edge case
        bc.prefixInPrefixCalculate("0.0.0.0", "32", 2, "32");
        // TODO: assertion
        
        // Error scenario
        bc.prefixInPrefixCalculate("12345", "24", 2, "28");
        // TODO: assertion
    }
}