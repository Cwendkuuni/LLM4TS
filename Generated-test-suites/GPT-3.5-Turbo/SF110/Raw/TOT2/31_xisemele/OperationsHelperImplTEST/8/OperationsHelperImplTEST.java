import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
    Document document = null;
    
    @Test
    public void testChildren() {
        Node node = document.createElement("parent");
        Node child1 = document.createElement("child1");
        Node child2 = document.createElement("child2");
        
        node.appendChild(child1);
        node.appendChild(child2);
        
        List<Node> children = operationsHelper.children(node);
        
        assertNotNull(children);
        assertEquals(2, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
    }
    
    @Test
    public void testFind() {
        document = documentBuilder.newDocument();
        
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        
        document.appendChild(root);
        root.appendChild(child1);
        child1.appendChild(child2);
        
        Node found = operationsHelper.find(document, "/root/child1");
        
        assertNotNull(found);
        assertEquals("child1", found.getNodeName());
        assertNull(operationsHelper.find(document, "/root/non-existent"));
    }
    
    @Test
    public void testNodeWithName() {
        Node node1 = document.createElement("node");
        Node node2 = document.createElement("node");
        Node node3 = document.createElement("differentNode");
        
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        
        Node foundNode = operationsHelper.nodeWithName(nodes, "node");
        
        assertNotNull(foundNode);
        assertEquals("node", foundNode.getNodeName());
        
        assertNull(operationsHelper.nodeWithName(nodes, "non-existentNode"));
    }
    
    @Test
    public void testSublistWithName() {
        Node node1 = document.createElement("node");
        Node node2 = document.createElement("node");
        Node node3 = document.createElement("differentNode");
        
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "node");
        
        assertEquals(2, sublist.size());
        assertEquals("node", sublist.get(0).getNodeName());
        assertEquals("node", sublist.get(1).getNodeName());
        
        sublist = operationsHelper.sublistWithName(nodes, "non-existentNode");
        assertTrue(sublist.isEmpty());
    }
}