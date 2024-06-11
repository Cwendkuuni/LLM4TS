import static org.junit.Assert.*;
import org.junit.Test;

import org.w3c.dom.*;

import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
    
    
    
    @Test
    public void testChildren() {
        // Test case 1: Empty node
        Node emptyNode = null;
        List<Node> emptyChildren = operationsHelper.children(emptyNode);
        assertTrue(emptyChildren.isEmpty());
        
        // Test case 2: Node with children
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("root");
            document.appendChild(rootElement);
            Element childElement = document.createElement("child");
            rootElement.appendChild(childElement);
            
            List<Node> children = operationsHelper.children(rootElement);
            assertEquals(1, children.size());
            assertEquals(childElement, children.get(0));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFind() {
        // Test case 1: Path exists
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("root");
            document.appendChild(rootElement);
            Element childElement = document.createElement("child");
            rootElement.appendChild(childElement);
            
            Node foundNode = operationsHelper.find(document, "root/child");
            assertEquals(childElement, foundNode);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 2: Path does not exist
        Document document = null;
        Node notFoundNode = operationsHelper.find(document, "root/child");
        assertNull(notFoundNode);
    }
    
    @Test
    public void testNodeWithName() {
        // Test case 1: Node with matching name
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element element = document.createElement("element");
            document.appendChild(element);
            
            List<Node> nodes = new ArrayList<Node>();
            nodes.add(element);
            
            Node foundNode = operationsHelper.nodeWithName(nodes, "element");
            assertEquals(element, foundNode);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 2: Node with non-matching name
        List<Node> nodes = new ArrayList<Node>();
        Node notFoundNode = operationsHelper.nodeWithName(nodes, "element");
        assertNull(notFoundNode);
    }
    
    @Test
    public void testSublistWithName() {
        // Test case 1: Nodes with matching name
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element element1 = document.createElement("element");
            document.appendChild(element1);
            Element element2 = document.createElement("element");
            document.appendChild(element2);
            
            List<Node> nodes = new ArrayList<Node>();
            nodes.add(element1);
            nodes.add(element2);
            
            List<Node> sublist = operationsHelper.sublistWithName(nodes, "element");
            assertEquals(2, sublist.size());
            assertEquals(element1, sublist.get(0));
            assertEquals(element2, sublist.get(1));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 2: Nodes with non-matching name
        List<Node> nodes = new ArrayList<Node>();
        List<Node> emptySublist = operationsHelper.sublistWithName(nodes, "element");
        assertTrue(emptySublist.isEmpty());
    }
    
    
}