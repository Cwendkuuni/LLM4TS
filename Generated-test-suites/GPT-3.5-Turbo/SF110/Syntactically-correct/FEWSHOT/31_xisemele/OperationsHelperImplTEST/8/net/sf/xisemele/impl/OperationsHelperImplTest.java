

package net.sf.xisemele.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        // Test when node has children
        Document document = createDocument();
        Element rootElement = document.getDocumentElement();
        Node child1 = document.createElement("child1");
        Node child2 = document.createElement("child2");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);

        OperationsHelperImpl helper = new OperationsHelperImpl();
        List<Node> children = helper.children(rootElement);
        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));

        // Test when node does not have children
        Node emptyNode = document.createElement("empty");
        List<Node> emptyChildren = helper.children(emptyNode);
        assertTrue(emptyChildren.isEmpty());
    }

    @Test
    public void testFind() {
        Document document = createDocument();
        Element rootElement = document.getDocumentElement();
        Element child1 = document.createElement("child1");
        Element subchild = document.createElement("subchild");
        subchild.setTextContent("content");
        child1.appendChild(subchild);
        rootElement.appendChild(child1);

        OperationsHelperImpl helper = new OperationsHelperImpl();

        // Test finding the root element
        Node root = helper.find(document, "root");
        assertEquals(rootElement, root);

        // Test finding a child element
        Node child = helper.find(document, "root/child1");
        assertEquals(child1, child);

        // Test finding a nested child element
        Node nestedChild = helper.find(document, "root/child1/subchild");
        assertEquals(subchild, nestedChild);

        // Test finding a non-existing element
        Node nonExisting = helper.find(document, "root/nonexisting");
        assertNull(nonExisting);
    }

    @Test
    public void testNodeWithName() {
        Document document = createDocument();
        Element rootElement = document.getDocumentElement();
        Element child1 = document.createElement("child");
        child1.setTextContent("content1");
        Element child2 = document.createElement("child");
        child2.setTextContent("content2");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);

        OperationsHelperImpl helper = new OperationsHelperImpl();

        // Test finding a node with matching name
        List<Node> children = helper.children(rootElement);
        Node matchingNode = helper.nodeWithName(children, "child");
        assertEquals(child1, matchingNode);

        // Test finding a node with non-matching name
        Node nonMatchingNode = helper.nodeWithName(children, "nonmatching");
        assertNull(nonMatchingNode);
    }

    @Test
    public void testSublistWithName() {
        Document document = createDocument();
        Element rootElement = document.getDocumentElement();
        Element child1 = document.createElement("child");
        Element child2 = document.createElement("child");
        Element child3 = document.createElement("other");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        rootElement.appendChild(child3);

        OperationsHelperImpl helper = new OperationsHelperImpl();

        // Test creating a sublist with matching name
        List<Node> children = helper.children(rootElement);
        List<Node> matchingSublist = helper.sublistWithName(children, "child");
        assertEquals(2, matchingSublist.size());
        assertTrue(matchingSublist.contains(child1));
        assertTrue(matchingSublist.contains(child2));

        // Test creating a sublist with non-matching name
        List<Node> nonMatchingSublist = helper.sublistWithName(children, "nonmatching");
        assertTrue(nonMatchingSublist.isEmpty());
    }

    private Document createDocument() {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
}
