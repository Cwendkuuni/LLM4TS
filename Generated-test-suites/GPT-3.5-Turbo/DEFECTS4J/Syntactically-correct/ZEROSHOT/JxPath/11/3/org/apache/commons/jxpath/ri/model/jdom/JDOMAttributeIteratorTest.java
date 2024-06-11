import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
  private JDOMAttributeIterator iterator;
  private NodePointer parent;

  @Before
  public void setUp() {
    Element element = new Element("testElement");
    Attribute attr1 = new Attribute("attr1", "value1");
    Attribute attr2 = new Attribute("attr2", "value2");
    element.setAttribute(attr1);
    element.setAttribute(attr2);
    parent = new JDOMNodePointer(element);
  }

  @Test
  public void testGetNodePointer() {
    QName attrName = new QName("attr1");
    iterator = new JDOMAttributeIterator(parent, attrName);
    NodePointer nodePointer = iterator.getNodePointer();
    assertNotNull(nodePointer);
    assertTrue(nodePointer instanceof JDOMAttributePointer);
    JDOMAttributePointer attrPointer = (JDOMAttributePointer) nodePointer;
    assertEquals("attr1", attrPointer.getAttribute().getName());
    assertEquals("value1", attrPointer.getAttribute().getValue());
  }

  @Test
  public void testGetPosition() {
    QName attrName = new QName("attr1");
    iterator = new JDOMAttributeIterator(parent, attrName);
    assertEquals(0, iterator.getPosition());
    iterator.setPosition(1);
    assertEquals(1, iterator.getPosition());
  }

  @Test
  public void testSetPosition() {
    QName attrName = new QName("attr1");
    iterator = new JDOMAttributeIterator(parent, attrName);
    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    assertFalse(iterator.setPosition(0));
    assertEquals(1, iterator.getPosition());
    assertTrue(iterator.setPosition(2));
    assertEquals(2, iterator.getPosition());
    assertFalse(iterator.setPosition(3));
    assertEquals(2, iterator.getPosition());
  }
}