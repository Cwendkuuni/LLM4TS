import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.axes.UnionContext;

public class UnionContextTest {
  
  private UnionContext unionContext;
  private EvalContext parentContext;
  private EvalContext[] contexts;
  
  @Before
  public void setUp() {
    parentContext = new EvalContext();
    contexts = new EvalContext[2];
    contexts[0] = new EvalContext();
    contexts[1] = new EvalContext();
    unionContext = new UnionContext(parentContext, contexts);
  }
  
  @Test
  public void testConstructor() {
    assertEquals(parentContext, unionContext.getParentContext());
    assertTrue(unionContext.getNodeSet() instanceof BasicNodeSet);
    assertEquals(contexts, unionContext.getContexts());
    assertFalse(unionContext.isPrepared());
  }
  
  @Test
  public void testGetDocumentOrder() {
    assertEquals(1, unionContext.getDocumentOrder());
    
    contexts = new EvalContext[1];
    contexts[0] = new EvalContext();
    unionContext = new UnionContext(parentContext, contexts);
    assertEquals(0, unionContext.getDocumentOrder());
  }
  
  @Test
  public void testSetPositionNotPrepared() {
    assertFalse(unionContext.isPrepared());
    unionContext.setPosition(1);
    assertTrue(unionContext.isPrepared());
    assertEquals(1, unionContext.getPosition());
  }
  
  @Test
  public void testSetPositionPrepared() {
    unionContext.setPosition(1);
    assertTrue(unionContext.isPrepared());
    assertEquals(1, unionContext.getPosition());
    
    unionContext.setPosition(2);
    assertEquals(2, unionContext.getPosition());
  }
  
  @Test
  public void testSetPositionInvalid() {
    unionContext.setPosition(-1);
    assertFalse(unionContext.isPrepared());
    assertEquals(0, unionContext.getPosition());
  }
  
  @Test
  public void testSetPositionWithEmptyNodeSet() {
    unionContext.getPosition();
    unionContext.setPosition(1);
    assertTrue(unionContext.isPrepared());
    assertEquals(0, unionContext.getPosition());
  }
  
  @Test
  public void testSetPositionWithNodeSet() {
    BasicNodeSet nodeSet1 = new BasicNodeSet();
    BasicNodeSet nodeSet2 = new BasicNodeSet();
    nodeSet1.add(new NodePointer());
    nodeSet2.add(new NodePointer());
    contexts[0].setNodeSet(nodeSet1);
    contexts[1].setNodeSet(nodeSet2);
    
    unionContext.setPosition(1);
    unionContext.setPosition(2);
    
    assertNotNull(unionContext.getNodeSet());
    assertEquals(2, unionContext.getNodeSet().size());
  }
}