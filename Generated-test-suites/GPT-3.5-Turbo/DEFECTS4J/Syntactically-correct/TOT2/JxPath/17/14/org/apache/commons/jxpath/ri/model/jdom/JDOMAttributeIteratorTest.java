import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator jdomAttributeIterator;
    private Element element;

    @Before
    public void setUp() {
        element = new Element("test");
        Element child = new Element("child");
        child.setAttribute(new Attribute("attr1", "value1"));
        child.setAttribute(new Attribute("attr2", "value2"));
        element.addContent(child);
        element.setAttribute(new Attribute("attr3", "value3"));

        JXPathContext context = JXPathContext.newContext(element);
        NodePointer nodePointer = context.getPointer("test");
        jdomAttributeIterator = new JDOMAttributeIterator(nodePointer, new QName("attr", ""));
    }

    @Test
    public void testGetNodePointer() {
        assertNotNull(jdomAttributeIterator.getNodePointer());
        assertEquals("Attribute: attr1 value1", jdomAttributeIterator.getNodePointer().asString());
        assertTrue(jdomAttributeIterator.setPosition(2));
        assertNotNull(jdomAttributeIterator.getNodePointer());
        assertEquals("Attribute: attr2 value2", jdomAttributeIterator.getNodePointer().asString());
        assertTrue(jdomAttributeIterator.setPosition(3));
        assertNotNull(jdomAttributeIterator.getNodePointer());
        assertEquals("Attribute: attr3 value3", jdomAttributeIterator.getNodePointer().asString());
        assertFalse(jdomAttributeIterator.setPosition(4));
        assertNull(jdomAttributeIterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, jdomAttributeIterator.getPosition());
        jdomAttributeIterator.setPosition(1);
        assertEquals(1, jdomAttributeIterator.getPosition());
        jdomAttributeIterator.setPosition(2);
        assertEquals(2, jdomAttributeIterator.getPosition());
        jdomAttributeIterator.setPosition(3);
        assertEquals(3, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(jdomAttributeIterator.setPosition(1));
        assertTrue(jdomAttributeIterator.setPosition(2));
        assertTrue(jdomAttributeIterator.setPosition(3));
        assertFalse(jdomAttributeIterator.setPosition(0));
        assertFalse(jdomAttributeIterator.setPosition(4));
    }
}