package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.Funcs;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalWithDefaultCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair initialGuess = new PointValuePair(new double[]{-1.0, 1.0}, 0.0);
        double epsilon = 1e-6;
        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker(epsilon);

        optimizer.optimize(Funcs.ROSENBROCK, initialGuess, goalType, convergenceChecker);

        assertTrue(convergenceChecker.isConverged());
        assertEquals(-1.000214397084253, simplex[0].getPointRef()[0], epsilon);
        assertEquals(0.000183370326931, simplex[0].getPointRef()[1], epsilon);
    }

    @Test
    public void testMultiDirectionalWithCustomCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair initialGuess = new PointValuePair(new double[]{-2.0, -2.0}, 4.0);
        double epsilon = 1e-6;
        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker(epsilon);

        optimizer.optimize(Funcs.SIN, initialGuess, goalType, convergenceChecker);

        assertTrue(convergenceChecker.isConverged());
        assertEquals(-1.0, simplex[0].getPointRef()[0], epsilon);
        assertEquals(-1.0, simplex[0].getPointRef()[1], epsilon);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalWithInvalidCoefficients() {
        MultiDirectional optimizer = new MultiDirectional(-1.0, 0.5);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testMultiDirectionalWithFailedEvaluation() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair initialGuess = new PointValuePair(new double[]{Double.POSITIVE_INFINITY}, 0.0);
        double epsilon = 1e-6;
        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker(epsilon);

        optimizer.optimize(Funcs.SIN, initialGuess, goalType, convergenceChecker);
    }

    @Test(expected = OptimizationException.class)
    public void testMultiDirectionalWithMaxEvaluationsExceeded() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair initialGuess = new PointValuePair(new double[]{-1.0, 1.0}, 0.0);
        double epsilon = 1e-6;
        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker(epsilon);
        int maxEvaluations = 1;

        optimizer.setMaxIterations(maxEvaluations);

        optimizer.optimize(Funcs.ROSENBROCK, initialGuess, goalType, convergenceChecker);
    }

}