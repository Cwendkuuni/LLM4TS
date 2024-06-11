import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

  private NodePointer parent;
  private QName qname;
  private JDOMAttributeIterator iterator;

  @Before
  public void setUp() {
    parent = new JDOMNodePointer(new Element("parent"));
    qname = new QName("", "attrName");
    iterator = new JDOMAttributeIterator(parent, qname);
  }

  @Test
  public void testGetNodePointer() {
    NodePointer nodePointer = iterator.getNodePointer();
    assertNotNull(nodePointer);
    assertTrue(nodePointer instanceof JDOMAttributePointer);
  }

  @Test
  public void testGetPosition() {
    assertEquals(0, iterator.getPosition());
  }

  @Test
  public void testSetPositionValid() {
    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    assertTrue(iterator.setPosition(2));
    assertEquals(2, iterator.getPosition());
    assertTrue(iterator.setPosition(3));
    assertEquals(3, iterator.getPosition());
  }

  @Test
  public void testSetPositionInvalid() {
    assertFalse(iterator.setPosition(0));
    assertEquals(0, iterator.getPosition());
    assertFalse(iterator.setPosition(-1));
    assertEquals(0, iterator.getPosition());
    assertFalse(iterator.setPosition(4));
    assertEquals(0, iterator.getPosition());
  }
}