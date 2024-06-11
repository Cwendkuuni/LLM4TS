package net.sf.xisemele.impl;

import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }
    
    @Test
    public void testChildren() {
        // Create a test node
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            document.appendChild(root);
            Element child1 = document.createElement("child1");
            root.appendChild(child1);
            
            // Call the method being tested
            List<Node> result = operationsHelper.children(root);
            
            // Verify the result
            assertEquals(1, result.size());
            assertEquals(child1, result.get(0));
        } catch (ParserConfigurationException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testFind() {
        // Create a test document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            document.appendChild(root);
            Element child1 = document.createElement("child1");
            root.appendChild(child1);

            // Call the method being tested
            Node result = operationsHelper.find(document, "root/child1");
            
            // Verify the result
            assertNotNull(result);
            assertEquals(child1, result);
        } catch (ParserConfigurationException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testNodeWithName() {
        // Create a list of test nodes
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            document.appendChild(root);
            Element child1 = document.createElement("child1");
            root.appendChild(child1);
            Element child2 = document.createElement("child2");
            root.appendChild(child2);

            List<Node> nodes = new ArrayList<>();
            nodes.add(child1);
            nodes.add(child2);
            
            // Call the method being tested
            Node result = operationsHelper.nodeWithName(nodes, "child1");
            
            // Verify the result
            assertNotNull(result);
            assertEquals(child1, result);
        } catch (ParserConfigurationException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testSublistWithName() {
        // Create a list of test nodes
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            document.appendChild(root);
            Element child1 = document.createElement("child1");
            root.appendChild(child1);
            Element child2 = document.createElement("child1");
            root.appendChild(child2);

            List<Node> nodes = new ArrayList<>();
            nodes.add(child1);
            nodes.add(child2);
            
            // Call the method being tested
            List<Node> result = operationsHelper.sublistWithName(nodes, "child1");
            
            // Verify the result
            assertEquals(2, result.size());
            assertEquals(child1, result.get(0));
            assertEquals(child2, result.get(1));
        } catch (ParserConfigurationException e) {
            fail(e.getMessage());
        }
    }
}