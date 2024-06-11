import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.linear.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);

        double[][] expectedData = transformer.getP().getData();
        double[][] actualData = transformer.getP().getData();

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetPT() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);

        double[][] expectedData = transformer.getPT().getData();
        double[][] actualData = transformer.getPT().getData();

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetT() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);

        double[][] expectedData = transformer.getT().getData();
        double[][] actualData = transformer.getT().getData();

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testTransform() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);

        // Assert transformation is successful
        assertNotNull(transformer.getP());
        assertNotNull(transformer.getPT());
        assertNotNull(transformer.getT());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrix() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformConvergenceFailed() {
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(matrix);

        // Modify matrix to make transformation fail to converge
        double[][] matrixT = transformer.getT().getData();
        matrixT[0][0] = 0;
        transformer.transform();
    }
}