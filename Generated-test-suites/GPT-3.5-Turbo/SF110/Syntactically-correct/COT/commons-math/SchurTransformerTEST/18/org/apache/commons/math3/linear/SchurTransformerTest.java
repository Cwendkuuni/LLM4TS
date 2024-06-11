
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SchurTransformerTest {
    private double epsilon;
    private SchurTransformer schurTransformer;
    private RealMatrix matrix;
    
    @Before
    public void setup() {
        epsilon = Precision.EPSILON;
        matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1);
        matrix.setEntry(0, 1, 2);
        matrix.setEntry(0, 2, 3);
        matrix.setEntry(1, 0, 4);
        matrix.setEntry(1, 1, 5);
        matrix.setEntry(1, 2, 6);
        matrix.setEntry(2, 0, 7);
        matrix.setEntry(2, 1, 8);
        matrix.setEntry(2, 2, 9);
        schurTransformer = new SchurTransformer(matrix);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(2, 3);
        SchurTransformer schurTransformer = new SchurTransformer(nonSquareMatrix);
    }
    
    @Test
    public void testGetP() {
        double[][] expectedMatrixP = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix expectedP = MatrixUtils.createRealMatrix(expectedMatrixP);
        
        RealMatrix actualP = schurTransformer.getP();
        
        Assert.assertEquals(expectedP, actualP);
    }
    
    @Test
    public void testGetPT() {
        double[][] expectedMatrixPT = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(expectedMatrixPT);
        
        RealMatrix actualPT = schurTransformer.getPT();
        
        Assert.assertEquals(expectedPT, actualPT);
    }
    
    @Test
    public void testGetT() {
        double[][] expectedMatrixT = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expectedT = MatrixUtils.createRealMatrix(expectedMatrixT);
        
        RealMatrix actualT = schurTransformer.getT();
        
        Assert.assertEquals(expectedT, actualT);
    }
    
    @Test
    public void testTransform() {
        // test not implemented as it is a private method
    }
    
    @Test
    public void testGetNorm() {
        // test not implemented as it is a private method
    }
    
    @Test
    public void testFindSmallSubDiagonalElement() {
        // test not implemented as it is a private method
    }
    
    @Test
    public void testComputeShift() {
        // test not implemented as it is a private method
    }
    
    @Test
    public void testInitQRStep() {
        // test not implemented as it is a private method
    }
    
    @Test
    public void testPerformDoubleQRStep() {
        // test not implemented as it is a private method
    }
}
