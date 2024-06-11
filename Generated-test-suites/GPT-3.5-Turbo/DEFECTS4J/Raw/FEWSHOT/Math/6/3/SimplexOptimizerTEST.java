package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.OptimizationData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.util.Comparator;

public class SimplexOptimizerTest {
    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        
        MultivariateFunction function = mock(MultivariateFunction.class);
        GoalType goalType = GoalType.MINIMIZE;
        int maxEval = 100;
        OptimizationData[] optData = new OptimizationData[0];
        
        PointValuePair result = optimizer.optimizeInternal(maxEval, function, goalType, optData);
        assertNull(result);
        
        goalType = GoalType.MAXIMIZE;
        result = optimizer.optimizeInternal(maxEval, function, goalType, optData);
        assertNull(result);
    }
    
    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        
        Comparator<PointValuePair> comparator = mock(Comparator.class);
        
        optimizer.setConvergenceChecker(checker);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setSimplex(mock(AbstractSimplex.class));
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setMaxEvaluations(100);
        
        when(checker.converged(0, null, null)).thenReturn(true);
        when(comparator.compare(null, null)).thenReturn(0);
        
        PointValuePair result = optimizer.doOptimize();
        assertNull(result);
        
        optimizer.setGoalType(GoalType.MAXIMIZE);
        when(checker.converged(0, null, null)).thenReturn(false);
        result = optimizer.doOptimize();
        assertNull(result);
    }
}