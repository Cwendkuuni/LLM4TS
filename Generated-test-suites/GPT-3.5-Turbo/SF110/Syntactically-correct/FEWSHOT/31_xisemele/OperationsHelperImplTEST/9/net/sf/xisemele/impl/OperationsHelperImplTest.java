package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

    private OperationsHelperImpl helper;
    private Document document;

    @Before
    public void setUp() {
        helper = new OperationsHelperImpl();
        document = new DocumentStub();
    }

    private static class DocumentStub implements Document {
        // Implement the necessary methods for the stub

        @Override
        public Element getDocumentElement() {
            return new ElementStub();
        }

        // Other implemented methods
    }

    private static class ElementStub implements Element {
        // Implement the necessary methods for the stub

        @Override
        public String getNodeName() {
            return "element";
        }

        // Other implemented methods
    }

    @Test
    public void testChildren() {
        List<Node> children = helper.children(document);
        assertEquals(0, children.size());

        // Add child nodes to the document element and test again
        ElementStub element = (ElementStub) document.getDocumentElement();
        element.addChild(new NodeStub());
        element.addChild(new NodeStub());

        children = helper.children(document);
        assertEquals(2, children.size());
        assertTrue(children.get(0) instanceof NodeStub);
        assertTrue(children.get(1) instanceof NodeStub);
    }

    @Test
    public void testFind() {
        Node node = helper.find(document, "element");
        assertEquals(document.getDocumentElement(), node);

        // Create nested elements and test finding inner elements
        ElementStub element1 = (ElementStub) document.getDocumentElement();
        ElementStub element2 = new ElementStub();
        element1.addChild(element2);

        element2.addChild(new NodeStub());
        element2.addChild(new NodeStub());

        node = helper.find(document, "element/element");
        assertEquals(element2, node);

        node = helper.find(document, "element/element/element");
        assertNull(node);
    }

    @Test
    public void testNodeWithName() {
        List<Node> nodes = new ArrayList<Node>();
        nodes.add(new NodeStub());
        nodes.add(new NodeStub());

        Node node = helper.nodeWithName(nodes, "node");
        assertNull(node);

        node = helper.nodeWithName(nodes, "element");
        assertTrue(node instanceof NodeStub);
    }

    @Test
    public void testSublistWithName() {
        List<Node> nodes = new ArrayList<Node>();
        nodes.add(new NodeStub());
        nodes.add(new NodeStub());
        nodes.add(new NodeStub());

        List<Node> sublist = helper.sublistWithName(nodes, "node");
        assertEquals(0, sublist.size());

        sublist = helper.sublistWithName(nodes, "element");
        assertEquals(3, sublist.size());
        assertTrue(sublist.get(0) instanceof NodeStub);
        assertTrue(sublist.get(1) instanceof NodeStub);
        assertTrue(sublist.get(2) instanceof NodeStub);
    }

    private static class NodeStub implements Node {
        // Implement the necessary methods for the stub

        // Other implemented methods
    }
}