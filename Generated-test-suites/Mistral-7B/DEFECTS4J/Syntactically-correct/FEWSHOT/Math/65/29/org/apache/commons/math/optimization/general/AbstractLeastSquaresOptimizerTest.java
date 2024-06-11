package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.PointValues;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearEqualityConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearInequalityConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.linearRegression.LinearRegressionConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.linearRegression.LinearRegressionFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.linearRegression.LinearRegressionInitialGuess;
import org.apache.commons.math3.optim.nonlinear.vector.linear.linearRegression.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.point.RealVector;
import org.apache.commons.math3.optim.point.SingularValueDecomposition;
import org.apache.commons.math3.optim.point.SingularValueDecompositionImpl;
import org.apache.commons.math3.optim.point.SingularValueDecompositionSolver;
import org.apache.commons.math3.optim.point.SingularValues;
import org.apache.commons.math3.optim.point.VectorValuePair;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        // Test constructor with default settings
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSettersAndGetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        // Test setting max iterations
        optimizer.setMaxIterations(50);
        assertEquals(50, optimizer.getMaxIterations());

        // Test setting convergence checker
        optimizer.setConvergenceChecker(new VectorialValueChecker());
        assertEquals(new VectorialValueChecker(), optimizer.getConvergenceChecker());

        // Test setting max evaluations
        optimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());

        // Test checking iterations
        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
        assertEquals(2, optimizer.getIterations());

        // Test checking evaluations
        optimizer.updateResidualsAndCost();
        optimizer.updateResidualsAndCost();
        assertEquals(2, optimizer.getEvaluations());

        // Test checking jacobian evaluations
        try {
            optimizer.updateJacobian();
        } catch (FunctionEvaluationException e) {
            // expected
        }
        optimizer.updateJacobian();
        assertEquals(2, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testGetRMS() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        double[] objective = new double[target.length];
        double[] residuals = new double[target.length];

        optimizer.optimize(new LinearRegressionFunction(target, startPoint), target, weights, startPoint);
        optimizer.updateResidualsAndCost();

        assertEquals(Math.sqrt(3.0), optimizer.getRMS(), 1e-6);
    }

    @Test
    public void testGetChiSquare() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};

        optimizer.optimize(new LinearRegressionFunction(target, startPoint), target, weights, startPoint);
        optimizer.updateResidualsAndCost();

        assertEquals(3.0, optimizer.getChiSquare(), 1e-6);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};

        optimizer.optimize(new LinearRegressionFunction(target, startPoint), target, weights, startPoint);
        optimizer.updateJacobian();

        double[][] covariances = optimizer.getCovariances();

        // Singular value decomposition
        SingularValueDecomposition solver = new SingularValueDecompositionImpl(MatrixUtils.createRealMatrix(optimizer.getJacobian()));
        SingularValueDecompositionSolver svdsolver = solver.getSolver();
        SingularValues sValues = solver.getSingularValues();
        double[] sigmas = sValues.getSingularValues();

        // Check diagonal elements
        for (int i = 0; i < covariances.length; i++) {
            assertEquals(sigmas[i] * sigmas[i], covariances[i][i], 1e-6);
        }

        // Check off-diagonal elements
        for (int i = 0; i < covariances.length; i++) {
            for (int j = 0; j < covariances[i].length; j++) {
                if (i != j) {
                    assertEquals(0.0, covariances[i][j], 1e-6);
                }
            }
        }
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};

        optimizer.optimize(new LinearRegressionFunction(target, startPoint), target, weights, startPoint);
        optimizer.updateJacobian();

        double[] errors = optimizer.guessParametersErrors();

        // Check square roots of singular values
        SingularValueDecomposition solver = new SingularValueDecompositionImpl(MatrixUtils.createRealMatrix(optimizer.getJacobian()));
        SingularValueDecompositionSolver svdsolver = solver.getSolver();
        SingularValues sValues = solver.getSingularValues();
        double[] sigmas = sValues.getSingularValues();

        for (int i = 0; i < errors.length; i++) {
            assertEquals(Math.sqrt(sigmas[i]), errors[i], 1e-6);
        }
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        // Test optimization with default initial guess
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};

        PointValuePair result = optimizer.optimize(new LinearRegressionFunction(target, startPoint), target, weights, startPoint);

        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertEquals(target.length, result.getPoint().length);

        // Test optimization with custom initial guess
        double[] customStartPoint = {1.1, 1.1, 1.1};
        PointValuePair customResult = optimizer.optimize(new LinearRegressionFunction(target, customStartPoint), target, weights, customStartPoint);

        assertEquals(GoalType.MINIMIZE, customResult.getGoalType());
        assertEquals(target.length, customResult.getPoint().length);

        // Test optimization with constraints
        double[][] A = new double[][] {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        double[] b = new double[] {0.0, 0.0, 0.0};
        ObjectiveFunction objectiveFunction = new LinearObjectiveFunction(target);

        LinearEqualityConstraint eqConstraint = new LinearEqualityConstraint(A, b);
        LinearInequalityConstraint ineqConstraint = new LinearInequalityConstraint(A, b);
        LinearConstraint[] constraints = new LinearConstraint[] {eqConstraint, ineqConstraint};

        PointValues pointValues = new PointValues();

        LevenbergMarquardtOptimizer optimizerWithConstraints = new LevenbergMarquardtOptimizer(objectiveFunction, constraints);
        PointValuePair constraintResult = optimizerWithConstraints.optimize(new LinearRegressionInitialGuess(target), target, PointValues.INTERVAL, PointValues.CLOSED);

        assertEquals(GoalType.MINIMIZE, constraintResult.getGoalType());
        assertEquals(target.length, constraintResult.getPoint().length);
    }
}

class VectorialValueChecker implements VectorialConvergenceChecker {
    @Override
    public boolean isConverged(RealVector x, RealVector fx, double tolerance) {
        return x.getNorm().getValue() <= tolerance;
    }
}

class VectorialPointValuePair implements PointValuePair<RealVector, double[]> {
    private RealVector point;
    private double[] value;

    public VectorialPointValuePair(RealVector point, double[] value) {
        this.point = point;
        this.value = value;
    }

    @Override
    public RealVector getPoint() {
        return point;
    }

    @Override
    public double[] getValue() {
        return value;
    }

    @Override
    public GoalType getGoalType() {
        return GoalType.MINIMIZE;
    }
}

class VectorialPointValuePairCollection implements PointValuePairCollection<RealVector, double[]> {
    private PointValuePair[] pointValuePairs;

    public VectorialPointValuePairCollection(int size) {
        pointValuePairs = new PointValuePair[size];
    }

    @Override
    public void add(PointValuePair<RealVector, double[]> p) {
        pointValuePairs[pointValuePairs.length - 1] = p;
    }

    @Override
    public PointValuePair<RealVector, double[]> get(int index) {
        return pointValuePairs[index];
    }

    @Override
    public int size() {
        return pointValuePairs.length;
    }
}