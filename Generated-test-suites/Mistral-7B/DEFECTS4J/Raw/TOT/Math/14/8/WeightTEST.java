package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.GaussianFunction;
import org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.ScaledLevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtProblem;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtResult;
import org.apache.commons.math3.optim.nonlinear.linear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.linear.GaussNewtonResult;
import org.apache.commons.math3.optim.nonlinear.linear.LevenbergMarquardt;
import org.apache.commons.math3.optim.nonlinear.linear.LevenbergMarquardtAlgorithm;
import org.apache.commons.math3.optim.nonlinear.linear.LevenbergMarquardtParameters;
import org.apache.commons.math3.optim.nonlinear.linear.LevenbergMarquardtInformation;
import org.apache.commons.math3.optim.nonlinear.linear.LevenbergMarquardtHessian;
import org.apache.commons.math3.optim.nonlinear.linear.LevenbergMarquardtGradient;
import org.apache.commons.math3.optim.nonlinear.linear.LeastSquaresProblem;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testDiagonalWeightMatrixConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);

        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonDiagonalWeightMatrixConstructionThrowsException() {
        double[][] weights = {{1.0, 2.0}, {3.0, 4.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
    }

    @Test
    public void testSquareWeightMatrixConstruction() {
        double[][] weights = {{1.0, 0.0}, {0.0, 1.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        RealMatrix expectedWeight = new Array2DRowRealMatrix(2, 2);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 1.0);
        expectedWeight.setEntry(0, 1, 0.0);
        expectedWeight.setEntry(1, 0, 0.0);

        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightMatrixGetting() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);

        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightMatrixCopying() {
        double[][] weights = {{1.0, 0.0}, {0.0, 1.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        RealMatrix expectedWeight = new Array2DRowRealMatrix(2, 2);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 1.0);
        expectedWeight.setEntry(0, 1, 0.0);
        expectedWeight.setEntry(1, 0, 0.0);

        RealMatrix actualWeight = weight.getWeight().copy();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeWeightThrowsException() {
        double[] weights = {1.0, -2.0, 3.0};
        new Weight(weights);
    }
}