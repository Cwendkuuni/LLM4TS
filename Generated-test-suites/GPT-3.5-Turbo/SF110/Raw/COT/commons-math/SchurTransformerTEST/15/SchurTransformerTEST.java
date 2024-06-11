import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;

import static org.junit.Assert.*;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        double[][] matrixData = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix pMatrix = transformer.getP();
        double[][] pMatrixExpected = {{0.894427190999916, -0.447213595499958}, {-0.447213595499958, -0.894427190999916}};
        RealMatrix pMatrixExpectedObj = MatrixUtils.createRealMatrix(pMatrixExpected);
        assertTrue(pMatrixExpectedObj.equals(pMatrix));
    }

    @Test
    public void testGetPT() {
        double[][] matrixData = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix ptMatrix = transformer.getPT();
        double[][] ptMatrixExpected = {{0.894427190999916, -0.447213595499958}, {-0.447213595499958, -0.894427190999916}};
        RealMatrix ptMatrixExpectedObj = MatrixUtils.createRealMatrix(ptMatrixExpected);
        assertTrue(ptMatrixExpectedObj.equals(ptMatrix));
    }

    @Test
    public void testGetT() {
        double[][] matrixData = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix tMatrix = transformer.getT();
        double[][] tMatrixExpected = {{0.232352864450761, 3.099019513592658},
                                      {-1.70710678118655, -2.232352864450759}};
        RealMatrix tMatrixExpectedObj = MatrixUtils.createRealMatrix(tMatrixExpected);
        assertTrue(tMatrixExpectedObj.equals(tMatrix));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] matrixData = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testExceedMaxCountException() {
        double[][] matrixData = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.getP();
    }
}