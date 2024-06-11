import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testConstructor1() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                      {0.0, 2.0, 0.0},
                                                      {0.0, 0.0, 3.0}});
        RealMatrix actual = w.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructor2() {
        double[][] data = {{1.0, 0.0, 0.0},
                           {0.0, 2.0, 0.0},
                           {0.0, 0.0, 3.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
        RealMatrix actual = w.getWeight();
        assertEquals(weight, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor2NonSquareMatrix() {
        double[][] data = {{1.0, 0.0},
                           {0.0, 2.0},
                           {0.0, 0.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
    }
}