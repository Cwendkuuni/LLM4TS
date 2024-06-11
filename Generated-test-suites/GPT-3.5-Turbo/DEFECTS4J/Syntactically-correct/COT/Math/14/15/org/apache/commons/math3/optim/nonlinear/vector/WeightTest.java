import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightConstructor1() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertEquals(expected, w.getWeight());
    }

    @Test
    public void testWeightConstructor2() {
        double[][] weightArray = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightArray);
        Weight w = new Weight(weight);
        assertEquals(weight, w.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructor2NonSquareMatrix() {
        double[][] weightArray = {{1.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightArray);
        Weight w = new Weight(weight);
    }
}