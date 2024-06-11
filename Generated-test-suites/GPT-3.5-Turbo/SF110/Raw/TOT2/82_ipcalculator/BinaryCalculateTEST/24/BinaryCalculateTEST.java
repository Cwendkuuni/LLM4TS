import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {
    
    private BinaryCalculate binaryCalculate;
    
    @Before
    public void setUp() {
        binaryCalculate = new BinaryCalculate();
    }
    
    @Test
    public void testAddBinary() {
        String result = binaryCalculate.addBinary("1010", "110");
        assertEquals("10000", result);
    }
    
    @Test
    public void testSubBinary() {
        String result = binaryCalculate.subBinary("1010", "1");
        assertEquals("1001", result);
    }
    
    @Test
    public void testIPCalculate() {
        String result = binaryCalculate.IPCalculate("192.168.0.1", "24");
        // Assert the expected output
    }
    
    @Test
    public void testGetBinaryIP() {
        String result = binaryCalculate.getBinaryIP("10101010");
        // Assert the expected output
    }
    
    @Test
    public void testGetNetmask() {
        String result = binaryCalculate.getNetmask("10101010");
        // Assert the expected output
    }
    
    @Test
    public void testCalculate() {
        binaryCalculate.calculate("10101010", "24");
        // Assert the expected output
    }
    
    @Test
    public void testPrefixInPrefixCalculate() {
        binaryCalculate.prefixInPrefixCalculate("192.168.0.1", "24", 4, "28");
        // Assert the expected output
    }
    
    @Test
    public void testCheckIPType() {
        binaryCalculate.checkIPType(31);
        // Assert the expected output
    }
    
    @Test
    public void testConvert() {
        String result = binaryCalculate.convert("10101010");
        // Assert the expected output
    }
    
    @Test
    public void testConvertToBinary() {
        String result = binaryCalculate.convertToBinary("192.168.0.1");
        // Assert the expected output
    }
    
    @Test
    public void testGetStartIP() {
        String result = binaryCalculate.getStartIP();
        // Assert the expected output
    }
}