import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {
    
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1101", "1001");
        assertEquals("10110", result);
    }
    
    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("1101", "1001");
        assertEquals("0100", result);
    }
    
    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate("192.168.1.1", "24");
        assertNotNull(result);
    }
    
    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.getBinaryIP("1101");
        assertEquals("1101", result);
    }
    
    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.getNetmask("1101");
        assertEquals("0010", result);
    }
    
    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.calculate("1101", "24");
        assertNotNull(binaryCalculate.getTotalIPAddresses());
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.prefixInPrefixCalculate("192.168.1.1", "24", 3, "28");
        // Test for exception scenario
    }
    
    @Test
    public void testCheckIPType() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.checkIPType(31);
        assertTrue(binaryCalculate.isIPv4);
    }
    
    @Test
    public void testConvert() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.convert("11001010");
        assertEquals("202", result);
    }
    
    @Test
    public void testConvertToBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.convertToBinary("192.168.1.1");
        assertEquals("11000000101010000000000100000001", result);
    }
    
    @Test
    public void testGetStartIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.calculate("1101", "24");
        String result = binaryCalculate.getStartIP();
        assertNotNull(result);
    }
}