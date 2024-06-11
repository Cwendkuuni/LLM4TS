import org.apache.commons.math3.optim.initializemodels.InitialGuess;
import org.apache.commons.math3.optim.maximization.MaxEval;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.linear.GoalType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.linear.LsqLinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.linear.LsqLinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.linear.LsqLinConstraint;
import org.apache.commons.math3.optim.nonlinear.linear.LsqLinObjectiveFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.stats.Statistics;
import org.apache.commons.math3.optim.initializemodels.InitialGuessFactory;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WeightTest {

    // Test for the constructor that takes a double[] as argument

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);
        RealMatrix actualWeight = weight.getWeight();
        assertNotNull(actualWeight);
        assertEquals(expectedWeight, actualWeight);
    }

    // Test for the constructor that takes a RealMatrix as argument

    @Test
    public void testWeightMatrixConstructor() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}, {0.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(4, 2, weightMatrixData);
        Weight weight = new Weight(weightMatrix);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(4, 4).setIdentityMatrix(4);
        expectedWeight = expectedWeight.multiply(2);
        RealMatrix actualWeight = weight.getWeight();
        assertNotNull(actualWeight);
        assertEquals(expectedWeight, actualWeight);
    }

    // Test for IllegalArgumentException when the weightMatrix is not square

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixConstructor_squareMatrixException() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3, weightMatrixData);
        new Weight(weightMatrix);
    }

    // Test for getWeight() method

    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);
        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }
}