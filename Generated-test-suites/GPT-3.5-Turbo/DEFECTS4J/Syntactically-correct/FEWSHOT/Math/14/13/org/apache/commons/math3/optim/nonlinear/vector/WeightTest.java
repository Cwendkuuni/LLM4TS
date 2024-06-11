//Import statements
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

//Test class
public class WeightTest {

    @Test
    public void testConstructorWithArray() {
        double[] weight = {1, 2, 3, 4, 5};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0, 0, 0, 0},
                                                                     {0, 2, 0, 0, 0},
                                                                     {0, 0, 3, 0, 0},
                                                                     {0, 0, 0, 4, 0},
                                                                     {0, 0, 0, 0, 5}});
        assertTrue(w.getWeight().equals(expected));
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1, 2},
                                                                    {3, 4},
                                                                    {5, 6}});
        Weight w = new Weight(weight);
    }
    
    @Test
    public void testConstructorWithSquareMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1, 2, 3},
                                                                    {4, 5, 6},
                                                                    {7, 8, 9}});
        Weight w = new Weight(weight);
        assertTrue(w.getWeight().equals(weight));
    }
}