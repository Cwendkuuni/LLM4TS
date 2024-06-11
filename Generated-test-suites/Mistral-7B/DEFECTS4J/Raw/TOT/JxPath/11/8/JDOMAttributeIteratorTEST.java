import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        Element emptyElement = new Element("empty");
        NodeIterator iterator = new JDOMAttributeIterator(new NodePointer(emptyElement), new QName("", ""));
        assertEquals(NodePointer.NULL_POINTER, iterator.getNodePointer());
        assertEquals(-1, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleAttributeIterator() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "xml:lang";
        Namespace namespace = new Namespace(xmlns, prefix);

        Element rootElement = new Element("root");
        rootElement.addNamespaceDeclaration(namespace);
        rootElement.addAttribute(new Attribute(localName, "en"));

        NodePointer rootPointer = new NodePointer(rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(rootPointer, new QName(prefix, localName));

        NodePointer attributePointer = iterator.getNodePointer();
        assertEquals(rootPointer, attributePointer.getParent());
        assertEquals(localName, attributePointer.getName().getLocalPart());
        assertEquals(namespace, attributePointer.getName().getNamespace());
        assertEquals("en", attributePointer.getNode().getValue());

        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleAttributesIterator() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";

        String localName1 = "xml:lang";
        String localName2 = "xml:version";

        Element rootElement = new Element("root");
        rootElement.addNamespaceDeclaration(new Namespace(xmlns, prefix));
        rootElement.addAttribute(new Attribute(localName1, "en"));
        rootElement.addAttribute(new Attribute(localName2, "1.0"));

        NodePointer rootPointer = new NodePointer(rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(rootPointer, new QName(prefix, "*"));

        int count = 0;
        while (iterator.hasNext()) {
            NodePointer attributePointer = iterator.getNodePointer();
            assertEquals(rootPointer, attributePointer.getParent());
            assertEquals(++count, iterator.getPosition());
        }

        assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParent() {
        new JDOMAttributeIterator(null, new QName("", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        NodePointer parentPointer = new NodePointer(new Element("parent"));
        new JDOMAttributeIterator(parentPointer, null);
    }

    @Test
    public void testEmptyName() {
        Element emptyElement = new Element("empty");
        NodeIterator iterator = new JDOMAttributeIterator(new NodePointer(emptyElement), new QName("", ""));
        assertEquals(NodePointer.NULL_POINTER, iterator.getNodePointer());
        assertEquals(-1, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNonExistingName() {
        Element rootElement = new Element("root");
        NodePointer rootPointer = new NodePointer(rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(rootPointer, new QName("nonExisting", ""));
        assertEquals(NodePointer.NULL_POINTER, iterator.getNodePointer());
        assertEquals(-1, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }
}