import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

private DOMAttributeIterator iterator;
private Node node;
private Element element;

@Before
public void setUp() throws Exception {
 node = mock(Node.class);
 when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 when(node.getNamespaceURI()).thenReturn("http://test.com");
 element = mock(Element.class);
 when(element.getTagName()).thenReturn("testElement");
 when(element.getAttributes()).thenReturn(createMock(NamedNodeMap.class));
 when(element.getAttributeNode(eq("attr1"))).thenReturn(createMock(Attr.class));
 when(element.getAttributeNodeNS(eq("http://test.com"), eq("attr1"))).thenReturn(createMock(Attr.class));
 when(element.getAttributeNodeNS(eq("http://test.com"), eq("attr2"))).thenReturn(null);
 when(element.getAttributeNodes()).thenReturn(createMock(NodeList.class));
 when(element.getAttributeNodes()).thenReturn(createMock(NodeList.class));
 when(node.getParentNode()).thenReturn(createMock(Node.class));
 when(parentNode().getNamespaceURI("testPrefix")).thenReturn("http://test.com");
 when(parentNode().getNamespaceURI(null)).thenReturn("http://test.com");
 when(parentNode().getNamespaceResolver()).thenReturn(createMock(NamespaceResolver.class));
}

@Test
public void testCreateInstanceWithNullParentAndName() {
 iterator = new DOMAttributeIterator(null, null);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
}

@Test
public void testCreateInstanceWithNullParentAndNameWithEmptyAttributesList() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(mock(Node.class), mock(Node.class)), null);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
}

@Test
public void testCreateInstanceWithNonNullParentAndWildcardName() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), null);
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
}

@Test
public void testCreateInstanceWithNonNullParentAndSpecificName() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), new QName("testPrefix:attr1"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
}

@Test
public void testCreateInstanceWithNonNullParentAndSpecificNameWithEmptyAttributesList() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), new QName("testPrefix:attr2"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
}

@Test
public void testCreateInstanceWithNonNullParentAndSpecificNameWithOneAttribute() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), new QName("testPrefix:attr1"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
}

@Test
public void testCreateInstanceWithNonNullParentAndSpecificNameWithMultipleAttributes() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), new QName("attr1"));
 List<Attr> attributes = new ArrayList<>();
 attributes.add(createMock(Attr.class));
 attributes.add(createMock(Attr.class));
 when(element.getAttributeNodes()).thenReturn(attributes.toArray(new Node[0]));
 List<Attr> expected = new ArrayList<>();
 expected.add(createMock(Attr.class));
 assertEquals(expected, iterator.toList());
}

@Test
public void testCreateInstanceWithNonNullParentAndSpecificNameWithNoMatchingAttribute() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), new QName("testPrefix:attr2"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
}

@Test
public void testCreateInstanceWithNonNullParentAndNameWithNamespace() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), new QName("http://test.com:attr1"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
}

@Test
public void testCreateInstanceWithNonNullParentAndNameWithNamespaceNoMatch() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), new QName("http://test2.com:attr1"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
}

@Test
public void testGetNodePointer() {
 Attr attr = createMock(Attr.class);
 when(attr.getNodeName()).thenReturn("attr1");
 NodePointer nodePointer = new DOMAttributePointer(parent(), attr);
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), new QName("attr1"));
 Attr nextAttr = iterator.next();
 assertSame(nextAttr, attr);
 assertSame(iterator.getNodePointer(), nodePointer);
}

@Test
public void testGetPosition() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), null);
 iterator.next();
 assertEquals(1, iterator.getPosition());
}

@Test
public void testHasNext() {
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), null);
 assertTrue(iterator.hasNext());
 iterator.next();
 assertFalse(iterator.hasNext());
}

@Test
public void testToList() {
 List<Attr> attributes = new ArrayList<>();
 attributes.add(createMock(Attr.class));
 attributes.add(createMock(Attr.class));
 when(element.getAttributeNodes()).thenReturn(attributes.toArray(new Node[0]));
 iterator = new DOMAttributeIterator(new DOMAttitudePointer(element, null), null);
 List<Attr> expected = new ArrayList<>();
 expected.add(createMock(Attr.class));
 expected.add(createMock(Attr.class));
 assertEquals(expected, iterator.toList());
}
}