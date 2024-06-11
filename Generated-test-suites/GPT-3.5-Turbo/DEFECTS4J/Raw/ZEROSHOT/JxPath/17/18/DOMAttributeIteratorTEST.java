import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
  
  private DOMAttributeIterator domAttributeIterator;
  private NodePointer parent;
  private QName name;
  
  @Before
  public void setUp() {
    parent = new NodePointer();
    name = new QName("testName");
    domAttributeIterator = new DOMAttributeIterator(parent, name);
  }
  
  @Test
  public void testConstructor() {
    assertNotNull(domAttributeIterator);
  }
  
  @Test
  public void testGetNodePointer() {
    NodePointer nodePointer = domAttributeIterator.getNodePointer();
    assertNull(nodePointer);
  }
  
  @Test
  public void testGetPosition() {
    int position = domAttributeIterator.getPosition();
    assertEquals(0, position);
  }
  
  @Test
  public void testSetPosition() {
    boolean result = domAttributeIterator.setPosition(1);
    assertFalse(result);
  }
}