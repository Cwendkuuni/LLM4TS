import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class JDOMAttributeIteratorTest {
  @Test
  public void testGetNodePointer() {
    Element testElement = new Element("test");
    testElement.setAttribute("attr1", "value1");
    testElement.setAttribute("attr2", "value2");
    
    NodePointer parent = new NodePointer(testElement);
    QName attrName = new QName("attr1");
    
    JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attrName);
    NodePointer nodePointer = iterator.getNodePointer();
    
    assertTrue(nodePointer.getNode() instanceof Attribute);
    Attribute attribute = (Attribute) nodePointer.getNode();
    assertEquals("attr1", attribute.getName());
    assertEquals("value1", attribute.getValue());
  }
  
  @Test
  public void testGetPosition() {
    Element testElement = new Element("test");
    testElement.setAttribute("attr1", "value1");
    testElement.setAttribute("attr2", "value2");
    
    NodePointer parent = new NodePointer(testElement);
    QName attrName = new QName("attr1");
    
    JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attrName);
    assertEquals(0, iterator.getPosition());
    
    iterator.setPosition(1);
    assertEquals(1, iterator.getPosition());
    
    iterator.setPosition(2);
    assertEquals(2, iterator.getPosition());
  }
  
  @Test
  public void testSetPosition() {
    Element testElement = new Element("test");
    testElement.setAttribute("attr1", "value1");
    testElement.setAttribute("attr2", "value2");
    
    NodePointer parent = new NodePointer(testElement);
    QName attrName = new QName("attr1");
    
    JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attrName);
    
    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    
    assertTrue(iterator.setPosition(2));
    assertEquals(2, iterator.getPosition());
    
    assertFalse(iterator.setPosition(3));
    assertEquals(2, iterator.getPosition());
    
    assertFalse(iterator.setPosition(0));
    assertEquals(2, iterator.getPosition());
  }
}