import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class MannWhitneyUTestTest {
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    /**
     * Test UTest constructor with default NaN strategy and ties strategy.
     * The constructor should create a MannWhitneyUTest instance with NaN strategy FIXED and ties strategy AVERAGE.
     */
    @Test
    public void testMannWhitneyUTestConstructor() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        assertNotNull(uTest);
    }

    /**
     * Test UTest constructor with user-defined NaN strategy and ties strategy.
     * The constructor should create a MannWhitneyUTest instance with the specified NaN strategy and ties strategy.
     */
    @Test
    public void testMannWhitneyUTestConstructorWithStrategies() {
        MannWhitneyUTest uTest = new MannWhitneyUTest(NaNStrategy.MAXIMAL, TiesStrategy.RANDOM);
        assertNotNull(uTest);
    }

    /**
     * Test mannWhitneyU method with valid input.
     * The method should return the correct Mann-Whitney U statistic.
     */
    @Test
    public void testMannWhitneyUWithValidInput() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        assertEquals(0, Double.compare(uTest.mannWhitneyU(x, y), 0.0));
    }

    /**
     * Test mannWhitneyU method with null input.
     * The method should throw a NullArgumentException.
     */
    @Test
    public void testMannWhitneyUWithNullInput() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = null;
        exceptionRule.expect(NullArgumentException.class);
        uTest.mannWhitneyU(x, y);
    }

    /**
     * Test mannWhitneyU method with empty input.
     * The method should throw a NoDataException.
     */
    @Test
    public void testMannWhitneyUWithEmptyInput() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        exceptionRule.expect(NoDataException.class);
        uTest.mannWhitneyU(x, y);
    }

    /**
     * Test mannWhitneyUTest method with valid input.
     * The method should return the correct p-value.
     */
    @Test
    public void testMannWhitneyUTestWithValidInput() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        assertEquals(0, Double.compare(uTest.mannWhitneyUTest(x, y), 0.0));
    }

    /**
     * Test mannWhitneyUTest method with null input.
     * The method should throw a NullArgumentException.
     */
    @Test
    public void testMannWhitneyUTestWithNullInput() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = null;
        exceptionRule.expect(NullArgumentException.class);
        uTest.mannWhitneyUTest(x, y);
    }

    /**
     * Test mannWhitneyUTest method with empty input.
     * The method should throw a NoDataException.
     */
    @Test
    public void testMannWhitneyUTestWithEmptyInput() {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        exceptionRule.expect(NoDataException.class);
        uTest.mannWhitneyUTest(x, y);
    }

    /**
     * Test calculateAsymptoticPValue method with valid input.
     * The method should return the correct asymptotic p-value.
     */
    @Test
    public void testCalculateAsymptoticPValueWithValidInput() throws Exception {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        assertEquals(0, Double.compare(uTest.calculateAsymptoticPValue(10.0, 5, 5), 1.0));
    }

    /**
     * Test calculateAsymptoticPValue method with invalid input.
     * The method should throw a MaxCountExceededException.
     */
    @Test
    public void testCalculateAsymptoticPValueWithInvalidInput() throws Exception {
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        exceptionRule.expect(MaxCountExceededException.class);
        uTest.calculateAsymptoticPValue(10.0, -5, -5);
    }
}