import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix testData = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(testData);
        RealMatrix result = transformer.getP();
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][] {{1, 0}, {0, 1}});
        assertEquals(expected, result);
    }

    @Test
    public void testGetPT() {
        RealMatrix testData = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(testData);
        RealMatrix result = transformer.getPT();
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][] {{1, 0}, {0, 1}});
        assertEquals(expected, result);
    }

    @Test
    public void testGetT() {
        RealMatrix testData = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(testData);
        RealMatrix result = transformer.getT();
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {0, 3}});
        assertEquals(expected, result);
    }

    // Add more test cases for other methods if needed

}