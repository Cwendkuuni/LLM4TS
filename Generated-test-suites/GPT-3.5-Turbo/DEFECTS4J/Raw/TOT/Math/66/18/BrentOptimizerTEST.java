import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testOptimize_WithDefaultValues_ReturnsOptimum() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 2, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 5.0;
        
        // Act
        double result = 0;
        try {
            result = optimizer.optimize(f, goalType, min, max);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown");
        }
        
        // Assert
        assertEquals(2.0, result, 1E-10);
    }

    @Test
    public void testOptimize_WithInvalidRange_ThrowsException() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 2.0;
        double max = 1.0;
        
        // Act and Assert
        try {
            optimizer.optimize(f, goalType, min, max);
            fail("NotStrictlyPositiveException not thrown");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown");
        }
    }

    @Test
    public void testOptimize_WithMaxIterationsExceeded_ThrowsException() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        optimizer.setMaximalIterationCount(5);
        
        // Act and Assert
        try {
            optimizer.optimize(f, goalType, min, max);
            fail("MaxIterationsExceededException not thrown");
        } catch (MaxIterationsExceededException e) {
            assertTrue(true);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown");
        }
    }

    @Test
    public void testOptimize_WithFunctionException_ThrowsException() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 5.0;
        
        // Act and Assert
        try {
            optimizer.optimize(f, goalType, min, max);
            fail("FunctionEvaluationException not thrown");
        } catch (FunctionEvaluationException e) {
            assertTrue(true);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException thrown");
        }
    }

}