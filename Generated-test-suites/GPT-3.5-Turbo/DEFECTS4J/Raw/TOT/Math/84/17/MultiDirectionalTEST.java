import static org.junit.Assert.*;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();

        // Create a comparator
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                // Compare based on the objective function values
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Test when the simplex is already optimal
        RealPointValuePair[] simplex1 = new RealPointValuePair[3];
        simplex1[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        simplex1[1] = new RealPointValuePair(new double[]{1.1, 2.2, 3.3}, 0.1);
        simplex1[2] = new RealPointValuePair(new double[]{1.2, 2.4, 3.6}, 0.2);

        multiDirectional.setSimplex(simplex1);
        try {
            multiDirectional.iterateSimplex(comparator);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        RealPointValuePair[] expectedSimplex1 = new RealPointValuePair[3];
        expectedSimplex1[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        expectedSimplex1[1] = new RealPointValuePair(new double[]{1.1, 2.2, 3.3}, 0.1);
        expectedSimplex1[2] = new RealPointValuePair(new double[]{1.2, 2.4, 3.6}, 0.2);

        assertArrayEquals(expectedSimplex1, multiDirectional.getSimplex());

        // Test reflection step
        RealPointValuePair[] simplex2 = new RealPointValuePair[3];
        simplex2[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        simplex2[1] = new RealPointValuePair(new double[]{0.9, 1.8, 2.7}, 0.1);
        simplex2[2] = new RealPointValuePair(new double[]{0.8, 1.6, 2.4}, 0.2);

        multiDirectional.setSimplex(simplex2);
        try {
            multiDirectional.iterateSimplex(comparator);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        RealPointValuePair[] expectedSimplex2 = new RealPointValuePair[3];
        expectedSimplex2[0] = new RealPointValuePair(new double[]{0.8, 1.6, 2.4}, 0.2);
        expectedSimplex2[1] = new RealPointValuePair(new double[]{0.9, 1.8, 2.7}, 0.1);
        expectedSimplex2[2] = new RealPointValuePair(new double[]{0.8, 1.6, 2.4}, 0.2);

        assertArrayEquals(expectedSimplex2, multiDirectional.getSimplex());

        // Test acceptance of expanded simplex
        RealPointValuePair[] simplex3 = new RealPointValuePair[3];
        simplex3[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        simplex3[1] = new RealPointValuePair(new double[]{0.9, 1.8, 2.7}, 0.1);
        simplex3[2] = new RealPointValuePair(new double[]{0.8, 1.6, 2.4}, -1.0);

        multiDirectional.setSimplex(simplex3);
        try {
            multiDirectional.iterateSimplex(comparator);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        RealPointValuePair[] expectedSimplex3 = new RealPointValuePair[3];
        expectedSimplex3[0] = new RealPointValuePair(new double[]{0.7, 1.4, 2.1}, -2.0);
        expectedSimplex3[1] = new RealPointValuePair(new double[]{0.9, 1.8, 2.7}, 0.1);
        expectedSimplex3[2] = new RealPointValuePair(new double[]{0.8, 1.6, 2.4}, -1.0);

        assertArrayEquals(expectedSimplex3, multiDirectional.getSimplex());

        // Test acceptance of contracted simplex
        RealPointValuePair[] simplex4 = new RealPointValuePair[3];
        simplex4[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        simplex4[1] = new RealPointValuePair(new double[]{0.9, 1.8, 2.7}, 0.1);
        simplex4[2] = new RealPointValuePair(new double[]{0.8, 1.6, 2.4}, 1.0);

        multiDirectional.setSimplex(simplex4);
        try {
            multiDirectional.iterateSimplex(comparator);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        RealPointValuePair[] expectedSimplex4 = new RealPointValuePair[3];
        expectedSimplex4[0] = new RealPointValuePair(new double[]{0.9, 1.8, 2.7}, 0.1);
        expectedSimplex4[1] = new RealPointValuePair(new double[]{0.8, 1.6, 2.4}, 1.0);
        expectedSimplex4[2] = new RealPointValuePair(new double[]{0.7, 1.4, 2.1}, 2.0);

        assertArrayEquals(expectedSimplex4, multiDirectional.getSimplex());
    }
}