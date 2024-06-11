package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        double[][] testData = {{1, 2}, {3, 4}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(testMatrix);
        RealMatrix result = transformer.getP();
        double[][] expectedData = {{-0.415973, -0.909375}, {0.909375, -0.415973}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetPT() {
        double[][] testData = {{1, 2}, {3, 4}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(testMatrix);
        RealMatrix result = transformer.getPT();
        double[][] expectedData = {{-0.415973, 0.909375}, {-0.909375, -0.415973}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetT() {
        double[][] testData = {{1, 2}, {3, 4}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        SchurTransformer transformer = new SchurTransformer(testMatrix);
        RealMatrix result = transformer.getT();
        double[][] expectedData = {{0.158144, 2.55214}, {-3.44786, -0.158144}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        Assert.assertEquals(expected, result);
    }

}