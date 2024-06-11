package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new BrentOptimizer(-0.5, 0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new BrentOptimizer(0.5, -0.1);
    }

    @Test
    public void testDoOptimize() {
        ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1, checker);

        double result = optimizer.doOptimize().getPoint();

        assertEquals(0.0, result, Precision.EPSILON);
        verify(checker, times(1)).converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class));
    }

    @Test(expected = NullPointerException.class)
    public void testDoOptimizeWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);

        optimizer.doOptimize();
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);

        UnivariatePointValuePair result = new BrentOptimizer(0.5, 0.1).best(a, b, true);

        assertEquals(a, result);
    }

    @Test
    public void testBestWithNullA() {
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);

        UnivariatePointValuePair result = new BrentOptimizer(0.5, 0.1).best(null, b, true);

        assertEquals(b, result);
    }

    @Test
    public void testBestWithNullB() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);

        UnivariatePointValuePair result = new BrentOptimizer(0.5, 0.1).best(a, null, true);

        assertEquals(a, result);
    }

    @Test
    public void testBestWithIsMinimFalse() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);

        UnivariatePointValuePair result = new BrentOptimizer(0.5, 0.1).best(a, b, false);

        assertEquals(b, result);
    }
    
    @Test
    public void testConstructorWithValidParams() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);

        double result1 = optimizer.doOptimize().getPoint();
        double result2 = optimizer.doOptimize().getValue();

        assertEquals(0.0, result1, Precision.EPSILON);
        assertEquals(0.0, result2, Precision.EPSILON);
    }
}