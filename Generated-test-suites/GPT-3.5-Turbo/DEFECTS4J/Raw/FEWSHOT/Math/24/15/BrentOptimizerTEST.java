package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-7;

    @Test
    public void testDoOptimize() {
        UnivariatePointValuePair expected = new UnivariatePointValuePair(1.0, 0.0);

        BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
        optimizer.setMaxEvaluations(1000);
        optimizer.setUnivariateObjectiveFunction(x -> x * x);

        UnivariatePointValuePair result = optimizer.optimize(new MaxEval<>(100));

        assertEquals(expected.getPoint(), result.getPoint(), EPSILON);
        assertEquals(expected.getValue(), result.getValue(), EPSILON);
    }
}