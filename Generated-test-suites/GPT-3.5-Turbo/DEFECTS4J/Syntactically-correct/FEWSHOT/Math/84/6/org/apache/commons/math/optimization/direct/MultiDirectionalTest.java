import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Comparator;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
    
    private MultiDirectional optimizer;
    
    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }
    
    @Test
    public void testConstructor() {
        assertEquals(2.0, optimizer.getKhi(), 0.0);
        assertEquals(0.5, optimizer.getGamma(), 0.0);
    }
    
    @Test
    public void testGettersAndSetters() {
        optimizer.setKhi(3.0);
        optimizer.setGamma(0.3);
        
        assertEquals(3.0, optimizer.getKhi(), 0.0);
        assertEquals(0.3, optimizer.getGamma(), 0.0);
    }
    
    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] simplex = new RealPointValuePair[3];
        simplex[0] = new RealPointValuePair(new double[] {1.0, 2.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[] {1.5, 2.5}, 0.1, false);
        simplex[2] = new RealPointValuePair(new double[] {2.0, 3.0}, 0.2, false);
        
        optimizer.setSimplex(simplex);
        
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        
        optimizer.iterateSimplex(comparator);
        
        RealPointValuePair[] result = optimizer.getSimplex();
        
        assertEquals(0.0, result[0].getValue(), 0.0);
        assertEquals(1.0, result[0].getPointRef()[0], 0.0);
        assertEquals(2.0, result[0].getPointRef()[1], 0.0);
        
        assertEquals(0.1, result[1].getValue(), 0.0);
        assertEquals(1.5, result[1].getPointRef()[0], 0.0);
        assertEquals(2.5, result[1].getPointRef()[1], 0.0);
        
        assertEquals(0.2, result[2].getValue(), 0.0);
        assertEquals(2.0, result[2].getPointRef()[0], 0.0);
        assertEquals(3.0, result[2].getPointRef()[1], 0.0);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexWithFunctionEvaluationException() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] simplex = new RealPointValuePair[3];
        simplex[0] = new RealPointValuePair(new double[] {1.0, 2.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[] {1.5, 2.5}, 0.1, false);
        simplex[2] = new RealPointValuePair(new double[] {2.0, 3.0}, 0.2, false);
        
        optimizer.setSimplex(simplex);
        
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        
        optimizer.iterateSimplex(comparator);
    }
    
    @Test(expected = OptimizationException.class)
    public void testIterateSimplexWithOptimizationException() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] simplex = new RealPointValuePair[3];
        simplex[0] = new RealPointValuePair(new double[] {1.0, 2.0}, 0.0, false);
        simplex[1] = new RealPointValuePair(new double[] {1.5, 2.5}, 0.1, false);
        simplex[2] = new RealPointValuePair(new double[] {2.0, 3.0}, 0.2, false);
        
        optimizer.setSimplex(simplex);
        
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        
        optimizer.iterateSimplex(comparator);
        
        fail("OptimizationException expected");
    }
}