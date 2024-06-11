import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setup() {
        optimizer = new BrentOptimizer(0.0001, 0.001);
    }
    
    @Test
    public void testDoOptimize() {
        double minValue = optimizer.doOptimize().getValue();
        assertEquals(0.0, minValue, 0.001);
    }
    
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, -1.0);
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertEquals(b, best);
    }
    
    @Test
    public void testBestNull() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertNull(best);
    }
    
}