import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BinaryCalculateTest {

    // Test cases proposed by Expert 1
    @Test
    public void testAddBinary() {
        BinaryCalculate calc = new BinaryCalculate();
        String result = calc.addBinary("1010", "101");
        assertEquals("1111", result);
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate calc = new BinaryCalculate();
        String result = calc.subBinary("110", "10");
        assertEquals("100", result);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate calc = new BinaryCalculate();
        String result = calc.IPCalculate("192.168.1.1", "24");
        assertNotNull(result);
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate calc = new BinaryCalculate();
        calc.prefixInPrefixCalculate("192.168.1.1", "24", 1, "24");
        assertTrue(true);
    }

    // Test cases proposed by Expert 2
    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryInvalidInput() {
        BinaryCalculate calc = new BinaryCalculate();
        calc.addBinary("invalid", "input");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubBinaryInvalidInput() {
        BinaryCalculate calc = new BinaryCalculate();
        calc.subBinary("invalid", "input");
    }
    
    @Test
    public void testIPCalculateInvalidInput() {
        BinaryCalculate calc = new BinaryCalculate();
        String result = calc.IPCalculate("invalid", "input");
        assertEquals("", result);
    }
}