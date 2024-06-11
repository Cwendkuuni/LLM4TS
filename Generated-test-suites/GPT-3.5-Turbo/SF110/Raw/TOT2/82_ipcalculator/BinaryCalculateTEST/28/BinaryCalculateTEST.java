import static org.junit.Assert.*;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        String result = binaryCalculator.addBinary("10", "11");
        assertEquals("101", result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        String result = binaryCalculator.subBinary("1010", "110");
        assertEquals("1000", result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        String result = binaryCalculator.IPCalculate("192.168.1.1", "24");
        assertNotNull(result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        String result = binaryCalculator.getBinaryIP("11011010101101");
        assertEquals("1101101", result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        String result = binaryCalculator.getNetmask("11011010101101");
        assertEquals("00100101010010", result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        binaryCalculator.calculate("1010", "100");
        assertNotNull(binaryCalculator.getPrefix());
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        binaryCalculator.prefixInPrefixCalculate("192.168.1.1", "24", 4, "30");
        assertEquals("30", binaryCalculator.getPrefix());
    }

    @Test
    public void testCheckIPType() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        binaryCalculator.checkIPType(31);
        assertTrue(binaryCalculator.isIPv4);
    }

    @Test
    public void testConvert() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        String result = binaryCalculator.convert("11011010101101");
        assertEquals("173.173.173", result);
    }

    @Test
    public void testConvertToBinary() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        String result = binaryCalculator.convertToBinary("173 173 173");
        assertEquals("101011011010110101100101", result);
    }

    @Test
    public void testGetStartIP() {
        BinaryCalculate binaryCalculator = new BinaryCalculate();
        binaryCalculator.calculate("1010", "100");
        String result = binaryCalculator.getStartIP();
        assertEquals("21.0.0.0", result);
    }
}