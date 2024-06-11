import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {

  private UnionContext unionContext;
  private EvalContext[] contexts;
  
  @Before
  public void setUp() {
    contexts = new EvalContext[2];
    contexts[0] = mock(EvalContext.class);
    contexts[1] = mock(EvalContext.class);
    
    unionContext = new UnionContext(null, contexts);
  }
  
  @Test
  public void testGetPosition_Default_ReturnsZero() {
    int position = unionContext.getDocumentOrder();
    
    assertEquals(0, position);
  }
  
  @Test
  public void testGetPosition_WithContexts_ReturnsOne() {
    EvalContext[] contexts = { mock(EvalContext.class), mock(EvalContext.class) };
    UnionContext unionContext = new UnionContext(null, contexts);
    
    int position = unionContext.getDocumentOrder();
    
    assertEquals(1, position);
  }
  
  @Test
  public void testSetPosition_Default_ReturnsTrue() {
    boolean result = unionContext.setPosition(0);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testSetPosition_Prepared_ReturnsTrue() {
    unionContext.setPosition(0);
    
    boolean result = unionContext.setPosition(1);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testSetPosition_NotPrepared_ReturnsTrue() {
    boolean result = unionContext.setPosition(1);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testSetPosition_PreparedWithPointers_ReturnsTrue() {
    BasicNodeSet nodeSet = mock(BasicNodeSet.class);
    NodePointer pointer = mock(NodePointer.class);
    EvalContext context = mock(EvalContext.class);
    
    contexts[0] = context;
    unionContext.setPosition(0);
    
    boolean result = unionContext.setPosition(1);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testSetPosition_CurrentNodePointerNotContains_ReturnsTrue() {
    BasicNodeSet nodeSet = mock(BasicNodeSet.class);
    NodePointer pointer = mock(NodePointer.class);
    EvalContext context = mock(EvalContext.class);
    
    contexts[0] = context;
    unionContext.setPosition(0);
    
    boolean result = unionContext.setPosition(1);
    
    assertEquals(true, result);
  }
}