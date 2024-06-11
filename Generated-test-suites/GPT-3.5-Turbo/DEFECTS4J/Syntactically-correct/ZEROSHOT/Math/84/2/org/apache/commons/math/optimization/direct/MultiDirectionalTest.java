import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setup() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Test when the reflected point is better than the best point
        RealPointValuePair[] original = new RealPointValuePair[4];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 10.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0, 4.0}, 20.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 4.0, 5.0}, 30.0);
        original[3] = new RealPointValuePair(new double[]{4.0, 5.0, 6.0}, 40.0);
        multiDirectional.setSimplex(original);
        multiDirectional.iterateSimplex(comparator);
        RealPointValuePair[] expectedSimplex = new RealPointValuePair[4];
        expectedSimplex[0] = original[1];
        expectedSimplex[1] = original[2];
        expectedSimplex[2] = original[3];
        expectedSimplex[3] = new RealPointValuePair(new double[]{2.0, 3.0, 4.0}, 20.0);
        assertArrayEquals(expectedSimplex, multiDirectional.getSimplex());

        // Test when the reflected point is worse than the best point
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 10.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0, 4.0}, 5.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 4.0, 5.0}, 30.0);
        original[3] = new RealPointValuePair(new double[]{4.0, 5.0, 6.0}, 40.0);
        multiDirectional.setSimplex(original);
        multiDirectional.iterateSimplex(comparator);
        expectedSimplex[0] = original[0];
        expectedSimplex[1] = original[3];
        expectedSimplex[2] = new RealPointValuePair(new double[]{3.0, 4.0, 5.0}, 30.0);
        expectedSimplex[3] = original[1];
        assertArrayEquals(expectedSimplex, multiDirectional.getSimplex());
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        RealPointValuePair[] original = new RealPointValuePair[4];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 10.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0, 4.0}, 20.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 4.0, 5.0}, 30.0);
        original[3] = new RealPointValuePair(new double[]{4.0, 5.0, 6.0}, 40.0);
        multiDirectional.setSimplex(original);
        multiDirectional.evaluateNewSimplex(original, 1.0, comparator);
        RealPointValuePair[] expectedSimplex = new RealPointValuePair[4];
        expectedSimplex[0] = original[0];
        expectedSimplex[1] = new RealPointValuePair(new double[]{0.0, 1.0, 2.0}, Double.NaN, false);
        expectedSimplex[2] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, Double.NaN, false);
        expectedSimplex[3] = new RealPointValuePair(new double[]{2.0, 3.0, 4.0}, Double.NaN, false);
        assertArrayEquals(expectedSimplex, multiDirectional.getSimplex());
    }

}