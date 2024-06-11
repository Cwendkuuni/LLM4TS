package ipac;

import static org.junit.Assert.*;
import org.junit.Test;

public class BinaryCalculateTest {
    
    @Test
    public void testAddBinary() {
        BinaryCalculate calc = new BinaryCalculate();
        String result = calc.addBinary("1010", "1101");
        assertEquals("Should add binary correctly", "10111", result);
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate calc = new BinaryCalculate();
        String result = calc.subBinary("1101", "1010");
        assertEquals("Should subtract binary correctly", "011", result);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate calc = new BinaryCalculate();
        String result = calc.IPCalculate("192.168.0.1", "24");
        assertNotNull("IP Calculation should return a string", result);
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate calc = new BinaryCalculate();
        calc.prefixInPrefixCalculate("192.168.0.1", "24", 2, "26");
        // Check for no exceptions thrown
    }
    
}
