package net.sf.xisemele.impl;

import org.junit.Test;
import org.junit.Before;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl operationsHelper;
    private Document document;
    
    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
        document = createTestDocument();
    }
    
    @Test
    public void testChildren_TypicalCase() {
        NodeList nodeList = document.getChildNodes();
        List<Node> expectedChildren = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
                expectedChildren.add(node);
        }
        List<Node> actualChildren = operationsHelper.children(document);
        assertEquals("Incorrect list of children nodes.", expectedChildren, actualChildren);
    }
    
    @Test
    public void testChildren_EmptyNode() {
        Node emptyNode = document.createTextNode("");
        List<Node> expectedChildren = new ArrayList<>();
        List<Node> actualChildren = operationsHelper.children(emptyNode);
        assertEquals("Incorrect list of children nodes for an empty node.", expectedChildren, actualChildren);
    }
    
    @Test
    public void testChildren_NoChildNodes() {
        Node elementNode = document.createElement("element");
        List<Node> expectedChildren = new ArrayList<>();
        List<Node> actualChildren = operationsHelper.children(elementNode);
        assertEquals("Incorrect list of children nodes for an element node with no child nodes.", expectedChildren, actualChildren);
    }
    
    @Test
    public void testFind_TypicalCase() {
        String path = "root/child/grandchild";
        Node expectedNode = document.getElementsByTagName("grandchild").item(0);
        Node actualNode = operationsHelper.find(document, path);
        assertEquals("Incorrect node found.", expectedNode, actualNode);
    }
    
    @Test
    public void testFind_RootNodeNotFound() {
        String path = "invalidroot/child";
        Node actualNode = operationsHelper.find(document, path);
        assertNull("Node found but expected no node to be found.", actualNode);
    }
    
    @Test
    public void testFind_MiddleNodeNotFound() {
        String path = "root/invalidchild/grandchild";
        Node actualNode = operationsHelper.find(document, path);
        assertNull("Node found but expected no node to be found.", actualNode);
    }
    
    @Test
    public void testFind_LastNodeNotFound() {
        String path = "root/child/invalidgrandchild";
        Node actualNode = operationsHelper.find(document, path);
        assertNull("Node found but expected no node to be found.", actualNode);
    }
    
    @Test
    public void testNodeWithName_TypicalCase() {
        List<Node> nodes = operationsHelper.children(document);
        String name = "child";
        Node expectedNode = document.getElementsByTagName(name).item(0);
        Node actualNode = operationsHelper.nodeWithName(nodes, name);
        assertEquals("Incorrect node found.", expectedNode, actualNode);
    }
    
    @Test
    public void testNodeWithName_NodeNotFound() {
        List<Node> nodes = operationsHelper.children(document);
        String name = "invalidchild";
        Node actualNode = operationsHelper.nodeWithName(nodes, name);
        assertNull("Node found but expected no node to be found.", actualNode);
    }
    
    @Test
    public void testSublistWithName_TypicalCase() {
        List<Node> nodes = operationsHelper.children(document);
        String name = "child";
        List<Node> expectedSublist = document.getElementsByTagName(name);
        List<Node> actualSublist = operationsHelper.sublistWithName(nodes, name);
        assertEquals("Incorrect sublist of nodes found.", expectedSublist, actualSublist);
    }
    
    @Test
    public void testSublistWithName_NoMatch() {
        List<Node> nodes = operationsHelper.children(document);
        String name = "invalidchild";
        List<Node> expectedSublist = new ArrayList<>();
        List<Node> actualSublist = operationsHelper.sublistWithName(nodes, name);
        assertEquals("Incorrect sublist of nodes found.", expectedSublist, actualSublist);
    }
    
    private Document createTestDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            
            Element root = document.createElement("root");
            Element child1 = document.createElement("child");
            Element child2 = document.createElement("child");
            Element grandchild = document.createElement("grandchild");

            child1.appendChild(grandchild);
            root.appendChild(child1);
            root.appendChild(child2);
            document.appendChild(root);

            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}