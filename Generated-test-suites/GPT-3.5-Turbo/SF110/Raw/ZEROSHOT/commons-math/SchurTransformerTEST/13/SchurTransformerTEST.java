import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;

public class SchurTransformerTest {
    
    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{-0.415974, -0.909379}, {-0.909379, 0.415974}});
        RealMatrix actualP = transformer.getP();
        
        assertArrayEquals(expectedP.getData(), actualP.getData());
    }
    
    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{-0.415974, -0.909379}, {-0.909379, 0.415974}});
        RealMatrix actualPT = transformer.getPT();
        
        assertArrayEquals(expectedPT.getData(), actualPT.getData());
    }
    
    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{0.854102, -2.48538}, {3.09807, -0.854102}});
        RealMatrix actualT = transformer.getT();
        
        assertArrayEquals(expectedT.getData(), actualT.getData());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testTransformConvergenceFailed() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
    
    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        double[][] expectedMatrixT = {{0.854102, -2.48538}, {3.09807, -0.854102}};
        assertArrayEquals(expectedMatrixT, transformer.getT().getData());
    }
}