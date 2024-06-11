import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    /*Test the optimize method for valid input and GoalType MINIMIZE*/
    @Test
    public void testOptimize_Minimize_ValidInput() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 3*x*x + 2*x + 1; //a simple quadratic function
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);

        Assert.assertEquals(-0.33333, result, 0.01);
    }

    /*Test the optimize method for valid input and GoalType MAXIMIZE*/
    @Test
    public void testOptimize_Maximize_ValidInput() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return -3*x*x + 2*x + 1; //a simple quadratic function
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);

        Assert.assertEquals(2.33333, result, 0.01);
    }

    /*Test the optimize method for valid input where min = max*/
    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_ValidInput_MinEqualsMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 3*x*x + 2*x + 1; //a simple quadratic function
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 2.0, 2.0);
    }

    /*Test the optimize method for invalid input where min > max*/
    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_ValidInput_MinGreaterThanMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 3*x*x + 2*x + 1; //a simple quadratic function
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 10.0, -10.0);
    }

    /*Test the optimize method for invalid input where function is null*/
    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_InvalidInput_NullFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = null;

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /*Test the optimize method for invalid input where GoalType is null*/
    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_InvalidInput_NullGoalType() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 3*x*x + 2*x + 1; //a simple quadratic function
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, null, -10.0, 10.0);
    }

    /*Test the localMin method for valid input*/
    @Test
    public void testLocalMin_ValidInput() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) {
                return 3*x*x + 2*x + 1; //a simple quadratic function
            }
        }, GoalType.MINIMIZE, -10.0, 0.0, 10.0, 0.001, 0.001);

        Assert.assertEquals(-0.33333, result, 0.01);
    }

    /*Test the localMin method for invalid input where eps <= 0*/
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_InvalidInput_EpsZero() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) {
                return 3*x*x + 2*x + 1; //a simple quadratic function
            }
        }, GoalType.MINIMIZE, -10.0, 0.0, 10.0, 0.0, 0.001);
    }

    /*Test the localMin method for invalid input where t <= 0*/
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_InvalidInput_TZero() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) {
                return 3*x*x + 2*x + 1; //a simple quadratic function
            }
        }, GoalType.MINIMIZE, -10.0, 0.0, 10.0, 0.001, 0.0);
    }

    /*Test the localMin method for invalid input where a > b*/
    @Test
    public void testLocalMin_InvalidInput_AGreaterThanB() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) {
                return 3*x*x + 2*x + 1; //a simple quadratic function
            }
        }, GoalType.MINIMIZE, 10.0, 0.0, -10.0, 0.001, 0.001);

        Assert.assertEquals(-0.33333, result, 0.01);
    }

    /*Test the localMin method for invalid input where maxIterationsCount is exceeded*/
    @Test(expected = MaxIterationsExceededException.class)
    public void testLocalMin_InvalidInput_MaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) + x - 1; //a function with local minimum and maximum
            }
        }, GoalType.MINIMIZE, -10.0, 0.0, 10.0, 0.001, 0.001);
    }

}