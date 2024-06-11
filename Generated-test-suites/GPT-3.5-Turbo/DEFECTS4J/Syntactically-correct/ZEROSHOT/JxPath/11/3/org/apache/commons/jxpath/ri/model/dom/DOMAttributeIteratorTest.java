import static org.junit.Assert.*;
import org.junit.Test;

public class DOMAttributeIteratorTest {
  
  @Test
  public void testGetNodePointer() {
    // Test when parent node is null
    DOMNodePointer parent = null;
    QName name = new QName("*");
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    assertNull(iterator.getNodePointer());
    
    // Test when parent node is not null but not an element node
    parent = new DOMNodePointer();
    iterator = new DOMAttributeIterator(parent, name);
    assertNull(iterator.getNodePointer());
    
    // Test when parent node is an element node and attribute exists
    Element element = new Element();
    DOMNodePointer domNodePointer = new DOMNodePointer(element);
    parent = new DOMNodePointer(domNodePointer);
    name = new QName("attributeName");
    iterator = new DOMAttributeIterator(parent, name);
    assertNotNull(iterator.getNodePointer());
    
    // Test when parent node is an element node but attribute does not exist
    name = new QName("nonExistingAttribute");
    iterator = new DOMAttributeIterator(parent, name);
    assertNull(iterator.getNodePointer());
  }
  
  @Test
  public void testGetPosition() {
    DOMNodePointer parent = null;
    QName name = new QName("*");
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    assertEquals(0, iterator.getPosition());
    
    Element element = new Element();
    DOMNodePointer domNodePointer = new DOMNodePointer(element);
    parent = new DOMNodePointer(domNodePointer);
    name = new QName("attributeName");
    iterator = new DOMAttributeIterator(parent, name);
    assertEquals(0, iterator.getPosition());
  }
  
  @Test
  public void testSetPosition() {
    DOMNodePointer parent = null;
    QName name = new QName("*");
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    assertFalse(iterator.setPosition(0));
    
    Element element = new Element();
    DOMNodePointer domNodePointer = new DOMNodePointer(element);
    parent = new DOMNodePointer(domNodePointer);
    name = new QName("attributeName");
    iterator = new DOMAttributeIterator(parent, name);
    assertTrue(iterator.setPosition(1));
    assertFalse(iterator.setPosition(0));
  }
  
}