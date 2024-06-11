import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
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
  public void setup() {
    parent = new NodePointer();
    name = new QName("test");
    domAttributeIterator = new DOMAttributeIterator(parent, name);
  }
  
  @Test
  public void testConstructor() {
    assertNotNull(domAttributeIterator);
    assertNull(domAttributeIterator.getNodePointer());
    assertEquals(0, domAttributeIterator.getPosition());
  }
  
  @Test
  public void testTestAttr() {
    Attr attr = new Attr();
    QName testName = new QName("test");
    
    boolean result = domAttributeIterator.testAttr(attr, testName);
    
    assertFalse(result);
  }
  
  @Test
  public void testEqualStrings() {
    boolean result = domAttributeIterator.equalStrings("test", "test");
    
    assertTrue(result);
  }
  
  @Test
  public void testGetAttribute() {
    Element element = new Element();
    QName testName = new QName("test");
    
    Attr result = domAttributeIterator.getAttribute(element, testName);
    
    assertNull(result);
  }
  
  @Test
  public void testGetNodePointer() {
    NodePointer result = domAttributeIterator.getNodePointer();
    
    assertNull(result);
  }
  
  @Test
  public void testGetPosition() {
    int result = domAttributeIterator.getPosition();
    
    assertEquals(0, result);
  }
  
  @Test
  public void testSetPosition() {
    boolean result = domAttributeIterator.setPosition(1);
    
    assertTrue(result);
    assertEquals(1, domAttributeIterator.getPosition());
  }
}