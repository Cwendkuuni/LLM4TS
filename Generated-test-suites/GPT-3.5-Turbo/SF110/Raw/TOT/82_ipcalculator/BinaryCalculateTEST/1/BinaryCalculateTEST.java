import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {
    
    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        
        // Typical use case
        String result1 = bc.addBinary("1011", "1101");
        assertEquals("11000", result1);
        
        // Edge cases
        String result2 = bc.addBinary("0", "0");
        assertEquals("0", result2);
        
        // Error scenario
        String result3 = bc.addBinary("11", "1001");
        assertNotEquals("111", result3);
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        
        // Typical use case
        String result1 = bc.subBinary("1011", "1101");
        assertEquals("1010", result1);
        
        // Edge cases
        String result2 = bc.subBinary("0", "0");
        assertEquals("0", result2);
        
        // Error scenario
        String result3 = bc.subBinary("11", "1010");
        assertNotEquals("101", result3);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        
        // Typical use case
        String result1 = bc.IPCalculate("192.168.0.1", "24");
        assertNotNull(result1);
        
        // Edge cases
        String result2 = bc.IPCalculate("255.255.255.255", "32");
        assertNotNull(result2);
        
        // Error scenario
        String result3 = bc.IPCalculate("192.168.0.1", "33");
        assertNull(result3);
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        
        // Typical use case
        bc.prefixInPrefixCalculate("192.168.0.0", "24", 5, "29");
        
        // No assertion needed
    }
}