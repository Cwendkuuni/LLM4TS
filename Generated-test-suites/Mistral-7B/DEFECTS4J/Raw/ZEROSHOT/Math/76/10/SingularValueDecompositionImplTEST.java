import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.54522718, 0.81915255},
                                                                            {-0.81915255, 0.54522718}});
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.70710678, 0.69282032},
                                                                            {-0.69282032, 0.70710678}});
            assertEquals("U is not as expected", expectedU, u);
            assertEquals("V is not as expected", expectedV, v);
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{8.22633366, 0.0}, {0.0, 1.22474487}});
            assertEquals("S is not as expected", expectedS, s);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionWithThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{5.0, 5.0, 5.0}, {5.0, 5.0, 5.0}, {5.0, 5.0, 5.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.74121949, 0.53851609, 0.49477633},
                                                                            {0.53851609, 0.74121949, 0.49477633},
                                                                            {-0.49477633, -0.49477633, 1.12315625}});
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{7.94494731, 0.0, 0.0},
                                                                            {0.0, 4.02332381, 0.0},
                                                                            {0.0, 0.0, 2.23606798}});
            assertEquals("U is not as expected", expectedU, u);
            assertEquals("V is not as expected", expectedV, v);
            assertEquals("S is not as expected", expectedS, s);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double expected = Math.sqrt(5.0);
            double actual = svd.getNorm();
            assertEquals("Norm is not as expected", expected, actual);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetConditionNumber() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double expected = 3.0 / Math.sqrt(5.0);
            double actual = svd.getConditionNumber();
            assertEquals("Condition number is not as expected", expected, actual);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            int expected = 2;
            int actual = svd.getRank();
            assertEquals("Rank is not as expected", expected, actual);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCovariance() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.5, 0.1, 0.2}, {0.1, 0.5, 0.1}, {0.2, 0.1, 0.5}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
            SingularValueDecompositionImpl svdWithThreshold = new SingularValueDecompositionImpl(matrix, 1);
            double[] singularValues = svd.getSingularValues();
            assertEquals("Singular values should be 3, 1, 0", 3, singularValues.length);
            double threshold = Math.sqrt(singularValues[2]);
            RealMatrix covarianceWithThreshold = svdWithThreshold.getCovariance(threshold);
            RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.26291148, 0.22413262, 0.14138223},
                                                                                        {0.22413262, 0.26291148, 0.14138223},
                                                                                        {0.14138223, 0.14138223, 0.02851768}});
            assertEquals("Covariance matrix is not as expected", expectedCovariance, covarianceWithThreshold);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            Solver solver = svd.getSolver();
            RealMatrix testMatrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}});
            RealVector testVector = new ArrayRealVector(new double[]{1.0, 1.0});
            RealVector expectedVector = new ArrayRealVector(new double[]{1.53936883, 0.45353111});
            RealVector solution = solver.solve(testVector);
            assertEquals("Solution is not as expected", expectedVector, solution);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}