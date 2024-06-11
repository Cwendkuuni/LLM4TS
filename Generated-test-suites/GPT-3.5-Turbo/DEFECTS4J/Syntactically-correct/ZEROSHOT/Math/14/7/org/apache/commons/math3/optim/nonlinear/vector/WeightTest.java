import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class WeightTest {

    private Weight weight;

    @Before
    public void setUp() throws Exception {
        double[] w = {1.0, 2.0, 3.0};
        weight = new Weight(w);
    }

    @Test
    public void testConstructor() {
        double[] w = {1.0, 2.0, 3.0};
        RealMatrix expected = new Array2DRowRealMatrix(w.length, w.length);
        for (int i = 0; i < w.length; i++) {
            expected.setEntry(i, i, w[i]);
        }
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConstructorWithMatrix() {
        double[][] w = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(w);
        weight = new Weight(weightMatrix);
        RealMatrix actual = weight.getWeight();
        assertEquals(weightMatrix, actual);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithInvalidMatrix() {
        double[][] w = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(w);
        weight = new Weight(weightMatrix);
    }
}