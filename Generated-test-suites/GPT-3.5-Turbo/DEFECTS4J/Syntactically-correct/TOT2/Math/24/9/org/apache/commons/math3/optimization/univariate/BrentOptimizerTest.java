import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {
  
  @Test
  public void testConstructorWithConvergenceChecker() {
    // Given
    double rel = 0.001;
    double abs = 0.1;
    ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<>();
    
    // When
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    
    // Then
    assertNotNull(optimizer);
  }
  
  @Test
  public void testConstructorWithoutConvergenceChecker() {
    // Given
    double rel = 0.001;
    double abs = 0.1;
    
    // When
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    
    // Then
    assertNotNull(optimizer);
  }
  
  @Test
  public void testDoOptimize() {
    // Given
    BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1, null);
    
    // When
    UnivariatePointValuePair result = optimizer.doOptimize();
    
    // Then
    assertNotNull(result);
  }
  
  @Test
  public void testBest() {
    // Given
    UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
    UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
    
    // When
    UnivariatePointValuePair bestResult = BrentOptimizer.best(a, b, true);
    
    // Then
    assertEquals(b, bestResult);
  }
}