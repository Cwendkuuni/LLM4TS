import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {
  
  private NodePointer parent;
  
  @Before
  public void setUp() {
    Element element = new Element("testElement");
    element.setAttribute("attr1", "value1");
    element.setAttribute("attr2", "value2");
    element.setAttribute("attr3", "value3");
    parent = new JDOMNodePointer(element);
  }
  
  @Test
  public void testGetNodePointer() {
    QName name = new QName("attr1");
    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
    NodePointer nodePointer = attributeIterator.getNodePointer();
    assertEquals("attr1", ((Attribute)nodePointer.getNode()).getName());
    assertEquals("value1", ((Attribute)nodePointer.getNode()).getValue());
  }
  
  @Test
  public void testGetPosition() {
    QName name = new QName("attr1");
    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
    assertEquals(0, attributeIterator.getPosition());
    attributeIterator.setPosition(1);
    assertEquals(1, attributeIterator.getPosition());
  }
  
  @Test
  public void testSetPosition() {
    QName name = new QName("attr1");
    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
    assertTrue(attributeIterator.setPosition(1));
    assertTrue(attributeIterator.setPosition(2));
    assertFalse(attributeIterator.setPosition(5));
  }
  
  @Test
  public void testSetPositionWithEmptyAttributes() {
    Element emptyElement = new Element("emptyElement");
    parent = new JDOMNodePointer(emptyElement);
    QName name = new QName("attr1");
    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
    assertFalse(attributeIterator.setPosition(1));
  }
  
  @Test
  public void testSetPositionWithInvalidPosition() {
    QName name = new QName("attr1");
    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
    assertFalse(attributeIterator.setPosition(0));
  }
  
  @Test
  public void testGetNodePointerWithInvalidPosition() {
    QName name = new QName("attr1");
    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
    attributeIterator.setPosition(0);
    assertNull(attributeIterator.getNodePointer());
    attributeIterator.setPosition(5);
    assertNull(attributeIterator.getNodePointer());
  }
  
  @Test
  public void testGetNodePointerWithWildcardName() {
    QName name = new QName("*");
    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
    NodePointer nodePointer = attributeIterator.getNodePointer();
    assertEquals("attr1", ((Attribute)nodePointer.getNode()).getName());
    assertEquals("value1", ((Attribute)nodePointer.getNode()).getValue());
    attributeIterator.setPosition(2);
    nodePointer = attributeIterator.getNodePointer();
    assertEquals("attr2", ((Attribute)nodePointer.getNode()).getName());
    assertEquals("value2", ((Attribute)nodePointer.getNode()).getValue());
    attributeIterator.setPosition(3);
    nodePointer = attributeIterator.getNodePointer();
    assertEquals("attr3", ((Attribute)nodePointer.getNode()).getName());
    assertEquals("value3", ((Attribute)nodePointer.getNode()).getValue());
  }
  
  @Test
  public void testGetNodePointerWithNamespace() {
    QName name = new QName("attr1");
    JDOMNamespaceResolver namespaceResolver = new JDOMNamespaceResolver();
    namespaceResolver.addNamespace("prefix", "http://example.com");
    parent.setNamespaceResolver(namespaceResolver);
    parent.setNode(new Element("testElement", Namespace.getNamespace("http://example.com")));
    JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
    NodePointer nodePointer = attributeIterator.getNodePointer();
    assertNull(nodePointer);
  }
}