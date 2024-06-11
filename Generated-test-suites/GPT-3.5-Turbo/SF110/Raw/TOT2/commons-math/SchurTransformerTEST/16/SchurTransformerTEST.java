import org.junit.Test;
import org.apache.commons.math3.linear.*;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix resultP = transformer.getP();
        RealMatrix expectedP = MatrixUtils.createRealMatrix(transformer.matrixP);
        assertEquals(expectedP, resultP);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix resultPT = transformer.getPT();
        RealMatrix expectedPT = transformer.getP().transpose();
        assertEquals(expectedPT, resultPT);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix resultT = transformer.getT();
        RealMatrix expectedT = MatrixUtils.createRealMatrix(transformer.matrixT);
        assertEquals(expectedT, resultT);
    }

    // Add more test methods for other public methods if needed

}