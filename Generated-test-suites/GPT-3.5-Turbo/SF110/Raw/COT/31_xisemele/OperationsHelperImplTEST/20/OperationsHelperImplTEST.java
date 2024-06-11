
package net.sf.xisemele.impl;

import org.junit.Test;
import org.w3c.dom.*;

import java.util.*;

import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

    // Test case for children() method
    @Test
    public void testChildren() {
        // Create a test Node
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            root.appendChild(child1);
            Element child2 = document.createElement("child2");
            root.appendChild(child2);
            Element child3 = document.createElement("child3");
            root.appendChild(child3);
            document.appendChild(root);

            // Test the children method
            List<Node> result = operationsHelper.children(root);
            
            // Verify the result
            assertEquals(3, result.size());
            assertEquals(child1, result.get(0));
            assertEquals(child2, result.get(1));
            assertEquals(child3, result.get(2));
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Test case for find() method
    @Test
    public void testFind() {
        // Create a test Document
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            root.appendChild(child1);
            Element child2 = document.createElement("child2");
            root.appendChild(child2);
            Element child3 = document.createElement("child3");
            root.appendChild(child3);
            document.appendChild(root);

            // Test the find method
            Node result = operationsHelper.find(document, "root/child1");
            
            // Verify the result
            assertNotNull(result);
            assertEquals(child1, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Test case for nodeWithName() method
    @Test
    public void testNodeWithName() {
        // Create a test Node list
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            root.appendChild(child1);
            Element child2 = document.createElement("child2");
            root.appendChild(child2);
            Element child3 = document.createElement("child3");
            root.appendChild(child3);
            document.appendChild(root);
            List<Node> nodes = operationsHelper.children(root);

            // Test the nodeWithName method
            Node result = operationsHelper.nodeWithName(nodes, "child2");
            
            // Verify the result
            assertNotNull(result);
            assertEquals(child2, result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Test case for sublistWithName() method
    @Test
    public void testSublistWithName() {
        // Create a test Node list
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            root.appendChild(child1);
            Element child2 = document.createElement("child2");
            root.appendChild(child2);
            Element child3 = document.createElement("child3");
            root.appendChild(child3);
            document.appendChild(root);
            List<Node> nodes = operationsHelper.children(root);

            // Test the sublistWithName method
            List<Node> result = operationsHelper.sublistWithName(nodes, "child2");
            
            // Verify the result
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(child2, result.get(0));
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

}
