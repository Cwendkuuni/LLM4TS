package org.apache.commons.math.linear;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.util.MathUtils;

public class SingularValueDecompositionImplTest {

    private static final int ROWS = 3;
    private static final int COLS = 3;
    private static final int MAX_SINGULAR_VALUES = 2;
    private RealMatrix matrix;
    private SingularValueDecompositionImpl svd;

    @Before
    public void setUp() {
        double[][] data = {{1.0, 0.1, 0.2}, {0.1, 1.0, 0.1}, {0.2, 0.1, 1.0}};
        matrix = new Array2DRowRealMatrix(data, false);
    }

    @Test
    public void testSingularValueDecompositionImplConstructor() {
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplConstructorWithMaxSingularValues() {
        try {
            svd = new SingularValueDecompositionImpl(matrix, MAX_SINGULAR_VALUES);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSingularValueDecompositionImplConstructorWithInvalidMatrix() {
        double[][] data = {{1.0, 0.1}, {0.1, 1.0}};
        RealMatrix invalidMatrix = new Array2DRowRealMatrix(data, false);
        try {
            new SingularValueDecompositionImpl(invalidMatrix);
        } catch (InvalidMatrixException e) {
            throw new MathIllegalArgumentException("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        try {
            RealMatrix u = svd.getU();
            assertEquals("U has incorrect dimensions", ROWS, u.getRowDimension());
            assertEquals("U has incorrect dimensions", MAX_SINGULAR_VALUES, u.getColumnDimension());
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        try {
            RealMatrix ut = svd.getUT();
            assertEquals("UT has incorrect dimensions", MAX_SINGULAR_VALUES, ut.getRowDimension());
            assertEquals("UT has incorrect dimensions", ROWS, ut.getColumnDimension());
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        try {
            RealMatrix s = svd.getS();
            assertEquals("S has incorrect dimensions", MAX_SINGULAR_VALUES, s.getRowDimension());
            assertEquals("S has incorrect dimensions", MAX_SINGULAR_VALUES, s.getColumnDimension());
            for (int i = 0; i < MAX_SINGULAR_VALUES; ++i) {
                assertTrue("Singular values are not positive", svd.singularValues[i] > 0);
            }
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        try {
            double[] singularValues = svd.getSingularValues();
            assertEquals("Singular values have incorrect length", MAX_SINGULAR_VALUES, singularValues.length);
            for (int i = 0; i < MAX_SINGULAR_VALUES; ++i) {
                assertTrue("Singular values are not positive", singularValues[i] > 0);
            }
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        try {
            RealMatrix v = svd.getV();
            assertEquals("V has incorrect dimensions", COLS, v.getRowDimension());
            assertEquals("V has incorrect dimensions", MAX_SINGULAR_VALUES, v.getColumnDimension());
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetVT() throws InvalidMatrixException {
        try {
            RealMatrix vt = svd.getVT();
            assertEquals("VT has incorrect dimensions", MAX_SINGULAR_VALUES, vt.getRowDimension());
            assertEquals("VT has incorrect dimensions", COLS, vt.getColumnDimension());
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        try {
            RealMatrix covariance = svd.getCovariance(0);
            assertEquals("Covariance has incorrect dimensions", ROWS, covariance.getRowDimension());
            assertEquals("Covariance has incorrect dimensions", ROWS, covariance.getColumnDimension());
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        try {
            double norm = svd.getNorm();
            assertEquals("Norm is incorrect", svd.singularValues[0], norm);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        try {
            double conditionNumber = svd.getConditionNumber();
            assertEquals("Condition number is incorrect", svd.singularValues[0] / svd.singularValues[MAX_SINGULAR_VALUES - 1], conditionNumber);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        try {
            int rank = svd.getRank();
            assertEquals("Rank is incorrect", Math.min(ROWS, COLS), rank);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetSolver() throws InvalidMatrixException {
        try {
            DecompositionSolver solver = svd.getSolver();
            assertTrue("Solver is not initialized", solver != null);
            assertFalse("Solver is singular", solver.isNonSingular());
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
}