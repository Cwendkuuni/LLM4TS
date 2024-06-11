import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void addBinary_basicCase() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.addBinary("1010", "11");
        assertEquals("1111", result);
    }

    @Test
    public void addBinary_differentLengths() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.addBinary("1010", "1101");
        assertEquals("11011", result);
    }

    @Test
    public void addBinary_nonBinaryInput() {
        BinaryCalculate bc = new BinaryCalculate();
        // Add test case here
    }

    @Test
    public void addBinary_largeNumbers() {
        BinaryCalculate bc = new BinaryCalculate();
        // Add test case here
    }

    // Add more test cases for other methods here

}