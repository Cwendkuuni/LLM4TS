import org.apache.commons.math3.linear.SchurTransformer;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import org.junit.Assert;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}}));
        Assert.assertNotNull(transformer.getP());
    }

    @Test
    public void testGetPT() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}}));
        Assert.assertNotNull(transformer.getPT());
    }

    @Test
    public void testGetT() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}}));
        Assert.assertNotNull(transformer.getT());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}}));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testConvergenceFailedException() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(realMatrix);

        // Set matrixT to large values to force ConvergenceFailedException
        transformer.matrixT[0][0] = 9999;
        transformer.matrixT[1][1] = 9999;

        transformer.transform();
    }

    // Add more test cases for complete coverage of all methods in SchurTransformer class

}