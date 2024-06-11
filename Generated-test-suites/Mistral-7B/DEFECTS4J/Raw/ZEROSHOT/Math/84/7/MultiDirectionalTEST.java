package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleValuesFunction;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.initialization.SimplexInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultiDirectional class.
 */
public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private SimpleValuesFunction function;
    private RealPointValuePair[] initialPoints;
    private ConvergenceChecker<RealPointValuePair> convergenceChecker;
    private SimplexInitializer initializer;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        function = new SimpleValuesFunction(x -> Math.pow(x[0], 2) + Math.pow(x[1], 2));
        initialPoints = new RealPointValuePair[] {new RealPointValuePair(new double[] {-1.0, -1.0}, 4.0),
                                                 new RealPointValuePair(new double[] {-1.0, 1.0}, 4.0),
                                                 new RealPointValuePair(new double[] {1.0, -1.0}, 4.0),
                                                 new RealPointValuePair(new double[] {1.0, 1.0}, 4.0)};
        convergenceChecker = new RealConvergenceChecker(1.0E-6, 100);
        initializer = new SimplexInitializer(initialPoints);
    }

    @Test
    public void testMultiDirectionalOptimizerWithDefaultCoefficients() throws Exception {
        optimizer.setFunction(function);
        optimizer.optimize(initializer, GoalType.MINIMIZE, convergenceChecker);
        PointValuePair bestSolution = optimizer.getBestSolution();
        assertNotNull(bestSolution);
        double expectedBestValue = 0.0;
        double epsilon = 1.0E-6;
        assertTrue(Math.abs(bestSolution.getValue() - expectedBestValue) < epsilon);
    }

    @Test
    public void testMultiDirectionalOptimizerWithCustomCoefficients() throws Exception {
        optimizer = new MultiDirectional(3.0, 0.8);
        optimizer.setFunction(function);
        optimizer.optimize(initializer, GoalType.MINIMIZE, convergenceChecker);
        PointValuePair bestSolution = optimizer.getBestSolution();
        assertNotNull(bestSolution);
        double expectedBestValue = 0.0144225; // Approximate minimum value
        double epsilon = 1.0E-6;
        assertTrue(Math.abs(bestSolution.getValue() - expectedBestValue) < epsilon);
    }

}