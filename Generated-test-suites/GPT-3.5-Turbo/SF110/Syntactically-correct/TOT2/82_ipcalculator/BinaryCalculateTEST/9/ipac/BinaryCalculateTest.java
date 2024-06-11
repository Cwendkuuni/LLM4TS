package ipac;

import static org.junit.Assert.*;
import org.junit.Test;

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

        String result = binaryCalculate.IPCalculate("192.168.1.1", "24");

        assertNotNull(result);
    }

    // Add more test cases for other public methods

}
