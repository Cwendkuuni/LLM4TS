import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        // Typical use case
        Node parent = document.createElement("parent");
        Node child1 = document.createElement("child1");
        parent.appendChild(child1);
        Node child2 = document.createElement("child2");
        parent.appendChild(child2);
        
        List<Node> result = operationsHelper.children(parent);
        
        assertEquals(2, result.size());
        assertEquals(child1, result.get(0));
        assertEquals(child2, result.get(1));
    }

    @Test
    public void testFind() {
        // Typical use case
        Node node = document.createElement("root");
        Node child1 = document.createElement("child1");
        node.appendChild(child1);
        Node child2 = document.createElement("child2");
        node.appendChild(child2);
        
        Node result = operationsHelper.find(document, "root/child1");
        
        assertEquals(child1, result);
        
        // Edge case: path not found
        result = operationsHelper.find(document, "root/child3");
        
        assertNull(result);
        
        // Exception handling: invalid document
        assertThrows(NullPointerException.class, () -> {
            operationsHelper.find(null, "root/child1");
        });
    }

    @Test
    public void testNodeWithName() {
        // Typical use case
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        Node node3 = document.createElement("node3");
        List<Node> nodes = Arrays.asList(node1, node2, node3);
        
        Node result = operationsHelper.nodeWithName(nodes, "node2");
        
        assertEquals(node2, result);
        
        // Edge case: node not found
        result = operationsHelper.nodeWithName(nodes, "node4");
        
        assertNull(result);
        
        // Exception handling: empty node list
        assertThrows(NullPointerException.class, () -> {
            operationsHelper.nodeWithName(new ArrayList<>(), "node2");
        });
    }

    @Test
    public void testSublistWithName() {
        // Typical use case
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        Node node3 = document.createElement("node3");
        List<Node> nodes = Arrays.asList(node1, node2, node3);
        
        List<Node> result = operationsHelper.sublistWithName(nodes, "node2");
        
        assertEquals(1, result.size());
        assertEquals(node2, result.get(0));
        
        // Edge case: no nodes with given name
        result = operationsHelper.sublistWithName(nodes, "node4");
        
        assertTrue(result.isEmpty());
    }
}