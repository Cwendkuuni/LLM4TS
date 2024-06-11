import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testDefaultAbsoluteAccuracy() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolverTestImpl();

        // Act
        double actual = solver.getDefaultAbsoluteAccuracy();

        // Assert
        Assert.assertEquals(1e-6, actual, 0.0);
    }

    @Test
    public void testConstructorWithAbsoluteAccuracy() {
        // Arrange
        double absoluteAccuracy = 0.01;
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(absoluteAccuracy);

        // Act
        double actual = solver.getAbsoluteAccuracy();

        // Assert
        Assert.assertEquals(absoluteAccuracy, actual, 0.0);
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracy() {
        // Arrange
        double relativeAccuracy = 0.5;
        double absoluteAccuracy = 0.01;
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(relativeAccuracy, absoluteAccuracy);

        // Act
        double actualRelativeAccuracy = solver.getRelativeAccuracy();
        double actualAbsoluteAccuracy = solver.getAbsoluteAccuracy();

        // Assert
        Assert.assertEquals(relativeAccuracy, actualRelativeAccuracy, 0.0);
        Assert.assertEquals(absoluteAccuracy, actualAbsoluteAccuracy, 0.0);
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteAndFunctionValueAccuracy() {
        // Arrange
        double relativeAccuracy = 0.5;
        double absoluteAccuracy = 0.01;
        double functionValueAccuracy = 0.001;
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(relativeAccuracy, absoluteAccuracy,
                                                              functionValueAccuracy);

        // Act
        double actualRelativeAccuracy = solver.getRelativeAccuracy();
        double actualAbsoluteAccuracy = solver.getAbsoluteAccuracy();
        double actualFunctionValueAccuracy = solver.getFunctionValueAccuracy();

        // Assert
        Assert.assertEquals(relativeAccuracy, actualRelativeAccuracy, 0.0);
        Assert.assertEquals(absoluteAccuracy, actualAbsoluteAccuracy, 0.0);
        Assert.assertEquals(functionValueAccuracy, actualFunctionValueAccuracy, 0.0);
    }

    @Test
    public void testSolveWithMaxEval() {
        // Arrange
        int maxEval = 100;
        UnivariateRealFunction f = new UnivariateRealFunctionImpl();
        double min = 0.0;
        double max = 10.0;
        double startValue = 1.0;
        BaseSecantSolver solver = new BaseSecantSolverTestImpl();

        // Act
        double actual = solver.solve(maxEval, f, min, max, startValue);

        // Assert
        Assert.assertEquals(2.0, actual, 0.0);
    }

    @Test
    public void testSolveWithMaxEvalAndAllowedSolution() {
        // Arrange
        int maxEval = 100;
        UnivariateRealFunction f = new UnivariateRealFunctionImpl();
        double min = 0.0;
        double max = 10.0;
        double startValue = 1.0;
        BaseSecantSolver solver = new BaseSecantSolverTestImpl();

        // Act
        double actual = solver.solve(maxEval, f, min, max, startValue, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        // Assert
        Assert.assertEquals(3.0, actual, 0.0);
    }

    @Test
    public void testSolveWithMaxEvalAndAllowedSolution_nullFunction() {
        // Arrange
        int maxEval = 100;
        UnivariateRealFunction f = null;
        double min = 0.0;
        double max = 10.0;
        double startValue = 1.0;
        BaseSecantSolver solver = new BaseSecantSolverTestImpl();

        // Act & Assert
        try {
            solver.solve(maxEval, f, min, max, startValue, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
            Assert.fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Assert
            Assert.assertEquals("Function is null", e.getMessage());
        }
    }

    @Test
    public void testSolveWithMaxEvalAndAllowedSolution_minEqualsMax() {
        // Arrange
        int maxEval = 100;
        UnivariateRealFunction f = new UnivariateRealFunctionImpl();
        double min = 0.0;
        double max = 0.0;
        double startValue = 1.0;
        BaseSecantSolver solver = new BaseSecantSolverTestImpl();

        // Act
        double actual = solver.solve(maxEval, f, min, max, startValue, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        // Assert
        Assert.assertEquals(min, actual, 0.0);
    }

    private class BaseSecantSolverTestImpl extends BaseSecantSolver {

        public BaseSecantSolverTestImpl() {
            super(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        }

        public BaseSecantSolverTestImpl(double absoluteAccuracy) {
            super(absoluteAccuracy, Method.REGULA_FALSI);
        }

        public BaseSecantSolverTestImpl(double relativeAccuracy, double absoluteAccuracy) {
            super(relativeAccuracy, absoluteAccuracy, Method.REGULA_FALSI);
        }

        public BaseSecantSolverTestImpl(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
            super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.REGULA_FALSI);
        }

        @Override
        protected double computeObjectiveValue(double x) {
            return 2 * x;
        }
    }

    private class UnivariateRealFunctionImpl implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return 2 * x;
        }
    }
}