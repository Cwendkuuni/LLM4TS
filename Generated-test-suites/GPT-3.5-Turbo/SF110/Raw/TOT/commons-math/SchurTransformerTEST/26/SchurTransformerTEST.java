
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.exception.NonSquareMatrixException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SchurTransformerTest {

    private SchurTransformer schurTransformer;

    @Before
    public void setup() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        schurTransformer = new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix expectedP = MatrixUtils.createRealMatrix(schurTransformer.matrixP);
        RealMatrix actualP = schurTransformer.getP();
        assertTrue(expectedP.equals(actualP));
    }

    @Test
    public void testGetPT() {
        RealMatrix expectedPT = schurTransformer.getP().transpose();
        RealMatrix actualPT = schurTransformer.getPT();
        assertTrue(expectedPT.equals(actualPT));
    }

    @Test
    public void testGetT() {
        RealMatrix expectedT = MatrixUtils.createRealMatrix(schurTransformer.matrixT);
        RealMatrix actualT = schurTransformer.getT();
        assertTrue(expectedT.equals(actualT));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] matrixData = {
            {1.0, 2.0},
            {4.0, 5.0},
            {7.0, 8.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        schurTransformer = new SchurTransformer(matrix);
    }

}

