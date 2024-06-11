import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
    Document document;
    Node rootNode;
    Node childNode1;
    Node childNode2;
    
    @Test
    public void testChildren() {
        ArrayList<Node> children = new ArrayList<Node>();
        rootNode = document.createElement("root");
        childNode1 = document.createElement("child1");
        childNode2 = document.createElement("child2");
        rootNode.appendChild(childNode1);
        rootNode.appendChild(childNode2);
        
        children = operationsHelper.children(rootNode);
        
        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }
    
    @Test
    public void testFind() {
        rootNode = document.createElement("root");
        childNode1 = document.createElement("child1");
        rootNode.appendChild(childNode1);
        
        Node foundNode = operationsHelper.find((Document)rootNode, "root/child1");
        
        assertEquals(childNode1, foundNode);
    }
    
    @Test
    public void testNodeWithName() {
        ArrayList<Node> nodes = new ArrayList<Node>();
        rootNode = document.createElement("root");
        childNode1 = document.createElement("child1");
        childNode2 = document.createElement("child2");
        rootNode.appendChild(childNode1);
        rootNode.appendChild(childNode2);
        nodes.add(childNode1);
        nodes.add(childNode2);
        
        Node foundNode = operationsHelper.nodeWithName(nodes, "child1");
        
        assertEquals(childNode1, foundNode);
    }
    
    @Test
    public void testSublistWithName() {
        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Node> sublist = new ArrayList<Node>();
        rootNode = document.createElement("root");
        childNode1 = document.createElement("child1");
        childNode2 = document.createElement("child2");
        rootNode.appendChild(childNode1);
        rootNode.appendChild(childNode2);
        nodes.add(childNode1);
        nodes.add(childNode2);
        
        sublist = operationsHelper.sublistWithName(nodes, "child1");
        
        assertEquals(1, sublist.size());
        assertTrue(sublist.contains(childNode1));
    }
}