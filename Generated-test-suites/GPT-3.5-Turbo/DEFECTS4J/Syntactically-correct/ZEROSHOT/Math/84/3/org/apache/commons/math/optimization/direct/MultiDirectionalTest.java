import static org.junit.Assert.*;
import java.util.Comparator;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
    
    private MultiDirectional multiDirectional;
    
    @Before
    public void setup() {
        multiDirectional = new MultiDirectional();
    }
    
    @Test
    public void testIterateSimplex_WithValidComparator_DoesNotThrowException() {
        try {
            Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
                public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                    return Double.compare(o1.getValue(), o2.getValue());
                }
            };
            multiDirectional.iterateSimplex(comparator);
        } catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testEvaluateNewSimplex_WithValidParameters_DoesNotThrowException() {
        try {
            RealPointValuePair[] original = new RealPointValuePair[2];
            double[] x0 = {1.0, 2.0};
            double[] x1 = {2.0, 3.0};
            original[0] = new RealPointValuePair(x0, 10.0);
            original[1] = new RealPointValuePair(x1, 20.0);
            double coeff = 0.5;
            Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
                public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                    return Double.compare(o1.getValue(), o2.getValue());
                }
            };
            multiDirectional.evaluateNewSimplex(original, coeff, comparator);
        } catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
}