
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private Element element;

    @Before
    public void setUp() {
        element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
    }

    @Test
    public void testGetNodePointer() {
        JXPathContext context = JXPathContext.newContext(element);
        NodeIterator iterator = new JDOMAttributeIterator(context.getPointer(), new QName("*"));
        
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        JXPathContext context = JXPathContext.newContext(element);
        NodeIterator iterator = new JDOMAttributeIterator(context.getPointer(), new QName("*"));
        
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JXPathContext context = JXPathContext.newContext(element);
        NodeIterator iterator = new JDOMAttributeIterator(context.getPointer(), new QName("*"));
        
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        
        assertFalse(iterator.setPosition(0));
        assertEquals(2, iterator.getPosition());
        
        assertFalse(iterator.setPosition(4));
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithNoAttributes() {
        JXPathContext context = JXPathContext.newContext(Collections.emptyList());
        NodeIterator iterator = new JDOMAttributeIterator(context.getPointer(), new QName("*"));
        
        assertFalse(iterator.setPosition(1));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetNodePointerWithInvalidPosition() {
        JXPathContext context = JXPathContext.newContext(element);
        NodeIterator iterator = new JDOMAttributeIterator(context.getPointer(), new QName("*"));
        
        assertFalse(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());
        
        assertFalse(iterator.setPosition(4));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointerWithNullNamespace() {
        JXPathContext context = JXPathContext.newContext(element);
        NodeIterator iterator = new JDOMAttributeIterator(context.getPointer(), new QName("attr1"));
        
        assertNotNull(iterator.getNodePointer());
        assertEquals("attr1", ((Attribute) iterator.getNodePointer().getNode()).getName());
    }

    @Test
    public void testGetNodePointerWithNamespace() {
        JXPathContext context = JXPathContext.newContext(element);
        NodeIterator iterator = new JDOMAttributeIterator(context.getPointer(), new QName("attr1", "http://example.com"));

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointerWithXmlNamespace() {
        JXPathContext context = JXPathContext.newContext(element);
        NodeIterator iterator = new JDOMAttributeIterator(context.getPointer(), new QName("xml", "lang"));

        assertNull(iterator.getNodePointer());
    }
}