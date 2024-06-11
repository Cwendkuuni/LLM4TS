import static org.junit.Assert.*;
import java.util.Comparator;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testIterateSimplex_ReflectionStep() throws OptimizationException {
        MultiDirectional optimizer = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new MaxValueComparator();

        // Test data
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};

        RealPointValuePair[] simplex = new RealPointValuePair[4];
        simplex[0] = new RealPointValuePair(point1, 1.0);
        simplex[1] = new RealPointValuePair(point2, 2.0);
        simplex[2] = new RealPointValuePair(point3, 3.0);
        simplex[3] = new RealPointValuePair(point4, 4.0);

        // Set simplex for reflection step
        optimizer.setSimplex(simplex);

        // Expected result after reflection step
        RealPointValuePair reflected = new RealPointValuePair(new double[]{9.0, 10.0}, 5.0);

        // Perform the reflection step
        optimizer.iterateSimplex(comparator);

        // Check if the reflected simplex is as expected
        RealPointValuePair[] result = optimizer.getSimplex();
        assertEquals(reflected, result[0]);
    }

    @Test
    public void testIterateSimplex_ContractionStep() throws OptimizationException {
        MultiDirectional optimizer = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new MaxValueComparator();

        // Test data
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};

        RealPointValuePair[] simplex = new RealPointValuePair[4];
        simplex[0] = new RealPointValuePair(point1, 1.0);
        simplex[1] = new RealPointValuePair(point2, 2.0);
        simplex[2] = new RealPointValuePair(point3, 3.0);
        simplex[3] = new RealPointValuePair(point4, 4.0);

        // Set simplex for contraction step
        optimizer.setSimplex(simplex);

        // Expected result after contraction step
        RealPointValuePair contracted = new RealPointValuePair(new double[]{0.5, 1.0}, 0.0);

        // Perform the contraction step
        optimizer.iterateSimplex(comparator);

        // Check if the contracted simplex is as expected
        RealPointValuePair[] result = optimizer.getSimplex();
        assertEquals(contracted, result[0]);
    }

    @Test
    public void testIterateSimplex_Convergence() throws OptimizationException {
        MultiDirectional optimizer = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new MaxValueComparator();

        // Test data
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};

        RealPointValuePair[] simplex = new RealPointValuePair[4];
        simplex[0] = new RealPointValuePair(point1, 1.0);
        simplex[1] = new RealPointValuePair(point2, 2.0);
        simplex[2] = new RealPointValuePair(point3, 3.0);
        simplex[3] = new RealPointValuePair(point4, 4.0);

        // Set simplex for convergence check
        optimizer.setSimplex(simplex);

        // Expected result after convergence
        RealPointValuePair[] expected = new RealPointValuePair[4];
        expected[0] = new RealPointValuePair(point1, 1.0);
        expected[1] = new RealPointValuePair(point2, 2.0);
        expected[2] = new RealPointValuePair(point3, 3.0);
        expected[3] = new RealPointValuePair(point4, 4.0);

        // Perform the convergence check
        optimizer.iterateSimplex(comparator);

        // Check if the simplex remains unchanged after convergence
        RealPointValuePair[] result = optimizer.getSimplex();
        assertArrayEquals(expected, result);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplex_FunctionEvaluationException() throws OptimizationException {
        MultiDirectional optimizer = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new MaxValueComparator();

        // Test data
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};

        RealPointValuePair[] simplex = new RealPointValuePair[4];
        simplex[0] = new RealPointValuePair(point1, 1.0);
        simplex[1] = new RealPointValuePair(point2, 2.0);
        simplex[2] = new RealPointValuePair(point3, 3.0);
        simplex[3] = new RealPointValuePair(point4, 4.0);

        // Set simplex for function evaluation exception
        optimizer.setSimplex(simplex);

        // Perform the iteration with a function evaluation exception
        optimizer.iterateSimplex(comparator);
    }

    // Custom comparator class for testing
    private static class MaxValueComparator implements Comparator<RealPointValuePair> {
        public int compare(RealPointValuePair pair1, RealPointValuePair pair2) {
            return Double.compare(pair2.getValue(), pair1.getValue());
        }
    }

}