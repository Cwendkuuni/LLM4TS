import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.optimization.RealPointValuePair;
import java.util.Comparator;
import java.util.Arrays;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        double[] point1 = {1.0, 2.0, 3.0};
        double[] point2 = {4.0, 5.0, 6.0};
        double[] point3 = {7.0, 8.0, 9.0};
        RealPointValuePair[] simplex = {
            new RealPointValuePair(point1, 0.0, false),
            new RealPointValuePair(point2, 0.0, false),
            new RealPointValuePair(point3, 0.0, false)
        };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        
        optimizer = new MultiDirectional();
        optimizer.setSimplex(simplex);
        optimizer.iterateSimplex(comparator);

        RealPointValuePair[] expected = {
            new RealPointValuePair(point1, 0.0, false),
            new RealPointValuePair(point2, 0.0, false),
            new RealPointValuePair(point3, 0.0, false)
        };
        RealPointValuePair[] actual = optimizer.getSimplex();

        assertTrue(Arrays.deepEquals(expected, actual));
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        double[] point1 = {1.0, 2.0, 3.0};
        double[] point2 = {4.0, 5.0, 6.0};
        double[] point3 = {7.0, 8.0, 9.0};
        RealPointValuePair[] original = {
            new RealPointValuePair(point1, 0.0, false),
            new RealPointValuePair(point2, 0.0, false),
            new RealPointValuePair(point3, 0.0, false)
        };
        double coeff = 0.5;
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        
        optimizer = new MultiDirectional();
        optimizer.setSimplex(original);
        RealPointValuePair expected = new RealPointValuePair(point1, 0.0, false);
        RealPointValuePair actual = optimizer.evaluateNewSimplex(original, coeff, comparator);

        assertEquals(expected, actual);
    }
}