import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolverDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .build();
        Assert.assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                solver.getAbsoluteAccuracy(), 0.0);
        Method method = solver.getMethod();
        Assert.assertEquals(Method.REGULA_FALSI, method);
    }

    @Test
    public void testBaseSecantSolverCustomConstructor() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .withRelativeAccuracy(relativeAccuracy)
                .withAbsoluteAccuracy(absoluteAccuracy)
                .build();
        Assert.assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        Assert.assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        Method method = solver.getMethod();
        Assert.assertEquals(Method.REGULA_FALSI, method);
    }

    @Test
    public void testBaseSecantSolverCustomConstructorWithFunctionValueAccuracy() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-5;
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .withRelativeAccuracy(relativeAccuracy)
                .withAbsoluteAccuracy(absoluteAccuracy)
                .withFunctionValueAccuracy(functionValueAccuracy)
                .build();
        Assert.assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        Assert.assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        Assert.assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0.0);
        Method method = solver.getMethod();
        Assert.assertEquals(Method.REGULA_FALSI, method);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSecantSolverInvalidConstructor() {
        new BaseSecantSolver.BaseSecantSolverBuilder().build();
    }

    @Test
    public void testSolveMethodWithDefaultValues() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .build();
        UnivariateRealFunction function = f -> Math.pow(Math.E, -2 * f);
        double min = 0.0;
        double max = 1.0;
        double solution = solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.0 / Math.E, solution, 1e-8);
    }

    @Test
    public void testSolveMethodWithCustomValues() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .withRelativeAccuracy(1e-5)
                .withAbsoluteAccuracy(1e-8)
                .build();
        UnivariateRealFunction function = f -> Math.pow(Math.E, -2 * f);
        double min = 0.0;
        double max = 1.0;
        double solution = solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.0 / Math.E, solution, 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveMethodWithNegativeMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .build();
        UnivariateRealFunction function = f -> f * f;
        double min = 0.0;
        double max = 1.0;
        solver.solve(-1, function, min, max, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveMethodWithNullFunction() {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .build();
        UnivariateRealFunction function = null;
        double min = 0.0;
        double max = 1.0;
        solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveMethodWithFunctionNotBracketing() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .build();
        UnivariateRealFunction function = f -> Math.pow(Math.E, -f);
        double min = 0.0;
        double max = 1.0;
        solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveMethodWithAllowedSolutionBelow() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .withAllowedSolution(AllowedSolution.BELOW_SIDE)
                .build();
        UnivariateRealFunction function = f -> Math.pow(Math.E, -2 * f);
        double min = 0.0;
        double max = 1.0;
        solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveMethodWithAllowedSolutionAbove() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .withAllowedSolution(AllowedSolution.ABOVE_SIDE)
                .build();
        UnivariateRealFunction function = f -> Math.pow(Math.E, -2 * f);
        double min = 0.0;
        double max = 1.0;
        solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveMethodWithAllowedSolutionLeft() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .withAllowedSolution(AllowedSolution.LEFT_SIDE)
                .build();
        UnivariateRealFunction function = f -> Math.pow(Math.E, -2 * f);
        double min = 0.0;
        double max = 1.0;
        solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveMethodWithAllowedSolutionRight() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .withAllowedSolution(AllowedSolution.RIGHT_SIDE)
                .build();
        UnivariateRealFunction function = f -> Math.pow(Math.E, -2 * f);
        double min = 0.0;
        double max = 1.0;
        solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolveMethodWithCustomFunctionValueAccuracy() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .withRelativeAccuracy(1e-5)
                .withAbsoluteAccuracy(1e-8)
                .withFunctionValueAccuracy(1e-9)
                .build();
        UnivariateRealFunction function = f -> Math.pow(Math.E, -2 * f);
        double min = 0.0;
        double max = 1.0;
        double solution = solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.0 / Math.E, solution, 1e-10);
    }

    enum AllowedSolution {
        LEFT_SIDE,
        RIGHT_SIDE,
        BELOW_SIDE,
        ABOVE_SIDE,
        ANY_SIDE;
    }
}