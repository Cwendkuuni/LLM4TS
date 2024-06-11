import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.*;

public class WeightTest {
    
    private Weight weight1;
    private Weight weight2;
    private Weight weight3;
    
    @Before
    public void setup() {
        double[] weights1 = {1.0, 2.0, 3.0};
        weight1 = new Weight(weights1);
        
        double[][] weightsData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightsMatrix = new Array2DRowRealMatrix(weightsData);
        weight2 = new Weight(weightsMatrix);
        
        double[][] nonSquareData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        RealMatrix nonSquareMatrix = new Array2DRowRealMatrix(nonSquareData);
        weight3 = new Weight(nonSquareMatrix);
    }
    
    @Test
    public void testGetWeight_diagonalMatrix() {
        RealMatrix expected = weight1.getWeight();
        RealMatrix actual = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test
    public void testGetWeight_squareMatrix() {
        RealMatrix expected = weight2.getWeight();
        RealMatrix actual = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor_nonSquareMatrix() {
        new Weight(weight3.getWeight());
    }
    
    @Test
    public void testGetWeight_immutable() {
        RealMatrix original = weight2.getWeight();
        original.setEntry(0, 0, 10.0);
        RealMatrix expected = weight2.getWeight();
        assertArrayEquals(expected.getData(), original.getData());
    }
}