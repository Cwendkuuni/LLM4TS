package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootFindException;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double DELTA = 1e-9;
    private static final double EPSILON = 1e-14;

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return E.EulerNumber().exp(Math.log(Math.abs(x)) - E.EulerNumber().gamma()) - x;
            }
        };
        solver = new RegulaFalsiSolver(DELTA, function);
    }

    

    // Test constructor with default arguments
    @Test
    public void testDefaultConstructor() {
        solver = new BaseSecantSolver(Method.REGULA_FALSI);
        Assert.assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    // Test constructor with custom method
    @Test
    public void testCustomConstructor() {
        solver = new BaseSecantSolver(Method.PEGASUS, 1e-8, 1e-9, 1e-12);
        Assert.assertEquals(Method.PEGASUS, solver.getMethod());
    }

    // Test solve method with default arguments
    @Test
    public void testSolveDefault() throws RootFindException, NoBracketingException, MaxEvalException {
        double root = solver.solve(50, function, -10, 10, 0.0);
        Assert.assertEquals(0.0, root, EPSILON);
    }

    // Test solve method with custom arguments
    @Test
    public void testSolveCustom() throws RootFindException, NoBracketingException, MaxEvalException {
        double root = solver.solve(50, function, -10, 10, 1.0);
        Assert.assertEquals(0.6686471060952403, root, EPSILON);
    }

    // Test solve method with inverted interval
    @Test(expected = NoBracketingException.class)
    public void testSolveInverted() throws RootFindException, NoBracketingException, MaxEvalException {
        solver = new BaseSecantSolver(Method.REGULA_FALSI, 1e-8, 1e-9, 1e-12);
        solver.solve(50, function, 10, -10, 0.0);
    }

    // Test solve method with function value accuracy smaller than relative accuracy
    @Test(expected = IllegalArgumentException.class)
    public void testSolveFunctionValueAccuracySmallerThanRelative() {
        solver = new BaseSecantSolver(Method.PEGASUS, 1e-15, 1e-9, 1e-12);
    }

    // Test solve method with absolute accuracy smaller than relative accuracy
    @Test(expected = IllegalArgumentException.class)
    public void testSolveAbsoluteAccuracySmallerThanRelative() {
        solver = new BaseSecantSolver(Method.PEGASUS, 1e-9, 1e-15, 1e-12);
    }

    // Test solve method with maxEval smaller than the required number of iterations
    @Test(expected = MaxEvalException.class)
    public void testSolveMaxEval() throws RootFindException, NoBracketingException, MaxEvalException {
        solver = new BaseSecantSolver(Method.PEGASUS, 1e-8, 1e-9, 1e-12);
        solver.solve(5, function, -10, 10, 0.0);
    }

    

}