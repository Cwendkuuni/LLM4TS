import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstruction() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            Assert.assertEquals(2, singularValueDecomposition.getRank());
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplSingularValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            double[] singularValues = singularValueDecomposition.getSingularValues();
            Assert.assertArrayEquals(new double[]{Math.sqrt(5.0), 0.0}, singularValues, 0.0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplUTMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            RealMatrix utMatrix = singularValueDecomposition.getUT();
            Assert.assertEquals(2, utMatrix.getRowDimension());
            Assert.assertEquals(2, utMatrix.getColumnDimension());
            RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{{0.53846068, 0.85704973}, {-0.85704973, 0.53846068}});
            Assert.assertArrayEquals(expectedUT.getData(), utMatrix.getData(), 0.0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplSMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            RealMatrix sMatrix = singularValueDecomposition.getS();
            Assert.assertTrue(sMatrix instanceof RealDiagonalMatrix);
            Assert.assertEquals(2, sMatrix.getRowDimension());
            Assert.assertEquals(2, sMatrix.getColumnDimension());
            double[] singularValues = ((RealDiagonalMatrix) sMatrix).getData();
            Assert.assertArrayEquals(new double[]{Math.sqrt(5.0), 0.0}, singularValues, 0.0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplVMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            RealMatrix vMatrix = singularValueDecomposition.getV();
            Assert.assertEquals(2, vMatrix.getRowDimension());
            Assert.assertEquals(2, vMatrix.getColumnDimension());
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.44721361, 0.89442718}, {-0.89442718, 0.44721361}});
            Assert.assertArrayEquals(expectedV.getData(), vMatrix.getData(), 0.0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplVTMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            RealMatrix vtMatrix = singularValueDecomposition.getVT();
            Assert.assertEquals(2, vtMatrix.getRowDimension());
            Assert.assertEquals(2, vtMatrix.getColumnDimension());
            RealMatrix expectedVT = new Array2DRowRealMatrix(new double[][]{{0.44721361, -0.89442718}, {0.89442718, 0.44721361}});
            Assert.assertArrayEquals(expectedVT.getData(), vtMatrix.getData(), 0.0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetCovariance() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix, 2);
            RealMatrix covariance = singularValueDecomposition.getCovariance(1.0);
            Assert.assertEquals(2, covariance.getRowDimension());
            Assert.assertEquals(2, covariance.getColumnDimension());
            double[][] covarianceData = covariance.getData();
            Assert.assertEquals(1.0, covarianceData[0][0], 0.0);
            Assert.assertEquals(1.0, covarianceData[1][1], 0.0);
            Assert.assertEquals(0.0, covarianceData[0][1], 0.0);
            Assert.assertEquals(0.0, covarianceData[1][0], 0.0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            double norm = singularValueDecomposition.getNorm();
            Assert.assertEquals(Math.sqrt(5.0), norm, 0.0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetConditionNumber() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            double conditionNumber = singularValueDecomposition.getConditionNumber();
            Assert.assertEquals(Math.sqrt(5.0) / 3.0, conditionNumber, 0.0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testSingularValueDecompositionImplInvalidMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0}});
        SingularValueDecompositionImpl singularValueDecomposition = null;
        try {
            singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            singularValueDecomposition = e;
        }
        Assert.assertNotNull(singularValueDecomposition);
    }

}