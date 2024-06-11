import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

    private NodePointer parentNode;
    private Element rootElement;

    @Before
    public void setUp() {
        rootElement = new Element("root");
        rootElement.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
        rootElement.addNamespaceDeclaration("test", "http://test.com");

        Element childElement = new Element("child");
        childElement.addNamespaceDeclaration("test", "http://test.com");
        childElement.addAttribute("attr1", "value1", "test");
        childElement.addAttribute("attr2", "value2", "xml");
        childElement.addAttribute("attr3", "value3");
        rootElement.addContent(childElement);

        parentNode = new NodePointer(rootElement, null);
    }

    @Test
    public void testEmptyIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("", "*"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithSingleAttribute() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("child", "attr1"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        assertSame(parentNode, nodePointer.getParent());
        assertSame("attr1", nodePointer.getNodeName().getLocalPart());
        assertEquals("value1", nodePointer.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("child", "*"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer1 = iterator.next();
        assertSame(parentNode, nodePointer1.getParent());
        assertSame("attr1", nodePointer1.getNodeName().getLocalPart());
        assertEquals("value1", nodePointer1.getNodeValue());

        assertTrue(iterator.hasNext());
        NodePointer nodePointer2 = iterator.next();
        assertSame(parentNode, nodePointer2.getParent());
        assertSame("attr2", nodePointer2.getNodeName().getLocalPart());
        assertEquals("value2", nodePointer2.getNodeValue());

        assertTrue(iterator.hasNext());
        NodePointer nodePointer3 = iterator.next();
        assertSame(parentNode, nodePointer3.getParent());
        assertSame("attr3", nodePointer3.getNodeName().getLocalPart());
        assertEquals("value3", nodePointer3.getNodeValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMixedNamespaces() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("child", "attr2"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        assertSame(parentNode, nodePointer.getParent());
        assertSame("attr2", nodePointer.getNodeName().getLocalPart());
        assertEquals("value2", nodePointer.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMixedPrefixes() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("child", "xml:attr3"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        assertSame(parentNode, nodePointer.getParent());
        assertSame("attr3", nodePointer.getNodeName().getLocalPart());
        assertEquals("value3", nodePointer.getNodeValue());
        assertFalse(iterator.hasNext());
    }
}