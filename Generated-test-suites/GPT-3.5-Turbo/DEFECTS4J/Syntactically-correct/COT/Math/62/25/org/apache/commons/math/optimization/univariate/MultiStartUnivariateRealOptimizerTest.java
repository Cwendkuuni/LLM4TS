import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.util.FastMath;

public class MultiStartUnivariateRealOptimizerTest {

    // Public methods in MultiStartUnivariateRealOptimizer
    // optimize(UnivariateRealFunction, GoalType, double, double) method will be tested

    // Test basic functionality of the optimize method
    @Test
    public void testOptimize() throws FunctionEvaluationException {
        // Create a simple function for optimization
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Create an instance of the optimizer with 5 starts
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(
                new MyOptimizer(), 5, null);

        // Optimize the function
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 5, null);
        UnivariateRealPointValuePair optimum = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, -10, 10);

        // Check the result
        assertEquals(2.0, optimum.getPoint(), 0.0001);
        assertEquals(0.0, optimum.getValue(), 0.0001);
    }

    // Test if exception is thrown when function evaluation fails
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() throws FunctionEvaluationException {
        // Create a function that always throws an exception
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        // Create an instance of the optimizer with 1 start
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(
                new MyOptimizer(), 1, null);

        // Optimize the function
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 1, null);
        multiStartOptimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }

    // Test if exception is thrown when convergence fails
    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithConvergenceException() throws FunctionEvaluationException {
        // Create an instance of the optimizer with 1 start
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(
                new MyOptimizer(), 1, null);

        // Optimize the function
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 1, null);
        multiStartOptimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, GoalType.MINIMIZE, -10, 10);
    }

    // Test if the maximum number of evaluations is correctly set
    @Test
    public void testMaxEvaluations() throws FunctionEvaluationException {
        // Create an instance of the optimizer with 10 maximum evaluations and 2 starts
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(
                new MyOptimizer(), 2, null);
        optimizer.setMaxEvaluations(10);

        // Optimize the function
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 2, null);
        multiStartOptimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, GoalType.MINIMIZE, -10, 10);

        // Check the number of evaluations
        assertEquals(10, optimizer.getEvaluations());
    }

    // Test if the optima array is correctly filled
    @Test
    public void testGetOptima() throws FunctionEvaluationException {
        // Create an instance of the optimizer with 3 starts
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(
                new MyOptimizer(), 3, null);

        // Optimize the function
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 3, null);
        multiStartOptimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, GoalType.MINIMIZE, -10, 10);

        // Get the optima array
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();

        // Check the size of the array
        assertEquals(3, optima.length);

        // Check if the optima are sorted correctly
        assertEquals(-10.0, optima[0].getPoint(), 0.0001);
        assertEquals(-10.0, optima[0].getValue(), 0.0001);
        assertNull(optima[1]);
        assertNull(optima[2]);
    }
}