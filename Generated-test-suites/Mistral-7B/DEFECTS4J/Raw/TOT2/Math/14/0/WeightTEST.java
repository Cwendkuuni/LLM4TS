import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {
    @Test
    public void testWeightDiagonalConstruction(){
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        });
        RealMatrix actual = weight.getWeight();
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightMatrixConstruction(){
        double[][] weights = new double[][]{{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);
        RealMatrix expected = weightMatrix.copy();
        RealMatrix actual = weight.getWeight();
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testGetWeight() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        });
        RealMatrix actual = weight.getWeight();
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }
}