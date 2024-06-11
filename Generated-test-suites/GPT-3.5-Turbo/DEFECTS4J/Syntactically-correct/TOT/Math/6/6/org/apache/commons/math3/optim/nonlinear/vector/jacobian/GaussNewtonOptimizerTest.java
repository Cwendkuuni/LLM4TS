import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimizeWithLU() {
        // Test with LU decomposition
        double[] target = {1, 2, 3, 4, 5};
        double[] startPoint = {0, 0, 0, 0, 0};
        Array2DRowRealMatrix weightMatrix = new Array2DRowRealMatrix(5, 5);
        weightMatrix.setEntry(0, 0, 1);
        weightMatrix.setEntry(1, 1, 1);
        weightMatrix.setEntry(2, 2, 1);
        weightMatrix.setEntry(3, 3, 1);
        weightMatrix.setEntry(4, 4, 1);
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        PointVectorValuePair result = optimizer.doOptimize();

        assertArrayEquals(expected, result.getPoint(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithQR() {
        // Test with QR decomposition
        double[] target = {1, 2, 3, 4, 5};
        double[] startPoint = {0, 0, 0, 0, 0};
        Array2DRowRealMatrix weightMatrix = new Array2DRowRealMatrix(5, 5);
        weightMatrix.setEntry(0, 0, 1);
        weightMatrix.setEntry(1, 1, 1);
        weightMatrix.setEntry(2, 2, 1);
        weightMatrix.setEntry(3, 3, 1);
        weightMatrix.setEntry(4, 4, 1);
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        PointVectorValuePair result = optimizer.doOptimize();

        assertArrayEquals(expected, result.getPoint(), 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithoutConvergenceChecker() {
        double[] target = {1, 2, 3, 4, 5};
        double[] startPoint = {0, 0, 0, 0, 0};
        Array2DRowRealMatrix weightMatrix = new Array2DRowRealMatrix(5, 5);
        weightMatrix.setEntry(0, 0, 1);
        weightMatrix.setEntry(1, 1, 1);
        weightMatrix.setEntry(2, 2, 1);
        weightMatrix.setEntry(3, 3, 1);
        weightMatrix.setEntry(4, 4, 1);

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        PointVectorValuePair result = optimizer.doOptimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeWithSingularMatrix() {
        double[] target = {1, 2, 3, 4, 5};
        double[] startPoint = {0, 0, 0, 0, 0};
        Array2DRowRealMatrix weightMatrix = new Array2DRowRealMatrix(5, 5);
        weightMatrix.setEntry(0, 0, 1);
        weightMatrix.setEntry(1, 1, 1);
        weightMatrix.setEntry(2, 2, 1);
        weightMatrix.setEntry(3, 3, 1);
        weightMatrix.setEntry(4, 4, 1);

        // Matrix with determinant = 0 to test singular matrix
        Array2DRowRealMatrix singularMatrix = new Array2DRowRealMatrix(5, 5);
        singularMatrix.setEntry(0, 0, 1);
        singularMatrix.setEntry(1, 1, 1);
        singularMatrix.setEntry(2, 2, 1);
        singularMatrix.setEntry(3, 3, 1);
        singularMatrix.setEntry(4, 4, 0);

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setWeight(singularMatrix);
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        PointVectorValuePair result = optimizer.doOptimize();
    }

}