package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenvalueDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.VectorUtils;
import org.apache.commons.math3.special.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    /**
     * Test the constructor with a valid square matrix.
     */
    @Test
    public void testConstructorSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3}, {2, 3, 4}, {3, 4, 5}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertEquals(3, svd.getRank());
            assertEquals(3, svd.getU().getRowDimension());
            assertEquals(3, svd.getU().getColumnDimension());
            assertEquals(3, svd.getS().getRowDimension());
            assertEquals(3, svd.getS().getColumnDimension());
            assertEquals(3, svd.getV().getRowDimension());
            assertEquals(3, svd.getV().getColumnDimension());
            RealMatrix u = svd.getU();
            assertTrue(u.isOrthogonal());
            RealMatrix v = svd.getV();
            assertTrue(v.isOrthogonal());
            RealMatrix ut = svd.getUT();
            assertEquals(3, ut.getRowDimension());
            assertEquals(3, ut.getColumnDimension());
            assertTrue(ut.isOrthogonal());
            RealMatrix vt = svd.getVT();
            assertEquals(3, vt.getRowDimension());
            assertEquals(3, vt.getColumnDimension());
            assertTrue(vt.isOrthogonal());
            RealMatrix s = svd.getS();
            assertTrue(VectorUtils.isEqual(VectorUtils.createRealVector(new double[]{1, 1, 1}), s.getDiagonalElements()));
            RealVector sv = svd.getSingularValues();
            assertTrue(VectorUtils.isEqual(VectorUtils.createRealVector(new double[]{3.1622776601683796, 1.5430806348251957, 0.0}), sv));
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test the constructor with a valid rectangular matrix.
     */
    @Test
    public void testConstructorRectangularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3}, {2, 3, 4}, {3, 4, 5}, {4, 5, 6}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
            assertEquals(3, svd.getRank());
            assertEquals(3, svd.getU().getRowDimension());
            assertEquals(3, svd.getU().getColumnDimension());
            assertEquals(2, svd.getS().getRowDimension());
            assertEquals(2, svd.getS().getColumnDimension());
            assertEquals(3, svd.getV().getRowDimension());
            assertEquals(3, svd.getV().getColumnDimension());
            RealMatrix u = svd.getU();
            assertTrue(u.isOrthogonal());
            RealMatrix v = svd.getV();
            assertTrue(v.isOrthogonal());
            RealMatrix ut = svd.getUT();
            assertEquals(3, ut.getRowDimension());
            assertEquals(3, ut.getColumnDimension());
            assertTrue(ut.isOrthogonal());
            RealMatrix vt = svd.getVT();
            assertEquals(3, vt.getRowDimension());
            assertEquals(3, vt.getColumnDimension());
            assertTrue(vt.isOrthogonal());
            RealMatrix s = svd.getS();
            assertTrue(VectorUtils.isEqual(VectorUtils.createRealVector(new double[]{3.1622776601683796, 1.5430806348251957}), s.getColumnVector(0)));
            assertTrue(VectorUtils.isEqual(VectorUtils.createRealVector(new double[]{0.0, 0.0}), s.getColumnVector(1)));
            RealVector sv = svd.getSingularValues();
            assertTrue(VectorUtils.isEqual(VectorUtils.createRealVector(new double[]{3.1622776601683796, 1.5430806348251957, 0.0}), sv));
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test the constructor with an invalid matrix.
     */
    @Test(expected = InvalidMatrixException.class)
    public void testConstructorInvalidMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2}, {3, 4}
        });
        new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test the getSolver method.
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3}, {2, 3, 4}, {3, 4, 5}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            DecompositionSolver solver = svd.getSolver();
            assertEquals(3, solver.getRank());
            assertEquals(3, solver.getInverse().getRowDimension());
            assertEquals(3, solver.getInverse().getColumnDimension());
            RealMatrix b = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 3}, {4, 5, 6}
            });
            RealVector x = solver.solve(new Array2DRowRealMatrix(new double[][]{
                {1, 1}, {2, 2}
            }));
            assertTrue(VectorUtils.isEqual(VectorUtils.createRealVector(new double[]{1.4529539470494834, 1.081723705858576}), x));
            RealVector y = solver.solve(new Array2DRowRealMatrix(new double[][]{
                {1, 4}, {2, 5}
            }));
            assertTrue(VectorUtils.isEqual(VectorUtils.createRealVector(new double[]{1.641533383263181, 1.791263468434898}), y));
            RealMatrix z = solver.solve(b);
            assertTrue(VectorUtils.isEqual(VectorUtils.createRealMatrix(new double[][]{
                {-0.107894271112418, 0.270365360225543},
                {0.372203746982284, -0.595316223243857},
                {-0.739246838886765, 0.215831488601685}
            }), z));
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test the getCovariance method with a valid matrix.
     */
    @Test
    public void testGetCovarianceValidMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3}, {2, 3, 4}, {3, 4, 5}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix covariance = svd.getCovariance(0.5);
            assertEquals(3, covariance.getRowDimension());
            assertEquals(3, covariance.getColumnDimension());
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (i == j) {
                        assertEquals(Math.pow(singularValue(i, svd), 2), covariance.getEntry(i, j), 1e-12);
                    } else {
                        assertEquals(0, covariance.getEntry(i, j), 1e-12);
                    }
                }
            }
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test the getCovariance method with a singular matrix.
     */
    @Test
    public void testGetCovarianceSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2}, {2, 2}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix covariance = svd.getCovariance(0.5);
            assertEquals(2, covariance.getRowDimension());
            assertEquals(2, covariance.getColumnDimension());
            assertEquals(Math.pow(singularValue(0, svd), 2), covariance.getEntry(0, 0), 1e-12);
            assertEquals(Math.pow(singularValue(1, svd), 2), covariance.getEntry(1, 1), 1e-12);
            assertEquals(0, covariance.getEntry(0, 1), 1e-12);
            assertEquals(0, covariance.getEntry(1, 0), 1e-12);
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test the getNorm method.
     */
    @Test
    public void testGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2}, {2, 3}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertEquals(Math.sqrt(5), svd.getNorm(), 1e-12);
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test the getConditionNumber method.
     */
    @Test
    public void testGetConditionNumber() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2}, {2, 3}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertEquals(3.1622776601683796, svd.getConditionNumber(), 1e-12);
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test the getRank method.
     */
    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2}, {2, 3}
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertEquals(2, svd.getRank());
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Get the i-th singular value of the given matrix.
     * @param i index of the singular value to retrieve
     * @param svd the singular value decomposition of the matrix
     * @return the i-th singular value
     */
    private double singularValue(int i, SingularValueDecompositionImpl svd) {
        return svd.getSingularValues()[i];
    }

    /**
     * Test the eigenvalue decomposition of the tridiagonal matrix.
     */
    @Test
    public void testEigenvalueDecomposition() {
        double[] mainDiagonal = {1, 2, 2, 3, 3, 3};
        double[] secondaryDiagonal = {0, Math.sqrt(2), Math.sqrt(2), 0, Math.sqrt(2), Math.sqrt(3)};
        EigenvalueDecomposition eigenvalueDecomposition = new EigenDecompositionImpl(
                new double[][]{mainDiagonal, secondaryDiagonal}, false);
        RealMatrix eigenvectors = eigenvalueDecomposition.getV();
        RealMatrix eigenvectorsTranspose = eigenvalueDecomposition.getVT();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{
                {MathUtils.cosd(Math.PI / 3), MathUtils.sin(Math.PI / 3), 0, 0, -MathUtils.sin(Math.PI / 3), MathUtils.cos(Math.PI / 3)},
                {-MathUtils.sin(Math.PI / 3), MathUtils.cos(Math.PI / 3), 0, 0, MathUtils.cos(Math.PI / 3), MathUtils.sin(Math.PI / 3)},
                {0, 0, 1, 0, 0, 1},
                {0, 0, 0, Math.sqrt(3), -Math.sqrt(2 / 3), Math.sqrt(2 / 3)},
                {0, 0, 0, Math.sqrt(2 / 3), Math.sqrt(2 / 3), -Math.sqrt(2 / 3)},
                {0, 0, 0, -Math.sqrt(2 / 3), Math.sqrt(2 / 3), Math.sqrt(2 / 3)}
        });
        RealMatrix expectedSingularValues = new Array2DRowRealMatrix(new double[][]{
                {Math.sqrt(3), 0, 0},
                {0, Math.sqrt(2), 0},
                {0, 0, Math.sqrt(1)}
        });
        RealMatrix u = new SingularValueDecompositionImpl(new Array2DRowRealMatrix(mainDiagonal, secondaryDiagonal)).getU();
        RealMatrix singularValues = new SingularValueDecompositionImpl(new Array2DRowRealMatrix(mainDiagonal, secondaryDiagonal)).getS();
        assertTrue(VectorUtils.isEqual(expectedU, u));
        assertTrue(VectorUtils.isEqual(expectedSingularValues, singularValues));
        assertTrue(VectorUtils.isEqual(eigenvectors, u));
        assertTrue(VectorUtils.isEqual(eigenvectorsTranspose, u.transpose()));
        assertTrue(VectorUtils.isEqual(eigenvalueDecomposition.getEigenvalues(), singularValues.getDiagonalElements()));
    }

}