package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class SchurTransformerTest {
    @Test
    public void testGetP() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix rm = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer st = new SchurTransformer(rm);
        RealMatrix expected = MatrixUtils.createRealMatrix(st.matrixP);
        assertEquals(expected, st.getP());
    }
    
    @Test
    public void testGetPT() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix rm = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer st = new SchurTransformer(rm);
        RealMatrix expected = st.getP().transpose();
        assertEquals(expected, st.getPT());
    }
    
    @Test
    public void testGetT() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix rm = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer st = new SchurTransformer(rm);
        RealMatrix expected = MatrixUtils.createRealMatrix(st.matrixT);
        assertEquals(expected, st.getT());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        double[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix rm = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer st = new SchurTransformer(rm);
    }
    
    @Test
    public void testTransform() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix rm = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer st = new SchurTransformer(rm);
        
        double[][] expectedMatrixP = {{-0.45, 0.83}, {0.89, 0.55}};
        double[][] expectedMatrixT = {{4.372281323269014, -0.636561226687974}, {0.0, 0.6277186767308311}};
        
        for (int i = 0; i < st.matrixP.length; i++) {
            for (int j = 0; j < st.matrixP[0].length; j++) {
                assertEquals(expectedMatrixP[i][j], st.matrixP[i][j], 1e-10);
            }
        }
        
        for (int i = 0; i < st.matrixT.length; i++) {
            for (int j = 0; j < st.matrixT[0].length; j++) {
                assertEquals(expectedMatrixT[i][j], st.matrixT[i][j], 1e-10);
            }
        }
    }
}