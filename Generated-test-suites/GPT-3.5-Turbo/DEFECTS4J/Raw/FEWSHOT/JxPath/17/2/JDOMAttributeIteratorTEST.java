
package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private Element element;
    private List<Attribute> attributes;

    @Before
    public void setUp() {
        parent = Mockito.mock(NodePointer.class);
        element = Mockito.mock(Element.class);
        attributes = new ArrayList<>();
        Mockito.when(parent.getNode()).thenReturn(element);
        Mockito.when(element.getAttributes()).thenReturn(attributes);
    }

    @Test
    public void testConstructorWithNonMatchingName() {
        name = new QName("nonMatching", "prefix");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.attributes.isEmpty());
    }

    @Test
    public void testConstructorWithMatchingName() {
        name = new QName("matching", "prefix");
        Mockito.when(parent.getNamespaceResolver()).thenReturn(Mockito.mock(NamespaceResolver.class));
        NamespaceResolver namespaceResolver = parent.getNamespaceResolver();
        Mockito.when(namespaceResolver.getNamespaceURI(name.getPrefix())).thenReturn("namespaceURI");
        Mockito.when(element.getAttribute(name.getName(), Namespace.getNamespace(name.getPrefix(), "namespaceURI"))).thenReturn(Mockito.mock(Attribute.class));

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertFalse(iterator.attributes.isEmpty());
    }

    @Test
    public void testConstructorWithWildcardName() {
        name = new QName("*", "prefix");
        Mockito.when(element.getAttributes()).thenReturn(attributes);
        attributes.add(Mockito.mock(Attribute.class));
        attributes.add(Mockito.mock(Attribute.class));
        attributes.add(Mockito.mock(Attribute.class));

        Mockito.when(attributes.get(0).getNamespace()).thenReturn(Namespace.getNamespace("namespaceURI"));
        Mockito.when(attributes.get(1).getNamespace()).thenReturn(Namespace.getNamespace("namespaceURI"));
        Mockito.when(attributes.get(2).getNamespace()).thenReturn(Namespace.getNamespace("otherNamespaceURI"));

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertEquals(2, iterator.attributes.size());
    }

    @Test
    public void testGetNodePointer() {
        Mockito.when(attributes.get(Mockito.anyInt())).thenReturn(Mockito.mock(Attribute.class));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(1);
        assertNotNull(iterator.getNodePointer());

        iterator.setPosition(2);
        assertNotNull(iterator.getNodePointer());

        iterator.setPosition(3);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointerWithEmptyAttributes() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());

        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertFalse(iterator.setPosition(-1));
        assertEquals(0, iterator.getPosition());

        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());

        assertFalse(iterator.setPosition(3));
        assertEquals(0, iterator.getPosition());
    }
}
