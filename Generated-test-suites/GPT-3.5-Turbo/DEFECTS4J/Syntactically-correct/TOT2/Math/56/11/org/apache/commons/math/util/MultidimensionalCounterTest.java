
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void TestMultidimensionalCounterConstructor() {
        // Test valid input sizes
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertNotNull(counter);

        // Test with negative input sizes
        try {
            MultidimensionalCounter counterNegative = new MultidimensionalCounter(-2, 4, 3);
            fail("Expected exception NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // success
        }
    }

    // Other test cases for each method
    // ...

}
