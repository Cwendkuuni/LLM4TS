import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;
import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

    private OperationsHelperImpl helper;

    @Before
    public void setUp() {
        helper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        Node node = createMockNode();
        List<Node> children = helper.children(node);
        assertEquals(2, children.size());
    }

    @Test
    public void testFind() {
        Document document = createMockDocument();
        String path = "root/child1/child2";
        Node found = helper.find(document, path);
        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        List<Node> nodes = createMockNodeList();
        String name = "child1";
        Node node = helper.nodeWithName(nodes, name);
        assertNotNull(node);
        assertEquals("child1", node.getNodeName());
    }

    @Test
    public void testSublistWithName() {
        List<Node> nodes = createMockNodeList();
        String name = "child2";
        List<Node> sublist = helper.sublistWithName(nodes, name);
        assertEquals(1, sublist.size());
        assertEquals("child2", sublist.get(0).getNodeName());
    }

    private Node createMockNode() {
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);
        return root;
    }

    private Document createMockDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        child1.appendChild(child2);
        document.appendChild(root);
        return document;
    }

    private List<Node> createMockNodeList() {
        List<Node> nodes = new ArrayList<>();
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child2");
        nodes.add(child1);
        nodes.add(child2);
        nodes.add(child3);
        return nodes;
    }
}