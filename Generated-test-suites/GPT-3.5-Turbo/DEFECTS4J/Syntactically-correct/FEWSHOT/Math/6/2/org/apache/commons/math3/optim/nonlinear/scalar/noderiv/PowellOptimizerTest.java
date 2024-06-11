package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.junit.Test;

import static org.junit.Assert.*;

public class PowellOptimizerTest {
    private double[] startPoint = {1, 2, 3};
    private double[][] lowerBound = {{0, 0, 0}};
    private double[][] upperBound = {{10, 10, 10}};
    private MultivariateOptimizer.Optimum optimum = new PointValuePair(new double[] {1, 2, 3}, 0);
    private ConvergenceChecker<PointValuePair> checker;

    @Test
    public void testDoOptimize() {
        checker = null;
        PowellOptimizer optimizer = new PowellOptimizer(startPoint.length, checker);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setSearchInterval(lowerBound, upperBound);
        PointValuePair result = optimizer.optimize(100, new MultiFunction(), GoalType.MINIMIZE);
        assertEquals(optimum.getPoint(), result.getPoint());
        assertEquals(optimum.getValue(), result.getValue(), 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelThreshold() {
        new PowellOptimizer(1e-16, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsThreshold() {
        new PowellOptimizer(1e-5, -1);
    }

    @Test
    public void testConstructorWithRelAndAbsThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        assertEquals(1e-5, optimizer.getRelativeThreshold(), 1e-5);
        assertEquals(1e-5, optimizer.getAbsoluteThreshold(), 1e-5);
    }
    
    private class MultiFunction implements MultivariateOptimizer.PointCostPair {
        public double[] getPoint() {
            return startPoint;
        }
        public double getPointCost() {
            return 0;
        }
        public void setPointCost(double cost) {
        }
    }
}