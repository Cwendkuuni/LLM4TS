import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl operationsHelper;
    private Document document;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
        document = DocumentHelper.createDocument();
        Element root = document.addElement("root");
        Element child1 = root.addElement("child1");
        Element child2 = root.addElement("child2");
    }

    @Test
    public void testChildren() {
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        assertEquals(2, children.size());
    }

    @Test
    public void testFind() {
        Node found = operationsHelper.find(document, "/root/child2");
        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        List<Node> nodes = operationsHelper.children(document.getDocumentElement());
        Node node = operationsHelper.nodeWithName(nodes, "child1");
        assertNotNull(node);
        assertEquals("child1", node.getNodeName());
    }

    @Test
    public void testSublistWithName() {
        List<Node> nodes = operationsHelper.children(document.getDocumentElement());
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child1");
        assertEquals(1, sublist.size());
        assertEquals("child1", sublist.get(0).getNodeName());
    }
}