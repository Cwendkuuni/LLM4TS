
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

  @Test
  public void testConstructor() {
    MultiDirectional multiDirectional = new MultiDirectional();
    assertEquals(2.0, multiDirectional.khi, 0.0);
    assertEquals(0.5, multiDirectional.gamma, 0.0);
  }
  
  @Test
  public void testCustomConstructor() {
    MultiDirectional multiDirectional = new MultiDirectional(3.0, 0.2);
    assertEquals(3.0, multiDirectional.khi, 0.0);
    assertEquals(0.2, multiDirectional.gamma, 0.0);
  }
  
  @Test
  public void testIterateSimplex() {
    MultiDirectional multiDirectional = new MultiDirectional();
    
    // TODO: Add test logic here
    
  }
  
  @Test
  public void testEvaluateNewSimplex() {
    MultiDirectional multiDirectional = new MultiDirectional();
    
    // TODO: Add test logic here
    
  }
}
