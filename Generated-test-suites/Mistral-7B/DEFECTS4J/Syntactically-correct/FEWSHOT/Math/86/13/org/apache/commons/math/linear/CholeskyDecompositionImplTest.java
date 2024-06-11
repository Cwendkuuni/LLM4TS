package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructorWithMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        assertEquals(l.getRowDimension(), lt.getColumnDimension(), 0);
        assertEquals(l.getRowDimension(), l.getColumnDimension(), 0);
        assertEquals(lt.getRowDimension(), lt.getColumnDimension(), 0);
        assertEquals(l.getRowDimension(), 3, 0);
        assertEquals(l.getColumnDimension(), 3, 0);
        assertEquals(lt.getRowDimension(), 3, 0);
        assertEquals(lt.getColumnDimension(), 3, 0);
        assertArrayEquals(new double[][]{
            {1.0, 0.0, 0.0},
            {0.44948974278317, 0.89899552394616, 0.0},
            {0.44948974278317, 0.0, 0.89899552394616}
        }, l.getData(), 0.0000001);
        assertArrayEquals(new double[][]{
            {1.0, 0.44948974278317, 0.44948974278317},
            {0.1, 0.89899552394616, 1.34537049679033},
            {0.1, 1.34537049679033, 2.12135226548679}
        }, lt.getData(), 0.0000001);
        assertEquals(decomposition.getDeterminant(), 2.1213522654867875, 0.0000001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {0.1, 0.1}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1}, {0.1, 1.0}, {0.1, 1.1}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1}, {0.1, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        assertArrayEquals(new double[][]{
            {1.0, 0.0, 0.0},
            {0.44948974278317, 0.89899552394616, 0.0},
            {0.44948974278317, 0.0, 0.89899552394616}
        }, l.getData(), 0.0000001);
    }

    @Test
    public void testGetLT() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1},
                {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = decomposition.getLT();
        assertArrayEquals(new double[][]{
            {1.0, 0.44948974278317, 0.44948974278317},
            {0.1, 0.89899552394616, 1.34537049679033},
            {0.1, 1.34537049679033, 2.12135226548679}
        }, lt.getData(), 0.0000001);
    }

    @Test
    public void testGetDeterminant() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1},
                {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        assertEquals(decomposition.getDeterminant(), 2.1213522654867875, 0.0000001);
    }

    @Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1},
                {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomposition.getSolver();

        double[] b = {1.0, 1.0, 1.0};
        RealVector x = solver.solve(new ArrayRealVector(b));
        assertArrayEquals(new double[]{1.0, 0.44948974278317, 0.27716501392837}, x.toArray(), 0.0000001);

        RealVector y = new ArrayRealVector(new double[]{-0.27716501392837, 0.44948974278317, 1.0});
        RealVector xy = solver.solve(y);
        assertArrayEquals(new double[]{1.0, 0.0, 0.0}, xy.toArray(), 0.0000001);

        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        RealVector b2 = new ArrayRealVector(new double[]{-0.27716501392837, 0.44948974278317, 1.0});
        RealVector x2 = solver.solve(b2);
        RealVector xy2 = A.operate(x2);
        assertArrayEquals(new double[]{1.0, 0.0, 0.0}, xy2.toArray(), 0.0000001);
    }

}