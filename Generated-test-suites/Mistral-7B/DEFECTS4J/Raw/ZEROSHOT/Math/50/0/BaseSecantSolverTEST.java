package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /** A simple function for testing. */
    private UnivariateFunction testFunction;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
    }

    /**
     * Tests the solver using the Regula Falsi method with the default
     * constructor.
     */
    @Test
    public void testRegulaFalsiDefault() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(testFunction, -5.0, 5.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Tests the solver using the Regula Falsi method with a custom constructor.
     */
    @Test
    public void testRegulaFalsiCustom() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(testFunction, -5.0, 5.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Tests the solver using the Illinois method with the default constructor.
     */
    @Test
    public void testIllinoisDefault() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(testFunction, -5.0, 5.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Tests the solver using the Illinois method with a custom constructor.
     */
    @Test
    public void testIllinoisCustom() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(testFunction, -5.0, 5.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Tests the solver using the Pegasus method with the default constructor.
     */
    @Test
    public void testPegasusDefault() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(testFunction, -5.0, 5.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Tests the solver using the Pegasus method with a custom constructor.
     */
    @Test
    public void testPegasusCustom() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(testFunction, -5.0, 5.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Tests the solver when the function is not bracketing the root.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNotBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(testFunction, 1.0, 1.0);
    }
}