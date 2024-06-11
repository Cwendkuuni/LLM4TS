import org.junit.Test;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_minimize() {
        // arrange
        double relativeThreshold = 1e-6;
        double absoluteThreshold = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // assert
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_maximize() {
        // arrange
        double relativeThreshold = 1e-6;
        double absoluteThreshold = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        // act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // assert
        assertEquals(10.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelativeThresholdTooSmall() {
        // arrange, act, assert
        double relativeThreshold = 1e-9;
        double absoluteThreshold = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsoluteThresholdNotStrictlyPositive() {
        // arrange, act, assert
        double relativeThreshold = 1e-6;
        double absoluteThreshold = -1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
    }

    // additional test cases for edge and error scenarios can be added here

}