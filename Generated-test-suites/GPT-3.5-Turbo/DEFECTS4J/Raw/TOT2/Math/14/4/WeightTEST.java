import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightConstructor1() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                           {0.0, 2.0, 0.0},
                                                                           {0.0, 0.0, 3.0}});
        assertEquals(expectedMatrix, weight.getWeight());
    }

    @Test
    public void testWeightConstructor2() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 2.0}});
        Weight weight = new Weight(matrix);

        assertEquals(matrix, weight.getWeight());
    }

    @Test
    public void testWeightConstructorNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                    {0.0, 2.0, 0.0}});
        
        try {
            new Weight(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Exception expected
        }
    }

}