package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimpleValueChecker.Throw;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimize;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximize;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.QPProblem;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexSolver.SimplexInitialPoint;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexSolver.SimplexMaximizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexSolver.SimplexMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexSolver.SimplexPoint;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexTenSimpleX;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexX;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SimplexOptimizer.
 */
public class SimplexOptimizerTest {

    // Test data

    /** A simple polynomial function as objective function. */
    private UnivariateFunction objectiveFunction;

    /** A simple polynomial constraint set. */
    private LinearConstraintSet constraintSet;

    /** A linear objective function for QP problem. */
    private LinearObjectiveFunction linObjFunc;

    /** A QP problem for testing the solver. */
    private QPProblem qpProblem;

    /** An instance of the simpled optimizer under test. */
    private SimplexOptimizer optimizer;

    /**
     * Sets up test data.
     */
    @Before
    public void setUp() {
        // Set up objective function for testing
        objectiveFunction = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0}, 3);

        // Set up constraint set for testing
        constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(new LinearConstraint(1.0, 1.0, 1.0, 0.0));

        // Set up linear objective function for QP problem
        linObjFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-3.0, -1.0});

        // Create QP problem for testing
        qpProblem = new QPProblem(linObjFunc, constraintSet, new double[]{-1.0, -1.0});

        // Initialize simpled optimizer
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimize() {
        // Test minimization
        double[] startPoint = {0.0, 0.0};
        PointValuePair result = optimizer.optimize(100, objectiveFunction, GoalType.MINIMIZE, startPoint);
        double expectedValue = -3.0;
        double epsilon = 1e-5;
        assertEquals("Unexpected minimum value", expectedValue, result.getValue(), epsilon);
        assertArrayEquals("Unexpected minimum point", new double[]{1.0, 1.0}, result.getPoint());

        // Test maximization
        startPoint = new double[]{0.0, 0.0};
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5, Throw.THROW_EXCEPTION));
        PointValuePair maxResult = optimizer.optimize(100, objectiveFunction,
            new GoalTypeWeightedSum(new double[]{0.5, 0.5}, GoalTypeMinimize, GoalTypeMaximize), startPoint);
        double maxExpectedValue = 3.0;
        assertEquals("Unexpected maximum value", maxExpectedValue, maxResult.getValue(), epsilon);
        assertArrayEquals("Unexpected maximum point", new double[]{1.0, 1.0}, maxResult.getPoint());

        // Test QP problem
        PointValuePair qpResult = optimizer.optimize(100, qpProblem, new GoalTypeWeightedSum(new double[]{0.5, 0.5},
            GoalTypeMinimize, GoalTypeMaximize), new OptimizationData[]{new SimplexX(new SimplexTenSimpleX())});
        double qpExpectedValue = -2.5;
        assertEquals("Unexpected QP problem value", qpExpectedValue, qpResult.getValue(), epsilon);
        assertArrayEquals("Unexpected QP problem point", new double[]{-0.5, -0.5}, qpResult.getPoint());
    }

    /**
     * Test method for {@link SimplexOptimizer#setSimplex(AbstractSimplex)}.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNullArgument() {
        optimizer.setSimplex(null);
    }
}