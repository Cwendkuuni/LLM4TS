import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.InitialGuess.IDENTITY;
import static org.apache.commons.math3.optim.PointValuePair.make;
import static org.apache.commons.math3.optim.SimpleBounds.bound;
import static org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer.FIND_MIN_DEPRECATED;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3).mapMultiply( weights );
        RealMatrix actual = weight.getWeight();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWeightNonSquareMatrixException() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 2);
        try {
            new Weight(weight);
            Assert.fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            //Expected
        }
    }

    @Test
    public void testWeightNonDiagonalConstruction() {
        double[][] weights = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(2, 2).setData(weights);
        RealMatrix actual = weight.getWeight();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWeightFunctionality() {
        double[] weights = new double[]{1.0, 0.1, 0.1};
        Weight weight = new Weight(weights);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setMaxIterations(MaxEval.MAX_EVAL);
        optimizer.setVerbose(false);
        optimizer.setMaxStepSize(1.0);
        optimizer.setInitialGuess(IDIDENTITY);

        PointValuePair result = optimizer.optimize(new MinimizerWeightedSumSquares(), bound(0.0, 10.0, 5.0), weight);

        Assert.assertNotNull(result.getValue());
        Assert.assertTrue(result.getValue().getNorm(org.apache.commons.math3.linear.Norm.FROBENIUS) < 1e-6);
    }
}