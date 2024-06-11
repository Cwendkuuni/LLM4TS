package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SchurTransformerTest {
    @Test
    public void testConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setRow(0, new double[]{1, 2, 3});
        matrix.setRow(1, new double[]{4, 5, 6});
        matrix.setRow(2, new double[]{7, 8, 9});

        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {0, 5, 6}, {0, 0, 9}});
        RealMatrix expectedPT = expectedP.transpose();
        
        assertEquals(expectedP, transformer.getP());
        assertEquals(expectedT, transformer.getT());
        assertEquals(expectedPT, transformer.getPT());
    }
}