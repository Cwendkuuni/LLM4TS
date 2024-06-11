package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private JDOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = new QName("prefix", "name");

        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        Attribute attribute = mock(Attribute.class);
        JDOMAttributePointer expectedPointer = new JDOMAttributePointer(parent, attribute);

        assertEquals(expectedPointer, iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertEquals(false, iterator.setPosition(0));
        assertEquals(true, iterator.setPosition(1));
        assertEquals(false, iterator.setPosition(2));
    }
}