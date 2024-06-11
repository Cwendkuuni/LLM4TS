package org.apache.commons.cli.test; // adjust the package name if necessary

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.apache.commons.math3.solver.BracketedRealSolver;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.LaguerreSolver;
import org.apache.commons.math3.solver.SecantSolver;

public class BaseSecantSolverTest {

    @Test
    public void testSolverWithPolynomialFunction() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 4, -1, 0});
        BracketedRealSolver<UnivariateFunction> solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS);

        try {
            double root = solver.solve(10, function, -5.0, 5.0);
            assertEquals(0.0, root, 1e-5);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // This should not happen
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithNegativeFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x, 2) + 1;
            }
        };
        BracketedRealSolver<UnivariateFunction> solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.PEGASUS);

        try {
            double root = solver.solve(10, function, -2.0, 2.0);
            assertEquals(0.0, root, 1e-5);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // This should not happen
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionValueLessThanZero() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - Math.log(2);
            }
        };
        BracketedRealSolver<UnivariateFunction> solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);

        try {
            double root = solver.solve(10, function, 0.0, 2.0);
            assertEquals(1.0, root, 1e-5);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // This should not happen
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionValueZeroAtStart() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) - 2;
            }
        };
        BracketedRealSolver<UnivariateFunction> solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS);

        try {
            double root = solver.solve(10, function, 1.0, 4.0);
            assertEquals(2.0, root, 1e-5);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // This should not happen
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionValueZeroAtEnd() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) - 2;
            }
        };
        BracketedRealSolver<UnivariateFunction> solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.PEGASUS);

        try {
            double root = solver.solve(10, function, 4.0, 1.0);
            assertEquals(2.0, root, 1e-5);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // This should not happen
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionValueZeroAtStartAndEnd() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x);
            }
        };
        BracketedRealSolver<UnivariateFunction> solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);

        try {
            double root = solver.solve(10, function, 0.0, 0.0);
            assertEquals(0.0, root, 1e-5);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // This should not happen
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionValueZeroAtStartAndEndBrentSolver() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x);
            }
        };
        BracketedRealSolver<UnivariateFunction> solver = new BrentSolver();

        try {
            double root = solver.solve(10, function, 0.0, 0.0);
            assertEquals(0.0, root, 1e-5);
        } catch (RootNotFoundException e) {
            // This should not happen
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionValueZeroAtStartAndEndLaguerreSolver() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x);
            }
        };
        BracketedRealSolver<UnivariateFunction> solver = new LaguerreSolver();

        try {
            double root = solver.solve(10, function, 0.0, 0.0);
            assertEquals(0.0, root, 1e-5);
        } catch (RootNotFoundException e) {
            // This should not happen
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionValueZeroAtStartAndEndSecantSolver() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x);
            }
        };
        BracketedRealSolver<UnivariateFunction> solver = new SecantSolver();

        try {
            double root = solver.solve(10, function, 0.0, 0.0);
            assertEquals(0.0, root, 1e-5);
        } catch (RootNotFoundException e) {
            // This should not happen
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithFunctionValueZeroAtStartAndEndDifferentSolvers() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x);
            }
        };

        // Test with different solvers
        BracketedRealSolver<UnivariateFunction> solver1 = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS);
        BracketedRealSolver<UnivariateFunction> solver2 = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.PEGASUS);
        BracketedRealSolver<UnivariateFunction> solver3 = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);

        try {
            double root1 = solver1.solve(10, function, 0.0, 0.0);
            double root2 = solver2.solve(10, function, 0.0, 0.0);
            double root3 = solver3.solve(10, function, 0.0, 0.0);
            assertEquals(0.0, root1, 1e-5);
            assertEquals(0.0, root2, 1e-5);
            assertEquals(0.0, root3, 1e-5);
        } catch (RootNotFoundException e) {
            // This should not happen
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}