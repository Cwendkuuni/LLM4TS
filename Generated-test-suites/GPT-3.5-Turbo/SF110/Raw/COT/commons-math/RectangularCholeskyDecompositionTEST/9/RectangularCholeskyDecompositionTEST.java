import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testRectangularCholeskyDecomposition() {
        // basic test case
        double[][] data = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix input = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcD = new RectangularCholeskyDecomposition(input);
        RealMatrix output = rcD.getRootMatrix();
        int rank = rcD.getRank();
        assertEquals(input, output);
        assertEquals(3, rank);
        
        // test case for non-positive definite matrix exception
        double[][] data2 = {{1, 2, 3}, {2, 4, 6}, {3, 6, 9}};
        RealMatrix input2 = MatrixUtils.createRealMatrix(data2);
        try {
            RectangularCholeskyDecomposition rcD2 = new RectangularCholeskyDecomposition(input2);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(1.0, e.getA(), 0.0);
            assertEquals(0, e.getRow(), 0);
            assertEquals(0.0, e.getSmall(), 0.0);
        }
        
        // additional test cases for edge cases and exceptions
        
        // test case for small value
        double[][] data3 = {{1e-10, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix input3 = MatrixUtils.createRealMatrix(data3);
        RectangularCholeskyDecomposition rcD3 = new RectangularCholeskyDecomposition(input3);
        RealMatrix output3 = rcD3.getRootMatrix();
        int rank3 = rcD3.getRank();
        assertEquals(input3, output3);
        assertEquals(3, rank3);
        
        // test case for non-positive definite matrix exception
        double[][] data4 = {{-1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix input4 = MatrixUtils.createRealMatrix(data4);
        try {
            RectangularCholeskyDecomposition rcD4 = new RectangularCholeskyDecomposition(input4);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getA(), 0.0);
            assertEquals(0, e.getRow(), 0);
            assertEquals(0.0, e.getSmall(), 0.0);
        }
    }
}