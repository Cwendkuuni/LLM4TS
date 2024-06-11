import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {
  
  @Test
  public void testGetDocumentOrderWithMultipleContexts() {
    EvalContext context1 = new EvalContext(parentContext, contexts);
    EvalContext context2 = new EvalContext(parentContext, contexts);
    UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
    int expected = 1;
    int actual = unionContext.getDocumentOrder();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetDocumentOrderWithSingleContext() {
    EvalContext context = new EvalContext(parentContext, contexts);
    UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});
    int expected = 0;
    int actual = unionContext.getDocumentOrder();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPosition() {
    EvalContext context1 = new EvalContext(parentContext, contexts);
    EvalContext context2 = new EvalContext(parentContext, contexts);
    UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
    int position = 0;
    boolean expected = true;
    boolean actual = unionContext.setPosition(position);
    assertEquals(expected, actual);
  }
}