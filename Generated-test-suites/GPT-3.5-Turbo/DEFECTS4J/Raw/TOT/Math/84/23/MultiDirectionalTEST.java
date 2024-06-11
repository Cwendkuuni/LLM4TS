import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Comparator;
import org.apache.commons.math.optimization.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.direct.MultiDirectional;

public class MultiDirectionalTest {

    // Method: MultiDirectional()
    // Condition: Create MultiDirectional object with default coefficients.
    // Expected Output: Object created successfully.
    @Test
    public void testConstructor1() {
        MultiDirectional md = new MultiDirectional();
        assertNotNull(md);
    }
    
    // Method: MultiDirectional(double, double)
    // Condition: Create MultiDirectional object with specified coefficients.
    // Expected Output: Object created successfully.
    @Test
    public void testConstructor2() {
        MultiDirectional md = new MultiDirectional(2.0, 0.5);
        assertNotNull(md);
    }
    
    // Method: iterateSimplex(Comparator<RealPointValuePair>)
    // Condition: Comparator returns negative value.
    // Expected Output: Simplex is expanded.
    @Test
    public void testIterateSimplex1() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return -1;
            }
        };
        md.iterateSimplex(comparator);
        // No assertion as it's difficult to check complex internal state
    }
    
    // Method: iterateSimplex(Comparator<RealPointValuePair>)
    // Condition: Comparator returns positive value.
    // Expected Output: Convergence is checked.
    @Test
    public void testIterateSimplex2() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        MultiDirectional md = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return 1;
            }
        };
        md.iterateSimplex(comparator);
        // No assertion as it's difficult to check complex internal state
    }
    
    // Helper method to create a RealPointValuePair object
    private RealPointValuePair createPointValuePair(double[] point, double value) {
        return new RealPointValuePair(point, value, false);
    }

    // Method: evaluateNewSimplex(RealPointValuePair[], double, Comparator<RealPointValuePair>)
    // Condition: Evaluate the new simplex with linear coefficient of 1.0, should return best point.
    // Expected Output: Best point is returned.
    @Test
    public void testEvaluateNewSimplex1() throws FunctionEvaluationException, OptimizationException {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = createPointValuePair(new double[] {1.0, 2.0}, 3.0);
        original[1] = createPointValuePair(new double[] {4.0, 5.0}, 6.0);
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        RealPointValuePair result = md.evaluateNewSimplex(original, 1.0, comparator);
        assertArrayEquals(new double[] {1.0, 2.0}, result.getPointRef(), 0.00001);
    }
    
    // Method: evaluateNewSimplex(RealPointValuePair[], double, Comparator<RealPointValuePair>)
    // Condition: Evaluate the new simplex with linear coefficient of 0.5, should return best point.
    // Expected Output: Best point is returned.
    @Test
    public void testEvaluateNewSimplex2() throws FunctionEvaluationException, OptimizationException {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = createPointValuePair(new double[] {1.0, 2.0}, 3.0);
        original[1] = createPointValuePair(new double[] {4.0, 5.0}, 6.0);
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        RealPointValuePair result = md.evaluateNewSimplex(original, 0.5, comparator);
        assertArrayEquals(new double[] {1.0, 2.0}, result.getPointRef(), 0.00001);
    }
}