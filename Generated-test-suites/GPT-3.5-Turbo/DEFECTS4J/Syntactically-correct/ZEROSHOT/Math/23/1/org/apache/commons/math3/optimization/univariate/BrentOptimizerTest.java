import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1: optimize a function that has a minimum value
        double rel1 = 1E-8;
        double abs1 = 1E-8;
        ConvergenceChecker<SimpleUnivariatePointValuePair> checker1 = new SimpleConvergenceChecker();
        BrentOptimizer optimizer1 = new BrentOptimizer(rel1, abs1, checker1);
        
        double min = optimizer1.optimize(100, n -> Math.pow(n - 3, 2)).getPoint();
        assertEquals(3.0, min, 1E-8);
        
        // Test case 2: optimize a function that has a maximum value
        double rel2 = 1E-6;
        double abs2 = 1E-4;
        ConvergenceChecker<SimpleUnivariatePointValuePair> checker2 = null;
        BrentOptimizer optimizer2 = new BrentOptimizer(rel2, abs2, checker2);
        
        double max = optimizer2.optimize(-100, n -> Math.pow(n - 3, 2)).getPoint();
        assertEquals(-3.0, max, 1E-5);
    }
}