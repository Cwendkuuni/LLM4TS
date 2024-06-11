package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.goaltype.GoalType;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealFunctionAdapter;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateRealFunction function = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.5});
        BrentOptimizer optimizer = new BrentOptimizer();

        double min = -3.0;
        double max = 3.0;
        double startValue = 0.0;
        double goalValue = 0.0;
        GoalType goalType = GoalType.MINIMIZE;

        try {
            PointValuePair result = optimizer.optimize(new UnivariateRealFunctionAdapter(function), goalType, min, max, startValue);
            double optimum = result.getPoint();
            double objectiveValue = result.getValue();
            assertEquals("The optimizer did not find the correct minimum", 1.5, objectiveValue, 1e-6);
            assertEquals("The optimizer did not find the correct minimum", 1.0, optimum, 1e-6);
        } catch (MaxIterationsExceededException | NotStrictlyPositiveException | FunctionEvaluationException e) {
            fail("An error occurred during optimization: " + e.getMessage());
        }

        try {
            PointValuePair result = optimizer.optimize(new UnivariateRealFunctionAdapter(new PolynomialFunction(2, new double[]{-1.0, -1.0, 1.0})), GoalType.MAXIMIZE, min, max, startValue);
            double optimum = result.getPoint();
            double objectiveValue = result.getValue();
            assertEquals("The optimizer did not find the correct maximum", -1.0, objectiveValue, 1e-6);
            assertEquals("The optimizer did not find the correct maximum", -1.0, optimum, 1e-6);
        } catch (MaxIterationsExceededException | NotStrictlyPositiveException | FunctionEvaluationException e) {
            fail("An error occurred during optimization: " + e.getMessage());
        }

        try {
            PointValuePair result = optimizer.optimize(new UnivariateRealFunctionAdapter(new UnivariateRealFunction() {
                @Override
                public double value(double x) throws FunctionEvaluationException {
                    return Math.log(Math.abs(x) + 1.0) + E.E;
                }
            }), GoalType.MINIMIZE, min, max, startValue);
            double optimum = result.getPoint();
            double objectiveValue = result.getValue();
            assertEquals("The optimizer did not find the correct minimum", 0.0, objectiveValue, 1e-6);
            assertEquals("The optimizer did not find the correct minimum", 0.0, optimum, 1e-6);
        } catch (MaxIterationsExceededException | NotStrictlyPositiveException | FunctionEvaluationException e) {
            fail("An error occurred during optimization: " + e.getMessage());
        }

        try {
            PointValuePair result = optimizer.optimize(new UnivariateRealFunctionAdapter(new UnivariateRealFunction() {
                @Override
                public double value(double x) throws FunctionEvaluationException {
                    throw new FunctionEvaluationException("Function evaluation exception");
                }
            }), GoalType.MINIMIZE, min, max, startValue);
            fail("An optimization was performed with a function that throws an exception");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        try {
            PointValuePair result = optimizer.optimize(new UnivariateRealFunctionAdapter(new UnivariateRealFunction() {
                @Override
                public double value(double x) throws FunctionEvaluationException {
                    throw new MaxIterationsExceededException("Max iterations exceeded exception");
                }
            }), GoalType.MINIMIZE, min, max, startValue);
            fail("An optimization was performed with a function that throws an exception");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        try {
            PointValuePair result = optimizer.optimize(new UnivariateRealFunctionAdapter(new UnivariateRealFunction() {
                @Override
                public double value(double x) throws FunctionEvaluationException {
                    throw new NotStrictlyPositiveException("Not strictly positive exception");
                }
            }), GoalType.MINIMIZE, min, max, startValue);
            fail("An optimization was performed with a function that throws an exception");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerWithBounds() {
        UnivariateRealFunction function = new PolynomialFunction(3, new double[]{1.0, -2.0, 1.0, 1.0});
        BrentOptimizer optimizer = new BrentOptimizer();

        double min = -5.0;
        double max = 5.0;
        double startValue = 0.0;
        double goalValue = 0.0;
        GoalType goalType = GoalType.MINIMIZE;

        SimpleBounds bounds = new SimpleBounds(1.0, 4.0);

        try {
            PointValuePair result = optimizer.optimize(new UnivariateRealFunctionAdapter(function), goalType, min, max, startValue, bounds);
            double optimum = result.getPoint();
            double objectiveValue = result.getValue();
            assertEquals("The optimizer did not find the correct minimum", 1.5, objectiveValue, 1e-6);
            assertEquals("The optimizer did not find the correct minimum", 1.0, optimum, 1e-6);
        } catch (MaxIterationsExceededException | NotStrictlyPositiveException | FunctionEvaluationException e) {
            fail("An error occurred during optimization: " + e.getMessage());
        }
    }
}