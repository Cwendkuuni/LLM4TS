import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {
    
    @Test
    public void testFit_WithInitialGuess() {
        // Create GaussianFitter object
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // Add observed points
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

        // Set initial guess values
        double[] initialGuess = {6000000, 4.06, 0.01};

        // Fit the Gaussian function
        double[] parameters = fitter.fit(initialGuess);

        // Check if the parameters are within expected range
        assertEquals(3580526.0, parameters[0], 0.1);
        assertEquals(4.05326982, parameters[1], 0.001);
        assertEquals(0.01, parameters[2], 0.001);
    }

    @Test
    public void testFit_WithoutInitialGuess() {
        // Create GaussianFitter object
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // Add observed points
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

        // Fit the Gaussian function
        double[] parameters = fitter.fit();

        // Check if the parameters are within expected range
        assertEquals(3580526.0, parameters[0], 0.1);
        assertEquals(4.05326982, parameters[1], 0.001);
        assertEquals(0.01, parameters[2], 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testFit_NullInitialGuess() {
        // Create GaussianFitter object
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // Fit the Gaussian function with null initial guess
        fitter.fit(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFit_InsufficientObservedPoints() {
        // Create GaussianFitter object
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // Fit the Gaussian function with insufficient observed points
        fitter.fit();
    }

}