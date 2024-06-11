package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.DirectSearchOptimizer;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalConstructorDefault() {
        MultiDirectional optimizer = new MultiDirectional();
        assertNotNull(optimizer);
    }

    @Test
    public void testMultiDirectionalConstructorWithCustomCoefs() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        assertNotNull(optimizer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexThrowIllegalArgumentException() {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.simplex = null;
        optimizer.iterateSimplex(null);
    }

    @Test
    public void testIterateSimplexReflectedSimplexFails() throws OptimizationException, FunctionEvaluationException {
        MultiDirectional optimizer = new MultiDirectional();
        PointValueFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        RealPointValuePair[] simplex = {new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
                                       new RealPointValuePair(new double[]{1.0, 1.0}, 4.0),
                                       new RealPointValuePair(new double[]{-1.0, -1.0}, 3.0),
                                       new RealPointValuePair(new double[]{1.0, -1.0}, 2.0),
                                       new RealPointValuePair(new double[]{-1.0, 1.0}, 1.0)};
        optimizer.simplex = simplex;
        optimizer.comparator = null;
        optimizer.iterateSimplex(null);
    }

    @Test
    public void testIterateSimplexReflectedSimplexCorrect() throws OptimizationException, FunctionEvaluationException {
        MultiDirectional optimizer = new MultiDirectional();
        PointValueFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        RealPointValuePair[] simplex = {new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
                                       new RealPointValuePair(new double[]{1.0, 1.0}, 4.0),
                                       new RealPointValuePair(new double[]{-1.0, -1.0}, 3.0),
                                       new RealPointValuePair(new double[]{1.0, -1.0}, 2.0),
                                       new RealPointValuePair(new double[]{-1.0, 1.0}, 1.0)};
        optimizer.simplex = simplex;
        optimizer.comparator = new java.util.Collections.ReverseOrderComparator<>(new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        optimizer.iterateSimplex(optimizer.comparator);
        RealPointValuePair reflected = optimizer.simplex[1];
        RealPointValuePair expanded = optimizer.evaluateNewSimplex(optimizer.simplex, optimizer.khi, optimizer.comparator);
        assertTrue(optimizer.comparator.compare(reflected, expanded) <= 0);
    }

    @Test
    public void testIterateSimplexContractedSimplexFails() throws OptimizationException, FunctionEvaluationException {
        MultiDirectional optimizer = new MultiDirectional();
        PointValueFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        RealPointValuePair[] simplex = {new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
                                       new RealPointValuePair(new double[]{1.0, 1.0}, 4.0),
                                       new RealPointValuePair(new double[]{-1.0, -1.0}, 3.0),
                                       new RealPointValuePair(new double[]{1.0, -1.0}, 2.0),
                                       new RealPointValuePair(new double[]{-1.0, 1.0}, 1.0)};
        optimizer.simplex = simplex;
        optimizer.comparator = null;
        optimizer.iterateSimplex(null);
        optimizer.iterateSimplex(null);
        optimizer.iterateSimplex(null);
        RealPointValuePair contracted = optimizer.evaluateNewSimplex(optimizer.simplex, optimizer.gamma, optimizer.comparator);
        assertFalse(optimizer.comparator.compare(contracted, optimizer.simplex[0]) < 0);
    }

    @Test
    public void testIterateSimplexContractedSimplexCorrect() throws OptimizationException, FunctionEvaluationException {
        MultiDirectional optimizer = new MultiDirectional();
        PointValueFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        RealPointValuePair[] simplex = {new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
                                       new RealPointValuePair(new double[]{1.0, 1.0}, 4.0),
                                       new RealPointValuePair(new double[]{-1.0, -1.0}, 3.0),
                                       new RealPointValuePair(new double[]{1.0, -1.0}, 2.0),
                                       new RealPointValuePair(new double[]{-1.0, 1.0}, 1.0)};
        optimizer.simplex = simplex;
        optimizer.comparator = new java.util.Collections.ReverseOrderComparator<>(new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        optimizer.iterateSimplex(optimizer.comparator);
        optimizer.iterateSimplex(optimizer.comparator);
        optimizer.iterateSimplex(optimizer.comparator);
        RealPointValuePair contracted = optimizer.evaluateNewSimplex(optimizer.simplex, optimizer.gamma, optimizer.comparator);
        assertTrue(optimizer.comparator.compare(contracted, optimizer.simplex[0]) < 0);
    }

    @Test
    public void testEvaluateNewSimplexFunctionEvaluationException() throws OptimizationException, FunctionEvaluationException {
        MultiDirectional optimizer = new MultiDirectional();
        PointValueFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) throws FunctionEvaluationException {
                if (x[0] < 0) {
                    throw new FunctionEvaluationException("Error in function evaluation", null);
                }
                return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        RealPointValuePair[] simplex = {new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
                                       new RealPointValuePair(new double[]{1.0, 1.0}, 4.0),
                                       new RealPointValuePair(new double[]{-1.0, -1.0}, 3.0),
                                       new RealPointValuePair(new double[]{1.0, -1.0}, 2.0),
                                       new RealPointValuePair(new double[]{-1.0, 1.0}, 1.0)};
        optimizer.simplex = simplex;
        optimizer.comparator = null;
        try {
            optimizer.evaluateNewSimplex(optimizer.simplex, 1.0, optimizer.comparator);
            fail();
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateNewSimplexMaxEvalExceededException() throws OptimizationException, FunctionEvaluationException {
        MultiDirectional optimizer = new MultiDirectional();
        PointValueFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) throws FunctionEvaluationException {
                return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        RealPointValuePair[] simplex = {new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
                                       new RealPointValuePair(new double[]{1.0, 1.0}, 4.0),
                                       new RealPointValuePair(new double[]{-1.0, -1.0}, 3.0),
                                       new RealPointValuePair(new double[]{1.0, -1.0}, 2.0),
                                       new RealPointValuePair(new double[]{-1.0, 1.0}, 1.0)};
        optimizer.simplex = simplex;
        optimizer.comparator = null;
        optimizer.setMaxEvaluations(1);
        try {
            optimizer.iterateSimplex(optimizer.comparator);
            fail();
        } catch (MaxEvalExceededException e) {
            // expected
        }
    }

    @Test
    public void testIterateSimplexNoBracketingException() throws OptimizationException, FunctionEvaluationException {
        MultiDirectional optimizer = new MultiDirectional();
        PointValueFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        RealPointValuePair[] simplex = {new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
                                       new RealPointValuePair(new double[]{1.0, 1.0}, 4.0),
                                       new RealPointValuePair(new double[]{-1.0, -1.0}, 3.0),
                                       new RealPointValuePair(new double[]{1.0, -1.0}, 2.0),
                                       new RealPointValuePair(new double[]{-1.0, 1.0}, 1.0)};
        optimizer.simplex = simplex;
        optimizer.comparator = null;
        optimizer.setMaxEvaluations(10000);
        try {
            optimizer.setMinimizer(new InitialGuess(new double[]{0.1, 0.1}));
            optimizer.setBounds(new SimpleBounds(-5.0, 5.0));
            optimizer.iterateSimplex(optimizer.comparator);
            fail();
        } catch (NoBracketingException e) {
            // expected
        }
    }

}