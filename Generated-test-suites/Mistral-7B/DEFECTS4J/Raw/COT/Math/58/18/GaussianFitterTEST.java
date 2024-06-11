package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.initialguess.ArrayInitialGuesser;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleXDownhillPoint;
import org.apache.commons.math3.optim.points.PointValuePairList;
import org.apache.commons.math3.optim.points.RealVector;
import org.apache.commons.math3.optim.points.SimplePoints;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.GaussErrorFunction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class GaussianFitterTest {

    private Double[] initialGuess;
    private Double[] expectedResult;

    public GaussianFitterTest(Double[] initialGuess, Double[] expectedResult) {
        this.initialGuess = initialGuess;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters(name = "{index}: initialGuess = {0} -> expectedResult = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Double[]{1.0, 2.5, 1.0}, new Double[]{1.0, 2.5, 1.0}},
                {new Double[]{1.0, 2.5, 0.5}, new Double[]{1.12786, 2.5, 0.57645}},
                {new Double[]{1.0, 2.5, 2.0}, new Double[]{2.22182, 2.5, 1.10715}},
                {new Double[]{1.0, 3.0, 1.0}, new Double[]{1.0, 3.0, 1.0}},
                {new Double[]{1.0, 3.0, 3.0}, new Double[]{2.37162, 3.0, 1.53426}},
                {new Double[]{1.0, 3.0, 5.0}, new Double[]{3.44939, 3.0, 2.52253}},
                {new Double[]{1.0, 4.0, 1.0}, new Double[]{1.0, 4.0, 1.0}},
                {new Double[]{1.0, 4.0, 2.0}, new Double[]{1.84862, 4.0, 1.01132}},
                {new Double[]{1.0, 4.0, 4.0}, new Double[]{2.68593, 4.0, 2.01909}},
                {new Double[]{1.0, 5.0, 1.0}, new Double[]{1.0, 5.0, 1.0}},
                {new Double[]{1.0, 5.0, 3.0}, new Double[]{2.34245, 5.0, 1.61815}},
                {new Double[]{1.0, 5.0, 5.0}, new Double[]{3.02584, 5.0, 2.59106}}
        });
    }

    @Test
    public void testFit() throws Exception {
        NonlinearOptimizer optimizer = new GaussNewtonOptimizer(1e-12);
        ConvergenceChecker checker = new ConvergenceChecker(1e-12, MaxEval.EVAL_REACHED, GoalType.ABS_FE);
        ObjectiveFunction objectiveFunction = new GaussianFunction();
        PointValuePairList solutions = new PointValuePairList();
        optimizer.optimize(objectiveFunction, new SimpleBounds(0.0, Double.MAX_VALUE),
                new SimpleXDownhillPoint(initialGuess), solutions, checker, new ArrayInitialGuesser());
        Double[] result = solutions.getPoint(0).toArray();
        assertArrayEquals(expectedResult, result);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        fitter.addObservedPoint(null, 0.0);
        try {
            fitter.fit();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithEmptyObservations() {
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        try {
            fitter.fit(new Double[0][3]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidObservations() {
        Double[][] observations = {
                {null, 0.0},
                {1.0, null},
                {null, null}
        };
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        try {
            fitter.fit(observations);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFitWithInitialGuessNull() throws Exception {
        Double[][] observations = {
                {1.0, 2.5},
                {2.0, 2.5},
                {3.0, 2.5}
        };
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        try {
            fitter.fit(observations, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidInitialGuess() throws Exception {
        Double[][] observations = {
                {1.0, 2.5},
                {2.0, 2.5},
                {3.0, 2.5}
        };
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        try {
            fitter.fit(observations, new Double[]{3.0, 3.0, -1.0});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFitWithMinimalObservations() throws Exception {
        Double[][] observations = {
                {1.0, 2.5},
                {2.0, 2.5}
        };
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        fitter.fit(observations);
    }

    @Test
    public void testFitWithSimpleObservations() throws Exception {
        Double[][] observations = {
                {1.0, 2.5},
                {2.0, 2.5},
                {3.0, 2.5},
                {4.0, 2.5}
        };
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        fitter.fit(observations);
    }

    @Test
    public void testFitGaussianFunction() {
        ObjectiveFunction objectiveFunction = new GaussianFunction();
        Double[][] observations = {
                {1.0, 2.5},
                {2.0, 2.5},
                {3.0, 2.5},
                {4.0, 2.5}
        };
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        fitter.fit(observations);
        RealVector expected = RealVector.valueOf(new double[]{1.3958234, 2.5, 0.6643898});
        RealVector result = fitter.getFittedFunction().value(SimplePoints.singleton(0.0));
        assertArrayEquals(Erf.erf(expected.getEntry(0) / (2 * Math.SQRT_2)), result.getEntry(0), 0.001);
        assertEquals(expected.getEntry(1), result.getEntry(1), 0.001);
        assertEquals(expected.getEntry(2), result.getEntry(2), 0.001);
    }

    @Test
    public void testFitWithInitialGuessAndSimpleObservations() throws Exception {
        Double[][] observations = {
                {1.0, 2.5},
                {2.0, 2.5},
                {3.0, 2.5},
                {4.0, 2.5}
        };
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        fitter.fit(observations, new Double[]{1.0, 2.5, 1.0});
        RealVector expected = RealVector.valueOf(new double[]{1.3958234, 2.5, 0.6643898});
        RealVector result = fitter.getFittedFunction().value(SimplePoints.singleton(0.0));
        assertArrayEquals(Erf.erf(expected.getEntry(0) / (2 * Math.SQRT_2)), result.getEntry(0), 0.001);
        assertEquals(expected.getEntry(1), result.getEntry(1), 0.001);
        assertEquals(expected.getEntry(2), result.getEntry(2), 0.001);
    }

    static class GaussianFunction implements ObjectiveFunction {
        @Override
        public PointValuePair value(RealVector x) {
            double norm = x.getEntry(0);
            double mean = x.getEntry(1);
            double sigma = x.getEntry(2);
            double expectedValue = 0.0;
            for (Double[] observation : observations) {
                expectedValue += new Gaussian(mean, sigma.sqrt()).value(observation[0]);
            }
            double actualValue = 0.0;
            for (Double[] observation : observations) {
                actualValue += Math.pow(GaussErrorFunction.erf(
                        (observation[0] - mean) / (2 * sigma), 1), 2);
            }
            return new PointValuePair(actualValue, expectedValue);
        }

        private static Double[][] observations = {
                {1.0, 2.5},
                {2.0, 2.5},
                {3.0, 2.5},
                {4.0, 2.5}
        };
    }
}