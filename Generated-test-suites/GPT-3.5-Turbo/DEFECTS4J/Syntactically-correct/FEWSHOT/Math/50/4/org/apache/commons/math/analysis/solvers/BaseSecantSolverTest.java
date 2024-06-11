
package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolveAnySide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return 0;
            }
        };
        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0, root, 0);
    }
    
    @Test
    public void testSolveLeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return 0;
            }
        };
        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(0, root, 0);
    }
    
    @Test
    public void testSolveRightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return 0;
            }
        };
        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(0, root, 0);
    }
    
    @Test
    public void testSolveBelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return 0;
            }
        };
        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        Assert.assertEquals(0, root, 0);
    }
    
    @Test
    public void testSolveAboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return 0;
            }
        };
        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(0, root, 0);
    }
    
    @Test(expected = MathInternalError.class)
    public void testSolveInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.SECANT) {
            @Override
            protected double computeObjectiveValue(double point) {
                return 0;
            }
        };
        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testDoSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return 0;
            }
        };
        solver.setFunctionValueAccuracy(1e-6);
        solver.setRelativeAccuracy(1e-6);
        solver.setAbsoluteAccuracy(1e-6);
        solver.setMin(0);
        solver.setMax(10);
        double root = solver.doSolve();
        Assert.assertEquals(0, root, 0);
    }
}
