import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
  
  private UnionContext unionContext;
  
  @Before
  public void setUp() {
    EvalContext parentContext = new EvalContext();
    EvalContext[] contexts = new EvalContext[2];
    contexts[0] = new EvalContext();
    contexts[1] = new EvalContext();
    unionContext = new UnionContext(parentContext, contexts);
  }
  
  @Test
  public void testGetDocumentOrder() {
    int documentOrder = unionContext.getDocumentOrder();
    assertEquals(1, documentOrder);
  }
  
  @Test
  public void testSetPosition() {
    boolean setPosition = unionContext.setPosition(1);
    assertTrue(setPosition);
  }
}