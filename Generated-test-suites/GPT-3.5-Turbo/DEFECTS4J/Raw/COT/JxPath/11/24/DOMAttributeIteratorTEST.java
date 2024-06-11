
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.*;
import org.apache.commons.jxpath.ri.model.dom.*;

public class DOMAttributeIteratorTest {

  @Test
  public void testDOMAttributeIterator() {
    DOMNodePointer parent = new DOMNodePointer();
    QName name = new QName();
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    assertNotNull(iterator);
    assertEquals(0, iterator.getPosition());
  }

  @Test
  public void testGetNodePointer() {
    DOMNodePointer parent = new DOMNodePointer();
    QName name = new QName();
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    assertNull(iterator.getNodePointer());
    iterator.setPosition(1);
    assertNotNull(iterator.getNodePointer());
  }

  @Test
  public void testGetPosition() {
    DOMNodePointer parent = new DOMNodePointer();
    QName name = new QName();
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    assertEquals(0, iterator.getPosition());
    iterator.setPosition(2);
    assertEquals(2, iterator.getPosition());
  }

  @Test
  public void testSetPosition() {
    DOMNodePointer parent = new DOMNodePointer();
    QName name = new QName();
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    assertFalse(iterator.setPosition(0));
    assertTrue(iterator.setPosition(1));
    assertTrue(iterator.setPosition(2));
    assertFalse(iterator.setPosition(3));
  }
}
