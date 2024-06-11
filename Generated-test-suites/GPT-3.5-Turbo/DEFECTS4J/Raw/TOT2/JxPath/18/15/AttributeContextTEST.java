import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
  
  private AttributeContext attributeContext;
  
  @Before
  public void setUp() {
    attributeContext = new AttributeContext(null, new NodeNameTest(new QName("test")));
  }

  @Test
  public void testGetCurrentNodePointer() {
    assertNull(attributeContext.getCurrentNodePointer());
  }
  
  @Test
  public void testReset() {
    attributeContext.reset();
    assertNull(attributeContext.getCurrentNodePointer());
  }
  
  @Test
  public void testSetPosition() {
    assertTrue(attributeContext.setPosition(0));
    assertTrue(attributeContext.setPosition(1));
    assertFalse(attributeContext.setPosition(2));
  }
  
  @Test
  public void testNextNode() {
    assertTrue(attributeContext.nextNode());
  }
}