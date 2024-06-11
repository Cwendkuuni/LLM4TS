
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {
    
    @Test
    public void testChildren() {
        // Test when node has children
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("root");
            Element childElement1 = document.createElement("child1");
            Element childElement2 = document.createElement("child2");
            rootElement.appendChild(childElement1);
            rootElement.appendChild(childElement2);
            document.appendChild(rootElement);
            
            OperationsHelperImpl helper = new OperationsHelperImpl();
            List<Node> children = helper.children(rootElement);
            
            assertEquals(2, children.size());
            assertEquals(childElement1, children.get(0));
            assertEquals(childElement2, children.get(1));
        } catch (Exception e) {
            fail("Exception thrown");
        }
        
        // Test when node has no children
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("root");
            document.appendChild(rootElement);
            
            OperationsHelperImpl helper = new OperationsHelperImpl();
            List<Node> children = helper.children(rootElement);
            
            assertEquals(0, children.size());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testFind() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("root");
            Element childElement = document.createElement("child");
            rootElement.appendChild(childElement);
            document.appendChild(rootElement);
            
            OperationsHelperImpl helper = new OperationsHelperImpl();
            Node foundNode = helper.find(document, "root/child");
            
            assertEquals(childElement, foundNode);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testNodeWithName() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("root");
            Element childElement1 = document.createElement("child");
            Element childElement2 = document.createElement("child");
            rootElement.appendChild(childElement1);
            rootElement.appendChild(childElement2);
            document.appendChild(rootElement);
            
            OperationsHelperImpl helper = new OperationsHelperImpl();
            List<Node> nodes = helper.children(rootElement);
            Node foundNode = helper.nodeWithName(nodes, "child");
            
            assertEquals(childElement1, foundNode);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testSublistWithName() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("root");
            Element childElement1 = document.createElement("child");
            Element childElement2 = document.createElement("child");
            Element childElement3 = document.createElement("child2");
            rootElement.appendChild(childElement1);
            rootElement.appendChild(childElement2);
            rootElement.appendChild(childElement3);
            document.appendChild(rootElement);
            
            OperationsHelperImpl helper = new OperationsHelperImpl();
            List<Node> nodes = helper.children(rootElement);
            List<Node> sublist = helper.sublistWithName(nodes, "child");
            
            assertEquals(2, sublist.size());
            assertEquals(childElement1, sublist.get(0));
            assertEquals(childElement2, sublist.get(1));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
}

