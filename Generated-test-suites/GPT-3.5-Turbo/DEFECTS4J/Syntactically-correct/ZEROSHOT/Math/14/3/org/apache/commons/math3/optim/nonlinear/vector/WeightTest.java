import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                           {0.0, 2.0, 0.0},
                                                                           {0.0, 0.0, 3.0}});
        RealMatrix actualMatrix = w.getWeight();
        assertTrue(expectedMatrix.equals(actualMatrix));
    }

    @Test
    public void testWeightConstructorWithMatrix() {
        double[][] weightArray = {{1.0, 0.0, 0.0},
                                  {0.0, 2.0, 0.0},
                                  {0.0, 0.0, 3.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightArray);
        Weight w = new Weight(weight);
        assertTrue(weight.equals(w.getWeight()));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] weightArray = {{1.0, 0.0},
                                  {0.0, 2.0},
                                  {0.0, 0.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightArray);
        Weight w = new Weight(weight);
    }

}