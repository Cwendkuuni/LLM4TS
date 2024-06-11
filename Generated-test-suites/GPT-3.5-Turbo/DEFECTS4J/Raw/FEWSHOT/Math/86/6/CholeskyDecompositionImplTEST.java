package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CholeskyDecompositionImplTest {
    
    @Test
    public void testGetL() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{9,3,0},{3,12,6},{0,6,20}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{3,0,0},{1,3,0},{0,2,4}});
        
        assertTrue(decomposition.getL().equals(expectedL));
    }
    
    @Test
    public void testGetLT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1,4,7},{2,5,8},{3,6,9}});
        
        assertTrue(decomposition.getLT().equals(expectedLT));
    }
    
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        
        assertEquals(0, decomposition.getDeterminant(), 1e-15);
    }
    
    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{9,3,0},{3,12,6},{0,6,20}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        
        DecompositionSolver solver = decomposition.getSolver();
        
        RealVector b = MatrixUtils.createRealVector(new double[]{1,2,3});
        RealVector x = solver.solve(b);
        
        assertTrue(x.equals(MatrixUtils.createRealVector(new double[]{0.25,0.375,0.175})));
    }
    
}