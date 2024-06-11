import org.junit.Test;
import org.junit.Assert;

public class MultiDirectionalTest {
    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Setup
        MultiDirectional multiDirectional = new MultiDirectional();
        Double[] point = {1.0, 2.0};
        RealPointValuePair[] original = {new RealPointValuePair(point, 3.0, false), new RealPointValuePair(point, 4.0, false)};
        double coeff = 1.0;
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Run
        multiDirectional.iterateSimplex(comparator);

        // Verify
        Assert.assertEquals(0, multiDirectional.getIterationsCounter());
        Assert.assertArrayEquals(original, multiDirectional.getSimplex());
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        // Setup
        MultiDirectional multiDirectional = new MultiDirectional();
        Double[] originalPoint = {2.0, 3.0};
        RealPointValuePair[] original = {new RealPointValuePair(originalPoint, 4.0, false)};
        double coeff = 0.5;
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Run
        RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, coeff, comparator);

        // Verify
        Assert.assertEquals(original[0], result);
    }
}