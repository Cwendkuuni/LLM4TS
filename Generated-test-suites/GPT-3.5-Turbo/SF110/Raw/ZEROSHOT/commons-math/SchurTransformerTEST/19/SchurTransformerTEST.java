package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{0.231f, 0.525f, 0.819f},{-0.520, -0.374, -0.229},{1, 0, 0}});
        RealMatrix actual = transformer.getP();
        Assert.assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{0.231f, -0.520f, 1},{0.525, -0.374, 0},{0.819, -0.229, 0}});
        RealMatrix actual = transformer.getPT();
        Assert.assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{14.034, 45.698, 0.0},{-45.698, -14.468, 0.0},{0.0, 0.0, 0.434}});
        RealMatrix actual = transformer.getT();
        Assert.assertArrayEquals(expected.getData(), actual.getData());
    }
}