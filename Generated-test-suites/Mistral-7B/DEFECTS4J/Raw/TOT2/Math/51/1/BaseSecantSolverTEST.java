package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    private static final double DELTA = 1e-10;

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    public BaseSecantSolverTest(Method method, double[] x, double[] y) {
        this.solver = new BaseSecantSolver(DELTA, method) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f,
                               double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, AllowedSolution.ANY_SIDE);
            }
        };
        this.function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return y[0] + (y[1] - y[0]) / (x - x[0]);
            }
        };
    }

    @Parameterized.Parameters(name = "{index}: ({0}): {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Method.REGULA_FALSI, new double[]{-1.0, 1.0}, new double[]{-0.7127, 0.0}},
                {Method.ILLINOIS, new double[]{-1.0, 1.0}, new double[]{-0.7127, 0.0}},
                {Method.PEGASUS, new double[]{-1.0, 1.0}, new double[]{-0.7127, 0.0}},
                {Method.REGULA_FALSI, new double[]{0.0, 1.0}, new double[]{0.0, 1.0}},
                {Method.ILLINOIS, new double[]{0.0, 1.0}, new AssertionError("Bracketing violation")},
                {Method.PEGASUS, new double[]{0.0, 1.0}, new AssertionError("Bracketing violation")},
                {Method.REGULA_FALSI, new double[]{1.0, 1.0}, new double[]{-Inf, 1.0}},
                {Method.ILLINOIS, new double[]{1.0, 1.0}, new AssertionError("Maximum number of evaluations exceeded: 1000")},
                {Method.PEGASUS, new double[]{1.0, 1.0}, new AssertionError("Maximum number of evaluations exceeded: 1000")}
        });
    }

    @Before
    public void setUp() {
        // Empty
    }

    @Test
    public void testSolve_DefaultConstructor() {
        // Test solver created with default constructor.
        assertNotNull(solver);
        assertEquals(Method.PEGASUS, solver.getMethod());
    }

    @Test
    public void testSolve_CustomConstructor() {
        // Test solver created with custom constructor.
        assertNotNull(solver);
        assertEquals(method, solver.getMethod());
    }

    @Test
    public void testSolve_GivenFunctionWithRootInInterval() throws Exception {
        // Test solver with a function that has its root within the given interval.
        double root = solver.solve(100, function, -1.0, 1.0);
        assertEquals(x[0], Precision.round(root, 6), DELTA);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolve_GivenFunctionWithoutBracketing() {
        // Test solver with a function that doesn't bracket its root.
        solver.solve(1, function, 0.0, 1.0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolve_GivenFunctionWithMaxEvalExceeded() {
        // Test solver with a function that requires more than the given maximum number of evaluations.
        solver.solve(1, function, -1.0, 1.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolve_GivenFunctionWithBracketingViolation() {
        // Test solver with a function that doesn't meet the bracketing condition.
        solver.solve(1, function, 0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_GivenNullFunction() {
        // Test solver with a null function.
        assertThrows(IllegalArgumentException.class, () -> solver.solve(1, null, -1.0, 1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_GivenNullInterval() {
        // Test solver with a null interval.
        assertThrows(IllegalArgumentException.class, () -> solver.solve(1, function, null, 1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_GivenEmptyInterval() {
        // Test solver with an empty interval.
        assertThrows(IllegalArgumentException.class, () -> solver.solve(1, function, 1.0, 1.0));
    }

    @Test
    public void testSolve_GivenFunctionWithMultipleRoots() {
        // Test solver with a function that has multiple roots.
        double root1 = solver.solve(100, function, -1.0, 1.0);
        double root2 = solver.solve(100, function, root1, 1.0);
        assertEquals(x[1], Precision.round(root2, 6), DELTA);
    }

    @Test
    public void testVerifyBracketing() {
        // Test verifyBracketing method.
        assertFalse(solver.verifyBracketing(x[0], x[1]));
        assertFalse(solver.verifyBracketing(x[1], x[0]));
        assertTrue(solver.verifyBracketing(x[1], x[1]));
        assertTrue(solver.verifyBracketing(x[0], x[0]));
    }
}