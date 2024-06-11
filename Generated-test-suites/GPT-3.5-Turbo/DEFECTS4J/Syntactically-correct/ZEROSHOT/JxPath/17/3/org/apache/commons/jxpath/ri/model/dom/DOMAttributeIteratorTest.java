import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

  @Test
  public void testConstructor() {
    NodePointer parent = mock(NodePointer.class);
	QName name = new QName("testName");
	
	DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
	
    // assertions
    assertNotNull(iterator);
    verify(parent).getNode();
  }

  @Test
  public void testTestAttr() {
    Attr attr = mock(Attr.class);
	
	DOMAttributeIterator iterator = new DOMAttributeIterator(mock(NodePointer.class), mock(QName.class));
	boolean result = iterator.testAttr(attr);
	
    // assertions
    assertTrue(result);
    verify(attr).getPrefix();
  }

  @Test
  public void testEqualStrings() {
    String s1 = "testString1";
    String s2 = "testString2";
	
	DOMAttributeIterator iterator = new DOMAttributeIterator(mock(NodePointer.class), mock(QName.class));
	boolean result = iterator.equalStrings(s1, s2);
	
    // assertions
    assertFalse(result);
  }

  @Test
  public void testGetAttribute() {
    Element element = mock(Element.class);
    QName name = new QName("testName");
	
	DOMAttributeIterator iterator = new DOMAttributeIterator(mock(NodePointer.class), mock(QName.class));
	Attr attr = iterator.getAttribute(element, name);
	
    // assertions
    assertNotNull(attr);
    verify(element).getAttributeNode(name.getName());
  }

  @Test
  public void testGetNodePointer() {
    NodePointer parent = mock(NodePointer.class);
	Attr attr = mock(Attr.class);
	
	DOMAttributeIterator iterator = new DOMAttributeIterator(parent, mock(QName.class));
	iterator.attributes.add(attr);
	
	NodePointer nodePointer = iterator.getNodePointer();
	
    // assertions
    assertNotNull(nodePointer);
    verify(parent, times(2)).getNamespaceResolver();
  }

  @Test
  public void testGetPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(mock(NodePointer.class), mock(QName.class));
	iterator.position = 5;
	
	int position = iterator.getPosition();
	
    // assertions
    assertEquals(5, position);
  }

  @Test
  public void testSetPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(mock(NodePointer.class), mock(QName.class));
	
	boolean result = iterator.setPosition(5);
	
    // assertions
    assertTrue(result);
    assertEquals(5, iterator.position);
  }
}