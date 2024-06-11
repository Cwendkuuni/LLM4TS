package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.goaltype.AveragingGoalType;
import org.apache.commons.math3.optim.initialguess.RealPointValuePairInitialGuess;
import org.apache.commons.math3.optim.point.RealPointValuePairList;
import org.apache.commons.math3.optim.point.RealVector;
import org.apache.commons.math3.optim.simple.FunctionValue;
import org.apache.commons.math3.optim.simple.FunctionWithGradient;
import org.apache.commons.math3.optim.simple.Gradient;
import org.apache.commons.math3.optim.simple.NoSolutionException;
import org.apache.commons.math3.optim.simple.Problem;
import org.apache.commons.math3.optim.simple.StoppingCondition;
import org.junit.Test;

public class MultiDirectionalTest {
    @Test
    public void testIterateSimplex() {
        // Test with defaults
        MultiDirectional md = new MultiDirectional();

        Comparator<RealPointValuePair> comparator = mock(Comparator.class);
        when(comparator.compare(RealPointValuePair.MIN_VALUE, RealPointValuePair.MIN_VALUE)).thenReturn(-1);

        RealPointValuePairInitialGuess initialGuess = mock(RealPointValuePairInitialGuess.class);
        when(initialGuess.getInitialPoint()).thenReturn(new RealVector(new double[] {0.0, 0.0}));

        Problem problem = new Problem() {
            @Override
            public FunctionValue evaluate(RealVector point) {
                return new FunctionValue(Math.pow(Math.sin(point.getEntry(0)) * Math.sin(point.getEntry(1)), 2));
            }

            @Override
            public String getName() {
                return "Test Problem";
            }

            @Override
            public RealPointValuePair getInitialGuess() {
                return initialGuess.getInitialPoint();
            }

            @Override
            public AveragingGoalType getGoalType() {
                return null;
            }

            @Override
            public StoppingCondition getStoppingCondition() {
                return null;
            }
        };

        MultiDirectional mdInstance = new MultiDirectional(md);
        mdInstance.setProblem(problem);
        mdInstance.setInitialGuess(initialGuess);
        mdInstance.setFunctionEvaluator(mdInstance);
        mdInstance.setOptimizer(mdInstance);
        mdInstance.setIterations(1);
        mdInstance.setMaxEvaluations(1);
        mdInstance.setComparator(comparator);

        try {
            mdInstance.optimize();
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        }

        // Test with user-defined coefficients
        MultiDirectional mdUserDefined = new MultiDirectional(3.0, 0.5);

        RealPointValuePairList simplexList = new RealPointValuePairList(new double[][] {{0.0, 0.0}, {1.0, 1.0},
                {2.0, 2.0}}, new double[] {0.0, 1.1, 2.2});

        when(comparator.compare(RealPointValuePair.MIN_VALUE, simplexList.getVector(0))).thenReturn(-1);

        Problem problemUserDefined = new Problem() {
            @Override
            public FunctionValue evaluate(RealVector point) {
                return new FunctionValue(Math.pow(Math.sin(point.getEntry(0)) * Math.sin(point.getEntry(1)), 2) + 1);
            }

            @Override
            public String getName() {
                return "Test Problem";
            }

            @Override
            public RealPointValuePair getInitialGuess() {
                return new RealPointValuePair(new double[] {0.0, 0.0}, 0.0);
            }

            @Override
            public AveragingGoalType getGoalType() {
                return null;
            }

            @Override
            public StoppingCondition getStoppingCondition() {
                return null;
            }
        };

        MultiDirectional mdUserDefinedInstance = new MultiDirectional(mdUserDefined);
        mdUserDefinedInstance.setProblem(problemUserDefined);
        mdUserDefinedInstance.setInitialGuess(new RealPointValuePair(new double[] {0.0, 0.0}, 0.0));
        mdUserDefinedInstance.setFunctionEvaluator(mdUserDefinedInstance);
        mdUserDefinedInstance.setOptimizer(mdUserDefinedInstance);
        mdUserDefinedInstance.setIterations(1);
        mdUserDefinedInstance.setMaxEvaluations(1);
        mdUserDefinedInstance.setComparator(comparator);
        mdUserDefinedInstance.simplex = simplexList.getVector();

        try {
            mdUserDefinedInstance.optimize();
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        }

        // Test convergence
        RealPointValuePairList convergedSimplexList = new RealPointValuePairList(new double[][] {{1.0, 1.0},
                {1.2, 1.2}, {1.4, 1.4}}, new double[] {1.1, 1.3, 1.5});

        when(comparator.compare(RealPointValuePair.MIN_VALUE, convergedSimplexList.getVector(0))).thenReturn(0);

        Problem problemConverged = new Problem() {
            @Override
            public FunctionValue evaluate(RealVector point) {
                return new FunctionValue(Math.pow(Math.sin(point.getEntry(0)) * Math.sin(point.getEntry(1)), 2));
            }

            @Override
            public String getName() {
                return "Test Problem";
            }

            @Override
            public RealPointValuePair getInitialGuess() {
                return new RealPointValuePair(new double[] {0.0, 0.0}, 0.0);
            }

            @Override
            public AveragingGoalType getGoalType() {
                return null;
            }

            @Override
            public StoppingCondition getStoppingCondition() {
                return new RealConvergenceChecker(1e-8, 1e-8);
            }
        };

        MultiDirectional mdConvergedInstance = new MultiDirectional(md);
        mdConvergedInstance.setProblem(problemConverged);
        mdConvergedInstance.setInitialGuess(new RealPointValuePair(new double[] {0.0, 0.0}, 0.0));
        mdConvergedInstance.setFunctionEvaluator(mdConvergedInstance);
        mdConvergedInstance.setOptimizer(mdConvergedInstance);
        mdConvergedInstance.setIterations(1);
        mdConvergedInstance.setMaxEvaluations(1);
        mdConvergedInstance.setComparator(comparator);
        mdConvergedInstance.simplex = convergedSimplexList.getVector();

        try {
            mdConvergedInstance.optimize();
            assertTrue(mdConvergedInstance.isOptimized());
        } catch (OptimizationException e) {
            fail("Unexpected optimization exception: " + e.getMessage());
        }

        // Test reflection
        RealPointValuePairList reflectedSimplexList = new RealPointValuePairList(new double[][] {{0.0, 0.0}, {1.0, 1.0},
                {2.0, 2.0}}, new double[] {0.0, 1.1, 2.2});

        when(comparator.compare(RealPointValuePair.MIN_VALUE, reflectedSimplexList.getVector(0))).thenReturn(-1);

        Problem problemReflected = new Problem() {
            @Override
            public FunctionValue evaluate(RealVector point) {
                return new FunctionValue(Math.pow(Math.sin(point.getEntry(0)) * Math.sin(point.getEntry(1)), 2) + 1);
            }

            @Override
            public String getName() {
                return "Test Problem";
            }

            @Override
            public RealPointValuePair getInitialGuess() {
                return new RealPointValuePair(new double[] {0.0, 0.0}, 0.0);
            }

            @Override
            public AveragingGoalType getGoalType() {
                return null;
            }

            @Override
            public StoppingCondition getStoppingCondition() {
                return null;
            }
        };

        MultiDirectional mdReflectedInstance = new MultiDirectional(md);
        mdReflectedInstance.setProblem(problemReflected);
        mdReflectedInstance.setInitialGuess(new RealPointValuePair(new double[] {0.0, 0.0}, 0.0));
        mdReflectedInstance.setFunctionEvaluator(mdReflectedInstance);
        mdReflectedInstance.setOptimizer(mdReflectedInstance);
        mdReflectedInstance.setIterations(1);
        mdReflectedInstance.setMaxEvaluations(1);
        mdReflectedInstance.setComparator(comparator);
        mdReflectedInstance.simplex = reflectedSimplexList.getVector();

        try {
            mdReflectedInstance.optimize();
            assertTrue(mdReflectedInstance.isOptimized());
            assertArrayEquals(new double[] {1.0, 1.0}, mdReflectedInstance.getOptimum().getPointRef(), 0.0);
            assertEquals(Math.pow(Math.sin(1.0) * Math.sin(1.0), 2) + 1, mdReflectedInstance.getOptimum().getValue(),
                        1e-6);
        } catch (OptimizationException e) {
            fail("Unexpected optimization exception: " + e.getMessage());
        }

        // Test expansion
        RealPointValuePairList expandedSimplexList = new RealPointValuePairList(new double[][] {{0.0, 0.0}, {1.0, 1.0},
                {2.0, 2.0}}, new double[] {0.0, 1.1, 2.2});

        when(comparator.compare(RealPointValuePair.MIN_VALUE, expandedSimplexList.getVector(0))).thenReturn(-1);

        Problem problemExpanded = new Problem() {
            @Override
            public FunctionValue evaluate(RealVector point) {
                return new FunctionValue(Math.pow(Math.sin(point.getEntry(0)) * Math.sin(point.getEntry(1)), 2) + 1);
            }

            @Override
            public String getName() {
                return "Test Problem";
            }

            @Override
            public RealPointValuePair getInitialGuess() {
                return new RealPointValuePair(new double[] {0.0, 0.0}, 0.0);
            }

            @Override
            public AveragingGoalType getGoalType() {
                return null;
            }

            @Override
            public StoppingCondition getStoppingCondition() {
                return null;
            }
        };

        MultiDirectional mdExpandedInstance = new MultiDirectional(md);
        mdExpandedInstance.setProblem(problemExpanded);
        mdExpandedInstance.setInitialGuess(new RealPointValuePair(new double[] {0.0, 0.0}, 0.0));
        mdExpandedInstance.setFunctionEvaluator(mdExpandedInstance);
        mdExpandedInstance.setOptimizer(mdExpandedInstance);
        mdExpandedInstance.setIterations(1);
        mdExpandedInstance.setMaxEvaluations(1);
        mdExpandedInstance.setComparator(comparator);
        mdExpandedInstance.simplex = expandedSimplexList.getVector();
        mdExpandedInstance.khi = 3.0;

        try {
            mdExpandedInstance.optimize();
            assertTrue(mdExpandedInstance.isOptimized());
            assertFalse(mdExpandedInstance.getOptimum().isFeasible());
            throw new AssertionError("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (OptimizationException e) {
            fail("Unexpected optimization exception: " + e.getMessage());
        } catch (NoSolutionException e) {
            fail("Unexpected no solution exception: " + e.getMessage());
        }

        // Test reflection and expansion
        RealPointValuePairList reflectedAndExpandedSimplexList = new RealPointValuePairList(new double[][] {{0.0, 0.0},
                {1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}}, new double[] {0.0, 1.1, 2.2, 4.5});

        when(comparator.compare(RealPointValuePair.MIN_VALUE, reflectedAndExpandedSimplexList.getVector(0))).thenReturn(-1);

        Problem problemReflectedAndExpanded = new Problem() {
            @Override
            public FunctionValue evaluate(RealVector point) {
                return new FunctionValue(Math.pow(Math.sin(point.getEntry(0)) * Math.sin(point.getEntry(1)), 2) + 1);
            }

            @Override
            public String getName() {
                return "Test Problem";
            }

            @Override
            public RealPointValuePair getInitialGuess() {
                return new RealPointValuePair(new double[] {0.0, 0.0}, 0.0);
            }

            @Override
            public AveragingGoalType getGoalType() {
                return null;
            }

            @Override
            public StoppingCondition getStoppingCondition() {
                return null;
            }
        };

        MultiDirectional mdReflectedAndExpandedInstance = new MultiDirectional(md);
        mdReflectedAndExpandedInstance.setProblem(problemReflectedAndExpanded);
        mdReflectedAndExpandedInstance.setInitialGuess(new RealPointValuePair(new double[] {0.0, 0.0}, 0.0));
        mdReflectedAndExpandedInstance.setFunctionEvaluator(mdReflectedAndExpandedInstance);
        mdReflectedAndExpandedInstance.setOptimizer(mdReflectedAndExpandedInstance);
        mdReflectedAndExpandedInstance.setIterations(1);
        mdReflectedAndExpandedInstance.setMaxEvaluations(1);
        mdReflectedAndExpandedInstance.setComparator(comparator);
        mdReflectedAndExpandedInstance.simplex = reflectedAndExpandedSimplexList.getVector();
        mdReflectedAndExpandedInstance.khi = 3.0;

        try {
            mdReflectedAndExpandedInstance.optimize();
            assertTrue(mdReflectedAndExpandedInstance.isOptimized());
            assertFalse(mdReflectedAndExpandedInstance.getOptimum().isFeasible());
            assertEquals(3.0, mdReflectedAndExpandedInstance.getOptimum().getPointRef()[0], 0.0);
            assertEquals(3.0, mdReflectedAndExpandedInstance.getOptimum().getPointRef()[1], 0.0);
            assertEquals(27.0, mdReflectedAndExpandedInstance.getOptimum().getValue(), 1e-6);
        } catch (OptimizationException e) {
            fail("Unexpected optimization exception: " + e.getMessage());
        }
    }
}