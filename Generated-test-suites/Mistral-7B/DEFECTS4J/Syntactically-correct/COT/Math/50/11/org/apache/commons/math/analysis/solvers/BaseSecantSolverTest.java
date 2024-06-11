package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.AllowedSolution;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    // Test methods for BaseSecantSolver class

    @org.junit.Test
    public void testConstructorWithAbsoluteAccuracy() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertTrue(solver instanceof BaseSecantSolver);
        assertTrue(solver instanceof BracketedUnivariateRealSolver<? super UnivariateRealFunction>);
        assertTrue(solver.getAllowed() == AllowedSolution.ANY_SIDE);
        assertTrue(solver.getMethod() == BaseSecantSolver.Method.REGULA_FALSI);
        assertTrue(Precision.isClose(solver.getAbsoluteAccuracy(), absoluteAccuracy, 1e-9));
    }

    @org.junit.Test
    public void testConstructorWithRelativeAndAbsoluteAccuracy() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);
        assertTrue(solver instanceof BaseSecantSolver);
        assertTrue(solver instanceof BracketedUnivariateRealSolver<? super UnivariateRealFunction>);
        assertTrue(solver.getAllowed() == AllowedSolution.ANY_SIDE);
        assertTrue(solver.getMethod() == BaseSecantSolver.Method.ILLINOIS);
        assertTrue(Precision.isClose(solver.getRelativeAccuracy(), relativeAccuracy, 1e-9));
        assertTrue(Precision.isClose(solver.getAbsoluteAccuracy(), absoluteAccuracy, 1e-9));
    }

    @org.junit.Test
    public void testConstructorWithRelativeAccuracyFunctionValueAccuracyAndMethod() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-7;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
        assertTrue(solver instanceof BaseSecantSolver);
        assertTrue(solver instanceof BracketedUnivariateRealSolver<? super UnivariateRealFunction>);
        assertTrue(solver.getAllowed() == AllowedSolution.ANY_SIDE);
        assertTrue(solver.getMethod() == BaseSecantSolver.Method.PEGASUS);
        assertTrue(Precision.isClose(solver.getRelativeAccuracy(), relativeAccuracy, 1e-9));
        assertTrue(Precision.isClose(solver.getAbsoluteAccuracy(), absoluteAccuracy, 1e-9));
        assertTrue(Precision.isClose(solver.getFunctionValueAccuracy(), functionValueAccuracy, 1e-9));
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testNegativeRelativeAccuracy() {
        double relativeAccuracy = -1e-4;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, BaseSecantSolver.Method.PEGASUS);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testNegativeAbsoluteAccuracy() {
        double absoluteAccuracy = -1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        solver.setAbsoluteAccuracy(absoluteAccuracy);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testNullMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.NULL);
    }

    @org.junit.Test
    public void testSolveWithDefaults() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver();
        double root = solver.solve(testFunction, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.isClose(root, 0.881375587928, 1e-4));
    }

    @org.junit.Test
    public void testSolveWithMinMaxAndAllowedSolution() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(testFunction, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
        assertTrue(Precision.isClose(root, -1.0, 1e-4));
    }

    @org.junit.Test
    public void testSolveWithMinMaxAndStartValue() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(testFunction, -10.0, 10.0, 5.0);
        assertTrue(Precision.isClose(root, 0.881375587928, 1e-4));
    }

    @org.junit.Test
    public void testSolveWithMinMaxAndStartValueAndAllowedSolution() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(testFunction, -2.0, 2.0, 1.5, AllowedSolution.RIGHT_SIDE);
        assertTrue(Precision.isClose(root, 1.0, 1e-4));
    }

    @org.junit.Test(expected = MathInternalError.class)
    public void testSolveWithInvalidMethod() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.NULL);
        solver.solve(testFunction, -10.0, 10.0);
    }

    @org.junit.Test(expected = MathInternalError.class)
    public void testVerifyBracketingWithInvalidMinMax() {
        BaseSecantSolver solver = new BaseSecantSolver();
        solver.verifyBracketing(-1.0, 1.0);
    }
}