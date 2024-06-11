import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.univariate.BaseAbstractUnivariateOptimizer;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(1e-6, 1e-8, null);
    }
    
    @Test
    public void testDoOptimize() {
        // Test case for a general optimization
        double min = optimizer.optimize(100, new UnivariateFunction() {
            public double value(double x) {
                return x * x * x - x * x + x - 1;
            }
        }).getPoint();
        assertEquals("General optimization test", 0.6823278038, min, 1e-6);
    }
    
    @Test
    public void testConstructor() {
        // Test case for creating an instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8, null);
        assertNotNull("BrentOptimizer instance not null", optimizer);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorAbsNotStrictlyPositive() {
        // Test case for when abs <= 0 in the constructor
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-8, null);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorRelTooSmall() {
        // Test case for when rel < 2 * Math.ulp(1d) in the constructor
        BrentOptimizer optimizer = new BrentOptimizer(1e-16, 1e-8, null);
    }
    
    @Test
    public void testConstructorWithoutChecker() {
        // Test case for creating an instance of BrentOptimizer without convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        assertNotNull("BrentOptimizer instance not null", optimizer);
    }
    
    @Test
    public void testDoOptimizeWithConvergenceChecker() {
        // Test case for optimization with a convergence checker
        double min = optimizer.optimize(100, new UnivariateFunction() {
            public double value(double x) {
                return x * x * x - x * x + x - 1;
            }
        }, GoalType.MINIMIZE, new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(previous.getValue() - current.getValue()) < 1e-6;
            }
        }).getPoint();
        assertEquals("Optimization with convergence checker", 0.6823278038, min, 1e-6);
    }
    
    @Test
    public void testBest() {
        // Test case for selecting the best of two points
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(2.0, 1.0);
        UnivariatePointValuePair bestPoint;
        
        // Test minimization
        bestPoint = optimizer.best(pointA, pointB, true);
        assertEquals("Best point selection (minimization)", pointB, bestPoint);
        
        // Test maximization
        bestPoint = optimizer.best(pointA, pointB, false);
        assertEquals("Best point selection (maximization)", pointA, bestPoint);
    }
}