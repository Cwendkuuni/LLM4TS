package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AveragedStartingPoint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleGradientDescent;
import org.apache.commons.math3.optim.nonlinear.vector.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.vector.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.LinearVectorConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.VectorValuePair;

import java.util.Arrays;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testOptimize() throws OptimizationException, FunctionEvaluationException {
        int columns = 2;
        int rows = 3;

        // Set up test problem data
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] target = {2.0, 4.0};
        double[] weights = {1.0, 2.0};
        double[] startPoint = {1.0, 1.0};

        DifferentiableMultivariateVectorialFunction objectiveFunction = new LinearObjectiveFunction(data, target);
        MultivariateMatrixFunction jacobianFunction = new LinearObjectiveFunction(data, target).jacobian();

        // Create AbstractLeastSquaresOptimizer instance
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                SimpleGradientDescent solver = new SimpleGradientDescent();
                solver.setMaxIterations(100);
                solver.setMaxEvaluations(100);
                return solver.optimize(objectiveFunction, startPoint, null);
            }
        };

        // Set optimizer properties
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(20);

        // Run optimization
        VectorialPointValuePair result = optimizer.optimize(objectiveFunction, target, weights, startPoint);

        // Check results
        double[] actualResult = result.getPoint();
        double[] expectedResult = new double[]{1.5, 2.5}; // Expected solution based on given data and weights
        assertEquals(expectedResult.length, actualResult.length);
        Arrays.equals(expectedResult, actualResult);
        assertEquals(expectedResult[0], actualResult[0], 0.001);
        assertEquals(expectedResult[1], actualResult[1], 0.001);

        // Check convergence
        assertTrue(optimizer.getIterations() >= 10);
        assertTrue(optimizer.getEvaluations() >= 20);
        assertTrue(optimizer.getCost() < 1.0e-5);

        // Check jacobian
        double[][] jacobian = (double[][]) jacobianFunction.value(result.getPoint());
        assertEquals(rows, jacobian.length);
        assertEquals(columns, jacobian[0].length);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertEquals(0.0, jacobian[i][j], 0.001);
            }
        }

        // Test constraints
        LinearConstraints constraints = new LinearConstraints(new LinearVectorConstraint[]{
                new LinearVectorConstraint(new RealVector[]{1.0, 1.0}, 0.0),
                new LinearVectorConstraint(new RealVector[]{1.0, 2.0}, 1.0),
                new LinearVectorConstraint(new RealVector[]{2.0, 1.0}, 0.0),
                new LinearVectorConstraint(new RealVector[]{2.0, 2.0}, 1.0)
        });
        GoalType goalType = GoalType.MINIMIZE;
        ObjectiveFunction objectiveFunctionWithConstraints = new LinearObjectiveFunction(data, target);
        Constraint[] constraintsArray = constraints.getConstraints();
        OptimizationProblem problem = new OptimizationProblem(objectiveFunctionWithConstraints, startPoint, constraintsArray, goalType);
        OptimizationResult optimizationResult = new AveragedStartingPoint(problem).optimize();

        // Check if the solution from the AbstractLeastSquaresOptimizer is close to the solution from the OptimizationProblem
        double error = 0.0;
        for (int i = 0; i < columns; i++) {
            error += Math.pow(Math.abs(optimizationResult.getPoint()[i] - result.getPoint()[i]), 2);
        }
        assertEquals(0.0, Math.sqrt(error), 0.01);
    }
}