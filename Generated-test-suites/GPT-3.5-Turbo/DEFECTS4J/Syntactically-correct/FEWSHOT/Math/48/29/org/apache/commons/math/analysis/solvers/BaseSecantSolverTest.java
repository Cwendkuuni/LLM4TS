package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private BaseSecantSolver regulaFalsiSolver;
    private BaseSecantSolver illinoisSolver;
    private BaseSecantSolver pegasusSolver;

    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        regulaFalsiSolver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        illinoisSolver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        pegasusSolver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
    }
    
    @Test
    public void testRegulaFalsiSolver() {
        assertEquals(5.0, regulaFalsiSolver.solve(100, function, 0, 10), 1e-6);
    }

    @Test
    public void testIllinoisSolver() {
        assertEquals(5.0, illinoisSolver.solve(100, function, 0, 10), 1e-6);
    }

    @Test
    public void testPegasusSolver() {
        assertEquals(5.0, pegasusSolver.solve(100, function, 0, 10), 1e-6);
    }
}