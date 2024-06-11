package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Comparator;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    private static class DummyUnivariateRealFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x; // Dummy function
        }
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
            public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
                return true; // Dummy checker
            }
        };
        MultiStartUnivariateRealOptimizer<DummyUnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(null, 0, null);
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        MultiStartUnivariateRealOptimizer<DummyUnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(null, 0, null);
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        MultiStartUnivariateRealOptimizer<DummyUnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(null, 0, null);
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaError() {
        MultiStartUnivariateRealOptimizer<DummyUnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(null, 0, null);
        optimizer.getOptima();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws FunctionEvaluationException {
        MultiStartUnivariateRealOptimizer<DummyUnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<DummyUnivariateRealFunction>() {
            public UnivariateRealPointValuePair optimize(DummyUnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                throw new ConvergenceException(); // Dummy optimizer
            }

            public double getResult() {
                return 0; // Dummy optimizer
            }

            public int getIterations() {
                return 0; // Dummy optimizer
            }

            public int getEvaluations() {
                return 0; // Dummy optimizer
            }

            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null; // Dummy optimizer
            }

            public int getMaxEvaluations() {
                return 0; // Dummy optimizer
            }

            public void setMaxEvaluations(int maxEvaluations) {
                // Dummy optimizer
            }

            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                // Dummy optimizer
            }
        }, 2, null);

        optimizer.optimize(new DummyUnivariateRealFunction(), GoalType.MINIMIZE, 0, 1);
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        MultiStartUnivariateRealOptimizer<DummyUnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<DummyUnivariateRealFunction>() {
            public UnivariateRealPointValuePair optimize(DummyUnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return new UnivariateRealPointValuePair(0, 0); // Dummy optimizer
            }

            public double getResult() {
                return 0; // Dummy optimizer
            }

            public int getIterations() {
                return 0; // Dummy optimizer
            }

            public int getEvaluations() {
                return 0; // Dummy optimizer
            }

            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null; // Dummy optimizer
            }

            public int getMaxEvaluations() {
                return 0; // Dummy optimizer
            }

            public void setMaxEvaluations(int maxEvaluations) {
                // Dummy optimizer
            }

            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                // Dummy optimizer
            }
        }, 2, null);

        UnivariateRealPointValuePair optima = optimizer.optimize(new DummyUnivariateRealFunction(), GoalType.MINIMIZE, 0, 1);
        assertEquals(new UnivariateRealPointValuePair(0, 0), optima);
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException {
        MultiStartUnivariateRealOptimizer<DummyUnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<DummyUnivariateRealFunction>() {
            public UnivariateRealPointValuePair optimize(DummyUnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                if (min == 0 && max == 1) {
                    return new UnivariateRealPointValuePair(0, 0); // Dummy optimizer
                } else {
                    return new UnivariateRealPointValuePair(1, 1); // Dummy optimizer
                }
            }

            public double getResult() {
                return 0; // Dummy optimizer
            }

            public int getIterations() {
                return 0; // Dummy optimizer
            }

            public int getEvaluations() {
                return 0; // Dummy optimizer
            }

            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null; // Dummy optimizer
            }

            public int getMaxEvaluations() {
                return 0; // Dummy optimizer
            }

            public void setMaxEvaluations(int maxEvaluations) {
                // Dummy optimizer
            }

            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                // Dummy optimizer
            }
        }, 2, null);

        UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[] {
            new UnivariateRealPointValuePair(0, 0),
            new UnivariateRealPointValuePair(1, 1)
        };

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(expectedOptima[0], optima[0]);
        assertEquals(expectedOptima[1], optima[1]);
    }
}