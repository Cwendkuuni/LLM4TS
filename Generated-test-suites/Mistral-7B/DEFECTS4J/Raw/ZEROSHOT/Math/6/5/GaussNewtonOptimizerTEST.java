import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.AbstractOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        try {
            optimizer.optimize();
            Assert.fail("An optimization problem with no initial point should throw an exception");
        } catch (NullArgumentException e) {
            // Expected
        }

        double[] initialPoint = {0.0, 0.0};
        double[] targetValues = {1.0, 2.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        GaussNewtonOptimizer optimizer1 = new GaussNewtonOptimizer(true) {
            @Override
            protected PointValuePair optimizeInternal() throws Exception {
                throw new UnsupportedOperationException("Do not call this method directly");
            }
        };
        optimizer1.setInitialPoint(initialPoint);
        optimizer1.setFunctionValues(targetValues);
        optimizer1.setGradient(targetValues, initialPoint);
        optimizer1.setWeight(weightMatrix);
        optimizer1.setConvergenceChecker(new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        PointValuePair result1 = optimizer1.optimize();
        Assert.assertEquals(2, result1.getValue().length);
        Assert.assertEquals(1.0, result1.getValue()[0], 1.0e-6);
        Assert.assertEquals(1.5, result1.getValue()[1], 1.0e-6);

        GaussNewtonOptimizer optimizer2 = new GaussNewtonOptimizer(false) {
            @Override
            protected PointValuePair optimizeInternal() throws Exception {
                throw new UnsupportedOperationException("Do not call this method directly");
            }
        };
        optimizer2.setInitialPoint(initialPoint);
        optimizer2.setFunctionValues(targetValues);
        optimizer2.setGradient(targetValues, initialPoint);
        optimizer2.setWeight(weightMatrix);
        optimizer2.setConvergenceChecker(new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        PointValuePair result2 = optimizer2.optimize();
        Assert.assertEquals(2, result2.getValue().length);
        Assert.assertEquals(1.0, result2.getValue()[0], 1.0e-6);
        Assert.assertEquals(1.5, result2.getValue()[1], 1.0e-6);

        // Test error cases
        double[] targetValuesSingular = {1.0, 0.0};
        GaussNewtonOptimizer optimizer3 = new GaussNewtonOptimizer(true) {
            @Override
            protected PointValuePair optimizeInternal() throws Exception {
                throw new UnsupportedOperationException("Do not call this method directly");
            }
        };
        optimizer3.setInitialPoint(initialPoint);
        optimizer3.setFunctionValues(targetValuesSingular);
        optimizer3.setGradient(targetValuesSingular, initialPoint);
        optimizer3.setWeight(new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}}));
        optimizer3.setConvergenceChecker(new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        try {
            optimizer3.optimize();
            Assert.fail("Singular weight matrix should lead to a ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        double[] targetValuesNaN = {Double.NaN, Double.NaN};
        GaussNewtonOptimizer optimizer4 = new GaussNewtonOptimizer(true) {
            @Override
            protected PointValuePair optimizeInternal() throws Exception {
                throw new UnsupportedOperationException("Do not call this method directly");
            }
        };
        optimizer4.setInitialPoint(initialPoint);
        optimizer4.setFunctionValues(targetValuesNaN);
        optimizer4.setGradient(targetValues, initialPoint);
        optimizer4.setWeight(weightMatrix);
        optimizer4.setConvergenceChecker(new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        try {
            optimizer4.optimize();
            Assert.fail("TargetValues containing NaN should lead to a NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }
}