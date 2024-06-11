package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.optim.PointValuePair.Pair;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        try {
            Pair<double[], double[]> solution = solver.doOptimize();
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithEpsilon() {
        double epsilon = 1.0e-6;
        SimplexSolver solver = new SimplexSolver(epsilon);
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, 1}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 3}, 0),
                        new LinearConstraint(new double[]{1, 1}, 1)},
                GoalType.MINIMIZE, true, epsilon);
        try {
            solver.solvePhase1(tableau);
            fail("Expected OptimizationException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testDoOptimizeWithNoFeasibleSolution() {
        double epsilon = 1.0e-6;
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, 1}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 3}, 0),
                        new LinearConstraint(new double[]{1, 1}, 1)},
                GoalType.MINIMIZE, true, epsilon);
        SimplexSolver solver = new SimplexSolver(epsilon);
        try {
            solver.solvePhase1(tableau);
            tableau.discardArtificialVariables();
            solver.doIteration(tableau); // this should throw an exception
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testDoOptimizeWithUnboundedSolution() {
        double epsilon = 1.0e-6;
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, 1}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 3}, 0),
                        new LinearConstraint(new double[]{1, 1}, 1)},
                GoalType.MINIMIZE, true, epsilon);
        SimplexSolver solver = new SimplexSolver(epsilon);
        try {
            solver.solvePhase1(tableau);
            tableau.discardArtificialVariables();
            solver.doIteration(tableau);
            solver.doIteration(tableau);
            solver.doIteration(tableau);
            tableau.discardArtificialVariables();
            solver.doIteration(tableau); // this should throw an exception
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testDoOptimizeWithOptimalSolution() {
        double epsilon = 1.0e-6;
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, 1}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 3}, 0),
                        new LinearConstraint(new double[]{1, 1}, 1)},
                GoalType.MINIMIZE, true, epsilon);
        SimplexSolver solver = new SimplexSolver(epsilon);
        try {
            solver.solvePhase1(tableau);
            tableau.discardArtificialVariables();
            solver.doIteration(tableau);
            solver.doIteration(tableau);
            solver.doIteration(tableau);
            tableau.discardArtificialVariables();
            solver.doIteration(tableau);
            solver.doIteration(tableau);
            solver.doIteration(tableau);
            Pair<double[], double[]> solution = solver.doOptimize();
            assertEquals(new double[]{0.0, 1.5}, solution.getFirst(), epsilon);
            assertEquals(-3.0, solution.getSecond(), epsilon);
        } catch (OptimizationException e) {
            fail("Unexpected OptimizationException");
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testDoIterationWithMaxIterationsExceeded() {
        double epsilon = 1.0e-6;
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, 1}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 3}, 0),
                        new LinearConstraint(new double[]{1, 1}, 1)},
                GoalType.MINIMIZE, true, epsilon);
        SimplexSolver solver = new SimplexSolver(epsilon);
        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();
        for (int i = 0; i < 100; i++) {
            solver.doIteration(tableau);
        }
    }
}

class SimpleLinearObjectiveFunction implements LinearObjectiveFunction {

    private double[] coefficients;
    private double constant;

    public SimpleLinearObjectiveFunction(double[] coefficients, double constant) {
        this.coefficients = coefficients;
        this.constant = constant;
    }

    @Override
    public double value(double[] x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * x[i];
        }
        return result + constant;
    }
}