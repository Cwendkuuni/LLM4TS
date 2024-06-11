import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {
    
    @Test
    public void testWeightArrayConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        
        assertEquals(result.getRowDimension(), weights.length);
        assertEquals(result.getColumnDimension(), weights.length);
        
        for (int i = 0; i < weights.length; i++) {
            assertEquals(result.getEntry(i, i), weights[i], 0.0001);
        }
    }
    
    @Test
    public void testWeightArrayConstructorWithEmptyArray() {
        double[] weights = {};
        
        try {
            Weight weight = new Weight(weights);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // This is expected
        }
    }
    
    @Test
    public void testWeightMatrixConstructor() {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        
        Weight weight = new Weight(matrix);
        RealMatrix result = weight.getWeight();
        
        assertEquals(result.getRowDimension(), matrix.getRowDimension());
        assertEquals(result.getColumnDimension(), matrix.getColumnDimension());
        
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(result.getEntry(i, j), matrix.getEntry(i, j), 0.0001);
            }
        }
    }
    
    @Test
    public void testWeightMatrixConstructorWithNonSquareMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        
        try {
            Weight weight = new Weight(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // This is expected
        }
    }
}