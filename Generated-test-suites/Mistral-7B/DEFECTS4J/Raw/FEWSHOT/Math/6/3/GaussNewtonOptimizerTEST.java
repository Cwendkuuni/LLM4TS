package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;

public class GaussNewtonOptimizerTest {
    @Test(expected = NullArgumentException.class)
    public void testNullConstructor() {
        new GaussNewtonOptimizer(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        optimizer.setStartPoint(null);
        optimizer.doOptimize();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartPointLength() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 2.0, 3.0, 4.0, 5.0};
        optimizer.setStartPoint(new ArrayRealVector(startPoint.length - 1, null));
        optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        optimizer.doOptimize();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTargetLength() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        double[] target = {1.0, 2.0, 3.0};
        optimizer.setTarget(new ArrayRealVector(target.length - 1, null));
        optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullWeight() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        optimizer.doOptimize();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWeightDimension() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        double[] weight = {1.0, 2.0, 3.0};
        optimizer.setWeight(new ArrayRealVector(weight.length - 1, null));
        optimizer.doOptimize();
    }

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 2.0};
        double[] target = {3.0, 4.0};
        double[] weight = {1.0, 1.0};
        optimizer.setStartPoint(new ArrayRealVector(startPoint));
        optimizer.setTarget(new ArrayRealVector(target));
        optimizer.setWeight(new ArrayRealVector(weight));

        PointValuePair result = optimizer.doOptimize();
        double[] expectedPoint = {2.0, 2.0};
        double[] expectedValue = {1.0E-10, 1.0E-10};
        assertEquals(expectedPoint, result.getPoint(), 0.001);
        assertEquals(expectedValue, result.getValue(), 0.001);
    }

    @Test
    public void testGaussNewtonOptimizerLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker(), true);
        double[] startPoint = {1.0, 2.0};
        double[] target = {3.0, 4.0};
        double[] weight = {1.0, 1.0};
        optimizer.setStartPoint(new ArrayRealVector(startPoint));
        optimizer.setTarget(new ArrayRealVector(target));
        optimizer.setWeight(new ArrayRealVector(weight));

        PointValuePair result = optimizer.doOptimize();
        double[] expectedPoint = {2.0, 2.0};
        double[] expectedValue = {1.0E-10, 1.0E-10};
        assertEquals(expectedPoint, result.getPoint(), 0.001);
        assertEquals(expectedValue, result.getValue(), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerNoConvergence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 2.0};
        double[] target = {1.0, 2.0};
        double[] weight = {1.0, 1.0};
        optimizer.setStartPoint(new ArrayRealVector(startPoint));
        optimizer.setTarget(new ArrayRealVector(target));
        optimizer.setWeight(new ArrayRealVector(weight));

        optimizer.doOptimize();
    }

    @Test
    public void testGaussNewtonOptimizerSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 2.0};
        double[] target = {3.0, 4.0};
        double[] weight = {1.0, 1.0};
        optimizer.setStartPoint(new ArrayRealVector(startPoint));
        optimizer.setTarget(new ArrayRealVector(target));
        optimizer.setWeight(new ArrayRealVector(weight));

        double[][] jacobian = {{1.0, 1.0}, {1.0, 1.0}};
        RealMatrix weightedJacobian = new BlockRealMatrix(jacobian);
        try {
            optimizer.computeWeightedJacobian(startPoint);
        } catch (SingularMatrixException e) {
            // expected
            return;
        }

        fail("SingularMatrixException not thrown");
    }
}