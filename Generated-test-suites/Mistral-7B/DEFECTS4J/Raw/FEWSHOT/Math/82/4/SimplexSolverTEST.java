package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.PrecisionUtils.DoubleArray;
import org.junit.Test;

import org.apache.commons.cli.SimplexSolver;

public class SimplexSolverTest {

    @Test
    public void testSolverWithSimpleExample() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0},
            {1.0, 1.0, 3.0, 2.0},
            {2.0, -1.0, 1.0, 0.5},
            {-1.0, 2.0, -1.0, 2.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0, 2.0, 0.5, 4.0});
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair result = solver.doOptimize(A, b);
        assertTrue(result.getValue()[0] > Precision.EPSILON);
        assertArrayEquals(new double[] {-0.5, 1.0, 1.5, 2.5}, result.getPoint()[0], 0.001);
    }

    @Test
    public void testSolverWithNegativeValues() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0},
            {1.0, -1.0, 3.0, 2.0},
            {2.0, 1.0, 1.0, 0.5},
            {-1.0, 2.0, -1.0, 2.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0, 2.0, 0.5, 4.0});
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair result = solver.doOptimize(A, b);
        assertTrue(result.getValue()[0] > Precision.EPSILON);
        assertArrayEquals(new double[] {1.0, -0.5, 1.5, 2.5}, result.getPoint()[0], 0.001);
    }

    @Test
    public void testSolverWithUnboundedSolution() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0},
            {1.0, -1.0, 3.0, 2.0},
            {2.0, 1.0, 1.0, 0.5},
            {-1.0, 2.0, -1.0, 2.0},
            {0.0, 0.0, 0.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0, 2.0, 0.5, 4.0});
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (org.apache.commons.math.optimization.UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithNoFeasibleSolution() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0},
            {1.0, -1.0, 3.0, 2.0},
            {2.0, 1.0, 1.0, 0.5},
            {-1.0, 2.0, -1.0, 2.0},
            {0.0, 0.0, 0.0, 1.0},
            {0.0, 0.0, 0.0, -1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0, 2.0, 0.5, 4.0});
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (org.apache.commons.math.optimization.NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithNoSolution() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0},
            {1.0, -1.0, 3.0, 2.0},
            {2.0, 1.0, 1.0, 0.5},
            {-1.0, 2.0, -1.0, 2.0},
            {0.0, 0.0, 0.0, 1.0},
            {0.0, 0.0, 0.0, 0.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0, 2.0, 0.5, 4.0});
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (org.apache.commons.math.optimization.UnsatisfiedConstraintException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithZeroObjectiveFunction() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0},
            {1.0, 1.0, 3.0, 2.0},
            {2.0, -1.0, 1.0, 0.5},
            {-1.0, 2.0, -1.0, 2.0},
            {0.0, 0.0, 0.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0, 2.0, 0.5, 0.0});
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair result = solver.doOptimize(A, b);
        assertTrue(result.getValue()[0] > Precision.EPSILON);
        assertArrayEquals(new double[] {0.5, 1.0, 1.5, 2.5}, result.getPoint()[0], 0.001);
    }

    @Test
    public void testSolverWithZeroMatrix() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {});
        RealVector b = new ArrayRealVector(new double[] {});
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithZeroB() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {});
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithZeroEpsilon() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0});
        SimplexSolver solver = new SimplexSolver(0.0);
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithNullMatrix() {
        RealMatrix A = null;
        RealVector b = new ArrayRealVector(new double[] {3.0});
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithNullB() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0}
        });
        RealVector b = null;
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithNullSolver() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0});
        SimplexSolver solver = null;
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithDifferentMatrixAndBVectorSizes() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0, 4.0});
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithNegativeEpsilon() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {3.0});
        SimplexSolver solver = new SimplexSolver(-1.0);
        try {
            solver.doOptimize(A, b);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithZeroObjectiveFunctionAndZeroB() {
        RealMatrix A = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 1.0, 1.0}
        });
        RealVector b = new ArrayRealVector(new double[] {0.0});
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair result = solver.doOptimize(A, b);
        assertTrue(result.getValue()[0] > Precision.EPSILON);
        assertArrayEquals(new double[] {0.0, 0.0, 0.0, 0.0}, result.getPoint()[0], 0.001);
    }
}