package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {
    private SchurTransformer schurTransformer;
    private RealMatrix matrix;
    
    @Before
    public void setUp() {
        matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        schurTransformer = new SchurTransformer(matrix);
    }
    
    @Test
    public void testGetP() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        RealMatrix actual = schurTransformer.getP();
        
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test
    public void testGetPT() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        RealMatrix actual = schurTransformer.getPT();
        
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test
    public void testGetT() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][] {{1, 2, 3}, {0, -3, -6}, {0, 0, 0}});
        RealMatrix actual = schurTransformer.getT();
        
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerNonSquareMatrixException() {
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2, 3}, {4, 5, 6}});
        SchurTransformer schurTransformer = new SchurTransformer(nonSquareMatrix);
    }
}