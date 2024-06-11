import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;
    private RealMatrix matrix;

    @Before
    public void setUp() {
        double[][] data = {{4.0, 12.0, -16.0}, {12.0, 37.0, -43.0}, {-16.0, -43.0, 98.0}};
        matrix = MatrixUtils.createRealMatrix(data);

        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix root = decomposition.getRootMatrix();

        // Typical use case
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        assertEquals(2.0, root.getEntry(0, 0), 0.000001);
        assertEquals(0.0, root.getEntry(0, 1), 0.000001);
        assertEquals(0.0, root.getEntry(0, 2), 0.000001);
        assertEquals(6.0, root.getEntry(1, 0), 0.000001);
        assertEquals(1.0, root.getEntry(1, 1), 0.000001);
        assertEquals(0.0, root.getEntry(1, 2), 0.000001);
        assertEquals(-8.0, root.getEntry(2, 0), 0.000001);
        assertEquals(5.0, root.getEntry(2, 1), 0.000001);
        assertEquals(1.0, root.getEntry(2, 2), 0.000001);

        // Edge case
        double[][] data = {{16.0, 12.0, -16.0}, {12.0, 37.0, -43.0}, {-16.0, -43.0, 98.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        assertEquals(4.0, root.getEntry(0, 0), 0.000001);
        assertEquals(0.0, root.getEntry(0, 1), 0.000001);
        assertEquals(0.0, root.getEntry(0, 2), 0.000001);
        assertEquals(3.0, root.getEntry(1, 0), 0.000001);
        assertEquals(4.0, root.getEntry(1, 1), 0.000001);
        assertEquals(0.0, root.getEntry(1, 2), 0.000001);
        assertEquals(-4.0, root.getEntry(2, 0), 0.000001);
        assertEquals(5.0, root.getEntry(2, 1), 0.000001);
        assertEquals(-6.0, root.getEntry(2, 2), 0.000001);

        // Error scenario
        double[][] data = {{-4.0, 12.0, -16.0}, {12.0, 37.0, -43.0}, {-16.0, -43.0, 98.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-4.0, e.getMainElement(), 0.000001);
            assertEquals(0, e.getRowIndex());
            assertEquals(0.000001, e.getSmall(), 0.000001);
        }
    }

    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();

        // Typical use case
        assertEquals(3, rank);

        // Edge case
        double[][] data = {{16.0, 12.0, -16.0}, {12.0, 37.0, -43.0}, {-16.0, -43.0, 98.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        int rank = decomposition.getRank();
        assertEquals(3, rank);

        // Error scenario
        double[][] data = {{-4.0, 12.0, -16.0}, {12.0, 37.0, -43.0}, {-16.0, -43.0, 98.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-4.0, e.getMainElement(), 0.000001);
            assertEquals(0, e.getRowIndex());
            assertEquals(0.000001, e.getSmall(), 0.000001);
        }
    }
}