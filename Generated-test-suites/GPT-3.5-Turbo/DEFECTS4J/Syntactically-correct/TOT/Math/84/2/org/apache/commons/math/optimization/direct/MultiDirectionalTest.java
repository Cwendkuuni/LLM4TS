import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testIterateSimplex() {
        // Create an instance of MultiDirectional with default coefficients
        MultiDirectional optimizer = new MultiDirectional();

        // Test the iteration when comparator.compare(reflected, best) < 0
        // Create a sample simplex with three vertices
        RealPointValuePair[] simplex = new RealPointValuePair[3];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, true);
        simplex[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 4.0, true);
        simplex[2] = new RealPointValuePair(new double[]{3.0, 4.0}, 5.0, true);

        // Set the simplex in the optimizer
        optimizer.setSimplex(simplex);

        // Create a sample comparator that always returns -1
        Comparator<RealPointValuePair> comparator = (o1, o2) -> -1;

        try {
            // Invoke the iterateSimplex method
            optimizer.iterateSimplex(comparator);

            // Verify that the simplex is updated with the reflected simplex
            assertArrayEquals(simplex, optimizer.getSimplex());

        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("Exception occurred during the iteration: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        // Create an instance of MultiDirectional with default coefficients
        MultiDirectional optimizer = new MultiDirectional();

        // Test the evaluation of a new simplex
        // Create a sample original simplex with three vertices
        RealPointValuePair[] original = new RealPointValuePair[4];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, true);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 4.0, true);
        original[2] = new RealPointValuePair(new double[]{3.0, 4.0}, 5.0, true);
        original[3] = new RealPointValuePair(new double[]{4.0, 5.0}, 6.0, true);

        // Create a sample comparator that always returns -1
        Comparator<RealPointValuePair> comparator = (o1, o2) -> -1;

        try {
            // Invoke the evaluateNewSimplex method
            RealPointValuePair result = optimizer.evaluateNewSimplex(original, 0.5, comparator);

            // Verify that the transformed simplex is updated correctly
            RealPointValuePair[] expectedSimplex = new RealPointValuePair[4];
            expectedSimplex[0] = original[0];
            expectedSimplex[1] = new RealPointValuePair(new double[]{1.5, 2.5}, Double.NaN, false);
            expectedSimplex[2] = new RealPointValuePair(new double[]{2.5, 3.5}, Double.NaN, false);
            expectedSimplex[3] = new RealPointValuePair(new double[]{3.5, 4.5}, Double.NaN, false);
            assertArrayEquals(expectedSimplex, optimizer.getSimplex());

            // Verify that the best point in the transformed simplex is returned
            assertEquals(expectedSimplex[0], result);

        } catch (FunctionEvaluationException | OptimizationException e) {
            fail("Exception occurred during the evaluation: " + e.getMessage());
        }
    }

}