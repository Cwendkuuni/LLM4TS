import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightConstructorWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix actualWeightMatrix = weight.getWeight();
        
        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0, 0.0},
            {0.0, 2.0, 0.0},
            {0.0, 0.0, 3.0}
        });
        
        assertEquals(expectedWeightMatrix, actualWeightMatrix);
    }

    @Test
    public void testWeightConstructorWithMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0},
            {0.0, 2.0}
        });
        
        Weight weight = new Weight(matrix);
        RealMatrix actualWeightMatrix = weight.getWeight();
        
        assertEquals(matrix, actualWeightMatrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0},
            {2.0},
            {3.0}
        });
        
        Weight weight = new Weight(matrix);
    }
}