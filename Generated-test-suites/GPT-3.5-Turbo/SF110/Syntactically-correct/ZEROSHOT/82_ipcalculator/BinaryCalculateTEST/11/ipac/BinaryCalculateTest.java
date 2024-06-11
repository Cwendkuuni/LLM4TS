import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {
    
    private BinaryCalculate binaryCalculate;
    
    @Before
    public void setUp() {
        binaryCalculate = new BinaryCalculate();
    }
    
    @Test
    public void testAddBinary() {
        String result = binaryCalculate.addBinary("110", "10");
        assertEquals("1000", result);
        
        result = binaryCalculate.addBinary("1010", "110");
        assertEquals("10000", result);
    }
    
    @Test
    public void testSubBinary() {
        String result = binaryCalculate.subBinary("110", "10");
        assertEquals("100", result);
        
        result = binaryCalculate.subBinary("1010", "110");
        assertEquals("100", result);
    }
    
    @Test
    public void testIPCalculate() {
        String result = binaryCalculate.IPCalculate("192.168.0.1", "24");
        assertNotNull(result);
        
        result = binaryCalculate.IPCalculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "64");
        assertNotNull(result);
    }
    
    @Test
    public void testGetBinaryIP() {
        String result = binaryCalculate.getBinaryIP("10101010");
        assertEquals("1010-1010", result);
    }
    
    @Test
    public void testGetNetmask() {
        String result = binaryCalculate.getNetmask("10101010");
        assertEquals("01010101", result);
    }
    
    @Test
    public void testCalculate() {
        binaryCalculate.calculate("192.168.0.1", "24");
        assertEquals("192.168.0.0", binaryCalculate.getStartIP());
        
        binaryCalculate.calculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "64");
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7330", binaryCalculate.getStartIP());
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        binaryCalculate.prefixInPrefixCalculate("192.168.0.1", "24", 2, "27");
        assertEquals("192.168.0.1", binaryCalculate.getStartIP());
    }    
}