import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.optimization.Weight;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightConstructorOne() {
        double[] weightValues = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightValues);

        double[][] expectedMatrixData = {{1.0, 0.0, 0.0},
                                         {0.0, 2.0, 0.0},
                                         {0.0, 0.0, 3.0}};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(expectedMatrixData);

        assertMatrixEquals(expectedMatrix, weight.getWeight());
    }

    @Test
    public void testWeightConstructorTwo() {
        double[][] weightMatrixData = {{1.0, 0.0},
                                       {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weight = new Weight(weightMatrix);

        assertMatrixEquals(weightMatrix, weight.getWeight());
    }

    @Test
    public void testNonSquareMatrixException() {
        double[][] weightMatrixData = {{1.0, 0.0, 0.0},
                                       {0.0, 2.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);

        try {
            new Weight(weightMatrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            assertTrue(true);
        }
    }

    private void assertMatrixEquals(RealMatrix expected, RealMatrix actual) {
        double[][] expectedData = expected.getData();
        double[][] actualData = actual.getData();

        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());

        for (int i = 0; i < expected.getRowDimension(); i++) {
            assertArrayEquals(expectedData[i], actualData[i], 0.0);
        }
    }

}