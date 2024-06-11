import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.addBinary("110", "101");
        assertEquals("1011", result);
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.subBinary("1011", "101");
        assertEquals("110", result);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.IPCalculate("192.168.0.1", "24");
        // Assert expected output
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        bc.prefixInPrefixCalculate("192.168.0.1", "24", 3, "27");
        // Assert expected output
    }
}