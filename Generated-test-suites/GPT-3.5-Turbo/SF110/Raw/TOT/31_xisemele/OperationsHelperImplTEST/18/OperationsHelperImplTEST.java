
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class OperationsHelperImplTest {
    private Document document;
    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        document = createSampleDocument();
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        Node root = document.getDocumentElement();
        List<Node> children = operationsHelper.children(root);

        assertEquals(3, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
        assertEquals("child3", children.get(2).getNodeName());
    }

    @Test
    public void testFind() {
        Node foundNode = operationsHelper.find(document, "root/child1/grandchild");
        assertNotNull(foundNode);
        assertEquals("grandchild", foundNode.getNodeName());

        foundNode = operationsHelper.find(document, "root/child1/grandchild/invalid");
        assertNull(foundNode);

        foundNode = operationsHelper.find(document, "invalid/path");
        assertNull(foundNode);
    }

    @Test
    public void testNodeWithName() {
        Node root = document.getDocumentElement();
        List<Node> children = operationsHelper.children(root);
        Node node = operationsHelper.nodeWithName(children, "child2");
        assertNotNull(node);
        assertEquals("child2", node.getNodeName());

        node = operationsHelper.nodeWithName(children, "invalid_name");
        assertNull(node);
    }

    @Test
    public void testSublistWithName() {
        Node root = document.getDocumentElement();
        List<Node> children = operationsHelper.children(root);
        List<Node> sublist = operationsHelper.sublistWithName(children, "child2");
        assertNotNull(sublist);
        assertEquals(1, sublist.size());
        assertEquals("child2", sublist.get(0).getNodeName());

        sublist = operationsHelper.sublistWithName(children, "nonexistent");
        assertNotNull(sublist);
        assertEquals(0, sublist.size());
    }

    private Document createSampleDocument() {
        Document document = DocumentBuilder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        root.appendChild(child1);

        Element grandchild = document.createElement("grandchild");
        child1.appendChild(grandchild);

        Element child2 = document.createElement("child2");
        root.appendChild(child2);

        Element child3 = document.createElement("child3");
        root.appendChild(child3);

        return document;
    }
}
