import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test with a simple linear equation y = mx + b
        // Equation used: y = 0.5x + 2

        // Test input data
        double[] x = new double[]{1, 2, 3, 4, 5};
        double[] y = new double[]{2.5, 3.5, 4.5, 5.5, 6.5};

        // Define the model function
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 1}, {3, 1}, {4, 1}, {5, 1}});
        double[] parameters = new double[]{0.5, 2};

        // Create a GaussNewtonOptimizer instance and set convergence criteria
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);

        // Set input and output values for optimization
        optimizer.setObservation(x);
        optimizer.setTarget(y);
        optimizer.setWeight(RealMatrixUtils.createRealDiagonalMatrix(new double[]{1, 1, 1, 1, 1}));

        try {
            // Perform optimization
            PointVectorValuePair solution = optimizer.doOptimize();
            double[] fittedParameters = solution.getPoint();

            // Check if the fitted parameters match the actual parameters
            Assert.assertArrayEquals(parameters, fittedParameters, 1e-6);
        } catch (NullArgumentException e) {
            Assert.fail("NullArgumentException should not be thrown");
        } catch (ConvergenceException e) {
            Assert.fail("ConvergenceException should not be thrown");
        } catch (MathInternalError e) {
            Assert.fail("MathInternalError should not be thrown");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testSetObservationNull() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setObservation(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetTargetNull() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(null);
    }

    @Test
    public void testEmptyObservation() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setObservation(new double[]{});
        optimizer.setTarget(new double[]{});
        PointVectorValuePair result = optimizer.doOptimize();
        Assert.assertArrayEquals(new double[]{}, result.getPoint(), 0.0);
    }

    @Test
    public void testEmptyTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setObservation(new double[]{1, 2, 3});
        optimizer.setTarget(new double[]{});
        PointVectorValuePair result = optimizer.doOptimize();
        Assert.assertArrayEquals(new double[]{}, result.getPoint(), 0.0);
    }

}