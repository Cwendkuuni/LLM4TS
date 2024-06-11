package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    private BrentOptimizer brentOptimizer;
    private double rel;
    private double abs;
    private ConvergenceChecker<UnivariatePointValuePair> checker;

    @Before
    public void setUp() {
        rel = 0.1;
        abs = 0.01;
        checker = mock(ConvergenceChecker.class);
        brentOptimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test
    public void testConstructor() {
        assertNotNull(brentOptimizer);
        assertNull(brentOptimizer.getGoalType());
        assertEquals(rel, brentOptimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, brentOptimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(checker, brentOptimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        rel = -0.1;
        brentOptimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        abs = 0.0;
        brentOptimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test
    public void testDoOptimize() {
        UnivariatePointValuePair result = brentOptimizer.doOptimize();
        assertNotNull(result);
        // TODO: Add more test cases to achieve full coverage of doOptimize method
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        UnivariatePointValuePair result = brentOptimizer.best(a, b, true);
        assertEquals(a, result);

        result = brentOptimizer.best(a, b, false);
        assertEquals(b, result);
    }
}