import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
  
  private Element parentElement;
  private JDOMAttributeIterator iterator;
  
  @Before
  public void setUp() {
    // Create parent node
    parentElement = new Element("parent");
    // Add attributes to parent node
    parentElement.setAttribute("attr1", "value1");
    parentElement.setAttribute("attr2", "value2");
    parentElement.setAttribute(new Attribute("attr3", "value3", Namespace.getNamespace("http://example.com")));
    parentElement.setAttribute(new Attribute("attr4", "value4", Namespace.XML_NAMESPACE));
    
    // Create iterator with parent node and attribute name
    iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName("attributeName", "http://example.com"));
  }
  
  @After
  public void tearDown() {
    parentElement = null;
    iterator = null;
  }
  
  @Test
  public void testGetPosition() {
    // Test initial position
    assertEquals(0, iterator.getPosition());
    
    // Test setting and getting position
    iterator.setPosition(2);
    assertEquals(2, iterator.getPosition());
  }
  
  @Test
  public void testSetPosition() {
    // Test setting position within valid range
    assertTrue(iterator.setPosition(2));
    assertEquals(2, iterator.getPosition());
    
    // Test setting position outside valid range
    assertFalse(iterator.setPosition(5));
    assertEquals(2, iterator.getPosition());
    
    assertFalse(iterator.setPosition(0));
    assertEquals(2, iterator.getPosition());
  }
  
  @Test
  public void testGetNodePointer() {
    // Test getting node pointer at position 1
    iterator.setPosition(1);
    JDOMAttributePointer pointer1 = iterator.getNodePointer();
    assertNotNull(pointer1);
    assertEquals("attr1", pointer1.getName());
    assertEquals("value1", pointer1.getAttribute().getValue());
    
    // Test getting node pointer at position 2
    iterator.setPosition(2);
    JDOMAttributePointer pointer2 = iterator.getNodePointer();
    assertNotNull(pointer2);
    assertEquals("attr2", pointer2.getName());
    assertEquals("value2", pointer2.getAttribute().getValue());
    
    // Test getting node pointer with null attributes
    iterator.setPosition(3);
    JDOMAttributePointer pointer3 = iterator.getNodePointer();
    assertNull(pointer3);
    
    // Test getting node pointer at position 4
    iterator.setPosition(4);
    JDOMAttributePointer pointer4 = iterator.getNodePointer();
    assertNotNull(pointer4);
    assertEquals("attr3", pointer4.getName());
    assertEquals("value3", pointer4.getAttribute().getValue());
    assertEquals("http://example.com", pointer4.getAttribute().getNamespaceURI());
    
    // Test getting node pointer at position 5
    iterator.setPosition(5);
    JDOMAttributePointer pointer5 = iterator.getNodePointer();
    assertNotNull(pointer5);
    assertEquals("attr4", pointer5.getName());
    assertEquals("value4", pointer5.getAttribute().getValue());
    assertEquals(Namespace.XML_NAMESPACE, pointer5.getAttribute().getNamespace());
  }
  
}