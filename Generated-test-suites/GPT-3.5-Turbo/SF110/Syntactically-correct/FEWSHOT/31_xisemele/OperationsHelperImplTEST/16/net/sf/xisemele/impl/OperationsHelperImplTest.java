import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        // Create a sample document with child nodes
        Document document = createSampleDocument();
        
        // Test the children method
        List<Node> children = operationsHelper.children(document);
        
        // Verify the result
        assertEquals(3, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
        assertEquals("child3", children.get(2).getNodeName());
    }

    @Test
    public void testFind() {
        // Create a sample document with nested nodes
        Document document = createSampleDocument();
        
        // Test the find method
        Node node = operationsHelper.find(document, "root/child1/grandchild");
        
        // Verify the result
        assertNotNull(node);
        assertEquals("grandchild", node.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        // Create a sample list of nodes
        List<Node> nodes = createSampleNodeList();
        
        // Test the nodeWithName method
        Node node = operationsHelper.nodeWithName(nodes, "child2");
        
        // Verify the result
        assertNotNull(node);
        assertEquals("child2", node.getNodeName());
    }

    @Test
    public void testSublistWithName() {
        // Create a sample list of nodes
        List<Node> nodes = createSampleNodeList();
        
        // Test the sublistWithName method
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child1");
        
        // Verify the result
        assertEquals(2, sublist.size());
        assertEquals("child1", sublist.get(0).getNodeName());
        assertEquals("child1", sublist.get(1).getNodeName());
    }

    private Document createSampleDocument() {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        Element grandchild = document.createElement("grandchild");
        
        child1.appendChild(grandchild);
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);
        document.appendChild(root);
        
        return document;
    }

    private List<Node> createSampleNodeList() {
        Document document = createSampleDocument();
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            nodes.add(nodeList.item(i));
        }
        return nodes;
    }
}