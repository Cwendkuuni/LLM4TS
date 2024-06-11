
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testConstructor1() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                           {0.0, 2.0, 0.0},
                                                                           {0.0, 0.0, 3.0}});
        assertEquals(expectedMatrix, w.getWeight());
    }
    
    @Test
    public void testConstructor2() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                   {0.0, 2.0, 0.0},
                                                                   {0.0, 0.0, 3.0}});
        Weight w = new Weight(weight);
        assertEquals(weight, w.getWeight());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorInvalidMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                   {0.0, 2.0, 0.0}});
        Weight w = new Weight(weight);
    }
}
