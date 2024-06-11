import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class AttributeContextTest {
  
  private AttributeContext attributeContext;
  
  @Before
  public void setUp() {
    attributeContext = new AttributeContext(null, new NodeNameTest(null, null));
  }
  
  @Test
  public void testGetCurrentNodePointer() {
    assertNull(attributeContext.getCurrentNodePointer());
  }
  
  @Test
  public void testReset() {
    attributeContext.reset();
    assertFalse(attributeContext.setPosition(1));
    assertNull(attributeContext.getCurrentNodePointer());
  }
  
  @Test
  public void testSetPosition() {
    assertFalse(attributeContext.setPosition(1));
    assertNull(attributeContext.getCurrentNodePointer());
  }

  @Test
  public void testNextNode() {
    assertFalse(attributeContext.nextNode());
    assertNull(attributeContext.getCurrentNodePointer());
  }
}