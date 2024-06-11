import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.junit.Before;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTest;

    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
    }

    @Test
    public void testChiSquare() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};

        double result = chiSquareTest.chiSquare(expected, observed);

        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testChiSquareTest() throws MathException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};

        double result = chiSquareTest.chiSquareTest(expected, observed);

        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2, 3};

        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);

        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2, 3};

        double result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);

        assertEquals(1.0, result, 0.0001);
    }

}