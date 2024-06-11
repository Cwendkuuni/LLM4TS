import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix result = transformer.getP();
        
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{-0.56578, -0.82456}, {-0.82456, 0.56578}});
        assertTrue(expected.equals(result));
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix result = transformer.getPT();
        
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{-0.56578, -0.82456}, {-0.82456, 0.56578}});
        assertTrue(expected.equals(result));
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix result = transformer.getT();
        
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.10102, 3.65028}, {0, -0.10102}});
        assertTrue(expected.equals(result));
    }

}