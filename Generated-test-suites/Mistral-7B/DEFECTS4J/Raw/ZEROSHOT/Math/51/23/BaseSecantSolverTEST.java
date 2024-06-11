package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtil;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsi() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3);
        double root = solver.solve(new UnivariateRealFunctionWrapper(function), -2, 2);
        assertEquals(0.0, root, 1e-5);
    }

    @Test
    public void testSolver_Illinois() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-3);
        double root = solver.solve(new UnivariateRealFunctionWrapper(function), -3, 1);
        assertEquals(Math.log(2), root, 1e-5);
    }

    @Test
    public void testSolver_Pegasus() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1e-3);
        double root = solver.solve(new UnivariateRealFunctionWrapper(function), -3, 3);
        assertEquals(Math.PI / 3, root, 1e-5);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolver_InvalidFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3);
        solver.solve(new UnivariateRealFunctionWrapper(function), -2, 2);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolver_EmptyInterval() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3);
        solver.solve(new UnivariateRealFunctionWrapper(function), 0, 0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolver_SameRoots() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3);
        solver.solve(new UnivariateRealFunctionWrapper(function), 1, 1);
    }

    @Test
    public void testSolver_AllowedSolution_Left() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3);
        double root = solver.solve(new UnivariateRealFunctionWrapper(function), -2, 2, AllowedSolution.LEFT);
        assertEquals(-Math.sqrt(2), root, 1e-5);
    }

    @Test
    public void testSolver_AllowedSolution_Right() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3);
        double root = solver.solve(new UnivariateRealFunctionWrapper(function), -2, 2, AllowedSolution.RIGHT);
        assertEquals(Math.sqrt(2), root, 1e-5);
    }

    @Test
    public void testSolver_AllowedSolution_Below() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-3);
        double root = solver.solve(new UnivariateRealFunctionWrapper(function), -3, 1, AllowedSolution.BELOW);
        assertEquals(Math.log(2), root, 1e-5);
    }

    @Test
    public void testSolver_AllowedSolution_Above() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-3);
        double root = solver.solve(new UnivariateRealFunctionWrapper(function), -3, 1, AllowedSolution.ABOVE);
        assertEquals(Math.log(2), root, 1e-5);
    }

    @Test
    public void testSolver_AllowedSolution_AnySide() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3);
        double root = solver.solve(new UnivariateRealFunctionWrapper(function), -2, 2, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-5);
    }

    @Test
    public void testSolver_SolutionOrder_RegulaFalsi() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3);
        double x1 = solver.solve(new UnivariateRealFunctionWrapper(function), -2, 2);
        double x2 = solver.solve(new UnivariateRealFunctionWrapper(function), x1 - 1e-6, x1 + 1e-6);
        assertEquals(Precision.equals(x1, x2, 1e-6), true);
    }

    @Test
    public void testSolver_SolutionOrder_Illinois() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-3);
        double x1 = solver.solve(new UnivariateRealFunctionWrapper(function), -3, 1);
        double x2 = solver.solve(new UnivariateRealFunctionWrapper(function), x1 - 1e-6, x1 + 1e-6);
        assertEquals(Precision.equals(x1, x2, 1e-6), true);
    }

    @Test
    public void testSolver_SolutionOrder_Pegasus() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1e-3);
        double x1 = solver.solve(new UnivariateRealFunctionWrapper(function), -3, 3);
        double x2 = solver.solve(new UnivariateRealFunctionWrapper(function), x1 - 1e-6, x1 + 1e-6);
        assertEquals(Precision.equals(x1, x2, 1e-6), true);
    }

    @Test
    public void testSolver_SolverLimits() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3);
        int maxEval = 100;
        for (int i = 0; i < maxEval; i++) {
            try {
                solver.solve(new UnivariateRealFunctionWrapper(function), -2, 2);
            } catch (RuntimeException e) {
                break;
            }
        }
        TestUtil.assertTrue(i >= maxEval);
    }
}

class UnivariateRealFunctionWrapper implements UnivariateRealFunction {
    private final UnivariateFunction function;

    public UnivariateRealFunctionWrapper(UnivariateFunction function) {
        this.function = function;
    }

    @Override
    public double value(double x) {
        return function.value(x);
    }
}