import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private JXPathContext context;
    private Element element;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        element = new Element("test");
        element.setAttribute(new Attribute("attr1", "value1"));
        element.setAttribute(new Attribute("attr2", "value2"));
        element.setAttribute(new Attribute("attr3", "value3"));
        context = JXPathContext.newContext(element);
        iterator = new JDOMAttributeIterator(
            (NodePointer) context.getPointer("/test"), new QName("", "*"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer pointer = iterator.getNodePointer();
        assertTrue(pointer instanceof JDOMAttributePointer);
        assertEquals("attr1", ((Attribute) pointer.getNode()).getName());
        assertEquals("value1", ((Attribute) pointer.getNode()).getValue());
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
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(3));
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.setPosition(4));
        assertEquals(3, iterator.getPosition());
    }
}