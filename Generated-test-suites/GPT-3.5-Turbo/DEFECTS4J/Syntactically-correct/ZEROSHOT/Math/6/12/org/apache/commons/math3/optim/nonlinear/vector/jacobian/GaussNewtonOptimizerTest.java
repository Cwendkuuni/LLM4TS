import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1
        double[] targetValues = {1.0, 2.0, 3.0};
        double[][] weightMatrixValues = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        double[] startPoint = {0.0, 0.0, 0.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setWeight(new BlockRealMatrix(weightMatrixValues));
        optimizer.setStartPoint(new ArrayRealVector(startPoint));
        PointVectorValuePair result = optimizer.doOptimize();
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint().toArray(), 0.0);

        // Test case 2
        targetValues = new double[]{4.0, 5.0, 6.0};
        weightMatrixValues = new double[][]{{2.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 2.0}};
        startPoint = new double[]{1.0, 1.0, 1.0};
        optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(new BlockRealMatrix(weightMatrixValues));
        optimizer.setStartPoint(new ArrayRealVector(startPoint));
        result = optimizer.doOptimize();
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint().toArray(), 0.0);
    }
}