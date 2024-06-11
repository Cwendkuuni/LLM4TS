
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testConstructorWithArray() {
        double[] weight = {2, 5, 8};
        Weight w = new Weight(weight);
        RealMatrix result = w.getWeight();
        
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        
        assertEquals(2.0, result.getEntry(0, 0), 0.001);
        assertEquals(5.0, result.getEntry(1, 1), 0.001);
        assertEquals(8.0, result.getEntry(2, 2), 0.001);
    }

    @Test
    public void testConstructorWithMatrix() {
        double[][] matrixData = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        Weight w = new Weight(matrix);
        RealMatrix result = w.getWeight();
        
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        
        assertEquals(1.0, result.getEntry(0, 0), 0.001);
        assertEquals(5.0, result.getEntry(1, 1), 0.001);
        assertEquals(9.0, result.getEntry(2, 2), 0.001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] matrixData = { {1, 2, 3}, {4, 5, 6} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        Weight w = new Weight(matrix);
    }

}

