package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BFGS;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleXSQP;
import org.junit.Test;
import org.junit.Assert.*;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.optimization.nonlinear.scalar.*;
import org.apache.commons.math3.optimization.nonlinear.scalar.noderiv.*;

public class WeightTest {

    @Test
    public void testWeightConstruction() {
        // Test diagonal weight matrix construction
        double[] weights = {1.0, 2.0, 3.0};
        Weight w = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        RealMatrix weight = w.getWeight();
        assertEquals(expected, weight);

        // Test weight matrix construction
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 2.0);
        weightMatrix.setEntry(2, 2, 3.0);
        Weight w2 = new Weight(weightMatrix);
        weight = w2.getWeight();
        assertEquals(expected, weight);
    }

    @Test
    public void testNonSquareWeightMatrixConstruction() {
        // Test non-square matrix exception
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 2);
        try {
            new Weight(weightMatrix);
            fail();
        } catch (NonSquareMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testWeightUsage() {
        // Test diagonal weight matrix usage
        double[] weights = {1.0, 2.0, 3.0};
        Weight w = new Weight(weights);
        RealMatrix weight = w.getWeight();

        ObjectiveFunction f = new QuadraticFunction(-1.0, new RealMatrix(new Array2DRowRealMatrix(3,3).add(weight)));
        SimpleBounds bounds = new SimpleBounds(new RealPointValuePair(new Array2DRowRealVector(3, -10.0), new Array2DRowRealVector(3, 10.0)), null);

        GoalType goalType = GoalType.MINIMIZE;
        InitialGuess initialGuess = new Array2DRowRealVector(3, 0.0);

        BFGS bfgs = new BFGS();
        PointValuePair solution = bfgs.optimize(f, bounds, goalType, initialGuess, new MaxEval(500));
        System.out.println(solution);

        // Test weight matrix usage
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 2.0);
        weightMatrix.setEntry(2, 2, 3.0);
        Weight w2 = new Weight(weightMatrix);
        weight = w2.getWeight();

        ObjectiveFunction f2 = new QuadraticFunction(-1.0, new RealMatrix(new Array2DRowRealMatrix(3,3).add(weight)));
        SimpleBounds bounds2 = new SimpleBounds(new RealPointValuePair(new Array2DRowRealVector(3, -10.0), new Array2DRowRealVector(3, 10.0)), null);

        GoalType goalType2 = GoalType.MINIMIZE;
        InitialGuess initialGuess2 = new Array2DRowRealVector(3, 0.0);

        NelderMeadSimplex simp = new NelderMeadSimplex();
        PointValuePair solution2 = simp.optimize(f2, bounds2, goalType2, initialGuess2, new MaxEval(500));
        System.out.println(solution2);

        // Test weight usage with SimpleXSQP
        ObjectiveFunction f3 = new QuadraticFunction(-1.0, new RealMatrix(new Array2DRowRealMatrix(3,3).add(weight)));
        SimpleBounds bounds3 = new SimpleBounds(new RealPointValuePair(new Array2DRowRealVector(3, -10.0), new Array2DRowRealVector(3, 10.0)), null);

        GoalType goalType3 = GoalType.MINIMIZE;
        InitialGuess initialGuess3 = new Array2DRowRealVector(3, 0.0);

        SimpleXSQP simpleXSQP = new SimpleXSQP();
        PointValuePair solution3 = simpleXSQP.optimize(f3, bounds3, goalType3, initialGuess3, new MaxEval(500));
        System.out.println(solution3);
    }
}