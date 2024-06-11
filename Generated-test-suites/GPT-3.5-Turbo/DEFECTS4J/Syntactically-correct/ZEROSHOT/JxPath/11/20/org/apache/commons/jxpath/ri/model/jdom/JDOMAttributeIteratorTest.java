import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIteratorTest;

public class JDOMAttributeIteratorTest {

  @Test
  public void testGetNodePointer() {
    NodePointer nodePointer = new JDOMAttributeIterator(parent, name).getNodePointer();
    assertNotNull(nodePointer);
  }

  @Test
  public void testGetPosition() {
    int position = new JDOMAttributeIterator(parent, name).getPosition();
    assertEquals(0, position);
  }

  @Test
  public void testSetPosition() {
    boolean result = new JDOMAttributeIterator(parent, name).setPosition(1);
    assertTrue(result);
  }

}