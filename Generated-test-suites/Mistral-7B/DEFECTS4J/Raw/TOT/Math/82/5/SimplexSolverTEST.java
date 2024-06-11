package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealArrayRealVector;
import org.apache.commons.math3.linear.RealColumnVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleLinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearPointValuePair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SimplexSolverTest {

    private double[][] f;
    private LinearConstraint[] constraints;
    private LinearObjectiveFunction goalType;
    private boolean restrictToNonNegative;
    private double epsilon;

    public SimplexSolverTest(double[][] f, LinearConstraint[] constraints, LinearObjectiveFunction goalType, boolean restrictToNonNegative, double epsilon) {
        this.f = f;
        this.constraints = constraints;
        this.goalType = goalType;
        this.restrictToNonNegative = restrictToNonNegative;
        this.epsilon = epsilon;
    }

    @Parameterized.Parameters(name = "{index}: f = {0}, constraints = {1}, goalType = {2}, restrictToNonNegative = {3}, epsilon = {4}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new double[][]{{1.0, -1.0, 2.0}, {-1.0, 2.0, 3.0}, {3.0, 1.0, 1.0}},
                        new LinearConstraint[]{
                                new LinearConstraint(true, new RealColumnVector(new double[]{1.0, -1.0})),
                                new LinearConstraint(false, new RealColumnVector(new double[]{-1.0, 3.0}))},
                        new SimpleLinearObjectiveFunction(new RealColumnVector(new double[]{1.0})),
                        false, 1e-6},
                {new double[][]{{-1.0, 2.0, -3.0}, {-2.0, 3.0, 1.0}, {1.0, 5.0, 1.0}},
                        new LinearConstraint[]{
                                new LinearConstraint(false, new RealColumnVector(new double[]{1.0, 3.0})),
                                new LinearConstraint(true, new RealColumnVector(new double[]{-1.0, 2.0}))},
                        new SimpleLinearObjectiveFunction(new RealColumnVector(new double[]{1.0})),
                        false, 1e-6},
                {new double[][]{{1.0, -1.0, 2.0}, {-1.0, 2.0, 3.0}, {3.0, 1.0, 1.0}},
                        new LinearConstraint[]{
                                new LinearConstraint(true, new RealColumnVector(new double[]{1.0, -1.0})),
                                new LinearConstraint(false, new RealColumnVector(new double[]{-1.0, 3.0}))},
                        new SimpleLinearObjectiveFunction(new RealColumnVector(new double[]{1.0})),
                        true, 1e-6},
                {new double[][]{{-1.0, 2.0, -3.0}, {-2.0, 3.0, 1.0}, {1.0, 5.0, 1.0}},
                        new LinearConstraint[]{
                                new LinearConstraint(false, new RealColumnVector(new double[]{1.0, 3.0})),
                                new LinearConstraint(true, new RealColumnVector(new double[]{-1.0, 2.0}))},
                        new SimpleLinearObjectiveFunction(new RealColumnVector(new double[]{1.0})),
                        true, 1e-6},
                {new double[][]{{0.0, -1.0, 2.0}, {-1.0, 2.0, 3.0}, {3.0, 1.0, 1.0}},
                        new LinearConstraint[]{
                                new LinearConstraint(true, new RealColumnVector(new double[]{1.0, -1.0})),
                                new LinearConstraint(false, new RealColumnVector(new double[]{-1.0, 3.0}))},
                        new SimpleLinearObjectiveFunction(new RealColumnVector(new double[]{1.0})),
                        false, 1e-12}
        });
    }

    @Test
    public void testOptimize() throws Exception {
        SimplexSolver solver = new SimplexSolver(epsilon);
        RealPointValuePair solution = solver.doOptimize();
        LinearPointValuePair expectedSolution = new LinearPointValuePair(new RealColumnVector(solution.getPoint()), goalType.value(solution.getPoint()));
        PointValuePair expectedOptimalSolution = goalType.getValue(solver.getLinearConstraints().getConstraints());
        Assert.assertArrayEquals(expectedOptimalSolution.getPoint().toArray(), solution.getPoint().toArray(), 0.001);
        Assert.assertEquals(expectedOptimalSolution.getValue(), solution.getValue(), epsilon);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        SimplexSolver solver = new SimplexSolver(1e-12);
        RealMatrix constraintsMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 2.0}, {0.0, 1.0, 2.0}});
        RealVector b = new RealArrayRealVector(new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[2];
        for (int i = 0; i < 2; i++) {
            constraints[i] = new LinearConstraint(false, new RealColumnVector(constraintsMatrix.getColumnVector(i)), b.getEntry(i));
        }
        solver.optimize(new LinearObjectiveFunction(new SimpleLinearObjectiveFunction(new RealColumnVector(new double[]{1.0}))), constraints, restrictToNonNegative, epsilon);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver(epsilon);
        RealMatrix constraintsMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 2.0}, {0.0, 1.0, -2.0}});
        RealVector b = new RealArrayRealVector(new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[2];
        for (int i = 0; i < 2; i++) {
            constraints[i] = new LinearConstraint(false, new RealColumnVector(constraintsMatrix.getColumnVector(i)), b.getEntry(i));
        }
        solver.optimize(new LinearObjectiveFunction(new SimpleLinearObjectiveFunction(new RealColumnVector(new double[]{1.0}))), constraints, restrictToNonNegative, epsilon);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver(epsilon);
        RealMatrix constraintsMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 2.0}, {0.0, 1.0, 3.0}});
        RealVector b = new RealArrayRealVector(new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[2];
        for (int i = 0; i < 2; i++) {
            constraints[i] = new LinearConstraint(false, new RealColumnVector(constraintsMatrix.getColumnVector(i)), b.getEntry(i));
        }
        solver.optimize(new LinearObjectiveFunction(new SimpleLinearObjectiveFunction(new RealColumnVector(new double[]{1.0}))), constraints, restrictToNonNegative, epsilon);
    }
}