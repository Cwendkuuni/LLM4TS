import org.junit.Test;
import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        // Test case 1: Node with no children
        Node node1 = new NodeImpl();
        List<Node> children1 = operationsHelper.children(node1);
        assertEquals(0, children1.size());
        
        // Test case 2: Node with children
        Node node2 = new NodeImpl();
        Node child1 = new NodeImpl();
        Node child2 = new NodeImpl();
        node2.addChild(child1);
        node2.addChild(child2);
        List<Node> children2 = operationsHelper.children(node2);
        assertEquals(2, children2.size());
        assertTrue(children2.contains(child1));
        assertTrue(children2.contains(child2));
    }

    @Test
    public void testFind() {
        // Test case 1: Empty document and empty path
        Document document1 = new DocumentImpl();
        Node found1 = operationsHelper.find(document1, "");
        assertEquals(null, found1);
        
        // Test case 2: Non-empty document and empty path
        Document document2 = new DocumentImpl();
        Element rootElement2 = document2.createElement("root");
        document2.appendChild(rootElement2);
        Node found2 = operationsHelper.find(document2, "");
        assertEquals(rootElement2, found2);
        
        // Test case 3: Non-empty document and valid path
        Document document3 = new DocumentImpl();
        Element rootElement3 = document3.createElement("root");
        Element childElement3 = document3.createElement("child");
        Element grandchildElement3 = document3.createElement("grandchild");
        rootElement3.appendChild(childElement3);
        childElement3.appendChild(grandchildElement3);
        document3.appendChild(rootElement3);
        Node found3 = operationsHelper.find(document3, "root/child");
        assertEquals(childElement3, found3);
        
        // Test case 4: Non-empty document and invalid path
        Document document4 = new DocumentImpl();
        Element rootElement4 = document4.createElement("root");
        Element childElement4 = document4.createElement("child");
        rootElement4.appendChild(childElement4);
        document4.appendChild(rootElement4);
        Node found4 = operationsHelper.find(document4, "root/invalid");
        assertEquals(null, found4);
    }

    @Test
    public void testNodeWithName() {
        // Test case 1: Empty list of nodes
        List<Node> nodes1 = new ArrayList<>();
        Node found1 = operationsHelper.nodeWithName(nodes1, "name");
        assertEquals(null, found1);
        
        // Test case 2: List of nodes with no matching name
        List<Node> nodes2 = new ArrayList<>();
        Node node1 = new NodeImpl("node1");
        Node node2 = new NodeImpl("node2");
        nodes2.add(node1);
        nodes2.add(node2);
        Node found2 = operationsHelper.nodeWithName(nodes2, "name");
        assertEquals(null, found2);
        
        // Test case 3: List of nodes with matching name
        List<Node> nodes3 = new ArrayList<>();
        Node node3 = new NodeImpl("node3");
        Node node4 = new NodeImpl("name");
        nodes3.add(node3);
        nodes3.add(node4);
        Node found3 = operationsHelper.nodeWithName(nodes3, "name");
        assertEquals(node4, found3);
    }

    @Test
    public void testSublistWithName() {
        // Test case 1: Empty list of nodes
        List<Node> nodes1 = new ArrayList<>();
        List<Node> sublist1 = operationsHelper.sublistWithName(nodes1, "name");
        assertEquals(0, sublist1.size());
        
        // Test case 2: List of nodes with no matching name
        List<Node> nodes2 = new ArrayList<>();
        Node node1 = new NodeImpl("node1");
        Node node2 = new NodeImpl("node2");
        nodes2.add(node1);
        nodes2.add(node2);
        List<Node> sublist2 = operationsHelper.sublistWithName(nodes2, "name");
        assertEquals(0, sublist2.size());
        
        // Test case 3: List of nodes with matching name
        List<Node> nodes3 = new ArrayList<>();
        Node node3 = new NodeImpl("name");
        Node node4 = new NodeImpl("name");
        nodes3.add(node3);
        nodes3.add(node4);
        List<Node> sublist3 = operationsHelper.sublistWithName(nodes3, "name");
        assertEquals(2, sublist3.size());
        assertTrue(sublist3.contains(node3));
        assertTrue(sublist3.contains(node4));
    }
}