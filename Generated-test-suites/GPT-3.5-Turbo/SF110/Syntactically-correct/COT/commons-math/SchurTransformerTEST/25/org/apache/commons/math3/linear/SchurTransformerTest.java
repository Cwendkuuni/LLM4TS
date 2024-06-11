package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix result = transformer.getP();
        assertNotNull(result);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix result = transformer.getPT();
        assertNotNull(result);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix result = transformer.getT();
        assertNotNull(result);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
    }
}