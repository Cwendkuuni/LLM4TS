package net.sf.xisemele.impl;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {
    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

    @Test
    public void testChildren() {
        Document document = createDocument();
        Node parent = document.getDocumentElement();
        List<Node> children = operationsHelper.children(parent);

        Assert.assertEquals(3, children.size());
    }

    @Test
    public void testFind() {
        Document document = createDocument();
        Node found = operationsHelper.find(document, "parent/child2");

        Assert.assertNotNull(found);
        Assert.assertEquals("child2", found.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        Document document = createDocument();
        Node parent = document.getDocumentElement();

        List<Node> nodes = operationsHelper.children(parent);
        Node node = operationsHelper.nodeWithName(nodes, "child2");

        Assert.assertNotNull(node);
        Assert.assertEquals("child2", node.getNodeName());
    }

    @Test
    public void testSublistWithName() {
        Document document = createDocument();
        Node parent = document.getDocumentElement();

        List<Node> children = operationsHelper.children(parent);
        List<Node> sublist = operationsHelper.sublistWithName(children, "child");

        Assert.assertEquals(2, sublist.size());
    }

    private Document createDocument() {
        Document document = null;

        // Create an XML document to use for testing
        // Initialize the document with required elements

        return document;
    }
}