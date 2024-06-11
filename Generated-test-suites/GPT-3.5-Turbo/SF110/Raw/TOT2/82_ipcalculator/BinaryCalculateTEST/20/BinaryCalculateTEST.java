import static org.junit.Assert.*;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1010", "1100");
        assertEquals("Simple addition failed", "10110", result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("1100", "1010");
        assertEquals("Simple subtraction failed", "10", result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate("192.168.1.1", "24");
        assertNotNull(result);
        // Add more specific test cases for IP calculation here
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.getBinaryIP("11001100");
        assertEquals("Binary IP conversion failed", "1100", result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.getNetmask("11111111");
        assertEquals("Netmask generation failed", "00000000", result);
    }

    // Add more test cases for other methods as needed

}