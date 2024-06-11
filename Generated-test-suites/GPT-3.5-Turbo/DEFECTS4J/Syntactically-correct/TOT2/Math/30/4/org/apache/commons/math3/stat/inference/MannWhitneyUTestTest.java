import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU_NullInput() {
        MannWhitneyUTest test = new MannWhitneyUTest();
      
        try {
            test.mannWhitneyU(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testMannWhitneyU_EmptyInput() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        try {
            test.mannWhitneyU(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected exception
        }
    }

    @Test
    public void testMannWhitneyUTest_NullInput() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        try {
            test.mannWhitneyUTest(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testMannWhitneyUTest_EmptyInput() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        try {
            test.mannWhitneyUTest(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected exception
        }
    }

    // More test cases for edge cases and typical scenarios can be added here

}