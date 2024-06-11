package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public abstract class BaseSecantSolverTest {

    protected UnivariateRealFunction f;
    protected double absoluteAccuracy;
    protected double relativeAccuracy;
    protected double functionValueAccuracy;
    protected int maxEval;
    protected double min;
    protected double max;
    protected double startValue;
    protected AllowedSolution allowedSolution;

    protected abstract BaseSecantSolver createSolver();

    @Before
    public void setUp() {
        f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        absoluteAccuracy = 1e-6;
        relativeAccuracy = 1e-6;
        functionValueAccuracy = 1e-15;
        maxEval = 100;
        min = -10;
        max = 10;
        startValue = 1;
        allowedSolution = AllowedSolution.ANY_SIDE;
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = createSolver();
        double root = solver.solve(maxEval, f, min, max, startValue, allowedSolution);
        double expected = 2.0;
        assertEquals(expected, root, absoluteAccuracy);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.SECANT) {
            protected double doSolve() {
                return 0;
            }
        };
        solver.solve(maxEval, f, min, max, startValue, allowedSolution);
    }
}