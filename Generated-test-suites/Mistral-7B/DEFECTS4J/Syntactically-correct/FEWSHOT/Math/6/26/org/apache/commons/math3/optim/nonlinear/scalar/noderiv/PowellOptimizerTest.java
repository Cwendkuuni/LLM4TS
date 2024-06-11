package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializers.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.initializers.UniformRealVectorGenerator;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

public class PowellOptimizerTest {
    private PowellOptimizer powellOptimizer;
    private UnivariateObjectiveFunction objectiveFunction;

    @Before
    public void setUp() {
        // Initialize the objective function
        objectiveFunction = new UnivariateObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
    }

    @Test
    public void testPowellOptimizerConstruction() {
        powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        assertNull(powellOptimizer.getConvergenceChecker());
    }

    @Test
    public void testPowellOptimizerCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customConvergenceChecker = (iter, previous, current) -> true;
        powellOptimizer = new PowellOptimizer(1e-6, 1e-6, customConvergenceChecker);
        assertNotNull(powellOptimizer.getConvergenceChecker());
    }

    @Test
    public void testPowellOptimizerLineSearchTolerances() {
        powellOptimizer = new PowellOptimizer(1e-6, 1e-6, 1e-6, 1e-6);
        assertEquals(1e-6, powellOptimizer.getLineSearch().getRelTolerance(), 0.0);
        assertEquals(1e-6, powellOptimizer.getLineSearch().getAbsTolerance(), 0.0);
    }

    @Test
    public void testPowellOptimizerEmptyInitialGuess() {
        powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        assertThrows(IllegalArgumentException.class, () -> powellOptimizer.optimize(new PointValuePair[0], objectiveFunction));
    }

    @Test
    public void testPowellOptimizerSingleInitialGuess() {
        double initialGuess = 1.0;
        powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = powellOptimizer.optimize(new PointValuePair[]{new PointValuePair(initialGuess, objectiveFunction.value(initialGuess))}, objectiveFunction);
        assertEquals(initialGuess, result.getPoint(), 0.0);
    }

    @Test
    public void testPowellOptimizerMultipleInitialGuesses() {
        double[] initialGuesses = {1.0, 2.0, 3.0};
        powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = powellOptimizer.optimize(new PointValuePair[initialGuesses.length][], objectiveFunction, new ArrayRealVectorInitializer(initialGuesses));
        // The optimizer does not guarantee a specific order for the results
        // so we cannot directly check if a specific initial guess is the
        // solution, but we can check that all the results are valid points
        for (PointValuePair pv : result.getPoints()) {
            assertEquals(pv.getValue(), objectiveFunction.value(pv.getPoint()), 0.0);
        }
    }

    @Test
    public void testPowellOptimizerWithBounds() {
        double lowerBound = -5.0;
        double upperBound = 5.0;
        powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = powellOptimizer.optimize(new PointValuePair[0], new UnivariateObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        }, new ArrayRealVectorInitializer(new double[]{0.0}), new SimpleBounds(lowerBound, upperBound));
        assertEquals(lowerBound, result.getPoint(), 0.0);
    }

    @Test
    public void testPowellOptimizerWithRandomInitialGuesses() {
        int numInitialGuesses = 10;
        double lowerBound = -5.0;
        double upperBound = 5.0;
        powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = powellOptimizer.optimize(new PointValuePair[0], objectiveFunction, new UniformRealVectorGenerator(new MersenneTwister(), lowerBound, upperBound), numInitialGuesses);
        assertEquals(-2.38472112484773, result.getPoint(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        powellOptimizer = new PowellOptimizer(-1e-6, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeThreshold() {
        powellOptimizer = new PowellOptimizer(0.0, 1e-6);
    }
}