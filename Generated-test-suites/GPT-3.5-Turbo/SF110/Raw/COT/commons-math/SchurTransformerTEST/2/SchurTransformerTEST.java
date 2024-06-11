
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        double[][] matrixData = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[][] expectedData = {{0.452277442494746, -0.7893522171573595}, {0.8854560256532099, 0.4082482904638631}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(expected.equals(transformer.getP()));
    }

    @Test
    public void testGetPT() {
        double[][] matrixData = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[][] expectedData = {{0.452277442494746, 0.8854560256532099}, {-0.7893522171573595, 0.4082482904638631}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(expected.equals(transformer.getPT()));
    }

    @Test
    public void testGetT() {
        double[][] matrixData = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[][] expectedData = {{0.9354560256532099, 2.0878428424543505}, {-1.837783408286397, 0.06454397434679086}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(expected.equals(transformer.getT()));
    }
}

