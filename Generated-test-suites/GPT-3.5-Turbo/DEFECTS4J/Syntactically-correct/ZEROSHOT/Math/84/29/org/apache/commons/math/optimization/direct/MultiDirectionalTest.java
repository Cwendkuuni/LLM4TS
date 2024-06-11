import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;
    
    @Mock
    private Comparator<RealPointValuePair> comparator;
    
    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
        comparator = mock(Comparator.class);
    }
    
    @Test
    public void testDefaultConstructor() {
        assertEquals(2.0, multiDirectional.khi, 0.0);
        assertEquals(0.5, multiDirectional.gamma, 0.0);
    }
    
    @Test
    public void testParameterizedConstructor() {
        MultiDirectional md = new MultiDirectional(1.5, 0.3);
        assertEquals(1.5, md.khi, 0.0);
        assertEquals(0.3, md.gamma, 0.0);
    }
    
    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        RealPointValuePair[] original = new RealPointValuePair[3];
        
        double[] point1 = { 1.0, 2.0 };
        double value1 = 3.0;
        original[0] = new RealPointValuePair(point1, value1);
        
        double[] point2 = { 2.0, 3.0 };
        double value2 = 4.0;
        original[1] = new RealPointValuePair(point2, value2);
        
        double[] point3 = { 3.0, 4.0 };
        double value3 = 5.0;
        original[2] = new RealPointValuePair(point3, value3);
        
        when(comparator.compare(any(RealPointValuePair.class), any(RealPointValuePair.class)))
            .thenReturn(-1);
        when(comparator.compare(original[0], original[1]))
            .thenReturn(1);
        when(comparator.compare(original[1], original[2]))
            .thenReturn(1);
        when(comparator.compare(original[2], original[0]))
            .thenReturn(1);
        
        multiDirectional.iterateSimplex(comparator);
        
        assertEquals(point1, multiDirectional.simplex[0].getPointRef());
        assertEquals(point2, multiDirectional.simplex[1].getPointRef());
        assertEquals(point3, multiDirectional.simplex[2].getPointRef());
    }
    
    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = new RealPointValuePair[3];
        
        double[] point1 = { 1.0, 2.0 };
        double value1 = 3.0;
        original[0] = new RealPointValuePair(point1, value1);
        
        double[] point2 = { 2.0, 3.0 };
        double value2 = 4.0;
        original[1] = new RealPointValuePair(point2, value2);
        
        double[] point3 = { 3.0, 4.0 };
        double value3 = 5.0;
        original[2] = new RealPointValuePair(point3, value3);
        
        double coeff = 0.5;
        
        when(comparator.compare(any(RealPointValuePair.class), any(RealPointValuePair.class)))
            .thenReturn(-1);
        
        RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, coeff, comparator);
        
        assertEquals(point1, result.getPointRef());
        
        assertEquals(3, multiDirectional.simplex.length);
        assertEquals(point1, multiDirectional.simplex[0].getPointRef());
        assertEquals(point2, multiDirectional.simplex[1].getPointRef());
        assertEquals(point3, multiDirectional.simplex[2].getPointRef());
    }
}