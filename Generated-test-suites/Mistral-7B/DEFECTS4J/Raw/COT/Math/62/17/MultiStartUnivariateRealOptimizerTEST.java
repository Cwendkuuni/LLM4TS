package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeBoolean;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearFactor;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairList;
import org.apache.commons.math3.random.Gaussian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MultiStartUnivariateRealOptimizerTest<func extends UnivariateRealFunction> {

    @Mock
    private BaseUnivariateRealOptimizer<func> optimizer;

    @InjectMocks
    private MultiStartUnivariateRealOptimizer<func> optimizerMultiStart;

    private UnivariateRealFunction testFunction;

    @Before
    public void setUp() {
        testFunction = mock(UnivariateRealFunction.class);
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, ConvergenceException {
        // Test with default constructor
        optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Gaussian());
        PointValuePair bestOptim = optimizerMultiStart.optimize(testFunction, GoalType.MINIMIZE, 1.0, 10.0);
        assertNull(bestOptim);

        // Test with custom constructor
        optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Gaussian());
        when(optimizer.optimize(testFunction, GoalType.MINIMIZE, 1.0, 10.0)).thenReturn(new PointValuePair(2.0, 1.0));
        PointValuePair[] optima = optimizerMultiStart.optimize(testFunction, GoalType.MINIMIZE, 1.0, 10.0, 0);
        assertArrayEquals(new PointValuePair[]{new PointValuePair(2.0, 1.0), null, null, null, null, null, null, null, null, null}, optima);
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException, ConvergenceException {
        // Test with default constructor
        optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Gaussian());
        optimizerMultiStart.optimize(testFunction, GoalType.MINIMIZE, 1.0, 10.0);
        PointValuePair[] optima = optimizerMultiStart.getOptima();
        assertNull(optima);

        // Test with custom constructor and optimization result
        optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Gaussian());
        when(optimizer.optimize(testFunction, GoalType.MINIMIZE, 1.0, 10.0)).thenReturn(new PointValuePair(2.0, 1.0));
        optimizerMultiStart.optimize(testFunction, GoalType.MINIMIZE, 1.0, 10.0);
        optima = optimizerMultiStart.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(2.0, 1.0)}, optima);
    }
}