import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.MathUserException;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<FUNC> optimizer;
    private DummyOptimizer dummyOptimizer;

    private static class FUNC implements UnivariateRealFunction {
        public double value(double x) {
            return x * x;
        }
    }

    private static class DummyOptimizer implements BaseUnivariateRealOptimizer<FUNC> {
        private ConvergenceChecker<UnivariateRealPointValuePair> checker;
        private int maxEvaluations;
        private int evaluations;
        private boolean mustThrowException;

        public DummyOptimizer(boolean mustThrowException) {
            this.mustThrowException = mustThrowException;
        }

        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            this.checker = checker;
        }

        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return checker;
        }

        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        public int getEvaluations() {
            return evaluations;
        }

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public UnivariateRealPointValuePair optimize(FUNC f,
                                                 GoalType goal,
                                                 double min, double max)
                throws FunctionEvaluationException {
            evaluations++;
            if (mustThrowException) {
                throw new FunctionEvaluationException();
            }
            return new UnivariateRealPointValuePair(0, 0);
        }
    }

    @Before
    public void setUp() {
        dummyOptimizer = new DummyOptimizer(false);
        optimizer = new MultiStartUnivariateRealOptimizer<>(dummyOptimizer, 3, null);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
            public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
                return true;
            }
        };
        
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, dummyOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        assertNull(dummyOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, dummyOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, dummyOptimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        dummyOptimizer.setMaxEvaluations(100);
        assertEquals(100, dummyOptimizer.getMaxEvaluations());
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        UnivariateRealPointValuePair result = optimizer.optimize(new FUNC(), GoalType.MINIMIZE, min, max);
        assertNotNull(result);
        assertEquals(0, result.getValue(), 0.0);
        assertEquals(3, dummyOptimizer.getEvaluations());
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithException() throws FunctionEvaluationException {
        dummyOptimizer = new DummyOptimizer(true);
        optimizer = new MultiStartUnivariateRealOptimizer<>(dummyOptimizer, 3, null);
        double min = 0.0;
        double max = 1.0;
        optimizer.optimize(new FUNC(), GoalType.MINIMIZE, min, max);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimumBeforeOptimize() throws MathIllegalStateException {
        optimizer.getOptima();
    }

    @Test
    public void testGetOptimumAfterOptimize() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        optimizer.optimize(new FUNC(), GoalType.MINIMIZE, min, max);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNotNull(optima);
        assertEquals(3, optima.length);
        assertNotNull(optima[0]);
        assertNull(optima[1]);
        assertNull(optima[2]);
    }

    @Test
    public void testSortPairs() {
        UnivariateRealPointValuePair[] pairs = new UnivariateRealPointValuePair[3];
        pairs[0] = new UnivariateRealPointValuePair(0, 0);
        pairs[1] = null;
        pairs[2] = new UnivariateRealPointValuePair(1, 1);
        MultiStartUnivariateRealOptimizer.sortPairs(GoalType.MINIMIZE, pairs);
        assertTrue(pairs[0] == null);
        assertEquals(0, pairs[1].getPoint(), 0.0);
        assertEquals(0, pairs[1].getValue(), 0.0);
        assertEquals(1, pairs[2].getPoint(), 0.0);
        assertEquals(1, pairs[2].getValue(), 0.0);
    }

}