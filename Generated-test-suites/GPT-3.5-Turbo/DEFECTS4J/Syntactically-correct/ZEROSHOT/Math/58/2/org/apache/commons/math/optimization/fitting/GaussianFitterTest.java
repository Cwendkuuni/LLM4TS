import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623, 531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);
        fitter.addObservedPoint(4.04421621, 2687152.0);
        fitter.addObservedPoint(4.05132976, 3461228.0);
        fitter.addObservedPoint(4.05326982, 3580526.0);
        fitter.addObservedPoint(4.05779662, 3439750.0);
        fitter.addObservedPoint(4.0636168, 2877648.0);
        fitter.addObservedPoint(4.06943698, 2175960.0);
        fitter.addObservedPoint(4.07525716, 1447024.0);
        fitter.addObservedPoint(4.08237071, 717104.0);
        fitter.addObservedPoint(4.08366408, 620014.0);
        double[] initialGuess = {1.0, 4.0, 1.0};
        double[] result = fitter.fit(initialGuess);
        double[] expected = {4268623.9308, 4.05500393, 0.00881385};
        assertArrayEquals(expected, result, 0.0001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623, 531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);
        fitter.addObservedPoint(4.04421621, 2687152.0);
        fitter.addObservedPoint(4.05132976, 3461228.0);
        fitter.addObservedPoint(4.05326982, 3580526.0);
        fitter.addObservedPoint(4.05779662, 3439750.0);
        fitter.addObservedPoint(4.0636168, 2877648.0);
        fitter.addObservedPoint(4.06943698, 2175960.0);
        fitter.addObservedPoint(4.07525716, 1447024.0);
        fitter.addObservedPoint(4.08237071, 717104.0);
        fitter.addObservedPoint(4.08366408, 620014.0);
        double[] result = fitter.fit();
        double[] expected = {4268623.9248, 4.05500394, 0.00881386};
        assertArrayEquals(expected, result, 0.0001);
    }
}