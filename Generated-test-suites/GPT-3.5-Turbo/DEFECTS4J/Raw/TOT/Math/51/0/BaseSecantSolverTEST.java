import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve_WhenFunctionIsConstant_ThenIllegalArgumentException() {
        // Arrange
        double relativeAccuracy = 0.001;
        double absoluteAccuracy = 0.001;
        double functionValueAccuracy = 0.001;
        BaseSecantSolver solver = new DummySecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);

        // Act
        try {
            solver.solve(100, new ConstantFunction(5), 0, 10);
            // Assert
            assertEquals("No exceptions are thrown", true, false); // Should not reach this line
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Exception message", "Invalid interval: function values at endpoints do not have different signs.", e.getMessage());
        }
    }

    @Test
    public void testSolve_WhenMethodIsInvalid_ThenMathInternalError() {
        // Arrange
        double relativeAccuracy = 0.001;
        double absoluteAccuracy = 0.001;
        double functionValueAccuracy = 0.001;
        BaseSecantSolver.Method invalidMethod = null;
        BaseSecantSolver solver = new DummySecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, invalidMethod);

        // Act
        try {
            solver.solve(100, new LinearFunction(1, 0), 0, 10);
            // Assert
            assertEquals("No exceptions are thrown", true, false); // Should not reach this line
        } catch (MathInternalError e) {
            // Assert
            assertEquals("Exception message", "Invalid method.", e.getMessage());
        }
    }

    @Test
    public void testSolve_WhenAllowedSolutionIsAnySide_ThenCorrectRootReturned() {
        // Arrange
        double relativeAccuracy = 0.001;
        double absoluteAccuracy = 0.001;
        double functionValueAccuracy = 0.001;
        BaseSecantSolver.Method method = BaseSecantSolver.Method.REGULA_FALSI;
        BaseSecantSolver solver = new DummySecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);

        // Act
        double root = solver.solve(100, new QuadraticFunction(1, -5, 6), 0, 5);

        // Assert
        assertEquals("Root value", 3, root, EPSILON);
    }

    @Test
    public void testSolve_WhenAllowedSolutionIsLeftSide_ThenCorrectRootReturned() {
        // Arrange
        double relativeAccuracy = 0.001;
        double absoluteAccuracy = 0.001;
        double functionValueAccuracy = 0.001;
        BaseSecantSolver.Method method = BaseSecantSolver.Method.ILLINOIS;
        BaseSecantSolver solver = new DummySecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);

        // Act
        double root = solver.solve(100, new QuadraticFunction(1, -5, 6), 0, 5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        // Assert
        assertEquals("Root value", 2, root, EPSILON);
    }

    @Test
    public void testSolve_WhenAllowedSolutionIsRightSide_ThenCorrectRootReturned() {
        // Arrange
        double relativeAccuracy = 0.001;
        double absoluteAccuracy = 0.001;
        double functionValueAccuracy = 0.001;
        BaseSecantSolver.Method method = BaseSecantSolver.Method.PEGASUS;
        BaseSecantSolver solver = new DummySecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);

        // Act
        double root = solver.solve(100, new QuadraticFunction(1, -5, 6), 0, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        // Assert
        assertEquals("Root value", 4, root, EPSILON);
    }

    @Test
    public void testSolve_WhenAllowedSolutionIsBelowSide_ThenCorrectRootReturned() {
        // Arrange
        double relativeAccuracy = 0.001;
        double absoluteAccuracy = 0.001;
        double functionValueAccuracy = 0.001;
        BaseSecantSolver.Method method = BaseSecantSolver.Method.REGULA_FALSI;
        BaseSecantSolver solver = new DummySecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);

        // Act
        double root = solver.solve(100, new QuadraticFunction(1, -5, 6), 0, 5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        // Assert
        assertEquals("Root value", 3, root, EPSILON);
    }

    @Test
    public void testSolve_WhenAllowedSolutionIsAboveSide_ThenCorrectRootReturned() {
        // Arrange
        double relativeAccuracy = 0.001;
        double absoluteAccuracy = 0.001;
        double functionValueAccuracy = 0.001;
        BaseSecantSolver.Method method = BaseSecantSolver.Method.ILLINOIS;
        BaseSecantSolver solver = new DummySecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);

        // Act
        double root = solver.solve(100, new QuadraticFunction(1, -5, 6), 0, 5, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        // Assert
        assertEquals("Root value", 4, root, EPSILON);
    }

    private static class DummySecantSolver extends BaseSecantSolver {

        private final Method method;

        public DummySecantSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, Method method) {
            super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
            this.method = method;
        }

        public DummySecantSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
            this(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.REGULA_FALSI);
        }

        public DummySecantSolver(Method method) {
            this(DEFAULT_RELATIVE_ACCURACY, DEFAULT_ABSOLUTE_ACCURACY, DEFAULT_FUNCTION_VALUE_ACCURACY, method);
        }

        @Override
        protected double doSolve() {
            return 0; // Not used in the test
        }
    }

    private static class ConstantFunction implements UnivariateRealFunction {

        private final double constant;

        public ConstantFunction(double constant) {
            this.constant = constant;
        }

        @Override
        public double value(double x) {
            return constant;
        }
    }

    private static class LinearFunction implements UnivariateRealFunction {

        private final double slope;
        private final double constant;

        public LinearFunction(double slope, double constant) {
            this.slope = slope;
            this.constant = constant;
        }

        @Override
        public double value(double x) {
            return slope * x + constant;
        }
    }

    private static class QuadraticFunction implements UnivariateRealFunction {

        private final double a;
        private final double b;
        private final double c;

        public QuadraticFunction(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public double value(double x) {
            return a * x * x + b * x + c;
        }
    }
}