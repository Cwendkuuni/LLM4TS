package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

public class BaseSecantSolverTest {

    @org.junit.Test
    public void testSolve_RegulaFalsi() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .setMethod(BaseSecantSolver.Method.REGULA_FALSI)
                .build();

        try {
            double root = solver.solve(10, function, -1, 1);
            double expected = Precision.round(Math.sqrt(2), 5);
            assertEquals(expected, root, 1e-5);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        }
    }

    @org.junit.Test
    public void testSolve_Illinois() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .setMethod(BaseSecantSolver.Method.ILLINOIS)
                .build();

        try {
            double root = solver.solve(10, function, -1, 1);
            double expected = Precision.round(Math.PI, 5);
            assertEquals(expected, root, 1e-5);
        } catch (MaxEvalExceededException | NumberIsTooSmallException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSolve_Pegasus() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolverBuilder()
                .setMethod(BaseSecantSolver.Method.PEGASUS)
                .build();

        try {
            double root = solver.solve(10, function, -5, 0);
            double expected = Math.log(2) / Math.E;
            assertEquals(expected, root, 1e-5);
        } catch (MaxEvalExceededException | NumberIsTooSmallException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSolve_Secant() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-5);

        try {
            solver.solve(10, function, -1, 1);
        } finally {
            // The test fails because SecantSolver is not a bracketing method.
        }
    }
}

class BaseSecantSolverBuilder {
    private double absoluteAccuracy;
    private double relativeAccuracy;
    private double functionValueAccuracy;
    private BaseSecantSolver.Method method;

    public BaseSecantSolverBuilder setAbsoluteAccuracy(double absoluteAccuracy) {
        this.absoluteAccuracy = absoluteAccuracy;
        return this;
    }

    public BaseSecantSolverBuilder setRelativeAccuracy(double relativeAccuracy) {
        this.relativeAccuracy = relativeAccuracy;
        return this;
    }

    public BaseSecantSolverBuilder setFunctionValueAccuracy(double functionValueAccuracy) {
        this.functionValueAccuracy = functionValueAccuracy;
        return this;
    }

    public BaseSecantSolverBuilder setMethod(BaseSecantSolver.Method method) {
        this.method = method;
        return this;
    }

    public BaseSecantSolver build() {
        switch (method) {
            case REGULA_FALSI:
                return new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy,
                        BaseSecantSolver.Method.REGULA_FALSI);
            case ILLINOIS:
                return new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy,
                        BaseSecantSolver.Method.ILLINOIS);
            case PEGASUS:
                return new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy,
                        BaseSecantSolver.Method.PEGASUS);
            default:
                throw new UnsupportedOperationException("Unsupported method: " + method);
        }
    }
}