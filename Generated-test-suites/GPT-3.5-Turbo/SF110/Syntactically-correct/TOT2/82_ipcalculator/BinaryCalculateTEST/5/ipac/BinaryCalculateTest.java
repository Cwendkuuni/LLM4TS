package ipac;
import static org.junit.Assert.*;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1010", "1101");
        assertEquals("The result of adding binary numbers is incorrect", "10111", result);
    }
    
    // Similarly, other test cases for remaining methods can be added.

}