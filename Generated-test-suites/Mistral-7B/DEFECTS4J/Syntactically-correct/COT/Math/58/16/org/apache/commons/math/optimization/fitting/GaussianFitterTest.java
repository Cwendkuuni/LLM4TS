import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearProgram;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleXSearch;
import org.apache.commons.math3.optim.points.DoublePointValuePair;
import org.apache.commons.math3.optim.points.Points2D;
import org.apache.commons.math3.optim.initialization.Guess;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleXSearch.SimpleXSearchType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.LinearConstraintSet.ConstraintType;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.function.ParametricFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Test class for GaussianFitter.
 */
public class GaussianFitterTest {

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}.
     */
    @Test
    public void testGaussianFitterConstructor() {
        UnivariateOptimizer optimizer = new SimpleXSearch(SimpleXSearchType.QUADRILATERAL);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertNotNull(fitter);
    }

    /**
     * Test method for {@link GaussianFitter#fit(double[], double[])}.
     */
    @Test
    public void testFit() {
        double[] initialGuess = { 1.0, 2.0, 0.5 };
        double[] observations = { 1.5, 2.5, 3.5, 4.5, 5.5 };
        double[] expected = { 1.0, 2.5, 0.5 };

        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));
        fitter.addObservedPoint(new WeightedObservedPoint(observations[0], 1.0));
        fitter.addObservedPoint(new WeightedObservedPoint(observations[1], 1.0));
        fitter.addObservedPoint(new WeightedObservedPoint(observations[2], 1.0));
        fitter.addObservedPoint(new WeightedObservedPoint(observations[3], 1.0));
        fitter.addObservedPoint(new WeightedObservedPoint(observations[4], 1.0));

        double[] result = fitter.fit(initialGuess);
        assertArrayEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link GaussianFitter#fit()}.
     */
    @Test
    public void testFitWithoutInitialGuess() {
        double[] observations = { 1.5, 2.5, 3.5, 4.5, 5.5 };
        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));

        for (double observation : observations) {
            fitter.addObservedPoint(new WeightedObservedPoint(observation, 1.0));
        }

        double[] expected = { 1.435421012943457, 2.520583857643675, 0.4560636236836847 };
        double[] result = fitter.fit();
        assertArrayEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link GaussianFitter#addObservedPoint(double, double)}.
     */
    @Test
    public void testAddObservedPoint() {
        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));

        fitter.addObservedPoint(1.5, 1.0);
        WeightedObservedPoint point = fitter.getLastObservedPoint();
        assertEquals(1.5, point.getX(), 0.0);
        assertEquals(1.0, point.getY(), 0.0);
        assertEquals(1.0, point.getWeight(), 0.0);
    }

    /**
     * Test method for {@link GaussianFitter#getLastObservedPoint()}.
     */
    @Test
    public void testGetLastObservedPoint() {
        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));
        fitter.addObservedPoint(1.5, 1.0);

        WeightedObservedPoint point = fitter.getLastObservedPoint();
        assertEquals(1.5, point.getX(), 0.0);
        assertEquals(1.0, point.getY(), 0.0);
        assertEquals(1.0, point.getWeight(), 0.0);
    }

    /**
     * Test method for {@link GaussianFitter#getObservations()}.
     */
    @Test
    public void testGetObservations() {
        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));
        double[] observations = { 1.5, 2.5, 3.5, 4.5, 5.5 };

        for (double observation : observations) {
            fitter.addObservedPoint(observation, 1.0);
        }

        WeightedObservedPoint[] expected = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.5, 1.0),
            new WeightedObservedPoint(2.5, 1.0),
            new WeightedObservedPoint(3.5, 1.0),
            new WeightedObservedPoint(4.5, 1.0),
            new WeightedObservedPoint(5.5, 1.0)
        };

        WeightedObservedPoint[] observations2 = fitter.getObservations();
        assertArrayEquals(expected, observations2, 0.0);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ParametricFunction, double[])}.
     */
    @Test
    public void testFitWithParametricFunction() {
        double[] initialGuess = { 1.0, 2.0, 0.5 };
        double[] observations = { 1.5, 2.5, 3.5, 4.5, 5.5 };
        double[] expected = { 1.0, 2.5, 0.5 };

        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));
        ParametricFunction function = new Gaussian.Parametric();
        fitter.addObservedPoints(new WeightedObservedPoint[5], observations);

        double[] result = fitter.fit(function, initialGuess);
        assertArrayEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link GaussianFitter#fit()}.
     * Test with initial guess provided by {@link ParameterGuesser}.
     */
    @Test
    public void testFitWithBasicGuess() {
        double[] observations = { 1.5, 2.5, 3.5, 4.5, 5.5 };

        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));
        for (double observation : observations) {
            fitter.addObservedPoint(observation, 1.0);
        }

        PointValuePair guess = new DoublePointValuePair(observations[0], new double[] { 1.0, 2.5, 0.5 });
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(fitter.getJacobianMatrix());
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[] { -1.0, 0.0, 0.0 }, ConstraintType.LEQ, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[] { 0.0, -1.0, 0.0 }, ConstraintType.LEQ, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[] { 0.0, 0.0, -1.0 }, ConstraintType.LEQ, 0.0));

        LinearProgram program = new LinearProgram(objectiveFunction, constraints);
        Guess initialPoint = new Guess(guess.getPoint());
        SimpleXSearch solver = new SimpleXSearch(SimpleXSearchType.QUADRILATERAL);
        PointValuePair result = solver.optimize(program, initialPoint);

        double[] initialGuess = result.getPoint();
        double[] expected = { 1.0, 2.5, 0.5 };

        double[] resultFit = fitter.fit();
        assertArrayEquals(expected, resultFit, 0.001);
    }

    /**
     * Test method for {@link GaussianFitter#fit()}.
     * Test with constraints.
     */
    @Test
    public void testFitWithConstraints() {
        double[] observations = { 1.5, 2.5, 3.5, 4.5, 5.5 };

        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));
        for (double observation : observations) {
            fitter.addObservedPoint(observation, 1.0);
        }

        PointValuePair guess = new DoublePointValuePair(observations[0], new double[] { 1.0, 2.5, 0.5 });
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(fitter.getJacobianMatrix());
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[] { -1.0, 0.0, 0.0 }, ConstraintType.LEQ, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[] { 0.0, -1.0, 0.0 }, ConstraintType.LEQ, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[] { 0.0, 0.0, -1.0 }, ConstraintType.LEQ, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[] { 0.0, 0.0, 0.0 }, ConstraintType.EQ, 1.0));

        LinearProgram program = new LinearProgram(objectiveFunction, constraints);
        Guess initialPoint = new Guess(guess.getPoint());
        SimpleXSearch solver = new SimpleXSearch(SimpleXSearchType.QUADRILATERAL);
        PointValuePair result = solver.optimize(program, initialPoint);

        double[] initialGuess = result.getPoint();
        double[] expected = { 1.0, 2.5, 0.5 };

        double[] resultFit = fitter.fit();
        assertArrayEquals(expected, resultFit, 0.001);
    }

    /**
     * Test method for {@link GaussianFitter#testFitWithNegativeSigma()}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeSigma() {
        double[] observations = { 1.5, 2.5, 3.5, 4.5, 5.5 };

        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));
        for (double observation : observations) {
            fitter.addObservedPoint(observation, 1.0);
        }

        fitter.setFitStrategy(new GaussianFitter.ParameterGuesserStrategy());
        double[] resultFit = fitter.fit();

        // This test case is expected to throw NotStrictlyPositiveException
    }

    /**
     * Test method for {@link GaussianFitter#testFitWithNullData()}.
     */
    @Test(expected = NullPointerException.class)
    public void testFitWithNullData() {
        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));
        double[] observations = null;

        try {
            fitter.fit(observations);
        } catch (NullPointerException e) {
            Assert.assertEquals("observations is marked non-null but is null", e.getMessage());
            throw e;
        }
    }

    /**
     * Test method for {@link GaussianFitter#testFitWithZeroData()}.
     */
    @Test(expected = OutOfRangeException.class)
    public void testFitWithZeroData() {
        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));
        double[] observations = {};

        try {
            fitter.fit(observations);
        } catch (OutOfRangeException e) {
            Assert.assertEquals("Observations contain less than 3 points", e.getMessage());
            throw e;
        }
    }

    /**
     * Test method for {@link GaussianFitter#testFitWithDuplicateData()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithDuplicateData() {
        double[] observations = { 1.5, 2.5, 3.5, 2.5 };

        GaussianFitter fitter = new GaussianFitter(new SimpleXSearch(SimpleXSearchType.QUADRILATERAL));
        for (double observation : observations) {
            fitter.addObservedPoint(observation, 1.0);
        }

        try {
            fitter.fit();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Duplicate observation found", e.getMessage());
            throw e;
        }
    }
}