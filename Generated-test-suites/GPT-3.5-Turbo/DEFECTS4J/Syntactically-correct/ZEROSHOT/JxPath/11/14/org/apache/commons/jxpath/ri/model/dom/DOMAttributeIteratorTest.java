import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    DOMAttributeIterator domAttributeIterator;

    @Mock
    NodePointer nodePointer;

    @Mock
    QName qName;

    @Mock
    Node node;

    @Mock
    Element element;

    @Mock
    Attr attr;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        domAttributeIterator = new DOMAttributeIterator(nodePointer, qName);
    }

    @Test
    public void testConstructor() {
        assertNotNull(domAttributeIterator);
        verify(nodePointer, times(1)).getNode();
        verify(qName, times(1)).getName();
        verify(element, times(1)).getNode();
        verify(node, times(1)).getNodeType();
    }

    @Test
    public void testTestAttr() {
        when(attr.getPrefix()).thenReturn("prefix");
        when(attr.getLocalName()).thenReturn("localName");
        when(nodePointer.getNamespaceURI("prefix")).thenReturn("namespace");
        when(qName.getPrefix()).thenReturn("prefix");
        when(qName.getName()).thenReturn("localName");
        assertEquals(true, domAttributeIterator.testAttr(attr, qName));

        when(attr.getPrefix()).thenReturn("prefix");
        when(attr.getLocalName()).thenReturn("localName");
        when(nodePointer.getNamespaceURI("prefix")).thenReturn("namespace");
        when(qName.getPrefix()).thenReturn("prefix2");
        when(qName.getName()).thenReturn("localName");
        assertEquals(false, domAttributeIterator.testAttr(attr, qName));

        when(attr.getPrefix()).thenReturn(null);
        when(attr.getLocalName()).thenReturn("localName");
        when(nodePointer.getNamespaceURI(null)).thenReturn("namespace");
        when(qName.getPrefix()).thenReturn("prefix");
        when(qName.getName()).thenReturn("localName");
        assertEquals(false, domAttributeIterator.testAttr(attr, qName));

        when(attr.getPrefix()).thenReturn(null);
        when(attr.getLocalName()).thenReturn("localName");
        when(nodePointer.getNamespaceURI(null)).thenReturn("namespace");
        when(qName.getPrefix()).thenReturn(null);
        when(qName.getName()).thenReturn("localName");
        assertEquals(true, domAttributeIterator.testAttr(attr, qName));
    }

    @Test
    public void testEqualStrings() {
        String s1 = "string1";
        String s2 = "string1";
        assertEquals(true, domAttributeIterator.equalStrings(s1, s2));

        s1 = "string1";
        s2 = "string2";
        assertEquals(false, domAttributeIterator.equalStrings(s1, s2));

        s1 = null;
        s2 = "string2";
        assertEquals(false, domAttributeIterator.equalStrings(s1, s2));

        s1 = "string1";
        s2 = null;
        assertEquals(false, domAttributeIterator.equalStrings(s1, s2));

        s1 = null;
        s2 = null;
        assertEquals(true, domAttributeIterator.equalStrings(s1, s2));
    }

    @Test
    public void testGetAttribute() {
        when(qName.getPrefix()).thenReturn("prefix");
        when(qName.getName()).thenReturn("name");

        when(attr.getPrefix()).thenReturn(null);
        when(attr.getLocalName()).thenReturn("name");
        when(element.getAttributeNode("name")).thenReturn(attr);
        assertEquals(attr, domAttributeIterator.getAttribute(element, qName));

        when(attr.getPrefix()).thenReturn("prefix");
        when(attr.getLocalName()).thenReturn("name");
        when(element.getAttributeNodeNS("namespace", "name")).thenReturn(attr);
        when(nodePointer.getNamespaceURI("prefix")).thenReturn("namespace");
        assertEquals(attr, domAttributeIterator.getAttribute(element, qName));

        when(attr.getPrefix()).thenReturn("prefix2");
        when(attr.getLocalName()).thenReturn("name");
        when(element.getAttributeNodeNS("namespace", "name")).thenReturn(attr);
        when(nodePointer.getNamespaceURI("prefix")).thenReturn("namespace");
        assertEquals(null, domAttributeIterator.getAttribute(element, qName));

        NamedNodeMap namedNodeMap = mock(NamedNodeMap.class);
        when(element.getAttributes()).thenReturn(namedNodeMap);

        when(attr.getPrefix()).thenReturn("prefix");
        when(attr.getLocalName()).thenReturn("name");
        when(namedNodeMap.getLength()).thenReturn(1);
        when(namedNodeMap.item(0)).thenReturn(attr);
        when(nodePointer.getNamespaceURI("prefix")).thenReturn("namespace");
        assertEquals(attr, domAttributeIterator.getAttribute(element, qName));
    }

    @Test
    public void testGetNodePointer() {
        List<Attr> attributes = new ArrayList<>();
        attributes.add(attr);
        domAttributeIterator.attributes = attributes;
        domAttributeIterator.position = 1;

        DOMAttributePointer domAttributePointer = Mockito.mock(DOMAttributePointer.class);

        assertEquals(domAttributePointer, domAttributeIterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        domAttributeIterator.position = 1;
        assertEquals(1, domAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        domAttributeIterator.attributes = new ArrayList<>();
        domAttributeIterator.attributes.add(attr);
        assertEquals(true, domAttributeIterator.setPosition(1));
        assertEquals(false, domAttributeIterator.setPosition(2));
    }
}