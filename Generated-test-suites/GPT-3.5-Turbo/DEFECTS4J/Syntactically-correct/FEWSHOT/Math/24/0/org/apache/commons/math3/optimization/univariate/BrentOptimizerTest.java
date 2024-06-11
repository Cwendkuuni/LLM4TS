
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private double relativeThreshold;
    private double absoluteThreshold;
    private ConvergenceChecker<UnivariatePointValuePair> convergenceChecker;

    @Before
    public void setUp() {
        relativeThreshold = 0.0001;
        absoluteThreshold = 0.01;
        convergenceChecker = null;
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new BrentOptimizer(relativeThreshold, -1);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new BrentOptimizer(0.00001, absoluteThreshold);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setSearchInterval(0, 10);
        optimizer.setMaxEvaluations(1000);
        optimizer.setStartValue(5);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(0, result.getPoint(), 0.001);
    }

    @Test
    public void testDoOptimizeWithMaxEvaluations() {
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setSearchInterval(0, 10);
        optimizer.setMaxEvaluations(10);
        optimizer.setStartValue(5);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(10, result.getPoint(), 0.001);
    }

    @Test
    public void testDoOptimizeWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setSearchInterval(0, 10);
        optimizer.setMaxIterations(100000);
        optimizer.setStartValue(5);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(0, result.getValue(), 0.001);
    }
}
