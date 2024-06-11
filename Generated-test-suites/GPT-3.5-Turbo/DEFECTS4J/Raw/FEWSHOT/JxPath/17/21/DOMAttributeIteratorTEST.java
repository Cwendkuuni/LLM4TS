package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.xml.namespace.QName;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;
    private Element element;
    private NamedNodeMap attributes;
    private Attr attr1;
    private Attr attr2;
    private Attr attr3;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = new QName("test", "attribute");
        element = mock(Element.class);
        attributes = mock(NamedNodeMap.class);
        attr1 = mock(Attr.class);
        attr2 = mock(Attr.class);
        attr3 = mock(Attr.class);
        when(parent.getNode()).thenReturn(element);
        when(element.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(element.getAttributes()).thenReturn(attributes);
    }

    @Test
    public void testConstructor_nullAttributes() {
        when(attributes.getLength()).thenReturn(0);
        iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testConstructor_noMatchingAttributes() {
        when(attributes.getLength()).thenReturn(3);
        when(attributes.item(0)).thenReturn(attr1);
        when(attributes.item(1)).thenReturn(attr2);
        when(attributes.item(2)).thenReturn(attr3);
        when(attr1.getLocalName()).thenReturn("name1");
        when(attr2.getLocalName()).thenReturn("name2");
        when(attr3.getLocalName()).thenReturn("name3");
        iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testConstructor_matchingAttribute() {
        when(attributes.getLength()).thenReturn(3);
        when(attributes.item(0)).thenReturn(attr1);
        when(attributes.item(1)).thenReturn(attr2);
        when(attributes.item(2)).thenReturn(attr3);
        when(attr1.getLocalName()).thenReturn("name1");
        when(attr2.getLocalName()).thenReturn("attribute");
        when(attr3.getLocalName()).thenReturn("name3");
        iterator = new DOMAttributeIterator(parent, name);
        assertEquals(attr2, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }
}