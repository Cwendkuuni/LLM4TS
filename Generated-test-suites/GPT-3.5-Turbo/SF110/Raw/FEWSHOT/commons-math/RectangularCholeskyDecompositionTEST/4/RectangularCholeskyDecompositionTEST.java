package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testConstructor1() {
        RealMatrix matrix = new RealMatrix(3, 3); // create matrix
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected exception
        }
    }
    
    @Test
    public void testConstructor2() {
        RealMatrix matrix = new RealMatrix(3, 3); // create matrix
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected exception
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new RealMatrix(3, 3); // create matrix
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected exception
        }
    }
    
    @Test
    public void testGetRank() {
        RealMatrix matrix = new RealMatrix(3, 3); // create matrix
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            int rank = decomposition.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected exception
        }
    }
}