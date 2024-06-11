package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.addBinary("101", "110");
        assertEquals("Should return correct sum", "1011", result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.subBinary("1010", "100");
        assertEquals("Should return correct difference", "110", result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.IPCalculate("192.168.1.1", "24");
        assertNotNull("Should not return null", result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.getBinaryIP("101010101010");
        assertEquals("Should return correct binary IP format", "1010:1010:1010:", result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.getNetmask("11001100");
        assertEquals("Should return correct inverted netmask", "00110011", result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        bc.calculate("1010", "2");
        assertEquals("Should calculate correctly", "10", bc.prefix);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        bc.prefixInPrefixCalculate("192.168.1.1", "24", 2, "/27");
        assertNotNull("Should not throw an exception");
    }
}