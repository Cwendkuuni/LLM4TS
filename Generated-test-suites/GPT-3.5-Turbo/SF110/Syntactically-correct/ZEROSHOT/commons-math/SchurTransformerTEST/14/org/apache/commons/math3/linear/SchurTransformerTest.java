package org.apache.commons.math3.linear;

import org.junit.Test;
import org.junit.Assert;

public class SchurTransformerTest {
    
    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{0.3722813232690143, -0.8245648408917439}, {-0.8245648408917439, -0.3722813232690143}});
        Assert.assertEquals(expected, transformer.getP());
    }
    
    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{0.3722813232690143, -0.8245648408917439}, {-0.8245648408917439, -0.3722813232690143}});
        expected = expected.transpose();
        Assert.assertEquals(expected, transformer.getPT());
    }
    
    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{5.372281323269014, 0.6472118703791313}, {0.6472118703791313, -0.37228132326901425}});
        Assert.assertEquals(expected, transformer.getT());
    }
    
}