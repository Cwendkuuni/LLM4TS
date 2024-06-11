import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SchurTransformer;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        double[][] expectedP = {{-0.82456, -0.41597}, {-0.56577, 0.90961}};
        assertArrayEquals(expectedP, p.getData());
    }
    
    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix pt = transformer.getPT();
        double[][] expectedPt = {{-0.82456, -0.56577}, {-0.41597, 0.90961}};
        assertArrayEquals(expectedPt, pt.getData());
    }
    
    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix t = transformer.getT();
        double[][] expectedT = {{1.8541, 2.8029}, {0, -0.8541}};
        assertArrayEquals(expectedT, t.getData());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
    
    @Test
    public void testConstructorSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        assertNotNull(transformer);
    }
    
    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix t = transformer.getT();
        double[][] expectedT = {{1.8541, 2.8029}, {0, -0.8541}};
        assertArrayEquals(expectedT, t.getData());
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testTransformConvergenceFailed() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
}