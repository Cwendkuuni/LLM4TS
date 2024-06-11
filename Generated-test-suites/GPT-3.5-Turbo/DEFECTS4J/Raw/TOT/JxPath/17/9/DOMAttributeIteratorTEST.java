import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class DOMAttributeIteratorTest {

	@Test
	public void testDOMAttributeIterator() {
		NodePointer parent = null;
		QName name = new QName("test");
		
		DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
		
		assertNotNull(iterator);
		assertEquals(0, iterator.getPosition());
	}
	
	@Test
	public void testGetNodePointer() {
		Element element = null;
		Attr attr = null;
		NodePointer parent = null;
		DOMAttributePointer pointer = null;
		
		DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));
		
		parent = new DOMNodePointer(element, attr);
		iterator = new DOMAttributeIterator(parent, new QName("test"));
		
		pointer = iterator.getNodePointer();
		
		assertNotNull(pointer);
		assertTrue(pointer instanceof DOMAttributePointer);
	}
	
	@Test
	public void testGetPosition() {
		NodePointer parent = null;
		DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));
		
		int position = iterator.getPosition();
		
		assertEquals(0, position);
	}
	
	@Test
	public void testSetPosition() {
		NodePointer parent = null;
		DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));
		
		boolean result = iterator.setPosition(1);
		
		assertTrue(result);
		assertEquals(1, iterator.getPosition());
		
		result = iterator.setPosition(0);
		assertFalse(result);
		
		int count = iterator.attributes.size();
		
		result = iterator.setPosition(count + 1);
		assertFalse(result);
	}
	
	@Test
	public void testSetPositionWithEmptyAttributes() {
		NodePointer parent = null;
		DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));
		
		boolean result = iterator.setPosition(1);
		
		assertFalse(result);
		assertEquals(0, iterator.getPosition());
	}

	@Test
	public void testSetPositionWithInvalidPosition() {
		Element element = null;
		Attr attr = null;
		NodePointer parent = null;
		DOMAttributePointer pointer = null;
		int count = 0;
		
		parent = new DOMNodePointer(element, attr);
		count = element.getAttributes().getLength();
		DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));
		
		boolean result = iterator.setPosition(count + 1);
		assertFalse(result);
		assertEquals(0, iterator.getPosition());
	}
	
	@Test
	public void testEqualStrings() {
		String s1 = "test";
		String s2 = "test";
		boolean result = DOMAttributeIterator.equalStrings(s1, s2);
		
		assertTrue(result);
		
		s1 = null;
		result = DOMAttributeIterator.equalStrings(s1, s2);
		assertFalse(result);
		
		result = DOMAttributeIterator.equalStrings(s1, null);
		assertTrue(result);
		
		result = DOMAttributeIterator.equalStrings(s1, s1);
		assertTrue(result);
	}
	
	@Test
	public void testTestAttr() {
		Attr attr = null;
		boolean result = false;
		
		DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null, attr), new QName("test"));
		
		iterator.testAttr(attr);
		assertEquals(false, result);
	}
	
	@Test
	public void testGetAttribute() {
		Element element = null;
		Attr attr = null;
		NodePointer parent = null;
		int count = 0;
		
		parent = new DOMNodePointer(element, attr);
		count = element.getAttributes().getLength();
		DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));
		
		iterator.getAttribute(element, new QName("test"));
		assertEquals(count, element.getAttributes().getLength());
	}
}