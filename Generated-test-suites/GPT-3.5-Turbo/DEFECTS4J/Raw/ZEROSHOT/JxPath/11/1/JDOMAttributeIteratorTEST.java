import org.junit.Test;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {
  
  @Test
  public void testGetNodePointer() {
    Element element = new Element("testElement");
    Attribute attr1 = new Attribute("attr1", "value1", Namespace.NO_NAMESPACE);
    element.setAttribute(attr1);
    Attribute attr2 = new Attribute("attr2", "value2", Namespace.XML_NAMESPACE);
    element.setAttribute(attr2);
    NodePointer parent = new JDOMAttributePointer(element);
    JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
    
    NodePointer nodePointer1 = iterator.getNodePointer();
    assertNotNull(nodePointer1);
    assertTrue(nodePointer1 instanceof JDOMAttributePointer);
    assertEquals(attr1, ((JDOMAttributePointer) nodePointer1).getAttribute());
    
    assertTrue(iterator.setPosition(2));
    
    NodePointer nodePointer2 = iterator.getNodePointer();
    assertNotNull(nodePointer2);
    assertTrue(nodePointer2 instanceof JDOMAttributePointer);
    assertEquals(attr2, ((JDOMAttributePointer) nodePointer2).getAttribute());
  }
  
  @Test
  public void testGetPosition() {
    Element element = new Element("testElement");
    Attribute attr1 = new Attribute("attr1", "value1", Namespace.NO_NAMESPACE);
    element.setAttribute(attr1);
    Attribute attr2 = new Attribute("attr2", "value2", Namespace.XML_NAMESPACE);
    element.setAttribute(attr2);
    NodePointer parent = new JDOMAttributePointer(element);
    JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
    
    assertEquals(0, iterator.getPosition());
    
    iterator.setPosition(1);
    assertEquals(1, iterator.getPosition());
    
    iterator.setPosition(2);
    assertEquals(2, iterator.getPosition());
  }
  
  @Test
  public void testSetPosition() {
    Element element = new Element("testElement");
    Attribute attr1 = new Attribute("attr1", "value1", Namespace.NO_NAMESPACE);
    element.setAttribute(attr1);
    Attribute attr2 = new Attribute("attr2", "value2", Namespace.XML_NAMESPACE);
    element.setAttribute(attr2);
    NodePointer parent = new JDOMAttributePointer(element);
    JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
    
    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    
    assertTrue(iterator.setPosition(2));
    assertEquals(2, iterator.getPosition());
    
    assertFalse(iterator.setPosition(0));
    assertEquals(2, iterator.getPosition());
    
    assertFalse(iterator.setPosition(3));
    assertEquals(2, iterator.getPosition());
  }
  
  @Test
  public void testConstructor() {
    Element element = new Element("testElement");
    Attribute attr1 = new Attribute("attr1", "value1", Namespace.NO_NAMESPACE);
    element.setAttribute(attr1);
    Attribute attr2 = new Attribute("attr2", "value2", Namespace.XML_NAMESPACE);
    element.setAttribute(attr2);
    NodePointer parent = new JDOMAttributePointer(element);
    JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parent, new QName("*"));
    
    assertNotNull(iterator1);
    assertEquals(parent, iterator1.getParent());
    assertEquals(2, iterator1.getAttributes().size());
    assertNull(iterator1.getAttributes().get(0).getName());
    assertNull(iterator1.getAttributes().get(1).getName());
    
    JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(parent, new QName("attr1"));
    
    assertNotNull(iterator2);
    assertEquals(parent, iterator2.getParent());
    assertEquals(1, iterator2.getAttributes().size());
    assertEquals("attr1", iterator2.getAttributes().get(0).getName());
    
    JDOMAttributeIterator iterator3 = new JDOMAttributeIterator(parent, new QName("attr2"));
    
    assertNotNull(iterator3);
    assertEquals(parent, iterator3.getParent());
    assertEquals(1, iterator3.getAttributes().size());
    assertEquals("attr2", iterator3.getAttributes().get(0).getName());
    
    JDOMAttributeIterator iterator4 = new JDOMAttributeIterator(parent, new QName("*"));
    
    assertNotNull(iterator4);
    assertEquals(parent, iterator4.getParent());
    assertEquals(2, iterator4.getAttributes().size());
    assertNull(iterator4.getAttributes().get(0).getName());
    assertNull(iterator4.getAttributes().get(1).getName());
    
    JDOMAttributeIterator iterator5 = new JDOMAttributeIterator(parent, new QName("unknown"));
    
    assertNotNull(iterator5);
    assertEquals(parent, iterator5.getParent());
    assertEquals(0, iterator5.getAttributes().size());
  }
}