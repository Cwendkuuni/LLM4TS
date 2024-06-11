package ipac;

import static org.junit.Assert.assertEquals;

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
        // Add test case for subBinary method
    }

    @Test
    public void testIPCalculate() {
        // Add test case for IPCalculate method
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        // Add test case for prefixInPrefixCalculate method
    }
}
