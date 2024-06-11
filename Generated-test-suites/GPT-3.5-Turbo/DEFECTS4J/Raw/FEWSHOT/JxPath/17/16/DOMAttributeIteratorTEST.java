package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.mockito.Mockito;

public class DOMAttributeIteratorTest {

    private DOMNodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = Mockito.mock(DOMNodePointer.class);
        name = new QName("test", "name");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertSame(parent, iterator.parent);
        assertSame(name, iterator.name);
        assertEquals(0, iterator.position);
        assertEquals(0, iterator.attributes.size());
    }

    @Test
    public void testGetNodePointer() {
        iterator.position = 1;

        Attr attribute = Mockito.mock(Attr.class);
        iterator.attributes.add(attribute);

        DOMAttributePointer expectedNodePointer = new DOMAttributePointer(parent, attribute);
        NodePointer actualNodePointer = iterator.getNodePointer();

        assertSame(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testGetNodePointerWithZeroPosition() {
        iterator.position = 0;

        Attr attribute = Mockito.mock(Attr.class);
        iterator.attributes.add(attribute);

        DOMAttributePointer expectedNodePointer = new DOMAttributePointer(parent, attribute);
        NodePointer actualNodePointer = iterator.getNodePointer();

        assertSame(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testGetNodePointerWithInvalidPosition() {
        iterator.position = 5;

        Attr attribute = Mockito.mock(Attr.class);
        iterator.attributes.add(attribute);

        NodePointer actualNodePointer = iterator.getNodePointer();

        assertEquals(null, actualNodePointer);
    }

    @Test
    public void testGetPosition() {
        iterator.position = 5;

        int actualPosition = iterator.getPosition();

        assertEquals(5, actualPosition);
    }

    @Test
    public void testSetPosition() {
        int newPosition = 3;

        boolean isPositionSet = iterator.setPosition(newPosition);

        assertEquals(newPosition, iterator.position);
        assertEquals(true, isPositionSet);
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        int newPosition = 10;

        boolean isPositionSet = iterator.setPosition(newPosition);

        assertEquals(newPosition, iterator.position);
        assertEquals(false, isPositionSet);
    }

    @Test
    public void testSetPositionWithZeroPosition() {
        int newPosition = 0;

        boolean isPositionSet = iterator.setPosition(newPosition);

        assertEquals(newPosition, iterator.position);
        assertEquals(false, isPositionSet);
    }

    @Test
    public void testSetPositionWithNegativePosition() {
        int newPosition = -1;

        boolean isPositionSet = iterator.setPosition(newPosition);

        assertEquals(newPosition, iterator.position);
        assertEquals(false, isPositionSet);
    }

    @Test
    public void testTestAttrWithMatchingAttributes() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(attr.getPrefix()).thenReturn("testPrefix");
        Mockito.when(attr.getLocalName()).thenReturn("testName");
        Mockito.when(parent.getNamespaceURI("testPrefix")).thenReturn("testNamespace");
        name = new QName("testName", "testPrefix");

        boolean isAttrTested = iterator.testAttr(attr);

        assertEquals(true, isAttrTested);
    }

    @Test
    public void testTestAttrWithDifferentPrefix() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(attr.getPrefix()).thenReturn("wrongPrefix");
        Mockito.when(attr.getLocalName()).thenReturn("testName");
        Mockito.when(parent.getNamespaceURI("testPrefix")).thenReturn("testNamespace");
        name = new QName("testName", "testPrefix");

        boolean isAttrTested = iterator.testAttr(attr);

        assertEquals(false, isAttrTested);
    }

    @Test
    public void testTestAttrWithDifferentName() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(attr.getPrefix()).thenReturn("testPrefix");
        Mockito.when(attr.getLocalName()).thenReturn("wrongName");
        Mockito.when(parent.getNamespaceURI("testPrefix")).thenReturn("testNamespace");
        name = new QName("testName", "testPrefix");

        boolean isAttrTested = iterator.testAttr(attr);

        assertEquals(false, isAttrTested);
    }

    @Test
    public void testTestAttrWithNoPrefix() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(attr.getPrefix()).thenReturn(null);
        Mockito.when(attr.getLocalName()).thenReturn("testName");
        Mockito.when(parent.getNamespaceURI("testPrefix")).thenReturn("testNamespace");
        name = new QName("testName", "testPrefix");

        boolean isAttrTested = iterator.testAttr(attr);

        assertEquals(true, isAttrTested);
    }

    @Test
    public void testTestAttrWithXmlnsPrefix() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(attr.getPrefix()).thenReturn("xmlns");
        Mockito.when(attr.getLocalName()).thenReturn("testName");
        Mockito.when(parent.getNamespaceURI("testPrefix")).thenReturn("testNamespace");
        name = new QName("testName", "testPrefix");

        boolean isAttrTested = iterator.testAttr(attr);

        assertEquals(false, isAttrTested);
    }

    @Test
    public void testTestAttrWithNoPrefixAndXmlnsLocalName() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(attr.getPrefix()).thenReturn(null);
        Mockito.when(attr.getLocalName()).thenReturn("xmlns");
        Mockito.when(parent.getNamespaceURI("testPrefix")).thenReturn("testNamespace");
        name = new QName("testName", "testPrefix");

        boolean isAttrTested = iterator.testAttr(attr);

        assertEquals(false, isAttrTested);
    }

    @Test
    public void testEqualStringsWhenSameString() {
        String s1 = "test";
        String s2 = s1;

        boolean areStringsEqual = iterator.equalStrings(s1, s2);

        assertEquals(true, areStringsEqual);
    }

    @Test
    public void testEqualStringsWhenDifferentStrings() {
        String s1 = "test";
        String s2 = "wrong";

        boolean areStringsEqual = iterator.equalStrings(s1, s2);

        assertEquals(false, areStringsEqual);
    }

    @Test
    public void testEqualStringsWhenBothStringsNull() {
        String s1 = null;
        String s2 = null;

        boolean areStringsEqual = iterator.equalStrings(s1, s2);

        assertEquals(true, areStringsEqual);
    }

    @Test
    public void testEqualStringsWhenOneStringNull() {
        String s1 = "test";
        String s2 = null;

        boolean areStringsEqual = iterator.equalStrings(s1, s2);

        assertEquals(false, areStringsEqual);
    }

    @Test
    public void testGetAttributeWhenNamespaceProvided() {
        Element element = Mockito.mock(Element.class);
        Mockito.when(parent.getNamespaceURI("testPrefix")).thenReturn("testNamespace");
        name = new QName("testName", "testPrefix");

        Attr expectedAttribute = Mockito.mock(Attr.class);
        Mockito.when(element.getAttributeNodeNS("testNamespace", "testName")).thenReturn(expectedAttribute);

        Attr actualAttribute = iterator.getAttribute(element, name);

        assertSame(expectedAttribute, actualAttribute);
    }

    @Test
    public void testGetAttributeWhenNamespaceNotProvided() {
        Element element = Mockito.mock(Element.class);
        name = new QName("testName", "testPrefix");

        Attr expectedAttribute = Mockito.mock(Attr.class);
        Mockito.when(element.getAttributeNode("testName")).thenReturn(expectedAttribute);

        Attr actualAttribute = iterator.getAttribute(element, name);

        assertSame(expectedAttribute, actualAttribute);
    }

    @Test
    public void testGetAttributeWhenGetAttributeNodeThrowsException() {
        Element element = Mockito.mock(Element.class);
        Mockito.when(element.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenThrow(new RuntimeException());
        name = new QName("testName", "testPrefix");

        Attr actualAttribute = iterator.getAttribute(element, name);

        assertEquals(null, actualAttribute);
    }

    @Test
    public void testGetAttributeWhenAttributeNotFound() {
        Element element = Mockito.mock(Element.class);
        name = new QName("testName", "testPrefix");

        Attr actualAttribute = iterator.getAttribute(element, name);

        assertEquals(null, actualAttribute);
    }

    @Test
    public void testGetAttributeWhenMapSizeIsZero() {
        Element element = Mockito.mock(Element.class);

        Mockito.when(element.getAttributes()).thenReturn(Mockito.mock(NamedNodeMap.class));

        Attr actualAttribute = iterator.getAttribute(element, name);

        assertEquals(null, actualAttribute);
    }

    @Test
    public void testGetAttributeWhenMapSizeIsGreaterThanZero() {
        Element element = Mockito.mock(Element.class);

        NamedNodeMap map = Mockito.mock(NamedNodeMap.class);
        Mockito.when(element.getAttributes()).thenReturn(map);
        Mockito.when(map.getLength()).thenReturn(1);

        Attr expectedAttribute = Mockito.mock(Attr.class);
        Mockito.when(map.item(0)).thenReturn(expectedAttribute);

        Attr actualAttribute = iterator.getAttribute(element, name);

        assertSame(expectedAttribute, actualAttribute);
    }
}