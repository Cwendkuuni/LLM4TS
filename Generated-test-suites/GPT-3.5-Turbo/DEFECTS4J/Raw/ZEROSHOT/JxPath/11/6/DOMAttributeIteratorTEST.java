import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;

public class DOMAttributeIteratorTest {
  
  @Test
  public void testGetNodePointer() {
    NodePointer parent = createMockNodePointer();
    QName name = new QName("attribute", "test", "http://www.example.com");
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    NodePointer pointer1 = iterator.getNodePointer();
    assertNotNull(pointer1);
    assertTrue(pointer1 instanceof DOMAttributePointer);
    
    NodePointer pointer2 = iterator.getNodePointer();
    assertNotNull(pointer2);
    assertTrue(pointer2 instanceof DOMAttributePointer);
    assertNotEquals(pointer1, pointer2);
  }
  
  @Test
  public void testGetPosition() {
    NodePointer parent = createMockNodePointer();
    QName name = new QName("attribute", "test", "http://www.example.com");
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    assertEquals(0, iterator.getPosition());
    
    iterator.setPosition(3);
    assertEquals(3, iterator.getPosition());
    
    iterator.setPosition(0);
    assertEquals(0, iterator.getPosition());
    
    iterator.setPosition(-5);
    assertEquals(0, iterator.getPosition());
  }
  
  @Test
  public void testSetPosition() {
    NodePointer parent = createMockNodePointer();
    QName name = new QName("attribute", "test", "http://www.example.com");
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    
    assertTrue(iterator.setPosition(3));
    assertEquals(3, iterator.getPosition());
    
    assertFalse(iterator.setPosition(0));
    assertEquals(0, iterator.getPosition());
    
    assertFalse(iterator.setPosition(-5));
    assertEquals(0, iterator.getPosition());
    
    assertTrue(iterator.setPosition(2));
    assertEquals(2, iterator.getPosition());
  }
  
  private NodePointer createMockNodePointer() {
    Node node = createMockNode();
    return new DOMNodePointer(node, null, null);
  }
  
  private Node createMockNode() {
    Element element = new MockElement();
    element.setAttribute("testAttr1", "value1");
    element.setAttribute("testAttr2", "value2");
    element.setAttribute("testAttr3", "value3");
    return element;
  }
  
  private class MockElement implements Element {
    @Override
    public String getTagName() {
      return null;
    }
    
    @Override
    public void setAttribute(String name, String value) {
      
    }
    
    @Override
    public boolean hasAttribute(String name) {
      return false;
    }
    
    //Implement the remaining methods of Element interface
    //...
  }
}