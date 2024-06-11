import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        // Test when node has children
        Document document = createDocument();
        Node node = document.getDocumentElement();
        List<Node> children = helper.children(node);
        assertEquals(1, children.size());
        
        // Test when node does not have children
        document = createEmptyDocument();
        node = document.getDocumentElement();
        children = helper.children(node);
        assertEquals(0, children.size());
    }
    
    @Test
    public void testFind() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        // Test when node is found
        Document document = createDocument();
        Node found = helper.find(document, "root/child");
        assertNotNull(found);
        
        // Test when node is not found
        found = helper.find(document, "root/nonexistent");
        assertNull(found);
        
        // Test when empty path is provided
        found = helper.find(document, "");
        assertNull(found);
    }
    
    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        // Test when node is found
        Document document = createDocument();
        Node root = document.getDocumentElement();
        List<Node> children = helper.children(root);
        Node node = helper.nodeWithName(children, "child");
        assertNotNull(node);
        
        // Test when node is not found
        node = helper.nodeWithName(children, "nonexistent");
        assertNull(node);
    }
    
    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        // Test when nodes are found
        Document document = createDocument();
        Node root = document.getDocumentElement();
        List<Node> children = helper.children(root);
        List<Node> sublist = helper.sublistWithName(children, "child");
        assertEquals(1, sublist.size());
        
        // Test when nodes are not found
        sublist = helper.sublistWithName(children, "nonexistent");
        assertEquals(0, sublist.size());
    }
    
    // Helper method to create a document with a root element and a child element
    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            document.appendChild(root);
            Element child = document.createElement("child");
            root.appendChild(child);
            return document;
        } catch (Exception e) {
            return null;
        }
    }
    
    // Helper method to create an empty document
    private Document createEmptyDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            return document;
        } catch (Exception e) {
            return null;
        }
    }
}