import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateRealFunction function = new Function();

        // Test Minimization
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);
        optimizer.setAbsoluteAccuracy(1e-6);
        optimizer.setRelativeAccuracy(1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new SimpleBounds(1, 3), InitialGuess.THE_MINIMUM);
        GoalTypeChecker.assertIsMinimum(function, result.getPoint(), result.getValue());
        assertEquals(1.5, result.getPoint(), 1e-6);

        // Test Maximization
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair resultMax = optimizer.optimize(function, GoalType.MAXIMIZE, new SimpleBounds(1, 3), InitialGuess.THE_MAXIMUM);
        GoalTypeChecker.assertIsMaximum(function, resultMax.getPoint(), resultMax.getValue());
        assertEquals(2.5, resultMax.getPoint(), 1e-6);

        // Test with out of bounds initial guess
        UnivariateOptimizer optimizer2 = new BrentOptimizer();
        PointValuePair result2 = optimizer2.optimize(function, GoalType.MINIMIZE, new SimpleBounds(1, 3), new InitialGuess(-5));
        assertNotNull(result2);
        assertEquals(Double.NaN, result2.getPoint(), 1e-6);
        assertEquals(Double.NaN, result2.getValue(), 1e-6);

        // Test with function that does not converge
        UnivariateRealFunction functionDiverge = new FunctionDiverge();
        UnivariateOptimizer optimizerDiverge = new BrentOptimizer();
        try {
            optimizerDiverge.optimize(functionDiverge, GoalType.MINIMIZE, new SimpleBounds(1, 3), InitialGuess.THE_MINIMUM);
        } catch (MaxIterationsExceededException e) {
            // expected
        } catch (NonConvergentException e) {
            // expected
        }

        // Test with function that throws exception
        UnivariateRealFunction functionException = new FunctionException();
        UnivariateOptimizer optimizerException = new BrentOptimizer();
        try {
            optimizerException.optimize(functionException, GoalType.MINIMIZE, new SimpleBounds(1, 3), InitialGuess.THE_MINIMUM);
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }
}

class Function implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 1.5 * x + 2;
    }
}

class FunctionDiverge implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, -1);
    }
}

class FunctionException extends UnivariateRealFunction {
    @Override
    public double value(double x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException("Function threw an exception");
    }
}