import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class WeightTest {
    @Test
    public void testWeightConstructor() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        assertEquals(1.0, w.getWeight().getEntry(0,0), 0.0);
        assertEquals(2.0, w.getWeight().getEntry(1,1), 0.0);
        assertEquals(3.0, w.getWeight().getEntry(2,2), 0.0);
        
    }

    @Test
    public void testWeightConstructorNonSquareMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        try {
            new Weight(weight);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }
}