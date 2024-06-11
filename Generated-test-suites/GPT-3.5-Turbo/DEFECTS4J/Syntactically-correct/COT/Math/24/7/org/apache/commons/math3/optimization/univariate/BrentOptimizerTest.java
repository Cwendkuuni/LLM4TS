
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor1() {
        double rel = 0.001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor2() {
        double rel = 0.0001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor3() {
        double rel = 0.001;
        double abs = -0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test
    public void testOptimize() {
        double rel = 0.001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);

        UnivariatePointValuePair result = optimizer.optimize();

        assertEquals(0, result.getPoint(), 0.0);
        assertEquals(0, result.getValue(), 0.0);
    }
}
