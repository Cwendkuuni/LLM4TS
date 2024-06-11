package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import org.apache.commons.cli.SimplexSolver;
import org.apache.commons.cli.SimplexTableau;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_SimpleProblem() throws Exception {
        // Given
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 5.0}, {-3.0, -2.0}};
        SimplexTableau tableau = new SimplexTableau(f, new int[0], null, false, 1.0e-12);

        // When
        Pair<double[], Double> solution = solver.doOptimize();

        // Then
        assertNotNull(solution);
        assertEquals(2, solution.getFirst().length);
        assertEquals(0.0, Precision.round(solution.getFirst()[0], 10), 1.0e-12);
        assertEquals(5.0 / 3.0, Precision.round(solution.getFirst()[1], 10), 1.0e-12);
        assertEquals(4.0 / 3.0, Precision.round(solution.getSecond(), 10), 1.0e-12);
    }

    @Test
    public void testDoOptimize_FeasibleSolution() throws Exception {
        // Given
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{0.0, 5.0}, {1.0, 1.0}, {2.0, 2.0}};
        SimplexTableau tableau = new SimplexTableau(f, new int[0], null, false, 1.0e-12);

        // When
        Pair<double[], Double> solution = solver.doOptimize();

        // Then
        assertNotNull(solution);
        assertEquals(2, solution.getFirst().length);
        assertEquals(0.0, Precision.round(solution.getFirst()[0], 10), 1.0e-12);
        assertEquals(5.0 / 3.0, Precision.round(solution.getFirst()[1], 10), 1.0e-12);
        assertEquals(1.0 / 3.0, Precision.round(solution.getSecond(), 10), 1.0e-12);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution() throws Exception {
        // Given
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{0.0, 5.0}, {1.0, 1.0}, {Double.POSITIVE_INFINITY, 2.0}};
        SimplexTableau tableau = new SimplexTableau(f, new int[0], null, false, 1.0e-12);

        // When
        solver.doOptimize();

        // Then
        fail("An UnboundedSolutionException should have been thrown.");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() throws Exception {
        // Given
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{0.0, 5.0}, {1.0, 1.0}, {Double.NEGATIVE_INFINITY, 2.0}};
        SimplexTableau tableau = new SimplexTableau(f, new int[0], null, false, 1.0e-12);

        // When
        solver.doOptimize();

        // Then
        fail("A NoFeasibleSolutionException should have been thrown.");
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testDoOptimize_MaxIterationsExceeded() throws Exception {
        // Given
        SimplexSolver solver = new SimplexSolver(1.0e-4);
        double[][] f = {{1.0, -1.0, 0.0}, {1.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, new int[]{-1, -1, 1}, null, false, 1.0e-12);

        // When
        solver.doOptimize();

        // Then
        fail("A MaxIterationsExceededException should have been thrown.");
    }
}