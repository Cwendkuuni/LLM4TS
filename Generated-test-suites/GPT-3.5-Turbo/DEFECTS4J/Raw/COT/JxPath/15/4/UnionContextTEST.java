import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
  
  @Test
  public void testGetDocumentOrder() {
    EvalContext context1 = new EvalContext();
    EvalContext context2 = new EvalContext();
    UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
    
    // Test when contexts length is greater than 1
    int expected = 1;
    int actual = unionContext.getDocumentOrder();
    assertEquals(expected, actual);
    
    // Test when contexts length is 1
    context2 = null;
    unionContext = new UnionContext(null, new EvalContext[]{context1});
    expected = 0;
    actual = unionContext.getDocumentOrder();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPosition() {
    EvalContext context1 = new EvalContext();
    EvalContext context2 = new EvalContext();
    UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
    
    // Test when prepared is false
    boolean expected = true;
    boolean actual = unionContext.setPosition(0);
    assertEquals(expected, actual);
    
    // Test when prepared is true
    expected = true;
    actual = unionContext.setPosition(0);
    assertEquals(expected, actual);
  }
}