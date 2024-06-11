import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623,  531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);
        fitter.addObservedPoint(4.04421621, 2687152.0);
        fitter.addObservedPoint(4.05132976, 3461228.0);
        fitter.addObservedPoint(4.05326982, 3580526.0);
        fitter.addObservedPoint(4.05779662, 3439750.0);
        fitter.addObservedPoint(4.0636168,  2877648.0);
        fitter.addObservedPoint(4.06943698, 2175960.0);
        fitter.addObservedPoint(4.07525716, 1447024.0);
        fitter.addObservedPoint(4.08237071, 717104.0);
        fitter.addObservedPoint(4.08366408, 620014.0);

        double[] parameters = fitter.fit();
        double[] expectedParameters = {3.412648699280156E6, 4.058156741342684, 1.2071074664785714E-4};
        assertArrayEquals(expectedParameters, parameters, 0.0001);
    }

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623,  531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);
        fitter.addObservedPoint(4.04421621, 2687152.0);
        fitter.addObservedPoint(4.05132976, 3461228.0);
        fitter.addObservedPoint(4.05326982, 3580526.0);
        fitter.addObservedPoint(4.05779662, 3439750.0);
        fitter.addObservedPoint(4.0636168,  2877648.0);
        fitter.addObservedPoint(4.06943698, 2175960.0);
        fitter.addObservedPoint(4.07525716, 1447024.0);
        fitter.addObservedPoint(4.08237071, 717104.0);
        fitter.addObservedPoint(4.08366408, 620014.0);

        double[] initialGuess = {3.5E6, 4.1, 0.0001};
        double[] parameters = fitter.fit(initialGuess);
        double[] expectedParameters = {3.412648699280156E6, 4.058156741342684, 1.2071074664785714E-4};
        assertArrayEquals(expectedParameters, parameters, 0.0001);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[3];
        observations[0] = new WeightedObservedPoint(1, 1, 1);
        observations[1] = new WeightedObservedPoint(2, 2, 2);
        observations[2] = new WeightedObservedPoint(3, 3, 3);

        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(observations);
        double[] guessedParameters = parameterGuesser.guess();
        double[] expectedParameters = {3, 2, 0.7651459398632259};
        assertArrayEquals(expectedParameters, guessedParameters, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserNullInput() {
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserSmallInput() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[2];
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(observations);
    }

}