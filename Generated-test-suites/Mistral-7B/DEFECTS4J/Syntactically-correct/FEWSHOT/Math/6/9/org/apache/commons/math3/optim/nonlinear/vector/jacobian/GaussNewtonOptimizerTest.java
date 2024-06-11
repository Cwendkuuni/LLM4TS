package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.PointValuePair;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;

public class GaussNewtonOptimizerTest {

    @Test(expected = NullArgumentException.class)
    public void testNullConstructor() {
        new GaussNewtonOptimizer(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullConstructorWithChecker() {
        new GaussNewtonOptimizer(true, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullConstructorWithUseLU() {
        new GaussNewtonOptimizer(null, new SimpleVectorValueChecker());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullConstructorWithUseLUAndChecker() {
        new GaussNewtonOptimizer(true, null, new SimpleVectorValueChecker());
    }

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testGaussNewtonOptimizerWithChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testGaussNewtonOptimizerWithUseLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        assertNotNull(optimizer);
    }

    @Test
    public void testGaussNewtonOptimizerWithUseLUAndChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertNotNull(optimizer);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {2.0, 3.0};
        optimizer.optimize(startPoint, targetValues);
    }

    @Test
    public void testConvergence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {2.0, 2.0};
        PointValuePair result = optimizer.optimize(startPoint, targetValues);
        assertNotNull(result);
        assertEquals(0, result.getValue(), 0.001);
    }

    @Test
    public void testConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {2.0, 2.0};
        PointValuePair result = optimizer.optimize(startPoint, targetValues);
        assertNotNull(result);
        assertTrue(optimizer.isConverged());
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 0.0}
        });
        optimizer.setWeight(weightMatrix);
        optimizer.optimize(startPoint, targetValues);
    }
}