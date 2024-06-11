import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator attributeIterator;
    private Element element;
    private NamedNodeMap attributes;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        element = mock(Element.class);
        attributes = mock(NamedNodeMap.class);

        when(parent.getNode()).thenReturn((Node)element);
        when(element.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(element.getAttributes()).thenReturn(attributes);
    }

    @Test
    public void testConstructor() {
        attributeIterator = new DOMAttributeIterator(parent, name);

        assertEquals(parent, attributeIterator.parent);
        assertEquals(name, attributeIterator.name);
        assertNotNull(attributeIterator.attributes);
        assertEquals(0, attributeIterator.position);
    }

    @Test
    public void testTestAttr_withNodePrefixNonNull_and_nodeLocalNameEqualsName() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn("testPrefix");
        when(attr.getLocalName()).thenReturn("nodeName");

        when(name.getPrefix()).thenReturn("testPrefix");
        when(name.getName()).thenReturn("nodeName");

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.testAttr(attr);

        assertTrue(result);
    }

    @Test
    public void testTestAttr_withNodePrefixSameAsNodeLocalName_and_nodeLocalNameEqualsName() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn("nodeName");
        when(attr.getLocalName()).thenReturn("nodeName");

        when(name.getPrefix()).thenReturn("nodeName");
        when(name.getName()).thenReturn("nodeName");

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.testAttr(attr);

        assertTrue(result);
    }

    @Test
    public void testTestAttr_withNodePrefixNonNull_and_nodeLocalNameNotEqualsName() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn("testPrefix");
        when(attr.getLocalName()).thenReturn("nodeName");

        when(name.getPrefix()).thenReturn("testPrefix");
        when(name.getName()).thenReturn("testName");

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.testAttr(attr);

        assertFalse(result);
    }

    @Test
    public void testTestAttr_withNodePrefixNonNull_and_nodeLocalNameNotEqualsName_and_testNameEqualsAsterisk() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn("testPrefix");
        when(attr.getLocalName()).thenReturn("nodeName");

        when(name.getPrefix()).thenReturn("testPrefix");
        when(name.getName()).thenReturn("*");

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.testAttr(attr);

        assertTrue(result);
    }

    @Test
    public void testTestAttr_withNodePrefixNonNull_and_nodeLocalNameNotEqualsName_and_testNameNotEqualsAsterisk() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn("testPrefix");
        when(attr.getLocalName()).thenReturn("nodeName");

        when(name.getPrefix()).thenReturn("testPrefix");
        when(name.getName()).thenReturn("testName");

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.testAttr(attr);

        assertFalse(result);
    }

    @Test
    public void testTestAttr_withNodePrefixNull_and_nodeLocalNameEqualsName() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn(null);
        when(attr.getLocalName()).thenReturn("nodeName");

        when(name.getPrefix()).thenReturn(null);
        when(name.getName()).thenReturn("nodeName");

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.testAttr(attr);

        assertTrue(result);
    }

    @Test
    public void testTestAttr_withNodePrefixNull_and_nodeLocalNameNotEqualsName() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn(null);
        when(attr.getLocalName()).thenReturn("nodeName");

        when(name.getPrefix()).thenReturn(null);
        when(name.getName()).thenReturn("testName");

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.testAttr(attr);

        assertFalse(result);
    }

    @Test
    public void testTestAttr_withNodePrefixNull_and_nodeLocalNameNotEqualsName_and_testNameEqualsAsterisk() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn(null);
        when(attr.getLocalName()).thenReturn("nodeName");

        when(name.getPrefix()).thenReturn(null);
        when(name.getName()).thenReturn("*");

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.testAttr(attr);

        assertTrue(result);
    }

    @Test
    public void testTestAttr_withNodePrefixNull_and_nodeLocalNameNotEqualsName_and_testNameNotEqualsAsterisk() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn(null);
        when(attr.getLocalName()).thenReturn("nodeName");

        when(name.getPrefix()).thenReturn(null);
        when(name.getName()).thenReturn("testName");

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.testAttr(attr);

        assertFalse(result);
    }

    @Test
    public void testEqualStrings_withSameStrings() {
        String s1 = "test";
        String s2 = "test";

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.equalStrings(s1, s2);

        assertTrue(result);
    }

    @Test
    public void testEqualStrings_withDifferentStrings() {
        String s1 = "test1";
        String s2 = "test2";

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.equalStrings(s1, s2);

        assertFalse(result);
    }

    @Test
    public void testEqualStrings_withOneStringNull() {
        String s1 = "test1";
        String s2 = null;

        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.equalStrings(s1, s2);

        assertFalse(result);
    }

    @Test
    public void testGetAttribute_withTestPrefixNonNull_and_testNSNonNull_and_attributeNotNull() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn("testPrefix");
        when(attr.getLocalName()).thenReturn("testName");

        when(name.getPrefix()).thenReturn("testPrefix");
        when(name.getName()).thenReturn("testName");

        when(parent.getNamespaceResolver()).thenReturn(new DOMNamespaceResolver());

        when(element.getAttributeNodeNS("testNS", "testName")).thenReturn(attr);

        attributeIterator = new DOMAttributeIterator(parent, name);

        Attr result = attributeIterator.getAttribute(element, name);

        assertEquals(attr, result);
    }

    @Test
    public void testGetAttribute_withTestPrefixNonNull_and_testNSNonNull_and_attributeNull() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn("testPrefix");
        when(attr.getLocalName()).thenReturn("testName");

        when(name.getPrefix()).thenReturn("testPrefix");
        when(name.getName()).thenReturn("testName");

        when(parent.getNamespaceResolver()).thenReturn(new DOMNamespaceResolver());

        when(element.getAttributeNodeNS("testNS", "testName")).thenReturn(null);
        when(element.getAttributes()).thenReturn(attributes);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.item(0)).thenReturn(attr);

        attributeIterator = new DOMAttributeIterator(parent, name);

        Attr result = attributeIterator.getAttribute(element, name);

        assertEquals(attr, result);
    }

    @Test
    public void testGetAttribute_withTestPrefixNonNull_and_testNSNonNull_and_attributeNull_and_attributeListEmpty() {
        when(name.getPrefix()).thenReturn("testPrefix");
        when(name.getName()).thenReturn("testName");

        when(parent.getNamespaceResolver()).thenReturn(new DOMNamespaceResolver());

        when(element.getAttributeNodeNS("testNS", "testName")).thenReturn(null);
        when(element.getAttributes()).thenReturn(attributes);

        when(attributes.getLength()).thenReturn(0);

        attributeIterator = new DOMAttributeIterator(parent, name);

        Attr result = attributeIterator.getAttribute(element, name);

        assertNull(result);
    }

    @Test
    public void testGetAttribute_withTestPrefixNull_and_attributeNotNull() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn(null);
        when(attr.getLocalName()).thenReturn("testName");

        when(name.getPrefix()).thenReturn(null);
        when(name.getName()).thenReturn("testName");

        when(element.getAttributeNode("testName")).thenReturn(attr);

        attributeIterator = new DOMAttributeIterator(parent, name);

        Attr result = attributeIterator.getAttribute(element, name);

        assertEquals(attr, result);
    }

    @Test
    public void testGetAttribute_withTestPrefixNull_and_attributeNull() {
        when(attr.getPrefix()).thenReturn(null);
        when(attr.getLocalName()).thenReturn("testName");

        when(name.getPrefix()).thenReturn(null);
        when(name.getName()).thenReturn("testName");

        when(element.getAttributeNode("testName")).thenReturn(null);
        when(element.getAttributes()).thenReturn(attributes);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.item(0)).thenReturn(attr);

        attributeIterator = new DOMAttributeIterator(parent, name);

        Attr result = attributeIterator.getAttribute(element, name);

        assertEquals(attr, result);
    }

    @Test
    public void testGetAttribute_withTestPrefixNull_and_attributeNull_and_attributeListEmpty() {
        when(name.getPrefix()).thenReturn(null);
        when(name.getName()).thenReturn("testName");

        when(element.getAttributeNode("testName")).thenReturn(null);
        when(element.getAttributes()).thenReturn(attributes);

        when(attributes.getLength()).thenReturn(0);

        attributeIterator = new DOMAttributeIterator(parent, name);

        Attr result = attributeIterator.getAttribute(element, name);

        assertNull(result);
    }

    @Test
    public void testGetNodePointer_withPositionZero() {
        attributeIterator = new DOMAttributeIterator(parent, name);
        attributeIterator.position = 0;

        NodePointer result = attributeIterator.getNodePointer();

        assertNull(result);
    }

    @Test
    public void testGetNodePointer_withPositionOne() {
        attributeIterator = new DOMAttributeIterator(parent, name);
        attributeIterator.position = 1;

        NodePointer attributePointer = mock(DOMAttributePointer.class);
        when(attributes.get(0)).thenReturn(attributePointer);

        NodePointer result = attributeIterator.getNodePointer();

        assertEquals(attributePointer, result);
    }

    @Test
    public void testGetPosition() {
        attributeIterator = new DOMAttributeIterator(parent, name);
        attributeIterator.position = 5;

        int result = attributeIterator.getPosition();

        assertEquals(5, result);
    }

    @Test
    public void testSetPosition_withValidPosition() {
        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.setPosition(5);

        assertTrue(result);
        assertEquals(5, attributeIterator.position);
    }

    @Test
    public void testSetPosition_withInvalidPosition() {
        attributeIterator = new DOMAttributeIterator(parent, name);

        boolean result = attributeIterator.setPosition(0);

        assertFalse(result);
        assertEquals(0, attributeIterator.position);
    }
}