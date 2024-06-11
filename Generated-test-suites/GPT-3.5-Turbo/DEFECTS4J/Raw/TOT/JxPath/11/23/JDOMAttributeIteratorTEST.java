import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.util.BasicNodeSet;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for JDOMAttributeIterator class
 */
public class JDOMAttributeIteratorTest {

    private Element element;

    @Before
    public void setUp() {
        element = new Element("testElement");
        element.setNamespace(Namespace.NO_NAMESPACE);

        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
    }

    @Test
    public void testGetNodePointer() {
        JXPathContext context = JXPathContext.newContext(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
            new JDOMAttributePointer(null, null),
            null);

        // Iterate through each attribute
        for (int i = 1; i <= 3; i++) {
            assertTrue(iterator.setPosition(i));
            JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
            assertNotNull(pointer);
            assertTrue(pointer.isAttribute());
            assertEquals("attr" + i, pointer.getName());
            assertEquals("value" + i, pointer.getValue().getText());
        }

        // Test with invalid position
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(4));
    }

    @Test
    public void testGetPosition() {
        JXPathContext context = JXPathContext.newContext(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
            new JDOMAttributePointer(null, null),
            null);

        // Initial position
        assertEquals(0, iterator.getPosition());

        // Iterate through each attribute
        for (int i = 1; i <= 3; i++) {
            assertTrue(iterator.setPosition(i));
            assertEquals(i, iterator.getPosition());
        }

        // Test with invalid position
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(4));
    }

    @Test
    public void testSetPosition() {
        JXPathContext context = JXPathContext.newContext(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
            new JDOMAttributePointer(null, null),
            null);

        // Set valid positions
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());

        assertTrue(iterator.setPosition(3));
        assertEquals(3, iterator.getPosition());

        // Test out of range positions
        assertFalse(iterator.setPosition(0));
        assertEquals(3, iterator.getPosition());

        assertFalse(iterator.setPosition(4));
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testAttributesWithNamespace() {
        Namespace ns = Namespace.getNamespace("prefix", "http://example.com");
        element.setAttribute("attr4", "value4", ns);
        element.setAttribute("attr5", "value5", ns);
        element.setAttribute("attr6", "value6", ns);

        JXPathContext context = JXPathContext.newContext(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
            new JDOMAttributePointer(null, null),
            null);

        // Iterate through each attribute
        for (int i = 4; i <= 6; i++) {
            assertTrue(iterator.setPosition(i));
            JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
            assertNotNull(pointer);
            assertTrue(pointer.isAttribute());
            assertEquals("attr" + i, pointer.getName());
            assertEquals("value" + i, pointer.getValue().getText());
        }
    }

    @Test
    public void testAttributesWithXmlNamespace() {
        Namespace ns = Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        element.setAttribute("attr7", "value7", ns);
        element.setAttribute("attr8", "value8", ns);
        element.setAttribute("attr9", "value9", ns);
        
        JXPathContext context = JXPathContext.newContext(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
            new JDOMAttributePointer(null, null),
            null);

        // Iterate through each attribute
        for (int i = 7; i <= 9; i++) {
            assertTrue(iterator.setPosition(i));
            JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
            assertNotNull(pointer);
            assertTrue(pointer.isAttribute());
            assertEquals("attr" + i, pointer.getName());
            assertEquals("value" + i, pointer.getValue().getText());
        }
    }

    @Test
    public void testWildcardName() {
        JXPathContext context = JXPathContext.newContext(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
            new JDOMAttributePointer(null, null),
            new QName("*"));

        // Iterate through each attribute
        for (int i = 1; i <= 3; i++) {
            assertTrue(iterator.setPosition(i));
            JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
            assertNotNull(pointer);
            assertTrue(pointer.isAttribute());
            assertEquals("attr" + i, pointer.getName());
            assertEquals("value" + i, pointer.getValue().getText());
        }
    }

    @Test
    public void testWildcardNameWithNamespace() {
        Namespace ns = Namespace.getNamespace("prefix", "http://example.com");
        element.setAttribute("attr4", "value4", ns);
        element.setAttribute("attr5", "value5", ns);
        element.setAttribute("attr6", "value6", ns);

        JXPathContext context = JXPathContext.newContext(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
            new JDOMAttributePointer(null, null),
            new QName("*", "prefix"));

        // Iterate through each attribute
        for (int i = 4; i <= 6; i++) {
            assertTrue(iterator.setPosition(i));
            JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
            assertNotNull(pointer);
            assertTrue(pointer.isAttribute());
            assertEquals("attr" + i, pointer.getName());
            assertEquals("value" + i, pointer.getValue().getText());
        }
    }

}