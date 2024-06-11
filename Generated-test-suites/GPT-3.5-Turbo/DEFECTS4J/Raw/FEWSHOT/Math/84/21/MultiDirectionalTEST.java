package org.apache.commons.math.optimization.direct;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Comparator;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;
public class MultiDirectionalTest {
    private MultiDirectional multiDirectional;
    @Before
    public void setup() {
        multiDirectional = new MultiDirectional();
    }
    @Test
    public void testIterateSimplex_comparator_conditionsTrue() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        Comparator<RealPointValuePair> comparator = mock(Comparator.class);
        when(comparator.compare(reflected, best)).thenReturn(1);
        when(comparator.compare(reflected, expanded)).thenReturn(1);
        when(comparator.compare(contracted, best)).thenReturn(1);
        when(comparator.compare(contracted, best)).thenReturn(1);
        multiDirectional.iterateSimplex(comparator);
    }
    @Test
    public void testIterateSimplex_comparator_conditionsFalse() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        Comparator<RealPointValuePair> comparator = mock(Comparator.class);
        when(comparator.compare(reflected, best)).thenReturn(-1);
        when(comparator.compare(contracted, best)).thenReturn(-1);
        when(comparator.compare(contracted, best)).thenReturn(-1);
        multiDirectional.iterateSimplex(comparator);
    }
    @Test
    public void testEvaluateNewSimplex_conditionsTrue() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = new RealPointValuePair[1];
        double coeff = 1.5;
        Comparator<RealPointValuePair> comparator = mock(Comparator.class);
        when(original[0].getPointRef()).thenReturn(new double[]{1.0});
        when(original[1].getPointRef()).thenReturn(new double[]{2.0});
        multiDirectional.evaluateNewSimplex(original, coeff, comparator);
    }
    @Test
    public void testEvaluateNewSimplex_conditionsFalse() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = new RealPointValuePair[1];
        double coeff = 0.5;
        Comparator<RealPointValuePair> comparator = mock(Comparator.class);
        when(original[0].getPointRef()).thenReturn(new double[]{2.0});
        when(original[1].getPointRef()).thenReturn(new double[]{1.0});
        multiDirectional.evaluateNewSimplex(original, coeff, comparator);
    }
}