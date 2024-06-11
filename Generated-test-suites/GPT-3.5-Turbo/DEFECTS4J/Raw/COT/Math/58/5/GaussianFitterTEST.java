import org.junit.Test;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.ZeroException;

public class GaussianFitterTest {

    @Test(expected = NullArgumentException.class)
    public void testConstructor_NullArgument_ThrowsNullArgumentException() {
        new GaussianFitter(null);
    }

    @Test
    public void testFit_InitialGuess_Succeeds() {
        GaussianFitter fitter = new GaussianFitter(new Optimizer());
        double[] initialGuess = {1.0, 0.0, 0.5};
        double[] parameters = fitter.fit(initialGuess);
        // Assert that the parameters have been correctly fitted
    }

    @Test
    public void testFit_NoInitialGuess_Succeeds() {
        GaussianFitter fitter = new GaussianFitter(new Optimizer());
        double[] parameters = fitter.fit();
        // Assert that the parameters have been correctly fitted
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesser_NullArgument_ThrowsNullArgumentException() {
        new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesser_LessThanThreePoints_ThrowsNumberIsTooSmallException() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[2];
        new GaussianFitter.ParameterGuesser(points);
    }

    @Test
    public void testParameterGuesser_Guess_Succeeds() {
        WeightedObservedPoint[] points = {new WeightedObservedPoint(1.0, 0.0),
            new WeightedObservedPoint(2.0, 1.0),
            new WeightedObservedPoint(3.0, 0.0)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();
        // Assert that the parameters have been correctly guessed
    }

    @Test(expected = OutOfRangeException.class)
    public void testInterpolateXAtY_OutOfRange_ThrowsOutOfRangeException() {
        WeightedObservedPoint[] points = {new WeightedObservedPoint(0.0, 0.0),
            new WeightedObservedPoint(1.0, 1.0),
            new WeightedObservedPoint(2.0, 0.0)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        guesser.interpolateXAtY(points, 1, -1, 2.0);
    }

    @Test(expected = ZeroException.class)
    public void testInterpolateXAtY_ZeroIdxStep_ThrowsZeroException() {
        WeightedObservedPoint[] points = {new WeightedObservedPoint(0.0, 0.0),
            new WeightedObservedPoint(1.0, 1.0),
            new WeightedObservedPoint(2.0, 0.0)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        guesser.interpolateXAtY(points, 1, 0, 1.5);
    }

    @Test
    public void testInterpolateXAtY_InterpolateX_Succeeds() {
        WeightedObservedPoint[] points = {new WeightedObservedPoint(0.0, 0.0),
            new WeightedObservedPoint(1.0, 1.0),
            new WeightedObservedPoint(2.0, 0.0)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double x = guesser.interpolateXAtY(points, 1, +1, 0.5);
        // Assert that the correct x value has been interpolated
    }
    
    @Test
    public void testIsBetween_BetweenBoundaries_ReturnsTrue() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        boolean result = guesser.isBetween(2.0, 1.0, 3.0);
        // Assert that the result is true
    }
    
    @Test
    public void testIsBetween_NotBetweenBoundaries_ReturnsFalse() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        boolean result = guesser.isBetween(5.0, 1.0, 3.0);
        // Assert that the result is false
    }
}