import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate calculator = new BinaryCalculate();
        
        // Typical use case with positive numbers
        String result1 = calculator.addBinary("110", "101");
        assertEquals("1011", result1);

        // Edge case with one number being 0
        String result2 = calculator.addBinary("0", "1101");
        assertEquals("1101", result2);

        // Error scenario with invalid input
        String result3 = calculator.addBinary("01", "101");
        assertNull(result3);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate calculator = new BinaryCalculate();
        
        // Typical use case with positive numbers
        String result1 = calculator.subBinary("1011", "101");
        assertEquals("1010", result1);

        // Edge case with one number being all 1s
        String result2 = calculator.subBinary("1111", "110");
        assertEquals("10", result2);

        // Error scenario with invalid input
        String result3 = calculator.subBinary("101", "1101");
        assertNull(result3);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate calculator = new BinaryCalculate();
        
        // Typical use case with valid IP and prefix
        String result1 = calculator.IPCalculate("192.168.0.1", "24");
        assertNotNull(result1);

        // Edge case with minimum IP address and prefix
        String result2 = calculator.IPCalculate("0.0.0.0", "0");
        assertNotNull(result2);

        // Error scenario with invalid IP and prefix
        String result3 = calculator.IPCalculate("192.168.0", "24");
        assertNull(result3);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate calculator = new BinaryCalculate();
        
        // Typical use case with valid IP, prefix, and valid number of prefixes
        calculator.prefixInPrefixCalculate("192.168.0.1", "24", 5, "30");
        // Assert the expected output using a mocking framework or checking the console output
        
        // Edge case with minimum IP address, prefix, and 0 number of prefixes
        calculator.prefixInPrefixCalculate("0.0.0.0", "0", 0, "0");
        // Assert the expected output using a mocking framework or checking the console output

        // Error scenario with invalid IP, prefix, and negative number of prefixes
        calculator.prefixInPrefixCalculate("192.168.0", "24", -1, "30");
        // Assert the expected output using a mocking framework or checking the console output
    }

}