package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {
    private MultiStartUnivariateRealOptimizer optimizer;
    private BaseUnivariateRealOptimizer baseOptimizer;

    @Before
    public void setUp() {
        baseOptimizer = new BaseUnivariateRealOptimizer() {
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            }

            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 0;
            }

            public void setMaxEvaluations(int maxEvaluations) {
            }

            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return null;
            }
        };
        optimizer = new MultiStartUnivariateRealOptimizer(baseOptimizer, 5, new RandomGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            public void nextBytes(byte[] bytes) {
            }

            public int nextInt() {
                return 0;
            }

            public int nextInt(int n) {
                return 0;
            }

            public long nextLong() {
                return 0;
            }

            public boolean nextBoolean() {
                return false;
            }

            public float nextFloat() {
                return 0;
            }

            public double nextDouble() {
                return 0;
            }

            public double nextGaussian() {
                return 0;
            }
        });
    }

    @Test
    public void testSetConvergenceChecker() {
        optimizer.setConvergenceChecker(null);
        assertNull(baseOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptima() {
        try {
            optimizer.getOptima();
            fail("NoOptimumComputedYet exception expected");
        } catch (MathIllegalStateException e) {
            assertEquals(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, e.getSpecifier());
        }
    }

    @Test
    public void testOptimize1() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, 0, 0);
        assertNull(result);
    }

    @Test
    public void testOptimize2() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, 0, 0, 0);
        assertNull(result);
    }
}