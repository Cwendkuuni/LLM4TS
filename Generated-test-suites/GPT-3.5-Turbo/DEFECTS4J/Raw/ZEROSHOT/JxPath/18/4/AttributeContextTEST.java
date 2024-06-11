import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
  
  private AttributeContext context;
  
  @Before
  public void setUp() {
    EvalContext parentContext = new EvalContext(null);
    NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
    context = new AttributeContext(parentContext, nodeTest);
  }
  
  @Test
  public void testGetCurrentNodePointer() {
    assertNull(context.getCurrentNodePointer());
  }
  
  @Test
  public void testReset() {
    context.reset();
    assertFalse(context.setStarted);
    assertNull(context.iterator);
    assertNotNull(context.parentContext);
  }
  
  @Test
  public void testSetPosition() {
    assertFalse(context.setPosition(1));
    assertFalse(context.setPosition(2));
  }
  
  @Test
  public void testNextNode() {
    assertFalse(context.nextNode());
  }
}