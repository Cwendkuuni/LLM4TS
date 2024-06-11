import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;

    @Before
    public void setUp() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        parent = new JDOMNodePointer(element);
    }

    @Test
    public void testGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        NodePointer nodePointer = iterator.getNodePointer();

        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);

        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Attribute attribute = attributePointer.getAttribute();

        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertEquals(0, iterator.getPosition());

        iterator.getNodePointer();
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));

        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));

        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        // Testing with invalid attribute name
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("nonExistentAttribute"));
        assertNull(iterator.getNodePointer());

        // Testing with empty attributes list
        Element emptyElement = new Element("emptyElement");
        parent = new JDOMNodePointer(emptyElement);
        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertNull(iterator.getNodePointer());
    }
}