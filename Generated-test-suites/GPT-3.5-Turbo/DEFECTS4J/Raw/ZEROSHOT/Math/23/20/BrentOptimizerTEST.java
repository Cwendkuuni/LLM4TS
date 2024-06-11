import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  @Test
  public void testDoOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.01, null);
    
    double result = optimizer.doOptimize();
    
    // Add assertions here
    //assertEquals(expectedResult, result, delta);
  }

  @Test
  public void testBest() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.01, null);
    
    UnivariatePointValuePair a = new UnivariatePointValuePair(1, 2);
    UnivariatePointValuePair b = new UnivariatePointValuePair(3, 4);
    boolean isMinim = true;
    
    UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
    
    // Add assertions here
    //assertEquals(expectedResult, result);
  }

  @Test
  public void testBrentOptimizer() {
    double rel = 0.0001;
    double abs = 0.01;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;
    
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    
    // Add assertions here
    //assertEquals(expectedResult, optimizer);
  }

  @Test
  public void testBrentOptimizer2() {
    double rel = 0.0001;
    double abs = 0.01;
    
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    
    // Add assertions here
    //assertEquals(expectedResult, optimizer);
  }

  @Test
  public void testDoOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.01, null);
    
    UnivariatePointValuePair result = optimizer.doOptimize();
    
    // Add assertions here
    //assertEquals(expectedResult, result);
  }

  @Test
  public void testBest() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.01, null);
    
    UnivariatePointValuePair a = new UnivariatePointValuePair(1, 2);
    UnivariatePointValuePair b = new UnivariatePointValuePair(3, 4);
    boolean isMinim = true;
    
    UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
    
    // Add assertions here
    //assertEquals(expectedResult, result);
  }

  @Test
  public void testBrentOptimizer() {
    double rel = 0.0001;
    double abs = 0.01;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;
    
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    
    // Add assertions here
    //assertEquals(expectedResult, optimizer);
  }

  @Test
  public void testBrentOptimizer2() {
    double rel = 0.0001;
    double abs = 0.01;
    
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    
    // Add assertions here
    //assertEquals(expectedResult, optimizer);
  }

  @Test
  public void testDoOptimize() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.01, null);
    
    UnivariatePointValuePair result = optimizer.doOptimize();
    
    // Add assertions here
    //assertEquals(expectedResult, result);
  }

  @Test
  public void testBest() {
    BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.01, null);
    
    UnivariatePointValuePair a = new UnivariatePointValuePair(1, 2);
    UnivariatePointValuePair b = new UnivariatePointValuePair(3, 4);
    boolean isMinim = true;
    
    UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
    
    // Add assertions here
    //assertEquals(expectedResult, result);
  }

  @Test
  public void testBrentOptimizer() {
    double rel = 0.0001;
    double abs = 0.01;
    ConvergenceChecker<UnivariatePointValuePair> checker = null;
    
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    
    // Add assertions here
    //assertEquals(expectedResult, optimizer);
  }

  @Test
  public void testBrentOptimizer2() {
    double rel = 0.0001;
    double abs = 0.01;
    
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    
    // Add assertions here
    //assertEquals(expectedResult, optimizer);
  }
  
}