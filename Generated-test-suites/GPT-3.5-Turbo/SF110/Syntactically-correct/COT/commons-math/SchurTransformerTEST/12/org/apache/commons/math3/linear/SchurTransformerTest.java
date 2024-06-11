import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(testMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(transformer.matrixP);
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(testMatrix);
        RealMatrix expectedPT = transformer.getP().transpose();
        RealMatrix actualPT = transformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(testMatrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(transformer.matrixT);
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }
}