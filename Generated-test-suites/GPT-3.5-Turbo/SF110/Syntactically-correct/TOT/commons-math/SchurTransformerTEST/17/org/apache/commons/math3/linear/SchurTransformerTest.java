package org.apache.commons.math3.linear;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.SchurTransformer;

import static org.junit.Assert.*;

public class SchurTransformerTest {

    private SchurTransformer schurTransformer;
    private RealMatrix matrix;
    
    @Before
    public void setUp() {
        double[][] data = { {1, 2}, {3, 4} };
        matrix = MatrixUtils.createRealMatrix(data);
        schurTransformer = new SchurTransformer(matrix);
    }
    
    @Test
    public void testGetP() {
        RealMatrix expected = MatrixUtils.createRealMatrix(schurTransformer.getP().getData());
        RealMatrix actual = new SchurTransformer(matrix).getP();
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test
    public void testGetPT() {
        RealMatrix expected = MatrixUtils.createRealMatrix(schurTransformer.getPT().getData());
        RealMatrix actual = new SchurTransformer(matrix).getPT();
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test
    public void testGetT() {
        RealMatrix expected = MatrixUtils.createRealMatrix(schurTransformer.getT().getData());
        RealMatrix actual = new SchurTransformer(matrix).getT();
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] data = { {1, 2, 3}, {4, 5, 6} };
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(data);
        new SchurTransformer(nonSquareMatrix);
    }
    
    @Test
    public void testTransform() {
        double[][] data = { {1, 2}, {3, 4} };
        RealMatrix matrixA = MatrixUtils.createRealMatrix(data);
        
        double[][] expectedData = { {1.0, 3.0}, {0.0, 2.0} };
        RealMatrix expectedA = MatrixUtils.createRealMatrix(expectedData);
        
        SchurTransformer schurTransformerA = new SchurTransformer(matrixA);
        
        RealMatrix actualP = schurTransformerA.getP();
        RealMatrix actualPT = schurTransformerA.getPT();
        RealMatrix actualT = schurTransformerA.getT();
        
        assertArrayEquals(expectedA.getData(), actualT.multiply(actualPT).getData());
        assertArrayEquals(actualA.getData(), actualPT.multiply(actualA).getData());
        assertArrayEquals(MatrixUtils.createRealIdentityMatrix(matrixA.getRowDimension()).getData(), actualP.multiply(actualPT).getData());
    }

}