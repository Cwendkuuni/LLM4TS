import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;
import static org.junit.Assert.*;

public class OperationsHelperImplTest {
    
    private Document document;
    private OperationsHelperImpl operationsHelper;
    
    @Before
    public void setUp() {
        document = createTestDocument();
        operationsHelper = new OperationsHelperImpl();
    }
    
    private Document createTestDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        
        Document document = builder.newDocument();
        
        // create root node
        Element rootElement = document.createElement("root");
        document.appendChild(rootElement);
        
        // create child nodes
        for (int i = 0; i < 5; i++) {
            Element childElement = document.createElement("child");
            childElement.setAttribute("id", String.valueOf(i));
            rootElement.appendChild(childElement);
        }
        
        return document;
    }
    
    @Test
    public void testChildren_typicalCase() {
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        
        assertEquals(5, children.size());
        for (Node child : children) {
            assertEquals("child", child.getNodeName());
        }
    }
    
    @Test
    public void testChildren_noChildren() {
        Element element = document.createElement("empty");
        
        List<Node> children = operationsHelper.children(element);
        
        assertTrue(children.isEmpty());
    }
    
    @Test
    public void testChildren_nullNode() {
        List<Node> children = operationsHelper.children(null);
        
        assertNotNull(children);
        assertTrue(children.isEmpty());
    }
    
    @Test
    public void testFind_typicalCase() {
        Node found = operationsHelper.find(document, "root/child");
        
        assertNotNull(found);
        assertEquals("child", found.getNodeName());
        assertEquals("0", ((Element)found).getAttribute("id"));
    }
    
    @Test
    public void testFind_invalidPath() {
        Node found = operationsHelper.find(document, "root/nonexistent");
        
        assertNull(found);
    }
    
    @Test
    public void testFind_emptyPath() {
        Node found = operationsHelper.find(document, "");
        
        assertNull(found);
    }
    
    @Test
    public void testFind_nullDocument() {
        Node found = operationsHelper.find(null, "root/child");
        
        assertNull(found);
    }
    
    @Test
    public void testNodeWithName_typicalCase() {
        List<Node> nodes = operationsHelper.sublistWithName(document.getDocumentElement(), "child");
        
        Node found = operationsHelper.nodeWithName(nodes, "child");
        
        assertNotNull(found);
        assertEquals("child", found.getNodeName());
    }
    
    @Test
    public void testNodeWithName_noMatchingNode() {
        List<Node> nodes = operationsHelper.sublistWithName(document.getDocumentElement(), "nonexistent");
        
        Node found = operationsHelper.nodeWithName(nodes, "child");
        
        assertNull(found);
    }
    
    @Test
    public void testNodeWithName_nullList() {
        Node found = operationsHelper.nodeWithName(null, "child");
        
        assertNull(found);
    }
    
    @Test
    public void testSublistWithName_typicalCase() {
        List<Node> nodes = operationsHelper.sublistWithName(document.getDocumentElement(), "child");
        
        assertEquals(5, nodes.size());
        for (Node node : nodes) {
            assertEquals("child", node.getNodeName());
        }
    }
    
    @Test
    public void testSublistWithName_noMatchingNode() {
        List<Node> nodes = operationsHelper.sublistWithName(document.getDocumentElement(), "nonexistent");
        
        assertTrue(nodes.isEmpty());
    }
    
    @Test
    public void testSublistWithName_nullList() {
        List<Node> nodes = operationsHelper.sublistWithName(null, "child");
        
        assertNotNull(nodes);
        assertTrue(nodes.isEmpty());
    }
}