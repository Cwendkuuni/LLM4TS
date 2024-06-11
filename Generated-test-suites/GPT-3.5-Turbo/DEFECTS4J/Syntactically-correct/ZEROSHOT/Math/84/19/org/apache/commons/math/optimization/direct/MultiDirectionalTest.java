import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setup() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        multiDirectional.iterateSimplex(comparator);
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[]{1.0, 1.0, 1.0}, 1.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 2.0, 2.0}, 2.0);

        double coeff = 0.5;

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, coeff, comparator);

        Assert.assertNotNull(result);
    }
}