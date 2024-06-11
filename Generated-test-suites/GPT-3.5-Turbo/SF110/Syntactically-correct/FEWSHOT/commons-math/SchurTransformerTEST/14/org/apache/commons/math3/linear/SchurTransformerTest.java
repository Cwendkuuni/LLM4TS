package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SchurTransformerTest {
    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        assertNotNull(p);
        assertEquals(1.0, p.getEntry(0, 0), 0.0001);
        assertEquals(0.0, p.getEntry(0, 1), 0.0001);
        assertEquals(0.0, p.getEntry(1, 0), 0.0001);
        assertEquals(1.0, p.getEntry(1, 1), 0.0001);
    }
    
    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix pt = transformer.getPT();
        assertNotNull(pt);
        assertEquals(1.0, pt.getEntry(0, 0), 0.0001);
        assertEquals(0.0, pt.getEntry(0, 1), 0.0001);
        assertEquals(0.0, pt.getEntry(1, 0), 0.0001);
        assertEquals(1.0, pt.getEntry(1, 1), 0.0001);
    }
    
    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix t = transformer.getT();
        assertNotNull(t);
        assertEquals(1.0, t.getEntry(0, 0), 0.0001);
        assertEquals(2.0, t.getEntry(0, 1), 0.0001);
        assertEquals(0.0, t.getEntry(1, 0), 0.0001);
        assertEquals(3.2309, t.getEntry(1, 1), 0.0001);
    }
}