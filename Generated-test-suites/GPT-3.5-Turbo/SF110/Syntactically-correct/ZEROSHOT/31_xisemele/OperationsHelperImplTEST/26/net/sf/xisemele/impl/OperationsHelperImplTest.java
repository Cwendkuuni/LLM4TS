import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {
    
    @Test
    public void testChildren() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        // create a sample Node object
        Node node = createSampleNode();
        
        // test the children() method
        List<Node> result = operationsHelper.children(node);
        
        // assert the result
        assertEquals(2, result.size());
        assertEquals("child1", result.get(0).getNodeName());
        assertEquals("child2", result.get(1).getNodeName());
    }
    
    @Test
    public void testFind() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        // create a sample Document object
        Document document = createSampleDocument();
        String path = "root/child1";
        
        // test the find() method
        Node result = operationsHelper.find(document, path);
        
        // assert the result
        assertNotNull(result);
        assertEquals("child1", result.getNodeName());
    }
    
    @Test
    public void testNodeWithName() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        // create a sample Node list
        List<Node> nodes = createSampleNodeList();
        String name = "child1";
        
        // test the nodeWithName() method
        Node result = operationsHelper.nodeWithName(nodes, name);
        
        // assert the result
        assertNotNull(result);
        assertEquals("child1", result.getNodeName());
    }
    
    @Test
    public void testSublistWithName() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        // create a sample Node list
        List<Node> nodes = createSampleNodeList();
        String name = "child1";
        
        // test the sublistWithName() method
        List<Node> result = operationsHelper.sublistWithName(nodes, name);
        
        // assert the result
        assertEquals(1, result.size());
        assertEquals("child1", result.get(0).getNodeName());
    }
    
    // helper methods to create sample objects for testing
    
    private Node createSampleNode() {
        Document document = createSampleDocument();
        Element root = document.getDocumentElement();
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);
        return root;
    }
    
    private Document createSampleDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            document.appendChild(root);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private List<Node> createSampleNodeList() {
        List<Node> nodes = new ArrayList<>();
        Document document = createSampleDocument();
        Element root = document.getDocumentElement();
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);
        nodes.add(child1);
        nodes.add(child2);
        return nodes;
    }
}