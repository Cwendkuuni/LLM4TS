package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.w3c.dom.*;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test"));
        List<NodePointer> expectedList = createTestAttributeList();
        assertEquals(expectedList.size(), iterator.size());
        for (int i = 0; i < expectedList.size(); i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            assertEquals(expectedList.get(i).getNode().getNodeName(), nodePointer.getNode().getNodeName());
            iterator.next();
        }
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithWildcardQName() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        List<NodePointer> expectedList = createTestAllAttributesList(element);
        assertEquals(expectedList.size(), iterator.size());
        for (int i = 0; i < expectedList.size(); i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            assertEquals(expectedList.get(i).getNode().getNodeName(), nodePointer.getNode().getNodeName());
            iterator.next();
        }
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithPrefix() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:test"));
        List<NodePointer> expectedList = createTestNamespaceList(element);
        assertEquals(expectedList.size(), iterator.size());
        for (int i = 0; i < expectedList.size(); i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            assertEquals(expectedList.get(i).getNode().getNodeName(), nodePointer.getNode().getNodeName());
            iterator.next();
        }
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithEqualPrefix() {
        Element element = createTestElementWithPrefix();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test:test"));
        List<NodePointer> expectedList = createTestAttributeListWithPrefix(element);
        assertEquals(expectedList.size(), iterator.size());
        for (int i = 0; i < expectedList.size(); i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            assertEquals(expectedList.get(i).getNode().getNodeName(), nodePointer.getNode().getNodeName());
            iterator.next();
        }
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyName() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName(""));
        List<NodePointer> expectedList = new ArrayList<>();
        assertEquals(expectedList.size(), iterator.size());
        for (int i = 0; i < expectedList.size(); i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            assertNull(nodePointer.getNode());
            iterator.next();
        }
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithNullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("test"));
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNullName() {
        NodePointer parent = new DOMPointer(createTestElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.getNodePointer());
    }

    private Element createTestElement() {
        Element element = document.createElement("test");
        element.setAttribute("test1", "value1");
        element.setAttributeNS("xmlns:test", "test2", "value2");
        element.setAttribute("test3", "value3");
        document.appendChild(element);
        return element;
    }

    private Element createTestElementWithPrefix() {
        Element element = document.createElement("test:test");
        element.setAttribute("test:test1", "value1");
        element.setAttributeNS("xmlns:test", "test:test2", "value2");
        element.setAttribute("test:test3", "value3");
        document.appendChild(element);
        return element;
    }

    private List<NodePointer> createTestAttributeList() {
        List<NodePointer> list = new ArrayList<>();
        list.add(new DOMAttributePointer(new DOMPointer(createTestElement()), element.getAttributeNode("test1")));
        list.add(new DOMAttributePointer(new DOMPointer(element), element.getAttributeNodeNS("xmlns:test", "test2")));
        list.add(new DOMAttributePointer(new DOMPointer(element), element.getAttributeNode("test3")));
        return list;
    }

    private List<NodePointer> createTestAllAttributesList(Element element) {
        List<NodePointer> list = new ArrayList<>();
        NodeList nodeList = element.getAttributes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            list.add(new DOMAttributePointer(new DOMPointer(element), (Attr) node));
        }
        return list;
    }

    private List<NodePointer> createTestNamespaceList(Element element) {
        List<NodePointer> list = new ArrayList<>();
        list.add(new DOMAttributePointer(new DOMPointer(element), element.getAttributeNodeNS("xmlns:test", "test2")));
        return list;
    }

    private List<NodePointer> createTestAttributeListWithPrefix(Element element) {
        List<NodePointer> list = new ArrayList<>();
        list.add(new DOMAttrributePointer(new DOMPointer(element), element.getAttributeNode("test:test1")));
        return list;
    }

    private Document document = new DocumentImpl();
}