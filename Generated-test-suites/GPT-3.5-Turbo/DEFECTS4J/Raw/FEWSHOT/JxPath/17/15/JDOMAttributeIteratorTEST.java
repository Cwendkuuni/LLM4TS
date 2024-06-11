import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        Attribute attribute3 = new Attribute("attribute3", "value3");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);
        element.setAttribute(attribute3);

        parent = new NodePointer(element);
        iterator = new JDOMAttributeIterator(parent, new QName(""));
    }

    @Test
    public void testGetNodePointer() {
        assertEquals("attribute1", ((JDOMAttributePointer) iterator.getNodePointer()).getAttribute().getName());
        iterator.setPosition(2);
        assertEquals("attribute2", ((JDOMAttributePointer) iterator.getNodePointer()).getAttribute().getName());
        iterator.setPosition(3);
        assertEquals("attribute3", ((JDOMAttributePointer) iterator.getNodePointer()).getAttribute().getName());
        iterator.setPosition(4);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(3));
        assertFalse(iterator.setPosition(4));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(0));
    }
}