package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {
    private static NodePointer parent;
    private static QName name;
    private static Node node;
    private static Attr[] attrs;

    @BeforeClass
    public static void initialize() {
        parent = Mockito.mock(NodePointer.class);
        name = new QName("*", "", "");
        node = Mockito.mock(Node.class);
        attrs = new Attr[3];

        Mockito.when(parent.getNode()).thenReturn(node);
        Mockito.when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        Mockito.when(node.getAttributes()).thenReturn(Mockito.mock(NamedNodeMap.class));

        for (int i = 0; i < attrs.length; i++) {
            attrs[i] = Mockito.mock(Attr.class);
            Mockito.when(attrs[i].getPrefix()).thenReturn("");
            Mockito.when(attrs[i].getLocalName()).thenReturn("attr" + i);
            Mockito.when(attrs[i].getName()).thenReturn("attr" + i);
            Mockito.when(attrs[i].getNamespaceURI()).thenReturn("");
        }

        Mockito.when(attrs[0].getPrefix()).thenReturn("prefix");
        Mockito.when(attrs[0].getLocalName()).thenReturn("attr0");
        Mockito.when(attrs[0].getName()).thenReturn("prefix:attr0");
        Mockito.when(attrs[0].getNamespaceURI()).thenReturn("namespaceURI");
    }

    @Test
    public void testConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        List<Attr> attributes = new ArrayList<>();
        attributes.add(attrs[0]);
        attributes.add(attrs[1]);
        attributes.add(attrs[2]);

        Mockito.when(parent.getNamespaceURI(Mockito.anyString())).thenReturn("");
        Mockito.when(parent.getNamespaceURI("prefix")).thenReturn("namespaceURI");

        Mockito.when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        Mockito.when(node.getAttributes()).thenReturn(Mockito.mock(NamedNodeMap.class));

        Mockito.when(node.getAttributes().getLength()).thenReturn(attributes.size());
        for (int i = 0; i < attributes.size(); i++) {
            Mockito.when(node.getAttributes().item(i)).thenReturn(attributes.get(i));
        }

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        NodePointer pointer = iterator.getNodePointer();
        assertTrue(pointer instanceof DOMAttributePointer);
        assertEquals(attrs[0], ((DOMAttributePointer) pointer).getAttr());
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(2));

        pointer = iterator.getNodePointer();
        assertTrue(pointer instanceof DOMAttributePointer);
        assertEquals(attrs[1], ((DOMAttributePointer) pointer).getAttr());
        assertEquals(2, iterator.getPosition());

        assertTrue(iterator.setPosition(3));

        pointer = iterator.getNodePointer();
        assertTrue(pointer instanceof DOMAttributePointer);
        assertEquals(attrs[2], ((DOMAttributePointer) pointer).getAttr());
        assertEquals(3, iterator.getPosition());

        assertNull(iterator.getNodePointer());

        assertTrue(iterator.setPosition(4));
        assertNull(iterator.getNodePointer());

        assertTrue(iterator.setPosition(0));

        pointer = iterator.getNodePointer();
        assertTrue(pointer instanceof DOMAttributePointer);
        assertEquals(attrs[0], ((DOMAttributePointer) pointer).getAttr());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());

        assertTrue(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());

        assertTrue(iterator.setPosition(3));
        assertEquals(3, iterator.getPosition());

        assertFalse(iterator.setPosition(4));
        assertEquals(3, iterator.getPosition());

        assertFalse(iterator.setPosition(-1));
        assertEquals(3, iterator.getPosition());
    }

    private boolean assertFalse(boolean position) {
        return !position;
    }

    private boolean assertTrue(boolean position) {
        return position;
    }
}