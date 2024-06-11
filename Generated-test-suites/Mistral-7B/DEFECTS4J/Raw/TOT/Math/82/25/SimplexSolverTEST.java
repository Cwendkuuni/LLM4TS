package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.optim.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testEmptyProblem() {
        PointValuePair solution = new SimplexSolver().optimize();
        assertEquals(0, solution.getValue(), 0.0);
        assertEquals(new double[0], solution.getPoint(), 0.0);
    }

    @Test
    public void testSimpleProblem() {
        RealVector coefficients = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}).getColumnVector(0);
        RealVector constants = new Array2DRowRealMatrix(new double[][]{{-2.0}, {3.0}}).getColumnVector(0);
        OptimizationProblem problem = new LinearOptimizationProblem(coefficients, constants);
        PointValuePair solution = new SimplexSolver().optimize(problem);
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][]{{-2.0 / 3.0}, {2.0 / 3.0}}).getColumnVector(0);
        assertEquals(expectedSolution.getEntry(0), solution.getPoint().getEntry(0), 0.0);
        assertEquals(expectedSolution.getEntry(1), solution.getPoint().getEntry(1), 0.0);
        assertEquals(-2.0, solution.getValue(), 0.0);
    }

    @Test
    public void testMaxEvalException() throws MaxEvalException {
        RealVector coefficients = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}).getColumnVector(0);
        RealVector constants = new Array2DRowRealMatrix(new double[][]{{-2.0}, {Double.POSITIVE_INFINITY}}).getColumnVector(0);
        OptimizationProblem problem = new LinearOptimizationProblem(coefficients, constants);
        try {
            new SimplexSolver().optimize(problem);
            fail("MaxEvalException expected");
        } catch (MaxEvalException e) {
            // expected
        }
    }

    @Test
    public void testNoFeasibleSolutionException() throws NoFeasibleSolutionException {
        RealVector coefficients = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}).getColumnVector(0);
        RealVector constants = new Array2DRowRealMatrix(new double[][]{{-2.0}, {2.0}}).getColumnVector(0);
        OptimizationProblem problem = new LinearOptimizationProblem(coefficients, constants);
        try {
            new SimplexSolver().optimize(problem);
            fail("NoFeasibleSolutionException expected");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testUnboundedSolutionException() throws UnboundedSolutionException {
        RealVector coefficients = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}).getColumnVector(0);
        RealVector constants = new Array2DRowRealMatrix(new double[][]{{-2.0}, {0.0}}).getColumnVector(0);
        OptimizationProblem problem = new LinearOptimizationProblem(coefficients, constants);
        try {
            new SimplexSolver().optimize(problem);
            fail("UnboundedSolutionException expected");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testInfeasibleProblem() {
        RealVector coefficients = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}).getColumnVector(0);
        RealVector constants = new Array2DRowRealMatrix(new double[][]{{-2.0}, {4.0}}).getColumnVector(0);
        OptimizationProblem problem = new LinearOptimizationProblem(coefficients, constants);
        try {
            new SimplexSolver().optimize(problem);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDegenerateSolution() {
        RealVector coefficients = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 0.0}, {3.0, 1.0, 0.0}}).getColumnVector(0);
        RealVector constants = new Array2DRowRealMatrix(new double[][]{{-2.0}, {3.0}, {0.0}}).getColumnVector(0);
        OptimizationProblem problem = new LinearOptimizationProblem(coefficients, constants);
        PointValuePair solution = new SimplexSolver().optimize(problem);
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][]{{-2.0 / 3.0}, {2.0 / 3.0}, {0.0}}).getColumnVector(0);
        assertEquals(expectedSolution.getEntry(0), solution.getPoint().getEntry(0), 0.0);
        assertEquals(expectedSolution.getEntry(1), solution.getPoint().getEntry(1), 0.0);
        assertEquals(expectedSolution.getEntry(2), solution.getPoint().getEntry(2), 0.0);
        assertEquals(-2.0, solution.getValue(), 0.0);
    }

    @Test
    public void testEpsilon() {
        RealVector coefficients = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}, {1.0, -2.0}, {3.0, 1.0}}).getColumnVector(0);
        RealVector constants = new Array2DRowRealMatrix(new double[][]{{-2.0}, {3.0}, {2.0}, {3.0}}).getColumnVector(0);
        SimplexSolver solver = new SimplexSolver(1.0e-4);
        PointValuePair solution = solver.optimize(new LinearOptimizationProblem(coefficients, constants));
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][]{{-2.0 / 3.0 - 1.0e-4}, {2.0 / 3.0 + 1.0e-4}, {1.0 / 3.0}, {1.0 / 3.0}}).getColumnVector(0);
        assertEquals(expectedSolution.getEntry(0), solution.getPoint().getEntry(0), 1.0e-4);
        assertEquals(expectedSolution.getEntry(1), solution.getPoint().getEntry(1), 1.0e-4);
        assertEquals(expectedSolution.getEntry(2), solution.getPoint().getEntry(2), 1.0e-4);
        assertEquals(expectedSolution.getEntry(3), solution.getPoint().getEntry(3), 1.0e-4);
        assertEquals(-2.0, solution.getValue(), 1.0e-4);
    }
}