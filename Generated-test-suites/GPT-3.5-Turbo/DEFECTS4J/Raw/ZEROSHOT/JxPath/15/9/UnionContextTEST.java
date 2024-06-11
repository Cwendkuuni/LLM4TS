import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {
  
  private UnionContext unionContext;
  
  @Before
  public void setup() {
    EvalContext parentContext = new EvalContext();
    EvalContext[] contexts = new EvalContext[]{new EvalContext(), new EvalContext()};
    unionContext = new UnionContext(parentContext, contexts);
  }
  
  @Test
  public void testGetDocumentOrder() {
    int documentOrder = unionContext.getDocumentOrder();
    assertEquals(1, documentOrder);
  }
  
  @Test
  public void testSetPosition() {
    boolean positionSet = unionContext.setPosition(1);
    assertTrue(positionSet);
  }
  
  @Test
  public void testSetPosition_InvalidPosition() {
    boolean positionSet = unionContext.setPosition(-1);
    assertFalse(positionSet);
  }
  
  @Test
  public void testSetPosition_Prepared() {
    unionContext.setPosition(1);
    boolean positionSet = unionContext.setPosition(2);
    assertTrue(positionSet);
  }
  
  @Test
  public void testSetPosition_Prepared_InvalidPosition() {
    unionContext.setPosition(1);
    boolean positionSet = unionContext.setPosition(-1);
    assertFalse(positionSet);
  }
  
  @Test
  public void testSetPosition_NotPrepared() {
    boolean positionSet = unionContext.setPosition(2);
    assertFalse(positionSet);
  }
}