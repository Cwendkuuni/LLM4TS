package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertEquals(true, optimizer.isUseLU());

        GaussNewtonOptimizer optimizerQR = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        assertEquals(false, optimizerQR.isUseLU());
    }

    @Test
    public void testDoOptimize_LU() {
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weight = new QRDecomposition(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}})).getSolver().getInverse();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(RealVector point) {
                RealVector residuals = weight.operate(point).subtract(new ArrayRealVector(targetValues));
                return residuals.dotProduct(residuals);
            }

            @Override
            public RealVector gradient(RealVector point) {
                return weight.operate(point);
            }
        }, startPoint);

        assertEquals(targetValues, result.getPoint(), 1e-14);
    }

    @Test
    public void testDoOptimize_QR() {
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weight = new LUDecomposition(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}})).getSolver().getInverse();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());

        PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(RealVector point) {
                RealVector residuals = weight.operate(point).subtract(new ArrayRealVector(targetValues));
                return residuals.dotProduct(residuals);
            }

            @Override
            public RealVector gradient(RealVector point) {
                return weight.operate(point);
            }
        }, startPoint);

        assertEquals(targetValues, result.getPoint(), 1e-14);
    }
}

class ObjectiveFunction {
    public double value(RealVector point) {
        RealVector residuals = weight.operate(point).subtract(new ArrayRealVector(targetValues));
        return residuals.dotProduct(residuals);
    }

    public RealVector gradient(RealVector point) {
        return weight.operate(point);
    }

    private double[] targetValues;
    private RealMatrix weight;
}