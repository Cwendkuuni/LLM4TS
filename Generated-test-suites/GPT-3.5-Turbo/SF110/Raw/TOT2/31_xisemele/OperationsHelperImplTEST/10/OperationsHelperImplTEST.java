import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        List<Node> children = operationsHelper.children(document); // assume document is initialized
        assertNotNull(children);
        assertEquals(2, children.size()); // assuming there are two children nodes
    }

    @Test
    public void testFind() {
        Node found = operationsHelper.find(document, "root/node1/node2"); // assume document and path are initialized
        assertNotNull(found);
        assertEquals("node2", found.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        List<Node> nodes = new ArrayList<>();
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        nodes.add(node1);
        nodes.add(node2);
        
        Node result = operationsHelper.nodeWithName(nodes, "node2");
        assertEquals(node2, result);
    }

    @Test
    public void testSublistWithName() {
        List<Node> nodes = new ArrayList<>();
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        Node node3 = document.createElement("node2");
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "node2");
        assertEquals(2, sublist.size());
    }
}