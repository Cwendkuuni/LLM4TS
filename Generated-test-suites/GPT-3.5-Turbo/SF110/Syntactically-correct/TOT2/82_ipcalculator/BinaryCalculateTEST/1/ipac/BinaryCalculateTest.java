package ipac;
import static org.junit.Assert.*;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1010", "1111");
        assertEquals("11001", result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("1101", "1000");
        assertEquals("0001", result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate("192.168.0.1", "24");
        assertNotNull(result);
        assertTrue(result.contains("Total Range"));
        assertTrue(result.contains("Usable Range"));
        assertTrue(result.contains("Total usable IP Addresses"));
    }

}