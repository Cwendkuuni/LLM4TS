package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * Test the RegulaFalsiSolver with a simple quadratic function.
     */
    @Test
    public void testRegulaFalsiSolver() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        try {
            double result = solver.solve(10, function, -10, 10);
            assertEquals(-3.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test the IllinoisSolver with a simple quadratic function.
     */
    @Test
    public void testIllinoisSolver() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.IllinoisSolver(1e-6);
        try {
            double result = solver.solve(10, function, -10, 10);
            assertEquals(-3.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test the PegasusSolver with a simple quadratic function.
     */
    @Test
    public void testPegasusSolver() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.PegasusSolver(1e-6);
        try {
            double result = solver.solve(10, function, -10, 10);
            assertEquals(-3.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test the RegulaFalsiSolver with a function that has no root.
     */
    @Test(expected = MaxEvalException.class)
    public void testRegulaFalsiSolverNoRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        solver.solve(10, function, -10, 10);
    }

    /**
     * Test the IllinoisSolver with a function that has no root.
     */
    @Test(expected = MaxEvalException.class)
    public void testIllinoisSolverNoRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.IllinoisSolver(1e-6);
        solver.solve(10, function, -10, 10);
    }

    /**
     * Test the PegasusSolver with a function that has no root.
     */
    @Test(expected = MaxEvalException.class)
    public void testPegasusSolverNoRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.PegasusSolver(1e-6);
        solver.solve(10, function, -10, 10);
    }

    /**
     * Test the RegulaFalsiSolver with a function with
     * singularities at the root.
     */
    @Test
    public void testRegulaFalsiSolverSingularity() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new MathIllegalArgumentException("Function is undefined at x = 0.");
                }
                return Math.log(x) * Math.log(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        try {
            double result = solver.solve(10, function, 1e-10, 1);
            assertEquals(0.0, result, Precision.EPSILON);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test the IllinoisSolver with a function with
     * singularities at the root.
     */
    @Test
    public void testIllinoisSolverSingularity() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new MathIllegalArgumentException("Function is undefined at x = 0.");
                }
                return Math.log(x) * Math.log(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.IllinoisSolver(1e-6);
        try {
            double result = solver.solve(10, function, 1e-10, 1);
            assertEquals(0.0, result, Precision.EPSILON);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test the PegasusSolver with a function with
     * singularities at the root.
     */
    @Test
    public void testPegasusSolverSingularity() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new MathIllegalArgumentException("Function is undefined at x = 0.");
                }
                return Math.log(x) * Math.log(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.PegasusSolver(1e-6);
        try {
            double result = solver.solve(10, function, 1e-10, 1);
            assertEquals(0.0, result, Precision.EPSILON);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}