import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new NodePointer(null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        String xmlns = "xml";
        String namespaceURI = "http://www.w3.org/XML/1998/namespace";
        String className = "JDOMAttributeIteratorTest";

        Element element = new Element("root", "root");
        Namespace xmlNamespace = Namespace.getNamespace(xmlns, namespaceURI);
        Attribute attribute = new Attribute("test", "testValue", xmlNamespace);
        element.addContent(attribute);

        NodePointer parent = new NodePointer(element, xmlNamespace);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "test"));

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(new JDOMAttributePointer(parent, attribute), next);
        assertEquals("test", next.getName().getLocalPart());
        assertEquals("testValue", next.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        String xmlns = "xml";
        String namespaceURI = "http://www.w3.org/XML/1998/namespace";
        String className = "JDOMAttributeIteratorTest";

        Element element = new Element("root", "root");
        Namespace xmlNamespace = Namespace.getNamespace(xmlns, namespaceURI);
        Attribute attribute1 = new Attribute("test1", "testValue1", xmlNamespace);
        Attribute attribute2 = new Attribute("test2", "testValue2", xmlNamespace);
        attribute1.setNamespaceAware(false);
        attribute2.setNamespaceAware(false);
        element.addContent(attribute1);
        element.addContent(attribute2);

        NodePointer parent = new NodePointer(element, xmlNamespace);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "test"));

        assertTrue(iterator.hasNext());
        NodePointer next1 = iterator.next();
        assertEquals(new JDOMAttributePointer(parent, attribute1), next1);
        assertEquals("test1", next1.getName().getLocalPart());
        assertEquals("testValue1", next1.getNodeValue());

        assertTrue(iterator.hasNext());
        NodePointer next2 = iterator.next();
        assertEquals(new JDOMAttributePointer(parent, attribute2), next2);
        assertEquals("test2", next2.getName().getLocalPart());
        assertEquals("testValue2", next2.getNodeValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNamespaceAwareAttributes() {
        String xmlns = "xml";
        String namespaceURI = "http://www.w3.org/XML/1998/namespace";
        String prefix = "xs";
        String className = "JDOMAttributeIteratorTest";

        Element element = new Element("root", "root");
        Namespace xmlNamespace = Namespace.getNamespace(xmlns, namespaceURI);
        Namespace xsdNamespace = Namespace.getNamespace(prefix, "http://www.w3.org/2001/XMLSchema");
        Attribute attribute1 = new Attribute("test1", "testValue1", xmlNamespace);
        Attribute attribute2 = new Attribute("type", "int", xsdNamespace);
        element.addContent(attribute1);
        element.addContent(attribute2);

        NodePointer parent = new NodePointer(element, xmlNamespace);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "test"));

        assertFalse(iterator.hasNext());

        iterator = new JDOMAttributeIterator(parent, new QName(xsdNamespace, "type"));

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(new JDOMAttributePointer(parent, attribute2), next);
        assertEquals("type", next.getName().getLocalPart());
        assertEquals("int", next.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNoNamespace() {
        String className = "JDOMAttributeIteratorTest";

        Element element = new Element("root", "root");
        Attribute attribute = new Attribute("test", "testValue");
        element.addContent(attribute);

        NodePointer parent = new NodePointer(element, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "test"));

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(new JDOMAttributePointer(parent, attribute), next);
        assertEquals("test", next.getName().getLocalPart());
        assertEquals("testValue", next.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullParent() {
        NodePointer parent = null;
        QName name = new QName("", "");
        new JDOMAttributeIterator(parent, name);
    }
}