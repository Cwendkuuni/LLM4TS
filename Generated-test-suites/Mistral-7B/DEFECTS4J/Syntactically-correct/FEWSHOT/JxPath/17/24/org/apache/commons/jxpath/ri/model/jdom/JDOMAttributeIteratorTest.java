package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_EmptyAttributeList() {
        NodePointer parentNode = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, null);
        List<NodePointer> actualList = iterator.getNodes();
        assertTrue(Collections.isEmpty(actualList));
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        QName attributeName = new QName("test", "namespaceURI", "localName");
        Element element = new Element("testElement");
        Attribute attribute = new Attribute("attributeName", "attributeValue", null);
        attribute.setNamespace("namespaceURI");
        element.addContent(attribute);
        NodePointer parentNode = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attributeName);
        List<NodePointer> actualList = iterator.getNodes();
        NodePointer expectedNodePointer = new JDOMAttributePointer(parentNode, attribute);
        assertTrue(actualList.contains(expectedNodePointer));
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        QName attributeName = new QName("test", "namespaceURI", "localName");
        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("attributeName1", "attributeValue1", null);
        attribute1.setNamespace("namespaceURI");
        Attribute attribute2 = new Attribute("attributeName2", "attributeValue2", null);
        attribute2.setNamespace("namespaceURI");
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parentNode = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attributeName);
        List<NodePointer> actualList = iterator.getNodes();
        NodePointer expectedNodePointer1 = new JDOMAttributePointer(parentNode, attribute1);
        NodePointer expectedNodePointer2 = new JDOMAttributePointer(parentNode, attribute2);
        List<NodePointer> expectedList = new ArrayList<>();
        expectedList.add(expectedNodePointer1);
        expectedList.add(expectedNodePointer2);
        assertTrue(actualList.containsAll(expectedList));
    }

    @Test
    public void testJDOMAttributeIterator_MixedNamespaces() {
        QName attributeNamePrefix = new QName("xml", null, "xmlns");
        QName attributeName = new QName("test", null, "localName");
        Element element = new Element("testElement");
        element.setAttribute(new QName("xml", null, "xmlns:prefix"), "prefixURI");
        Attribute attribute1 = new Attribute("attributeName1", "attributeValue1", null);
        attribute1.setNamespace("prefixURI");
        Attribute attribute2 = new Attribute("attributeName2", "attributeValue2", null);
        attribute2.setNamespace("namespaceURI");
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parentNode = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attributeName);
        List<NodePointer> actualList = iterator.getNodes();
        NodePointer expectedNodePointer2 = new JDOMAttributePointer(parentNode, attribute2);
        assertTrue(actualList.contains(expectedNodePointer2));
    }

    @Test
    public void testJDOMAttributeIterator_NullParentNode() {
        QName attributeName = new QName("test", null, "localName");
        NodePointer parentNode = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attributeName);
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testJDOMAttributeIterator_IncorrectParentNodeType() {
        QName attributeName = new QName("test", null, "localName");
        NodePointer parentNode = new JDOMNodePointer(new Text("parentText"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attributeName);
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testJDOMAttributeIterator_IncorrectNamespace() {
        QName attributeName = new QName("test", "wrongNamespaceURI", "localName");
        NodePointer parentNode = new JDOMNodePointer(new Element("testElement"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attributeName);
        List<NodePointer> actualList = iterator.getNodes();
        assertTrue(Collections.isEmpty(actualList));
    }

    @Test
    public void testJDOMAttributeIterator_IncorrectAttributeName() {
        QName attributeName = new QName("test", null, "wrongLocalName");
        NodePointer parentNode = new JDOMNodePointer(new Element("testElement"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attributeName);
        List<NodePointer> actualList = iterator.getNodes();
        assertTrue(Collections.isEmpty(actualList));
    }
}