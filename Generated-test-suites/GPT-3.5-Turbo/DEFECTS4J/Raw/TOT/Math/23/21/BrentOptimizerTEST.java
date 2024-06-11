import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    // Test cases for the BrentOptimizer class

    // Test cases for constructors

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withInvalidRelValue_throwsException() {
        double rel = -0.5;
        double abs = 0.001;
        new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withInvalidAbsValue_throwsException() {
        double rel = 0.5;
        double abs = -0.001;
        new BrentOptimizer(rel, abs);
    }

    @Test
    public void testConstructor_withValidValues_doesNotThrowException() {
        double rel = 0.5;
        double abs = 0.001;
        new BrentOptimizer(rel, abs);
    }

    // Test cases for optimize method

    @Test
    public void testOptimize_withPositiveGoalType_returnsCorrectResult() {
        double rel = 0.5;
        double abs = 0.001;
        double min = 1.0;
        double max = 10.0;
        double startValue = 5.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(startValue);
        optimizer.setGoalType(GoalType.MINIMIZE);

        double result = optimizer.optimize();

        // expected result = minimum value between 1 and 10
        Assert.assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testOptimize_withNegativeGoalType_returnsCorrectResult() {
        double rel = 0.5;
        double abs = 0.001;
        double min = 1.0;
        double max = 10.0;
        double startValue = 5.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(startValue);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        double result = optimizer.optimize();

        // expected result = maximum value between 1 and 10
        Assert.assertEquals(10.0, result, 0.001);
    }

}