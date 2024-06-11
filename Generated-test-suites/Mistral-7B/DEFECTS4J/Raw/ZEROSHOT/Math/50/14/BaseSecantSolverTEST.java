package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.REGULA_FALSI).build();
            double result = solver.solve(function, -3.0, 3.0);
            assertEquals("Square root of 2 should be approximately 1.4142135625", Precision.round(Math.sqrt(2), 15), result, 1e-6);
        } catch (NoBracketingException e) {
            fail("Unexpected NoBracketingException: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.ILLINOIS).build();
            double result = solver.solve(function, -3.0, 3.0);
            assertEquals("Square root of 2 should be approximately 1.4142135625", Precision.round(Math.sqrt(2), 15), result, 1e-6);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.PEGASUS).build();
            double result = solver.solve(function, -3.0, 3.0);
            assertEquals("Square root of 2 should be approximately 1.4142135625", Precision.round(Math.sqrt(2), 15), result, 1e-6);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.REGULA_FALSI).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.LEFT_SIDE);
            assertTrue("Result must be less than zero", result < 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.ILLINOIS).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.LEFT_SIDE);
            assertTrue("Result must be less than zero", result < 0);
        } catch (NoBracketingException | MathIllegalArgumentException | UnsupportedOperationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.PEGASUS).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.LEFT_SIDE);
            assertTrue("Result must be less than zero", result < 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.REGULA_FALSI).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.RIGHT_SIDE);
            assertTrue("Result must be greater than zero", result > 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.ILLINOIS).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.RIGHT_SIDE);
            assertTrue("Result must be greater than zero", result > 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.PEGASUS).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.RIGHT_SIDE);
            assertTrue("Result must be greater than zero", result > 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.REGULA_FALSI).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.BELOW_SIDE);
            assertTrue("Result must be less than zero", result < 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.ILLINOIS).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.BELOW_SIDE);
            assertTrue("Result must be less than zero", result < 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.PEGASUS).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.BELOW_SIDE);
            assertTrue("Result must be less than zero", result < 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.REGULA_FALSI).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.ABOVE_SIDE);
            assertTrue("Result must be greater than zero", result > 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.ILLINOIS).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.ABOVE_SIDE);
            assertTrue("Result must be greater than zero", result > 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.PEGASUS).build();
            double result = solver.solve(function, -3.0, 3.0, AllowedSolution.ABOVE_SIDE);
            assertTrue("Result must be greater than zero", result > 0);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.REGULA_FALSI).build();
            double result = solver.solve(new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return FastMath.sin(x);
                }
            }, 0.0, 4.0, AllowedSolution.ANY_SIDE);
            fail("Expected MathIllegalArgumentException for sine function");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.ILLINOIS).build();
            double result = solver.solve(new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return FastMath.sin(x);
                }
            }, 0.0, 4.0, AllowedSolution.ANY_SIDE);
            fail("Expected MathIllegalArgumentException for sine function");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder(0.001, 0.01, Method.PEGASUS).build();
            double result = solver.solve(new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return FastMath.sin(x);
                }
            }, 0.0, 4.0, AllowedSolution.ANY_SIDE);
            fail("Expected MathIllegalArgumentException for sine function");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }
}

class BaseSecantSolverBuilder {
    private double absoluteAccuracy = 1e-6;
    private double relativeAccuracy = 0.001;
    private double functionValueAccuracy = 0.0;
    private Method method = Method.REGULA_FALSI;

    public BaseSecantSolverBuilder absoluteAccuracy(double absoluteAccuracy) {
        this.absoluteAccuracy = absoluteAccuracy;
        return this;
    }

    public BaseSecantSolverBuilder relativeAccuracy(double relativeAccuracy) {
        this.relativeAccuracy = relativeAccuracy;
        return this;
    }

    public BaseSecantSolverBuilder functionValueAccuracy(double functionValueAccuracy) {
        this.functionValueAccuracy = functionValueAccuracy;
        return this;
    }

    public BaseSecantSolverBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public BaseSecantSolver build() {
        switch (method) {
        case REGULA_FALSI:
            return new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        case ILLINOIS:
            return new IllinoisSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        case PEGASUS:
            return new PegasusSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        default:
            throw new IllegalStateException("Unsupported method: " + method);
        }
    }
}

enum Method {
    REGULA_FALSI,
    ILLINOIS,
    PEGASUS
}