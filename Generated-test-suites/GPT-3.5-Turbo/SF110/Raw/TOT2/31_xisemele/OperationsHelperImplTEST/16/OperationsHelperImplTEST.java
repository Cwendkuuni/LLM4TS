import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
    private Document document;

    @Test
    public void testChildren() {
        Node node = document.createElement("parent");
        Node child1 = document.createElement("child1");
        Node child2 = document.createElement("child2");
        
        node.appendChild(child1);
        node.appendChild(child2);
        
        List<Node> children = operationsHelper.children(node);
        
        assertEquals(2, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
    }
    
    @Test
    public void testFind() {
        Node rootNode = document.createElement("root");
        Node child1 = document.createElement("child1");
        Node child2 = document.createElement("child2");
        
        rootNode.appendChild(child1);
        
        String path = "root/child1";
        Node found = operationsHelper.find(document, path);
        
        assertEquals(rootNode, found);
    }
    
    @Test
    public void testNodeWithName() {
        Node node1 = document.createElement("node");
        Node node2 = document.createElement("node");
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        
        Node result = operationsHelper.nodeWithName(nodes, "node");
        
        assertEquals(node1, result);
    }
    
    @Test
    public void testSublistWithName() {
        Node node1 = document.createElement("node");
        Node node2 = document.createElement("node");
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "node");
        
        assertEquals(2, sublist.size());
        assertTrue(sublist.contains(node1));
        assertTrue(sublist.contains(node2));
    }
}