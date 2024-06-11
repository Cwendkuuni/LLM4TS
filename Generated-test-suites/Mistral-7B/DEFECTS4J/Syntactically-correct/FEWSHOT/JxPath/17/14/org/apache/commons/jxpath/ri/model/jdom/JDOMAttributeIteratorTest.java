package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private Element element;
    private QName name;
    private NodePointer parent;
    private JDOMAttributeIterator iterator;
    private List<Attribute> attributes;

    @Before
    public void setup() {
        element = mock(Element.class);
        name = new QName("test", "namespaceURI", "prefix");
        parent = new NodePointer(mock(NodeIterator.class), new JDOMNodePointer(element, null));

        attributes = new ArrayList<>();
        Attribute attr1 = new Attribute("attr1", "value1", null);
        attr1.setNamespace(new Namespace("namespace1", "namespaceURI1"));
        Attribute attr2 = new Attribute("attr2", "value2", null);
        Attribute attr3 = new Attribute("attr3", "value3", null);
        attributes.add(attr1);
        attributes.add(attr2);
        attributes.add(attr3);

        when(element.getAttributes()).thenReturn(attributes.subList(0, 3));
    }

    @Test
    public void testJDOMAttributeIterator() {
        iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        iterator = new JDOMAttributeIterator(parent, name);

        Attribute expected = attributes.get(0);
        NodePointer actual = iterator.getNodePointer();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) actual;
        assertEquals(expected, attributePointer.getAttribute());
    }

    @Test
    public void testSetPosition() {
        iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(1);
        Attribute expected = attributes.get(1);
        NodePointer actual = iterator.getNodePointer();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) actual;
        assertEquals(expected, attributePointer.getAttribute());
    }

    @Test
    public void testSetPositionOutOfBounds() {
        iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(4);
        assertNull(iterator.getNodePointer());
    }
}