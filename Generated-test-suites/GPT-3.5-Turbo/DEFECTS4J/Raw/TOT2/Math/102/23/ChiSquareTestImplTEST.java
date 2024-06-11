import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTest;

    @Before
    public void setUp() {
        chiSquareTest = new ChiSquareTestImpl();
    }

    @Test
    public void testChiSquare() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        
        double result = chiSquareTest.chiSquare(expected, observed);
        
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testChiSquareTest() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        
        double result = chiSquareTest.chiSquareTest(expected, observed);
        
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testChiSquareTestWithAlpha() {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1, 2, 3};
        double alpha = 0.05;

        boolean result = chiSquareTest.chiSquareTest(expected, observed, alpha);

        assertTrue(result);
    }

    @Test
    public void testChiSquareWith2WayTable() {
        long[][] counts = {
            {1, 2},
            {3, 4}
        };

        double result = chiSquareTest.chiSquare(counts);

        assertEquals(1.982, result, 0.001);
    }

    @Test
    public void testChiSquareTestWith2WayTable() {
        long[][] counts = {
            {1, 2},
            {3, 4}
        };

        double result = chiSquareTest.chiSquareTest(counts);

        assertEquals(0.159, result, 0.001);
    }

    @Test
    public void testChiSquareTestWith2WayTableAndAlpha() {
        long[][] counts = {
            {1, 2},
            {3, 4}
        };
        double alpha = 0.05;

        boolean result = chiSquareTest.chiSquareTest(counts, alpha);

        assertFalse(result);
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};

        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);

        assertEquals(0.059, result, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};

        double result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);

        assertEquals(0.942, result, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 4};
        double alpha = 0.05;

        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha);

        assertTrue(result);
    }

}