package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.PointsEvaluation;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.UnivariateConvergenceChecker;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer.Finding;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class BaseOptimizerTest<PAIR> {

    private BaseOptimizer<PAIR> optimizer;
    private UnivariateOptimizer mockOptimizer;
    private ConvergenceChecker<PAIR> convergenceChecker;

    public BaseOptimizerTest(UnivariateOptimizer optimizer) {
        this.optimizer = (BaseOptimizer<PAIR>) optimizer;
        this.mockOptimizer = Mockito.spy(optimizer);
        this.convergenceChecker = Mockito.mock(UnivariateConvergenceChecker.class);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new UnivariateOptimizer(){}
        });
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
        optimizer = new MyOptimizer();
        optimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
        mockOptimizer.optimize(Mockito.eq(new UnivariateOptimizer.Minimizer<Double>(){}),
                Mockito.eq(new InitialGuess(0.0)),
                Mockito.eq(new SimpleBounds(-1.0, 1.0)),
                Mockito.eq(new MaxEval(10)),
                Mockito.eq(convergenceChecker),
                Mockito.any(PointsEvaluation.class));
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerUnsupported() {
        optimizer.getClass(); // invoke getClass() to trigger error
        optimizer.setConvergenceChecker(convergenceChecker);
    }

    @Test
    public void testSetConvergenceChecker() {
        optimizer.setConvergenceChecker(convergenceChecker);
        Mockito.verify(mockOptimizer).setConvergenceChecker(convergenceChecker);
    }

    public static class MyOptimizer extends BaseOptimizer<Object> {
        @Override
        public int getMaxEvaluations() {
            return super.getMaxEvaluations();
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            super.setMaxEvaluations(maxEvaluations);
        }
    }
}