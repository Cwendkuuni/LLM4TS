package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.PointValueMap;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.initializers.AmoebaInitialGuess;
import org.apache.commons.math3.optim.convergers.SimpleLineSearch;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.PointValueMap;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Test;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link BaseOptimizer} interface.
 */
public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(50);
        assertEquals(50, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        optimizer.optimize(anyObject(), anyObject());
    }

    @Test
    public void testEdgeCasesAndExceptionHandling() {
        BaseOptimizer<PointValuePair> optimizer = new BrentOptimizer();

        // Test max evaluations reached
        ObjectiveFunction<Double> function = mock(ObjectiveFunction.class);
        InitialGuess<Double> initialGuess = new AmoebaInitialGuess(1.0);
        ConvergenceChecker<PointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        MaxEval maxEval = new MaxEval(1);

        optimizer.optimize(function, initialGuess, maxEval, convergenceChecker);
        verify(convergenceChecker, times(1)).isConverged(eq(null));
        verify(optimizer, times(1)).getMaxEvaluations();
        verify(optimizer, times(1)).getEvaluations();

        // Test initial guess is null
        optimizer = new BrentOptimizer();
        optimizer.optimize(function, null, maxEval, convergenceChecker);
        verify(optimizer, times(1)).optimize(function, anyObject(), maxEval, convergenceChecker);

        // Test convergence checker is null
        optimizer = new BrentOptimizer();
        optimizer.optimize(function, initialGuess, maxEval, null);
        verify(optimizer, times(1)).optimize(function, initialGuess, anyObject(), mock(SimpleLineSearch.class));

        // Test function is null
        optimizer = new BrentOptimizer();
        optimizer.optimize(null, initialGuess, maxEval, convergenceChecker);
        verifyZeroInteractions(optimizer);

        // Test bounds are null
        optimizer = new BrentOptimizer();
        InitialGuess<Double> nullInitialGuess = null;
        optimizer.optimize(function, nullInitialGuess, maxEval, convergenceChecker);
        verifyZeroInteractions(optimizer);

        // Test goal type is null
        optimizer = new BrentOptimizer();
        ObjectiveFunction<Double> nullFunction = null;
        GoalType nullGoalType = null;
        optimizer.optimize(nullFunction, initialGuess, maxEval, convergenceChecker, nullGoalType);
        verifyZeroInteractions(optimizer);
    }
}