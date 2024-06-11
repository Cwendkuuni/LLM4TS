import static org.junit.Assert.*;
import org.junit.Test;

public class MultiDirectionalTest {
  
  @Test
  public void testIterationSimplex_1() {
    MultiDirectional multiDirectional = new MultiDirectional();
    RealPointValuePair[] original = new RealPointValuePair[2];
    original[0] = new RealPointValuePair(new double[]{0.0}, 0.0);
    original[1] = new RealPointValuePair(new double[]{1.0}, 1.0);
    
    Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
      public int compare(RealPointValuePair a, RealPointValuePair b) {
        return Double.compare(a.getPointRef()[0], b.getPointRef()[0]);
      }
    };
    
    try {
      multiDirectional.iterateSimplex(comparator);
    } catch (Exception e) {
      fail("Exception during iteration");
    }
    
    assertTrue(true);
  }
  
  @Test
  public void testEvaluateNewSimplex_1() {
    MultiDirectional multiDirectional = new MultiDirectional();
    RealPointValuePair[] original = new RealPointValuePair[2];
    original[0] = new RealPointValuePair(new double[]{0.0}, 0.0);
    original[1] = new RealPointValuePair(new double[]{1.0}, 1.0);
    
    Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
      public int compare(RealPointValuePair a, RealPointValuePair b) {
        return Double.compare(a.getPointRef()[0], b.getPointRef()[0]);
      }
    };
    
    try {
      multiDirectional.evaluateNewSimplex(original, 1.0, comparator);
    } catch (Exception e) {
      fail("Exception during evaluation");
    }
    
    assertTrue(true);
  }
  
  @Test
  public void testEvaluateSimplex_1() {
    MultiDirectional multiDirectional = new MultiDirectional();
    RealPointValuePair[] simplex = new RealPointValuePair[3];
    simplex[0] = new RealPointValuePair(new double[]{0.0}, 0.0);
    simplex[1] = new RealPointValuePair(new double[]{1.0}, 1.0);
    simplex[2] = new RealPointValuePair(new double[]{2.0}, 2.0);
    
    Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
      public int compare(RealPointValuePair a, RealPointValuePair b) {
        return Double.compare(a.getPointRef()[0], b.getPointRef()[0]);
      }
    };
    
    try {
      multiDirectional.evaluateSimplex(comparator);
    } catch (Exception e) {
      fail("Exception during evaluation");
    }
    
    assertTrue(true);
  }
  
  @Test
  public void testIncrementIterationsCounter_1() {
    MultiDirectional multiDirectional = new MultiDirectional();
    
    try {
      multiDirectional.incrementIterationsCounter();
    } catch (Exception e) {
      fail("Exception during counter increment");
    }
    
    assertTrue(true);
  }
  
}