import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // TODO: Add test cases for each method

    // Test cases for constructor

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_withRelLessThanMinRelativeTolerance() {
        new BrentOptimizer(1e-15, 1e-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withAbsNotPositive() {
        new BrentOptimizer(1e-10, 0);
    }

    // Test cases for doOptimize

    @Test
    public void testDoOptimize_withMinimizationGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(2.5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertTrue(Precision.equals(result.getPoint(), 0, 1e-10));
    }

    @Test
    public void testDoOptimize_withMaximizationGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(2.5);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertTrue(Precision.equals(result.getPoint(), 10, 1e-10));
    }

    // TODO: Add more test cases

}