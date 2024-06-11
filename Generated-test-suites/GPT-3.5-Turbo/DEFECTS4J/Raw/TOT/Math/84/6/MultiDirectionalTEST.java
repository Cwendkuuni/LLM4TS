import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Comparator;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
  
  MultiDirectional optimizer;
  Comparator<RealPointValuePair> comparator;
  
  @Before
  public void setUp() {
    optimizer = new MultiDirectional(2.0, 0.5);
    comparator = new Comparator<RealPointValuePair>() {
      public int compare(RealPointValuePair p1, RealPointValuePair p2) {
        return Double.compare(p1.getValue(), p2.getValue());
      }
    };
  }
  
  @Test
  public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
    double[] point = {1.0, 2.0};
    RealPointValuePair[] simplex = new RealPointValuePair[2];
    simplex[0] = new RealPointValuePair(point, 0.0, true);
    point[0] = 2.0;
    point[1] = 3.0;
    simplex[1] = new RealPointValuePair(point, 1.0, false);
    
    optimizer.setSimplex(simplex);
    
    optimizer.iterateSimplex(comparator);
    
    double[] expected = {1.5, 2.5};
    assertTrue(optimizer.getSimplex()[0].equals(new RealPointValuePair(expected, 0.0, true)));
  }
  
  @Test
  public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
    double[] point = {1.0, 2.0};
    RealPointValuePair[] simplex = new RealPointValuePair[2];
    simplex[0] = new RealPointValuePair(point, 0.0, true);
    point[0] = 2.0;
    point[1] = 3.0;
    simplex[1] = new RealPointValuePair(point, 1.0, false);
    
    optimizer.setSimplex(simplex);
    
    double[] expected = {1.5, 2.5};
    assertTrue(optimizer.evaluateNewSimplex(simplex, 0.5, comparator).equals(new RealPointValuePair(expected, 0.0, true)));
  }
  
  @Test
  public void testMultiDirectionalDefaultCoefficients() {
    MultiDirectional optimizer = new MultiDirectional();
    assertEquals(2.0, optimizer.getKhi(), 0.0);
    assertEquals(0.5, optimizer.getGamma(), 0.0);
  }
  
  @Test
  public void testMultiDirectionalWithSpecifiedCoefficients() {
    MultiDirectional optimizer = new MultiDirectional(1.0, 0.1);
    assertEquals(1.0, optimizer.getKhi(), 0.0);
    assertEquals(0.1, optimizer.getGamma(), 0.0);
  }

}