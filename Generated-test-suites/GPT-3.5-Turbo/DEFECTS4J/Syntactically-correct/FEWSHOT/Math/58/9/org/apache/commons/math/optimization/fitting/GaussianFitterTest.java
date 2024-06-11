import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {
    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[4];
        points[0] = new WeightedObservedPoint(1.0, 0.0, 0.0);
        points[1] = new WeightedObservedPoint(2.0, 1.0, 0.0);
        points[2] = new WeightedObservedPoint(3.0, 2.0, 0.0);
        points[3] = new WeightedObservedPoint(4.0, 1.0, 0.0);

        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        
        double[] expected = {2.0, 3.0, (2.0 * Math.sqrt(2.0 * Math.log(2.0)))};
        double[] actual = guesser.guess();
        
        assertArrayEquals(expected, actual, 0.0001);
    }

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(0.0, 0.0);
        fitter.addObservedPoint(1.0, 1.0);
        fitter.addObservedPoint(2.0, 2.0);
        fitter.addObservedPoint(3.0, 1.0);
        fitter.addObservedPoint(4.0, 0.0);

        double[] expected = {2.0, 2.0, (2.0 * Math.sqrt(2.0 * Math.log(2.0)))};
        double[] actual = fitter.fit();

        assertArrayEquals(expected, actual, 0.0001);
    }

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(0.0, 0.0);
        fitter.addObservedPoint(1.0, 1.0);
        fitter.addObservedPoint(2.0, 2.0);
        fitter.addObservedPoint(3.0, 1.0);
        fitter.addObservedPoint(4.0, 0.0);

        double[] initialGuess = {1.0, 3.0, 2.0};
        double[] expected = {2.0, 2.0, (2.0 * Math.sqrt(2.0 * Math.log(2.0)))};
        double[] actual = fitter.fit(initialGuess);

        assertArrayEquals(expected, actual, 0.0001);
    }
}