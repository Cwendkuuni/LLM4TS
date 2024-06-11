package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.*;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructor1() {
        double[][] data = {{1,2},{3,4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(1.0, root.getEntry(0,0), 0.0001);
        assertEquals(0.0, root.getEntry(0,1), 0.0001);
        assertEquals(3.0, root.getEntry(1,0), 0.0001);
        assertEquals(1.0, root.getEntry(1,1), 0.0001);
    }
    
    @Test
    public void testConstructor2() {
        double[][] data = {{1,2},{3,4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.1);
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(1.0, root.getEntry(0,0), 0.0001);
        assertEquals(0.0, root.getEntry(0,1), 0.0001);
        assertEquals(3.0, root.getEntry(1,0), 0.0001);
        assertEquals(1.0, root.getEntry(1,1), 0.0001);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor3() {
        double[][] data = {{-1,2},{3,4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor4() {
        double[][] data = {{1,2},{3,4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 100);
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{1,2},{3,4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(1.0, root.getEntry(0,0), 0.0001);
        assertEquals(0.0, root.getEntry(0,1), 0.0001);
        assertEquals(3.0, root.getEntry(1,0), 0.0001);
        assertEquals(1.0, root.getEntry(1,1), 0.0001);
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{1,2},{3,4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        int rank = decomp.getRank();
        assertEquals(2, rank);
    }
}