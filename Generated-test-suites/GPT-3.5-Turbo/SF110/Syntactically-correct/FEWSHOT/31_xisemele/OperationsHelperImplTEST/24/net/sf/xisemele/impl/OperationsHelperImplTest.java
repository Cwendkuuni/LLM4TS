package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {
    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        Document document = createDocument();
        Node parentNode = document.getFirstChild();
        List<Node> childrenList = operationsHelper.children(parentNode);

        assertEquals(3, childrenList.size());
        assertEquals("child1", childrenList.get(0).getNodeName());
        assertEquals("child2", childrenList.get(1).getNodeName());
        assertEquals("child3", childrenList.get(2).getNodeName());
    }

    @Test
    public void testFind() {
        Document document = createDocument();
        Node foundNode = operationsHelper.find(document, "/root/child1");

        assertNotNull(foundNode);
        assertEquals("child1", foundNode.getNodeName());
    }

    @Test
    public void testFind_NoMatchingNode() {
        Document document = createDocument();
        Node foundNode = operationsHelper.find(document, "/root/child4");

        assertNull(foundNode);
    }

    @Test
    public void testNodeWithName() {
        Document document = createDocument();
        List<Node> nodeList = operationsHelper.children(document.getFirstChild());
        Node node = operationsHelper.nodeWithName(nodeList, "child1");

        assertNotNull(node);
        assertEquals("child1", node.getNodeName());
    }

    @Test
    public void testNodeWithName_NodeNotFound() {
        Document document = createDocument();
        List<Node> nodeList = operationsHelper.children(document.getFirstChild());
        Node node = operationsHelper.nodeWithName(nodeList, "child4");

        assertNull(node);
    }

    @Test
    public void testSublistWithName() {
        Document document = createDocument();
        List<Node> nodeList = operationsHelper.children(document.getFirstChild());
        List<Node> sublist = operationsHelper.sublistWithName(nodeList, "child1");

        assertEquals(1, sublist.size());
        assertEquals("child1", sublist.get(0).getNodeName());
    }

    @Test
    public void testSublistWithName_NoMatchingNodes() {
        Document document = createDocument();
        List<Node> nodeList = operationsHelper.children(document.getFirstChild());
        List<Node> sublist = operationsHelper.sublistWithName(nodeList, "child4");

        assertEquals(0, sublist.size());
    }

    private Document createDocument() {
        Document document = null;
        // code to create and populate the document
        return document;
    }
}