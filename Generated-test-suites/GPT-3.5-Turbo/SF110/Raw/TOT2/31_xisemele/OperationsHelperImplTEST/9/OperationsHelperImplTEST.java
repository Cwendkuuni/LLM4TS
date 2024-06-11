import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

    @Test
    public void testChildren_basic() {
        // Create a dummy element for testing
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element element = document.createElement("test");
        element.appendChild(document.createTextNode("child"));
        
        // Test the children method
        List<Node> children = operationsHelper.children(element);
        
        assertEquals(1, children.size());
        assertEquals("child", children.get(0).getTextContent());
    }
    
    @Test
    public void testFind_basic() {
        // Create a dummy document for testing
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("root");
        Element child = document.createElement("child");
        root.appendChild(child);
        document.appendChild(root);
        
        // Test the find method
        Node found = operationsHelper.find(document, "root/child");
        
        assertEquals("child", found.getNodeName());
    }
    
    @Test
    public void testNodeWithName_basic() {
        // Create a list of dummy nodes for testing
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element element1 = document.createElement("elem1");
        Element element2 = document.createElement("elem2");
        element1.appendChild(document.createTextNode("text1"));
        element2.appendChild(document.createTextNode("text2"));
        List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);
        
        // Test the nodeWithName method
        Node result = operationsHelper.nodeWithName(nodes, "elem2");
        
        assertEquals("elem2", result.getNodeName());
    }
    
    @Test
    public void testSublistWithName_basic() {
        // Create a list of dummy nodes for testing
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element element1 = document.createElement("elem1");
        Element element2 = document.createElement("elem2");
        Element element3 = document.createElement("elem1");
        List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);
        
        // Test the sublistWithName method
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "elem1");
        
        assertEquals(2, sublist.size());
        assertEquals("elem1", sublist.get(0).getNodeName());
        assertEquals("elem1", sublist.get(1).getNodeName());
    }
}