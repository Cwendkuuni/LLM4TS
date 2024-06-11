import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test);
    }

    @Test
    public void testChiSquareTestImplWithDistribution() {
        ChiSquareDistributionImpl distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        assertNotNull(test);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidArguments() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {-1};
        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareMethod() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0};
        long[] observed = {3.0, 2.0};
        double result = test.chiSquare(expected, observed);
        double expectedResult = 2.52; // Calculate the expected result using the provided input
        assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testChiSquareTestMethod() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0};
        long[] observed = {3.0, 2.0};
        double pValue = test.chiSquareTest(expected, observed);
        double expectedPValue = 0.253; // Calculate the expected p-value using the provided input
        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidArguments() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {-1};
        test.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidSignificanceLevel() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0};
        long[] observed = {3.0, 2.0};
        test.chiSquareTest(expected, observed, -0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidArguments() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 1.0};
        long[] observed = {-1, -1};
        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareDataSetsComparisonMethod() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4, 5};
        long[] observed2 = {4, 3, 2, 5};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        double expectedResult = 3.16; // Calculate the expected result using the provided input
        assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethod() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4, 5};
        long[] observed2 = {4, 3, 2, 5};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.094; // Calculate the expected p-value using the provided input
        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonMethodWithInvalidArguments() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3};
        long[] observed2 = {-1, 4};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithInvalidSignificanceLevel() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4, 5};
        long[] observed2 = {4, 3, 2, 5};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, -0.1);
        assertFalse(result);
    }
}