package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairCollection;
import org.apache.commons.math3.optim.simple.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.univariate.BrentMinimizer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest<PAIR extends PointValuePair<Number, Number>> {

    private ObjectiveFunction function;
    private ConvergenceChecker<PAIR> convergenceChecker;

    @Before
    public void setUp() {
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        convergenceChecker = new SimpleConvergenceChecker(1e-6, MaxEval.UNLIMITED);
    }

    @After
    public void tearDown() {
        function = null;
        convergenceChecker = null;
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BrentMinimizer<>(function, new SimpleBounds(0, 10), GoalType.MINIMIZE, convergenceChecker);
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertNotNull(maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BrentMinimizer<>(function, new SimpleBounds(0, 10), GoalType.MINIMIZE, convergenceChecker);
        optimizer.optimize();
        int evaluations = optimizer.getEvaluations();
        assertNotNull(evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BrentMinimizer<>(function, new SimpleBounds(0, 10), GoalType.MINIMIZE, convergenceChecker);
        ConvergenceChecker<PAIR> checker = optimizer.getConvergenceChecker();
        assertNotNull(checker);
        assertEquals(convergenceChecker, checker);
    }
}