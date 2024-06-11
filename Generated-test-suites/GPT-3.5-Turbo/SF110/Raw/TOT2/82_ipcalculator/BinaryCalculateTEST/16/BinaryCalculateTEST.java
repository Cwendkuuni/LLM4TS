import org.junit.*;
import static org.junit.Assert.*;

public class BinaryCalculateTest {
    
    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        assertEquals("110", bc.addBinary("100", "10"));
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        assertEquals("10", bc.subBinary("100", "10"));
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String expectedOutput = "Total Range: 192.168.1.0\nUsable Range: 192.168.1.1\nTotal usable IP Addresses : 256";
        assertEquals(expectedOutput, bc.IPCalculate("192.168.1.0", "24"));
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        // Add test case for prefixInPrefixCalculate method
    }
}