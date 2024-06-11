package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        Node node = document.getDocumentElement();
        List<Node> children = operationsHelper.children(node);
        assertEquals(2, children.size());

        node = children.get(0);
        children = operationsHelper.children(node);
        assertEquals(0, children.size());

        node = children.get(1);
        children = operationsHelper.children(node);
        assertEquals(2, children.size());
    }

    @Test
    public void testFind() {
        Document document = createDocument();
        Node found = operationsHelper.find(document, "/root");
        assertEquals("root", found.getNodeName());

        found = operationsHelper.find(document, "/root/child1");
        assertEquals("child1", found.getNodeName());

        found = operationsHelper.find(document, "/root/child1/grandchild1");
        assertEquals("grandchild1", found.getNodeName());

        found = operationsHelper.find(document, "/root/child1/grandchild2");
        assertNull(found);

        found = operationsHelper.find(document, "/root/child2");
        assertEquals("child2", found.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        Document document = createDocument();
        List<Node> nodes = operationsHelper.children(document.getDocumentElement());
        Node node = operationsHelper.nodeWithName(nodes, "child1");
        assertEquals("child1", node.getNodeName());

        node = operationsHelper.nodeWithName(nodes, "nonExistent");
        assertNull(node);
    }

    @Test
    public void testSublistWithName() {
        Document document = createDocument();
        List<Node> nodes = operationsHelper.children(document.getDocumentElement());
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child1");
        assertEquals(1, sublist.size());

        sublist = operationsHelper.sublistWithName(nodes, "nonExistent");
        assertEquals(0, sublist.size());
    }

    private Document createDocument() {
        Document document = new DocumentStub();
        
        Node root = document.createElement("root");

        Node child1 = document.createElement("child1");
        Node grandchild1 = document.createElement("grandchild1");
        Node grandchild2 = document.createElement("grandchild2");
        child1.appendChild(grandchild1);
        child1.appendChild(grandchild2);

        Node child2 = document.createElement("child2");

        root.appendChild(child1);
        root.appendChild(child2);

        document.appendChild(root);

        return document;
    }

    private class DocumentStub implements Document {

        @Override
        public Node appendChild(Node newChild) {
            return null;
        }

        @Override
        public NodeList getChildNodes() {
            return null;
        }

        // Implement other required methods
        
    }
}