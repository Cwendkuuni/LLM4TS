import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
  
  private MultiStartUnivariateRealOptimizer<FUNC> optimizer;
  
  @Before
  public void setUp() {
    optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(new BaseUnivariateRealOptimizer<FUNC>(), 3, new RandomGenerator());
  }
  
  @Test
  public void testSetConvergenceChecker() {
    // Test functionality of the method
    
  }
  
  @Test
  public void testGetConvergenceChecker() {
    // Test functionality of the method
    
  }
  
  @Test
  public void testGetMaxEvaluations() {
    // Test functionality of the method
    
  }
  
  @Test
  public void testGetEvaluations() {
    // Test functionality of the method
    
  }
  
  @Test
  public void testSetMaxEvaluations() {
    // Test functionality of the method
    
  }
  
  @Test
  public void testGetOptima() {
    // Test functionality of the method
    
  }
  
  @Test
  public void testOptimize1() {
    // Test functionality of the method
    
  }
  
  @Test
  public void testOptimize2() {
    // Test functionality of the method
    
  }
  
  @Test
  public void testSortPairs() {
    // Test functionality of the method
    
  }
  
}