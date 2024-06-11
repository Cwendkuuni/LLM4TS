import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test 
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("101", "11");
        assertEquals("1000", result);
    }
    
    @Test 
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("101", "11");
        assertEquals("10", result);
    }
    
    @Test 
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate("192.168.0.1", "24");
        assertEquals("Total Range: 0 -- 0\nUsable Range: 0 -- 0\n\nTotal usable IP Addresses : 0\nSubnet: 192.168.0.0\nBinary Subnet: 110000001010100000000000\nBroadcast Address: \nPrefix: /\nNetmask: 0", result);
    }
    
    @Test 
    public void testgetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.getBinaryIP("11000000");
        assertEquals("11", result);
    }
    
    @Test 
    public void testgetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.getNetmask("11000000");
        assertEquals("00111111", result);
    }
    
    @Test 
    public void testcalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.calculate("11000000", "24");
        String result = binaryCalculate.IPRangeAddress;
        assertEquals("192.168.0 -- ", result);
    }
    
    @Test 
    public void testprefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.prefixInPrefixCalculate("192.168.0", "24", 1, "26");
        String resultStartIP = binaryCalculate.getStartIP();
        assertEquals("192.168.0", resultStartIP);
    }
    
    @Test 
    public void testcheckIPType() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.checkIPType(31);
        boolean isIPv4 = binaryCalculate.isIPv4;
        assertEquals(true, isIPv4);
    }
    
    @Test 
    public void testconvert() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.convert("1100000000000000");
        assertEquals("192.0", result);
    }
    
    @Test 
    public void testconvertToBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.convertToBinary("192 168 0");
        assertEquals("110000001010100000000000", result);
    }
    
    @Test 
    public void testgetStartIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.IPRangeAddress = "192.168.0 -- 192.168.255";
        String result = binaryCalculate.getStartIP();
        assertEquals("192.168.0", result);
    }
}