package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

public class PowellOptimizerTest {
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorRelTooSmall() {
        new PowellOptimizer(1e-16, 1, new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous,
                    PointValuePair current) {
                return false;
            }
        });
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorAbsNotStrictlyPositive() {
        new PowellOptimizer(1e-6, -1, new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous,
                    PointValuePair current) {
                return false;
            }
        });
    }

    @Test
    public void testConstructorRelAbs() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1, new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous,
                    PointValuePair current) {
                return false;
            }
        });

        assertEquals(1e-6, optimizer.getRelativeThreshold(), 0);
        assertEquals(1, optimizer.getAbsoluteThreshold(), 0);
    }
}