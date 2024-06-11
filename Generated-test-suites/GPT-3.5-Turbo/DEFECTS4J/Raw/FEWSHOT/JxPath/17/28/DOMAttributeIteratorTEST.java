package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setup() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        // Test initialization of variables
        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        assertNull(iterator.attributes);
        assertEquals(0, iterator.position);
    }

    @Test
    public void testGetNodePointer() {
        // Test with position = 0
        iterator.position = 0;
        assertNull(iterator.getNodePointer());

        // Test with position = 1
        iterator.position = 1;

        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);

        Attr attr = mock(Attr.class);
        when(iterator.attributes.get(0)).thenReturn(attr);

        DOMAttributePointer pointer = mock(DOMAttributePointer.class);
        whenNew(DOMAttributePointer.class).withArguments(parent, attr).thenReturn(pointer);

        assertEquals(pointer, iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        // Test with position = 0
        assertEquals(0, iterator.getPosition());

        // Test with position > 0
        iterator.position = 2;
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        // Test with valid position
        int position = 2;
        iterator.attributes = new ArrayList<Attr>();
        iterator.attributes.add(mock(Attr.class));
        iterator.attributes.add(mock(Attr.class));

        assertTrue(iterator.setPosition(position));
        assertEquals(position, iterator.position);

        // Test with position < 1
        assertFalse(iterator.setPosition(0));
        assertEquals(position, iterator.position);

        // Test with position > attributes.size()
        assertFalse(iterator.setPosition(3));
        assertEquals(position, iterator.position);
    }
}