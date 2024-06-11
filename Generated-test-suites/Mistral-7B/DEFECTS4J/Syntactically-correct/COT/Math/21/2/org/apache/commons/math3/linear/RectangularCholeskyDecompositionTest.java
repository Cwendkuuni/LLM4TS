package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
@RunWith(Parameterized.class)
public class RectangularCholeskyDecompositionTest {

    /** The instance under test. */
    private RectangularCholeskyDecomposition decomp;

    /** The original matrix. */
    private RealMatrix matrix;

    /** The small threshold. */
    private double small;

    /**
     * Constructor.
     *
     * @param matrix the matrix to decompose
     * @param small the small threshold
     */
    public RectangularCholeskyDecompositionTest(RealMatrix matrix, double small) {
        this.matrix = matrix;
        this.small = small;
    }

    /**
     * Parameters for the test.
     *
     * @return the parameters
     */
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = {
            {MatrixUtils.denseMatrix(1.0, 0.1, 0.1, 0.1, 1.0, 0.1, 0.1, 0.1, 1.0), 0.0},
            {MatrixUtils.denseMatrix(1.0, 0.1, 0.1, 0.1, 1.0, 0.1, 0.1, 0.1, 1.0), 0.1},
            {MatrixUtils.denseMatrix(1.0, 0.1, 0.1, 0.1, 1.0, 0.1, 0.1, 0.1, 1.0), 1.0},
            {MatrixUtils.denseMatrix(1.0, 0.1, 0.1, 0.1, 1.0, 1.0, 1.0, 0.1, 1.0), 1.0},
            {MatrixUtils.denseMatrix(1.0, 0.1, 0.1, 0.1, 1.0, 0.1, 0.1, 0.1, 1.0), -1.0}
        };
        return Arrays.asList(data);
    }

    /**
     * Test constructor.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testConstructor() throws Exception {
        decomp = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomp.getRootMatrix());
        assertEquals(decomp.getRank(), matrix.getRowDimension());
    }

    /**
     * Test getRootMatrix.
     */
    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = new SingularValueDecomposition(matrix.transpose())
                .getV().transpose();
        RealMatrix actual = decomp.getRootMatrix();
        assertEquals(expected, actual);

        DecompositionSolver solver = new LUDecomposition(matrix);
        RealMatrix inverse = solver.getSolver().getSolver().getInverse();
        expected = MatrixUtils.multiply(inverse, expected);
        assertEquals(expected, actual);
    }

    /**
     * Test getRank.
     */
    @Test
    public void testGetRank() {
        assertEquals(decomp.getRank(), matrix.getRowDimension());
    }

    /**
     * Test exception handling.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testExceptionHandling() throws Exception {
        matrix = MatrixUtils.denseMatrix(1.0, 0.1, 0.1, 0.1, 1.0, -2.0, 0.1, 0.1, 1.0);
        new RectangularCholeskyDecomposition(matrix, small);
    }
}