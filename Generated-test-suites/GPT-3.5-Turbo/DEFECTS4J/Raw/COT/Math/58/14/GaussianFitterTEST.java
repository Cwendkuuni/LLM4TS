import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.ZeroException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    private GaussianFitter fitter;

    @Before
    public void setUp() {
        fitter = new GaussianFitter(null); //Null optimizer for testing purposes
    }

    @Test
    public void testFit() {
        //Generate test data
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {1.0, 2.0, 3.0, 4.0, 5.0};

        fitter.addObservedPoint(x[0], y[0]);
        fitter.addObservedPoint(x[1], y[1]);
        fitter.addObservedPoint(x[2], y[2]);
        fitter.addObservedPoint(x[3], y[3]);
        fitter.addObservedPoint(x[4], y[4]);

        double[] parameters = fitter.fit();

        //Check if parameters are within expected range
        assertTrue(parameters[0] >= 0.0); //Norm should be non-negative
        assertTrue(parameters[1] >= 0.0); //Mean should be non-negative
        assertTrue(parameters[2] >= 0.0); //Sigma should be non-negative
    }

    @Test(expected = NullArgumentException.class)
    public void testFit_NullArgumentException() {
        fitter.fit(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFit_NumberIsTooSmallException() {
        //Generate test data with less than 3 points
        double[] x = {1.0, 2.0};
        double[] y = {1.0, 2.0};

        fitter.addObservedPoint(x[0], y[0]);
        fitter.addObservedPoint(x[1], y[1]);

        fitter.fit();
    }

    @Test
    public void testFit_DifferentOptimizer() {
        //Generate test data
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {1.0, 2.0, 3.0, 4.0, 5.0};

        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer()); // Use different optimizer

        fitter.addObservedPoint(x[0], y[0]);
        fitter.addObservedPoint(x[1], y[1]);
        fitter.addObservedPoint(x[2], y[2]);
        fitter.addObservedPoint(x[3], y[3]);
        fitter.addObservedPoint(x[4], y[4]);

        double[] parameters = fitter.fit();

        //Check if parameters are within expected range
        assertTrue(parameters[0] >= 0.0); //Norm should be non-negative
        assertTrue(parameters[1] >= 0.0); //Mean should be non-negative
        assertTrue(parameters[2] >= 0.0); //Sigma should be non-negative
    }

    //Add more test cases for edge cases and exception handling scenarios as needed

}