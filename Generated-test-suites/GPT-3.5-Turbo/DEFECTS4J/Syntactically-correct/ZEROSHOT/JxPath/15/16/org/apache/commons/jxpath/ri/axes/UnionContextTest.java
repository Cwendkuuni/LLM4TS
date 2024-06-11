import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {
  @Test
  public void testGetDocumentOrder() {
    EvalContext parentContext = new TestEvalContext();
    EvalContext[] contexts = new EvalContext[2];
    contexts[0] = new TestEvalContext();
    contexts[1] = new TestEvalContext();
    UnionContext unionContext = new UnionContext(parentContext, contexts);
    
    int expected = 1;
    int actual = unionContext.getDocumentOrder();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPosition() {
    EvalContext parentContext = new TestEvalContext();
    EvalContext[] contexts = new EvalContext[2];
    contexts[0] = new TestEvalContext();
    contexts[1] = new TestEvalContext();
    UnionContext unionContext = new UnionContext(parentContext, contexts);
    
    int position = 0;
    boolean expected = true;
    boolean actual = unionContext.setPosition(position);
    assertEquals(expected, actual);
  }
  
  // Helper class for testing
  
  private class TestEvalContext extends EvalContext {
    public TestEvalContext() {
      super(null, null);
    }
    
    public boolean nextSet() {
      return true;
    }
    
    public boolean nextNode() {
      return true;
    }
    
    public NodePointer getCurrentNodePointer() {
      return null;
    }
  }
}