import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double optimum = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 5.0);

        Assert.assertEquals(0.0, optimum, EPSILON);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double optimum = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);

        Assert.assertEquals(0.0, optimum, EPSILON);
    }

    @Test
    public void testOptimizeWithMaximumIterationCount() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(5);
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            Assert.fail("Expected MaxIterationsExceededException to be thrown");
        } catch (MaxIterationsExceededException e) {
            // Pass test case
        } catch (FunctionEvaluationException e) {
            Assert.fail("Expected MaxIterationsExceededException to be thrown");
        }
    }

    @Test
    public void testOptimizeWithNotStrictlyPositiveException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 5.0);
            Assert.fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (FunctionEvaluationException e) {
            Assert.fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Pass test case
        }
    }

    
    // Add more test cases here
    
}