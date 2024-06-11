
package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullNode;
import org.apache.commons.jxpath.ri.xpath.XPathParser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;

public class JDOMAttributeIteratorTest {

    private static Element rootElement;
    private static Element testElement;
    private static Attribute testAttribute;

    @BeforeClass
    public static void setUp() {
        rootElement = new Element("root");
        testElement = new Element("test");
        testAttribute = new Attribute("attr", "value");
        testElement.setAttribute(testAttribute);
        rootElement.addContent(testElement);
    }

    @Test
    public void testConstructor() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(null, rootElement), null);
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(null, rootElement), null);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(testAttribute, ((JDOMAttributePointer) nodePointer).getAttribute());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(null, rootElement), null);
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());

        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(null, rootElement), null);
        assertTrue(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertFalse(iterator.setPosition(-1));
        assertEquals(1, iterator.getPosition());

        assertFalse(iterator.setPosition(100));
        assertEquals(1, iterator.getPosition());
    }
}
