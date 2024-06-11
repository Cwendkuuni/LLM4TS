package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.RootFindersException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestConstants;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsi_Simple() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.EPSILON,
                                                       BaseSecantSolver.Method.REGULA_FALSI);

        double root = solver.solve(10, function, -1.0, 1.0, 0.0);

        assertEquals(0.0, root, Precision.SAFE_EPSILON);
    }

    @Test
    public void testSolver_RegulaFalsi_InvalidBracketing() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.EPSILON,
                                                       BaseSecantSolver.Method.REGULA_FALSI);

        assertThrows(RootFindersException.class, () -> {
            solver.solve(10, function, 1.0, -1.0, 0.0);
        });
    }

    @Test
    public void testSolver_Illinois_Simple() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.EPSILON,
                                                       BaseSecantSolver.Method.ILLINOIS);

        double root = solver.solve(10, function, -TestConstants.PI, TestConstants.PI, 0.0);

        assertEquals(0.0, root, Precision.SAFE_EPSILON);
    }

    @Test
    public void testSolver_Illinois_FunctionValueError() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.EPSILON,
                                                       BaseSecantSolver.Method.ILLINOIS);

        assertThrows(UnsupportedOperationException.class, () -> {
            solver.solve(10, function, -1.0, 1.0, 0.0);
        });
    }

    @Test
    public void testSolver_Pegasus_Simple() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.EPSILON,
                                                       BaseSecantSolver.Method.PEGASUS);

        double root = solver.solve(10, function, -3.0, 0.0, 0.0);

        assertEquals(Math.log(2), root, Precision.SAFE_EPSILON);
    }

    @Test
    public void testSolver_Pegasus_InvalidBracketing() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.EPSILON,
                                                       BaseSecantSolver.Method.PEGASUS);

        assertThrows(RootFindersException.class, () -> {
            solver.solve(10, function, 0.0, -3.0, 0.0);
        });
    }

    @Test
    public void testSolver_RegulaFalsi_MaxIterations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1, 10,
                                                       TestConstants.EPSILON,
                                                       BaseSecantSolver.Method.REGULA_FALSI);

        assertThrows(MaxIterationsExceededException.class, () -> {
            solver.solve(10, function, -TestConstants.PI, TestConstants.PI, 0.0);
        });
    }
}