import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BaseAbstractUnivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithValidArguments() {
        double relativeThreshold = 0.0001;
        double absoluteThreshold = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        double relativeThreshold = 0.0001;
        double absoluteThreshold = -0.01;
        new BrentOptimizer(relativeThreshold, absoluteThreshold);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        double relativeThreshold = 0.0001;
        double absoluteThreshold = 0.00001;
        new BrentOptimizer(relativeThreshold, absoluteThreshold);
    }

    @Test
    public void testOptimize() {
        double relativeThreshold = 0.0001;
        double absoluteThreshold = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setStartValue(5);

        UnivariatePointValuePair result = optimizer.optimize();
        assertNotNull(result);
    }
}