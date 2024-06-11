package net.sf.xisemele.impl;

import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl helper;
    
    @Before
    public void setUp() {
        helper = new OperationsHelperImpl();
    }
    
    @Test
    public void testChildren() {
        // Test case 1: node with children
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            root.appendChild(child1);
            root.appendChild(child2);
            List<Node> result = helper.children(root);
            assertEquals(2, result.size());
            assertSame(child1, result.get(0));
            assertSame(child2, result.get(1));
        } catch (ParserConfigurationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Test case 2: node without children
        Node node = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("empty");
        List<Node> result = helper.children(node);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testFind() {
        // Test case 1: path exists in the document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            root.appendChild(child1);
            root.appendChild(child2);
            document.appendChild(root);
            Node result = helper.find(document, "root/child1");
            assertNotNull(result);
            assertSame(child1, result);
        } catch (ParserConfigurationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Test case 2: path does not exist in the document
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Node result = helper.find(document, "root/child1");
        assertNull(result);
    }
    
    @Test
    public void testNodeWithName() {
        // Test case 1: node with matching name exists
        List<Node> nodes = new ArrayList<Node>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            nodes.add(root);
            Node result = helper.nodeWithName(nodes, "root");
            assertNotNull(result);
            assertSame(root, result);
        } catch (ParserConfigurationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Test case 2: node with matching name does not exist
        Node result = helper.nodeWithName(nodes, "child1");
        assertNull(result);
    }
    
    @Test
    public void testSublistWithName() {
        // Test case 1: nodes with matching name exist
        List<Node> nodes = new ArrayList<Node>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            nodes.add(child1);
            nodes.add(root);
            nodes.add(child2);
            List<Node> result = helper.sublistWithName(nodes, "root");
            assertEquals(1, result.size());
            assertSame(root, result.get(0));
        } catch (ParserConfigurationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Test case 2: nodes with matching name do not exist
        List<Node> result = helper.sublistWithName(nodes, "child3");
        assertTrue(result.isEmpty());
    }
}