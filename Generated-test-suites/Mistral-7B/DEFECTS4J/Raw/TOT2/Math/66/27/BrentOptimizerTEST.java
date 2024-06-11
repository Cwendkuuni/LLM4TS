package org.apache.commons.math.optimization.univariate;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergenceException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.MathProgram;
import org.apache.commons.math3.optim.nonlinear.constraints.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.constraints.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.constraints.LinearEqualityConstraint;
import org.apache.commons.math3.optim.nonlinear.constraints.LinearInequalityConstraint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BrentOptimizerTest {

    private UnivariateOptimizer optimizer;
    private UnivariateRealFunction testFunction;

    @BeforeEach
    public void setup() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testSimpleMinimization() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        assertEquals(-2.5, result.getPoint());
        assertEquals(0.0, result.getValue(), 1e-10);
    }

    @Test
    public void testSimpleMaximization() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        assertEquals(2.5, result.getPoint());
        assertEquals(0.0, result.getValue(), 1e-10);
    }

    @Test
    public void testGoalTypeMaximization() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.exp(x);
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -Math.PI, Math.PI, 0.0);
        assertEquals(Math.PI, result.getPoint(), 1e-10);
        assertEquals(Math.E * Math.sin(Math.PI), result.getValue(), 1e-10);
    }

    @Test
    public void testEdgeCaseMinimization() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 1.0), 2) / (Math.abs(x - 1.0) + 1.0);
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 1.0);
        assertEquals(1.0, result.getPoint(), 1e-10);
        assertEquals(0.0, result.getValue(), 1e-10);
    }

    @Test
    public void testEdgeCaseMaximization() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x + 1.0), 2) / (Math.abs(x + 1.0) + 1.0);
            }
        };
        PointValuePair result = optimizer.optimizer(optimizer, testFunction, GoalType.MAXIMIZE, -5.0, 5.0, 1.0);
        assertEquals(1.0, result.getPoint(), 1e-10);
        assertEquals(0.0, result.getValue(), 1e-10);
    }

    @Test
    public void testExceptionHandling() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws NonConvergenceException, FunctionEvaluationException {
                throw new FunctionEvaluationException("Failed to evaluate function.");
            }
        };
        try {
            optimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
            fail();
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testConstraints() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.exp(x) - 2.71828;
            }
        };

        LinearConstraints constraints = new LinearConstraints();
        constraints.addConstraint(new LinearEqualityConstraint(new double[] {1.0}, 1.0));
        constraints.addConstraint(new LinearInequalityConstraint(new double[] {1.0}, 2.0));

        MathProgram program = new MathProgram();
        program.setObjectiveFunction(testFunction);
        program.setConstraints(constraints);

        optimizer = new BrentOptimizer();
        optimizer.setInitialGuess(new AroundMinimumInitializer(optimizer, testFunction, 0.0, 1.0).getInitialPoint());

        PointValuePair result = optimizer.optimize(program);
        assertEquals(1.0, result.getPoint(), 1e-10);
        assertEquals(-0.71828, result.getValue(), 1e-10);
    }
}