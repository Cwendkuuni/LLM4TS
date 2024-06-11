package net.sf.xisemele.impl;

import org.junit.*;
import org.w3c.dom.*;

import java.util.*;

import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        // Test case 1: empty node
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        List<Node> result = operationsHelper.children(document);
        assertEquals(0, result.size());

        // Test case 2: node with children
        Element element = document.createElement("parent");
        element.appendChild(document.createElement("child1"));
        element.appendChild(document.createElement("child2"));
        document.appendChild(element);
        result = operationsHelper.children(document);
        assertEquals(2, result.size());
    }

    @Test
    public void testFind() {
        // Test case 1: valid path
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element parent = document.createElement("parent");
        Element child1 = document.createElement("child1");
        Element child1_1 = document.createElement("child1_1");
        Element child2 = document.createElement("child2");
        document.appendChild(parent);
        parent.appendChild(child1);
        child1.appendChild(child1_1);
        parent.appendChild(child2);
        Node result = operationsHelper.find(document, "/parent/child1/child1_1");
        assertNotNull(result);
        assertEquals("child1_1", result.getNodeName());

        // Test case 2: invalid path
        result = operationsHelper.find(document, "/parent/child3/child3_1");
        assertNull(result);
    }

    @Test
    public void testNodeWithName() {
        // Test case 1: node with matching name
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element parent = document.createElement("parent");
        Element child1 = document.createElement("child");
        Element child2 = document.createElement("child");
        document.appendChild(parent);
        parent.appendChild(child1);
        parent.appendChild(child2);
        List<Node> nodes = operationsHelper.children(parent);
        Node result = operationsHelper.nodeWithName(nodes, "child");
        assertNotNull(result);
        assertEquals("child", result.getNodeName());

        // Test case 2: node without matching name
        result = operationsHelper.nodeWithName(nodes, "child3");
        assertNull(result);
    }

    @Test
    public void testSublistWithName() {
        // Test case 1: nodes with matching names
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element parent = document.createElement("parent");
        Element child1 = document.createElement("child");
        Element child2 = document.createElement("child");
        Element child3 = document.createElement("child3");
        document.appendChild(parent);
        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.appendChild(child3);
        List<Node> nodes = operationsHelper.children(parent);
        List<Node> result = operationsHelper.sublistWithName(nodes, "child");
        assertNotNull(result);
        assertEquals(2, result.size());

        // Test case 2: nodes without matching names
        result = operationsHelper.sublistWithName(nodes, "child4");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}