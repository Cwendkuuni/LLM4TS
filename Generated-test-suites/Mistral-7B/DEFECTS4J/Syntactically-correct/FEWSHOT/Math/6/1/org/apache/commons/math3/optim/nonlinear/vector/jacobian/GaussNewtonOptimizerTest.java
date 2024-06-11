package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonMethod;

public class GaussNewtonOptimizerTest extends UnconstrainedOptimizationTest {

    private static final double[][] DATA = {
            {1.0, 2.0, 3.0},
            {2.0, 3.0, 4.0},
            {3.0, 4.0, 5.0}
    };

    private static final double[] TARGET = {5.0, 6.0, 7.0};

    private static final double[] START_POINT = {1.0, 1.0, 1.0};

    private static final RealMatrix WEIGHT = new SingularValueDecomposition(new BlockRealMatrix(DATA)).getS()
            .multiply(new BlockRealMatrix(DATA.transpose()).getTranspose());

    private GaussNewtonOptimizer optimizer;

    @Override
    protected PointValuePair optimize() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setFunction(this::function);
        optimizer.setStartPoint(START_POINT);
        optimizer.initialize();
        return optimizer.optimize();
    }

    @Override
    protected void checkSolution(PointValuePair solution) {
        assertArrayEquals(TARGET, ((RealVector) solution.getPoint()).toArray(), 1e-10);
        assertEquals(0.0, optimizer.getCost(), 1e-10);
    }

    @Test
    public void testGaussNewtonOptimizer() {
        optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullConvergenceChecker() {
        new GaussNewtonOptimizer(true, null);
    }

    @Test
    public void testLUDecomposition() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setFunction(this::functionWithLU);
        optimizer.setStartPoint(START_POINT);
        optimizer.initialize();
        PointVectorValuePair optimalSolution = optimizer.optimize();
        assertEquals(0.0, optimizer.getCost(), 1e-10);
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setFunction(this::functionWithLU);
        optimizer.setStartPoint(START_POINT);
        optimizer.initialize();
        optimalSolution = optimizer.optimize();
        assertEquals(0.0, optimizer.getCost(), 1e-10);
    }

    @Test
    public void testQRDecomposition() {
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setFunction(this::functionWithQR);
        optimizer.setStartPoint(START_POINT);
        optimizer.initialize();
        PointVectorValuePair optimalSolution = optimizer.optimize();
        assertEquals(0.0, optimizer.getCost(), 1e-10);
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setFunction(this::functionWithQR);
        optimizer.setStartPoint(START_POINT);
        optimizer.initialize();
        optimalSolution = optimizer.optimize();
        assertEquals(0.0, optimizer.getCost(), 1e-10);
    }

    @Test(expected = ConvergenceException.class)
    public void testSingularMatrixException() {
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setFunction(this::functionWithSingularMatrix);
        optimizer.setStartPoint(START_POINT);
        optimizer.initialize();
        optimizer.optimize();
    }

    @Test
    public void testInitFunctionCalledOnce() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setFunction(this::function);
        optimizer.setStartPoint(START_POINT);
        optimizer.initialize();
        optimizer.setFunction(this::functionWithLU);
        optimizer.initialize();
        assertTrue(optimizer.isInitialized());
    }

    private double function(double[] x) {
        RealMatrix J = computeJacobian(x);
        RealMatrix weight = getWeight();
        RealMatrix weightJ = J.transpose().multiply(weight);
        RealVector b = weight.multiply(J.getColumnVector(0));
        return Math.pow(Math.max(b.getEntry(0), 0), 2) + Math.pow(Math.max(b.getEntry(1), 0), 2)
                + Math.pow(Math.max(b.getEntry(2), 0), 2);
    }

    private double[] computeObjectiveValue(double[] x) {
        return new double[]{function(x), function(x), function(x)};
    }

    private double[] computeResiduals(double[] objectiveValues) {
        double[] target = TARGET;
        double[] residuals = new double[target.length];
        for (int i = 0; i < target.length; i++) {
            residuals[i] = target[i] - objectiveValues[i];
        }
        return residuals;
    }

    private RealMatrix computeWeightedJacobian(double[] x) {
        RealMatrix J = computeJacobian(x);
        RealMatrix weight = getWeight();
        return J.multiply(weight);
    }

    private double[] computeCost(double[] residuals) {
        double cost = 0.0;
        for (int i = 0; i < residuals.length; i++) {
            cost += Math.pow(Math.abs(residuals[i]), 2);
        }
        return cost;
    }

    private RealMatrix computeJacobian(double[] x) {
        double[][] jacobian = new double[DATA.length][3];
        for (int i = 0; i < DATA.length; i++) {
            double[] grad = jacobian[i] = new double[3];
            double[] dataRow = DATA[i];
            for (int j = 0; j < 3; j++) {
                double[] xTemp = new double[3];
                System.arraycopy(x, 0, xTemp, 0, 3);
                xTemp[j] += 0.0001;
                double[] dataRowTemp = DATA[i];
                double difference = Math.abs(dataRowTemp[j] - dataRow[j]);
                grad[j] = (dataRowTemp[0] - dataRow[0]) / difference;
                grad[j] += (dataRowTemp[1] - dataRow[1]) / difference;
                grad[j] += (dataRowTemp[2] - dataRow[2]) / difference;
            }
        }
        return new BlockRealMatrix(jacobian);
    }

    private double functionWithLU(double[] x) {
        return function(x);
    }

    private double[] functionWithQR(double[] x) {
        return computeObjectiveValue(x);
    }

    private double functionWithSingularMatrix(double[] x) {
        RealMatrix weight = getWeight();
        RealMatrix weightMatrix = new BlockRealMatrix(weight).inverse();
        return Math.sqrt(weightMatrix.determinant()) / Math.sqrt(Math.abs(weightMatrix.getEntry(0, 0)));
    }

    private RealMatrix getWeight() {
        return WEIGHT;
    }
}