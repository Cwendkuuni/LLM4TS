import static org.junit.Assert.*;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Comparator;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex_ReflectionStep() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Set up
        RealPointValuePair[] simplex = new RealPointValuePair[4];
        simplex[0] = new RealPointValuePair(new double[] {1.0, 1.0, 1.0}, 2.0);
        simplex[1] = new RealPointValuePair(new double[] {2.0, 2.0, 2.0}, 4.0);
        simplex[2] = new RealPointValuePair(new double[] {3.0, 3.0, 3.0}, 6.0);
        simplex[3] = new RealPointValuePair(new double[] {4.0, 4.0, 4.0}, 8.0);
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Action
        multiDirectional.iterateSimplex(comparator);

        // Assertion
        RealPointValuePair[] expectedSimplex = new RealPointValuePair[4];
        expectedSimplex[0] = new RealPointValuePair(new double[] {-1.0, -1.0, -1.0}, -2.0);
        expectedSimplex[1] = new RealPointValuePair(new double[] {2.0, 2.0, 2.0}, 4.0);
        expectedSimplex[2] = new RealPointValuePair(new double[] {3.0, 3.0, 3.0}, 6.0);
        expectedSimplex[3] = new RealPointValuePair(new double[] {4.0, 4.0, 4.0}, 8.0);
        assertTrue(Arrays.equals(expectedSimplex, multiDirectional.getSimplex()));
    }

    @Test
    public void testIterateSimplex_ContractionStep() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Set up
        RealPointValuePair[] simplex = new RealPointValuePair[4];
        simplex[0] = new RealPointValuePair(new double[] {1.0, 1.0, 1.0}, 2.0);
        simplex[1] = new RealPointValuePair(new double[] {2.0, 2.0, 2.0}, 4.0);
        simplex[2] = new RealPointValuePair(new double[] {3.0, 3.0, 3.0}, 6.0);
        simplex[3] = new RealPointValuePair(new double[] {4.0, 4.0, 4.0}, 8.0);
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Action
        multiDirectional.iterateSimplex(comparator);

        // Assertion
        RealPointValuePair[] expectedSimplex = new RealPointValuePair[4];
        expectedSimplex[0] = new RealPointValuePair(new double[] {-1.0, -1.0, -1.0}, -2.0);
        expectedSimplex[1] = new RealPointValuePair(new double[] {2.0, 2.0, 2.0}, 4.0);
        expectedSimplex[2] = new RealPointValuePair(new double[] {3.0, 3.0, 3.0}, 6.0);
        expectedSimplex[3] = new RealPointValuePair(new double[] {0.5, 0.5, 0.5}, 1.0);
        assertTrue(Arrays.equals(expectedSimplex, multiDirectional.getSimplex()));
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Set up
        RealPointValuePair[] originalSimplex = new RealPointValuePair[4];
        originalSimplex[0] = new RealPointValuePair(new double[] {1.0, 1.0, 1.0}, 2.0);
        originalSimplex[1] = new RealPointValuePair(new double[] {2.0, 2.0, 2.0}, 4.0);
        originalSimplex[2] = new RealPointValuePair(new double[] {3.0, 3.0, 3.0}, 6.0);
        originalSimplex[3] = new RealPointValuePair(new double[] {4.0, 4.0, 4.0}, 8.0);
        double coeff = 0.5;
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Action
        RealPointValuePair bestPoint = multiDirectional.evaluateNewSimplex(originalSimplex, coeff, comparator);

        // Assertion
        RealPointValuePair[] expectedSimplex = new RealPointValuePair[4];
        expectedSimplex[0] = new RealPointValuePair(new double[] {0.5, 0.5, 0.5}, 1.0);
        expectedSimplex[1] = new RealPointValuePair(new double[] {1.5, 1.5, 1.5}, 3.0);
        expectedSimplex[2] = new RealPointValuePair(new double[] {2.5, 2.5, 2.5}, 5.0);
        expectedSimplex[3] = new RealPointValuePair(new double[] {3.5, 3.5, 3.5}, 7.0);
        assertTrue(Arrays.equals(expectedSimplex, multiDirectional.getSimplex()));
        assertEquals(expectedSimplex[0], bestPoint);
    }

    @Test
    public void testEvaluateNewSimplex_Exception() {
        // Set up
        RealPointValuePair[] originalSimplex = new RealPointValuePair[3];
        originalSimplex[0] = new RealPointValuePair(new double[] {1.0, 1.0}, 2.0);
        originalSimplex[1] = new RealPointValuePair(new double[] {2.0, 2.0}, 4.0);
        originalSimplex[2] = new RealPointValuePair(new double[] {3.0, 3.0}, 6.0);
        double coeff = 0.5;
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Action and assertion
        assertThrows(IllegalArgumentException.class, () -> {
            multiDirectional.evaluateNewSimplex(originalSimplex, coeff, comparator);
        });
    }

    @Test
    public void testEvaluateNewSimplex_Exception2() {
        // Set up
        RealPointValuePair[] originalSimplex = new RealPointValuePair[4];
        originalSimplex[0] = new RealPointValuePair(new double[] {1.0, 1.0, 1.0}, 2.0);
        originalSimplex[1] = new RealPointValuePair(new double[] {2.0, 2.0}, 4.0);
        originalSimplex[2] = new RealPointValuePair(new double[] {3.0, 3.0}, 6.0);
        originalSimplex[3] = new RealPointValuePair(new double[] {4.0, 4.0}, 8.0);
        double coeff = 0.5;
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Action and assertion
        assertThrows(IllegalArgumentException.class, () -> {
            multiDirectional.evaluateNewSimplex(originalSimplex, coeff, comparator);
        });
    }

    @Test
    public void testEvaluateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Set up
        RealPointValuePair[] simplex = new RealPointValuePair[4];
        simplex[0] = new RealPointValuePair(new double[] {1.0, 1.0, 1.0}, 2.0);
        simplex[1] = new RealPointValuePair(new double[] {2.0, 2.0, 2.0}, 4.0);
        simplex[2] = new RealPointValuePair(new double[] {3.0, 3.0, 3.0}, 6.0);
        simplex[3] = new RealPointValuePair(new double[] {4.0, 4.0, 4.0}, 8.0);
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Action
        multiDirectional.evaluateSimplex(comparator);

        // Assertion
        RealPointValuePair[] expectedSimplex = new RealPointValuePair[4];
        expectedSimplex[0] = new RealPointValuePair(new double[] {1.0, 1.0, 1.0}, 2.0);
        expectedSimplex[1] = new RealPointValuePair(new double[] {2.0, 2.0, 2.0}, 4.0);
        expectedSimplex[2] = new RealPointValuePair(new double[] {3.0, 3.0, 3.0}, 6.0);
        expectedSimplex[3] = new RealPointValuePair(new double[] {4.0, 4.0, 4.0}, 8.0);
        assertTrue(Arrays.equals(expectedSimplex, multiDirectional.getSimplex()));
    }

}