package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver.SimplexTableau;
import org.apache.commons.math3.optimization.linear.SimpleXProblem;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_oneVariable() {
        SimpleXProblem problem = new SimpleXProblem(1, 1);
        problem.setObjectiveFunction(0, 1.0);
        problem.setConstraint(0, 0, 1.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(1.0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_twoVariables() {
        SimpleXProblem problem = new SimpleXProblem(2, 2);
        problem.setObjectiveFunction(0, 1.0, 1.0);
        problem.setConstraint(0, 0, 1.0, 1.0);
        problem.setConstraint(1, 1, 1.0, -1.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(1.0 / 3.0, solution.getPoint()[0], 1e-6);
        assertEquals(1.0 / 3.0, solution.getPoint()[1], 1e-6);
        assertEquals(1.0 / 3.0, solution.getValue(), 1e-6);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_noFeasibleSolution() {
        SimpleXProblem problem = new SimpleXProblem(2, 2);
        problem.setObjectiveFunction(0, 1.0, 1.0);
        problem.setConstraint(0, 0, 1.0, 1.0);
        problem.setConstraint(1, 1, -1.0, -1.0);
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_unboundedSolution() {
        SimpleXProblem problem = new SimpleXProblem(2, 2);
        problem.setObjectiveFunction(0, 1.0, 1.0);
        problem.setConstraint(0, 0, 1.0, 1.0);
        problem.setConstraint(0, 1, -1.0, Integer.MAX_VALUE);
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimize_maxCountExceeded() {
        SimpleXProblem problem = new SimpleXProblem(2, 2);
        problem.setObjectiveFunction(0, 1.0, 1.0);
        problem.setConstraint(0, 0, 1.0, 1.0);
        problem.setConstraint(0, 1, -1.0, Integer.MIN_VALUE);
        SimplexSolver solver = new SimplexSolver(1e-12, 10);
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
        solver.doIteration(new SimplexTableau(problem.getSize(), problem.getNumConstraints()));
    }

    @Test
    public void testSolvePhase1() {
        SimpleXProblem problem = new SimpleXProblem(2, 2);
        problem.setObjectiveFunction(0, 1.0, 1.0);
        problem.setConstraint(0, 0, 1.0, 1.0);
        problem.setConstraint(0, 1, -1.0, Integer.MAX_VALUE);
        problem.setConstraint(1, 0, -1.0, Integer.MAX_VALUE);
        problem.setConstraint(1, 1, 1.0, 1.0);

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(problem.getSize(), problem.getNumConstraints());
        solver.solvePhase1(tableau);

        List<Double> expected = Arrays.asList(1.0e-6, -1.0e-6, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0,
            0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        assertArrayEquals(expected.stream().mapToDouble(d -> d).toArray(), tableau.getSolution(), 1e-6);
    }

    @Test
    public void testGetPivotColumn() {
        SimpleXProblem problem = new SimpleXProblem(3, 3);
        problem.setObjectiveFunction(0, 1.0, 1.0, 1.0);
        SimplexTableau tableau = new SimplexTableau(problem.getSize(), problem.getNumConstraints());
        tableau.setEntry(0, 0, -3.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -1.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, 3.0);
        tableau.setEntry(2, 0, 1.0);
        tableau.setEntry(2, 1, 1.0);
        tableau.setEntry(2, 2, 1.0);

        Integer pivotCol = SimplexSolver.getPivotColumn(tableau);
        assertEquals(2, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimpleXProblem problem = new SimpleXProblem(3, 3);
        problem.setObjectiveFunction(0, 1.0, 1.0, 1.0);
        SimplexTableau tableau = new SimplexTableau(problem.getSize(), problem.getNumConstraints());
        tableau.setEntry(0, 0, -3.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -1.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, 3.0);
        tableau.setEntry(2, 0, 1.0);
        tableau.setEntry(2, 1, 1.0);
        tableau.setEntry(2, 2, 1.0);

        Integer pivotRow = SimplexSolver.getPivotRow(tableau, 2);
        assertEquals(1, pivotRow.intValue());
    }
}