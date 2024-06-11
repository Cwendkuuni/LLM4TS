
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {
	
	private DOMAttributeIterator attributeIterator;
	private NodePointer parent;
	private QName name;
	private Node node;
	private Element element;
	private NamedNodeMap namedNodeMap;
	private Attr attr;
	
	@Before
	public void setUp() {
		parent = mock(NodePointer.class);
		name = new QName("testName");
		node = mock(Node.class);
		attributeIterator = new DOMAttributeIterator(parent, name);
		element = mock(Element.class);
		namedNodeMap = mock(NamedNodeMap.class);
		attr = mock(Attr.class);
	}
	
	@Test
	public void testConstructor_ElementNodeWithMatchingName_AddsAttributeToList() {
		when(parent.getNode()).thenReturn(element);
		when(element.getNodeType()).thenReturn(Node.ELEMENT_NODE);
		when(name.getName()).thenReturn("testName");
		when(element.getAttributes()).thenReturn(namedNodeMap);
		when(namedNodeMap.getLength()).thenReturn(1);
		when(namedNodeMap.item(0)).thenReturn(attr);
		when(attr.getName()).thenReturn("testName");
		
		attributeIterator = new DOMAttributeIterator(parent, name);
		
		assertEquals(1, attributeIterator.attributes.size());
		assertTrue(attributeIterator.attributes.contains(attr));
	}
	
	@Test
	public void testConstructor_ElementNodeWithNonMatchingName_DoesNotAddAttributeToList() {
		when(parent.getNode()).thenReturn(element);
		when(element.getNodeType()).thenReturn(Node.ELEMENT_NODE);
		when(name.getName()).thenReturn("testName");
		when(element.getAttributes()).thenReturn(namedNodeMap);
		when(namedNodeMap.getLength()).thenReturn(1);
		when(namedNodeMap.item(0)).thenReturn(attr);
		when(attr.getName()).thenReturn("otherName");
		
		attributeIterator = new DOMAttributeIterator(parent, name);
		
		assertEquals(0, attributeIterator.attributes.size());
		assertFalse(attributeIterator.attributes.contains(attr));
	}
	
	@Test
	public void testConstructor_ElementNodeWithWildcardName_AddsAllAttributesToList() {
		when(parent.getNode()).thenReturn(element);
		when(element.getNodeType()).thenReturn(Node.ELEMENT_NODE);
		when(name.getName()).thenReturn("*");
		when(element.getAttributes()).thenReturn(namedNodeMap);
		when(namedNodeMap.getLength()).thenReturn(2);
		when(namedNodeMap.item(0)).thenReturn(attr);
		when(namedNodeMap.item(1)).thenReturn(attr);
		when(attr.getName()).thenReturn("testName");
		
		attributeIterator = new DOMAttributeIterator(parent, name);
		
		assertEquals(2, attributeIterator.attributes.size());
		assertTrue(attributeIterator.attributes.contains(attr));
	}
	
	@Test
	public void testConstructor_ElementNodeWithNonMatchingWildcardName_DoesNotAddAttributeToList() {
		when(parent.getNode()).thenReturn(element);
		when(element.getNodeType()).thenReturn(Node.ELEMENT_NODE);
		when(name.getName()).thenReturn("*");
		when(element.getAttributes()).thenReturn(namedNodeMap);
		when(namedNodeMap.getLength()).thenReturn(1);
		when(namedNodeMap.item(0)).thenReturn(attr);
		when(attr.getName()).thenReturn("otherName");
		
		attributeIterator = new DOMAttributeIterator(parent, name);
		
		assertEquals(0, attributeIterator.attributes.size());
		assertFalse(attributeIterator.attributes.contains(attr));
	}
	
	@Test
	public void testTestAttr_NamespaceAttribute_ReturnsFalse() {
		when(attr.getPrefix()).thenReturn("xmlns");
		
		boolean result = attributeIterator.testAttr(attr);
		
		assertFalse(result);
	}
	
	@Test
	public void testTestAttr_DefaultNamespaceAttribute_ReturnsFalse() {
		when(attr.getPrefix()).thenReturn(null);
		when(attr.getLocalName()).thenReturn("xmlns");
		
		boolean result = attributeIterator.testAttr(attr);
		
		assertFalse(result);
	}
	
	@Test
	public void testTestAttr_LocalNameWildcard_ReturnsTrue() {
		when(attr.getPrefix()).thenReturn(null);
		when(attr.getLocalName()).thenReturn("testName");
		when(name.getName()).thenReturn("*");
		
		boolean result = attributeIterator.testAttr(attr);
		
		assertTrue(result);
	}
	
	@Test
	public void testTestAttr_LocalNameMatch_ReturnsTrue() {
		when(attr.getPrefix()).thenReturn(null);
		when(attr.getLocalName()).thenReturn("testName");
		when(name.getName()).thenReturn("testName");
		
		boolean result = attributeIterator.testAttr(attr);
		
		assertTrue(result);
	}
	
	@Test
	public void testTestAttr_LocalNameMismatch_ReturnsFalse() {
		when(attr.getPrefix()).thenReturn(null);
		when(attr.getLocalName()).thenReturn("testName");
		when(name.getName()).thenReturn("otherName");
		
		boolean result = attributeIterator.testAttr(attr);
		
		assertFalse(result);
	}
	
	@Test
	public void testTestAttr_PrefixMatch_ReturnsTrue() {
		when(attr.getPrefix()).thenReturn("testPrefix");
		when(attr.getLocalName()).thenReturn("testName");
		when(name.getName()).thenReturn("testName");
		when(name.getPrefix()).thenReturn("testPrefix");
		
		boolean result = attributeIterator.testAttr(attr);
		
		assertTrue(result);
	}
	
	@Test
	public void testTestAttr_PrefixMismatch_ReturnsFalse() {
		when(attr.getPrefix()).thenReturn("testPrefix");
		when(attr.getLocalName()).thenReturn("testName");
		when(name.getName()).thenReturn("testName");
		when(name.getPrefix()).thenReturn("otherPrefix");
		
		boolean result = attributeIterator.testAttr(attr);
		
		assertFalse(result);
	}
	
	@Test
	public void testTestAttr_NamespaceMatch_ReturnsTrue() {
		when(parent.getNamespaceURI("testPrefix")).thenReturn("testNamespace");
		when(attr.getPrefix()).thenReturn("testPrefix");
		when(attr.getLocalName()).thenReturn("testName");
		when(name.getName()).thenReturn("testName");
		when(name.getPrefix()).thenReturn("testPrefix");

		boolean result = attributeIterator.testAttr(attr);

		assertTrue(result);
	}
	
	@Test
	public void testTestAttr_NamespaceMismatch_ReturnsFalse() {
		when(parent.getNamespaceURI("testPrefix")).thenReturn("otherNamespace");
		when(attr.getPrefix()).thenReturn("testPrefix");
		when(attr.getLocalName()).thenReturn("testName");
		when(name.getName()).thenReturn("testName");
		when(name.getPrefix()).thenReturn("testPrefix");

		boolean result = attributeIterator.testAttr(attr);

		assertFalse(result);
	}
	
	@Test
	public void testEqualStrings_StringAndNull_ReturnsFalse() {
		assertFalse(DOMAttributeIterator.equalStrings("testString", null));
	}
	
	@Test
	public void testEqualStrings_TwoEqualStrings_ReturnsTrue() {
		assertTrue(DOMAttributeIterator.equalStrings("testString", "testString"));
	}
	
	@Test
	public void testEqualStrings_TwoDifferentStrings_ReturnsFalse() {
		assertFalse(DOMAttributeIterator.equalStrings("testString", "otherString"));
	}
	
	@Test
	public void testEqualStrings_NullAndNull_ReturnsTrue() {
		assertTrue(DOMAttributeIterator.equalStrings(null, null));
	}
	
	@Test
	public void testGetAttribute_NSStringNotNull_ReturnsAttributeNodeNS() {
		when(name.getPrefix()).thenReturn("testPrefix");
		when(parent.getNamespaceResolver().getNamespaceURI("testPrefix")).thenReturn("testNS");
		when(element.getAttributeNodeNS("testNS", "testName")).thenReturn(attr);
		
		Attr result = attributeIterator.getAttribute(element, name);
		
		assertEquals(attr, result);
	}
	
	@Test
	public void testGetAttribute_NSStringNull_ReturnsAttributeNode() {
		when(name.getPrefix()).thenReturn(null);
		when(element.getAttributeNode("testName")).thenReturn(attr);
		
		Attr result = attributeIterator.getAttribute(element, name);
		
		assertEquals(attr, result);
	}
	
	@Test
	public void testGetAttribute_AttributeNodeNSNotNull_ReturnsAttributeNodeNS() {
		when(name.getPrefix()).thenReturn("testPrefix");
		when(parent.getNamespaceResolver().getNamespaceURI("testPrefix")).thenReturn("testNS");
		when(element.getAttributeNodeNS("testNS", "testName")).thenReturn(null);
		when(element.getAttributes()).thenReturn(namedNodeMap);
		when(namedNodeMap.getLength()).thenReturn(1);
		when(namedNodeMap.item(0)).thenReturn(attr);
		when(attr.getPrefix()).thenReturn("testPrefix");
		when(attr.getName()).thenReturn("testName");
		
		Attr result = attributeIterator.getAttribute(element, name);
		
		assertEquals(attr, result);
	}
	
	@Test
	public void testGetAttribute_AttributeNodeNSNull_ReturnsAttributeNode() {
		when(name.getPrefix()).thenReturn("testPrefix");
		when(parent.getNamespaceResolver().getNamespaceURI("testPrefix")).thenReturn(null);
		when(element.getAttributeNodeNS("testNS", "testName")).thenReturn(null);
		when(element.getAttributeNode("testName")).thenReturn(attr);
		
		Attr result = attributeIterator.getAttribute(element, name);
		
		assertEquals(attr, result);
	}
	
	@Test
	public void testGetNodePointer_AttributeNotSet_ReturnsNull() {
		attributeIterator.attributes.clear();

		NodePointer result = attributeIterator.getNodePointer();

		assertNull(result);
	}
	
	@Test
	public void testGetNodePointer_PositionZero_ReturnsFirstItemInList() {
		attributeIterator.attributes.add(attr);
		attributeIterator.attributes.add(attr);
		
		NodePointer result = attributeIterator.getNodePointer();
		
		assertNotNull(result);
		assertEquals(attr, ((DOMAttributePointer)result).getAttr());
	}
	
	@Test
	public void testGetNodePointer_PositionOne_ReturnsFirstItemInList() {
		attributeIterator.attributes.add(attr);
		attributeIterator.attributes.add(attr);

		attributeIterator.setPosition(1);
		NodePointer result = attributeIterator.getNodePointer();

		assertNotNull(result);
		assertEquals(attr, ((DOMAttributePointer)result).getAttr());
	}
	
	@Test
	public void testGetNodePointer_PositionTwo_ReturnsSecondItemInList() {
		attributeIterator.attributes.add(attr);
		attributeIterator.attributes.add(attr);

		attributeIterator.setPosition(2);
		NodePointer result = attributeIterator.getNodePointer();

		assertNotNull(result);
		assertEquals(attr, ((DOMAttributePointer)result).getAttr());
	}
	
	@Test
	public void testGetPosition_InitialPosition_ReturnsZero() {
		assertEquals(0, attributeIterator.getPosition());
	}
	
	@Test
	public void testSetPosition_PositionLessThanOne_ReturnsFalse() {
		boolean result = attributeIterator.setPosition(0);
		
		assertFalse(result);
		assertEquals(0, attributeIterator.getPosition());
	}
	
	@Test
	public void testSetPosition_PositionGreaterThanListSize_ReturnsFalse() {
		attributeIterator.attributes.add(attr);
		attributeIterator.attributes.add(attr);
		
		boolean result = attributeIterator.setPosition(3);
		
		assertFalse(result);
		assertEquals(0, attributeIterator.getPosition());
	}
	
	@Test
	public void testSetPosition_PositionWithinListSize_ReturnsTrue() {
		attributeIterator.attributes.add(attr);
		attributeIterator.attributes.add(attr);
		
		boolean result = attributeIterator.setPosition(2);
		
		assertTrue(result);
		assertEquals(2, attributeIterator.getPosition());
	}
	
	@Test
	public void testSetPosition_PositionWithinListSize_ReturnsTrue() {
		attributeIterator.attributes.add(attr);
		attributeIterator.attributes.add(attr);
		
		boolean result = attributeIterator.setPosition(2);
		
		assertTrue(result);
		assertEquals(2, attributeIterator.getPosition());
	}
}
