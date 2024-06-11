import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl helper = new OperationsHelperImpl();
    
    @Test
    public void testChildren_EmptyNode() {
        Document doc = createEmptyDocument();
        Node node = doc.getDocumentElement();
        
        assertNotNull(helper.children(node));
        assertEquals(0, helper.children(node).size());
    }
    
    @Test
    public void testChildren_NodeWithChildren() {
        Document doc = createDocumentWithChildren();
        Node node = doc.getDocumentElement();
        
        assertNotNull(helper.children(node));
        assertEquals(2, helper.children(node).size());
        assertEquals("child1", helper.children(node).get(0).getNodeName());
        assertEquals("child2", helper.children(node).get(1).getNodeName());
    }
    
    @Test
    public void testFind_ValidPath() {
        Document doc = createDocumentWithChildren();
        String path = "/root/child1";
        
        Node node = helper.find(doc, path);
        
        assertNotNull(node);
        assertEquals("child1", node.getNodeName());
    }
    
    @Test
    public void testFind_InvalidPath() {
        Document doc = createDocumentWithChildren();
        String path = "/root/child3";
        
        Node node = helper.find(doc, path);
        
        assertNull(node);
    }
    
    @Test
    public void testFind_EmptyPath() {
        Document doc = createDocumentWithChildren();
        String path = "";
        
        Node node = helper.find(doc, path);
        
        assertNull(node);
    }
    
    @Test
    public void testNodeWithName_NodeExists() {
        Document doc = createDocumentWithChildren();
        Node node1 = doc.getDocumentElement().getFirstChild();
        String name = "child1";
        
        Node foundNode = helper.nodeWithName(doc.getChildNodes(), name);
        
        assertNotNull(foundNode);
        assertEquals(node1, foundNode);
    }
    
    @Test
    public void testNodeWithName_NodeDoesNotExist() {
        Document doc = createDocumentWithChildren();
        String name = "child3";
        
        Node foundNode = helper.nodeWithName(doc.getChildNodes(), name);
        
        assertNull(foundNode);
    }
    
    @Test
    public void testSublistWithName_NodesExist() {
        Document doc = createDocumentWithChildren();
        String name = "child1";
        
        assertNotNull(helper.sublistWithName(doc.getChildNodes(), name));
        assertEquals(1, helper.sublistWithName(doc.getChildNodes(), name).size());
        assertEquals("child1", helper.sublistWithName(doc.getChildNodes(), name).get(0).getNodeName());
    }
    
    @Test
    public void testSublistWithName_NodesDoNotExist() {
        Document doc = createDocumentWithChildren();
        String name = "child3";
        
        assertNotNull(helper.sublistWithName(doc.getChildNodes(), name));
        assertEquals(0, helper.sublistWithName(doc.getChildNodes(), name).size());
    }
    
    // Helper methods to create sample documents
    
    private Document createEmptyDocument() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            return db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Document createDocumentWithChildren() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element root = doc.createElement("root");
            Element child1 = doc.createElement("child1");
            Element child2 = doc.createElement("child2");
            root.appendChild(child1);
            root.appendChild(child2);
            doc.appendChild(root);
            return doc;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}