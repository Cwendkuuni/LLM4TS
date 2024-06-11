import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    private Weight weight;
    private double[] weightValues;
    private RealMatrix weightMatrix;

    @Before
    public void setUp() {
        weightValues = new double[]{1.0, 2.0, 3.0};
        weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                               {0.0, 2.0, 0.0},
                                                               {0.0, 0.0, 3.0}});
        weight = new Weight(weightValues);
    }

    @Test
    public void testGetWeight() {
        RealMatrix result = weight.getWeight();
        assertEquals(weightMatrix, result);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixWeight() {
        RealMatrix invalidWeightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                                 {3.0, 4.0},
                                                                                 {5.0, 6.0}});
        new Weight(invalidWeightMatrix);
    }

}