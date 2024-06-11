
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        Document document = createTestDocument();
        List<Node> children = operationsHelper.children(document.getDocumentElement());

        assertEquals(2, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
    }

    @Test
    public void testFind() {
        Document document = createTestDocument();
        Node found = operationsHelper.find(document, "root/child1");

        assertNotNull(found);
        assertEquals("child1", found.getNodeName());

        Node notFound = operationsHelper.find(document, "root/child3");
        assertNull(notFound);
    }

    @Test
    public void testNodeWithName() {
        Document document = createTestDocument();
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        Node node = operationsHelper.nodeWithName(children, "child1");

        assertNotNull(node);
        assertEquals("child1", node.getNodeName());

        Node wrongNode = operationsHelper.nodeWithName(children, "child2");
        assertNull(wrongNode);
    }

    @Test
    public void testSublistWithName() {
        Document document = createTestDocument();
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        List<Node> sublist = operationsHelper.sublistWithName(children, "child1");

        assertEquals(1, sublist.size());
        assertEquals("child1", sublist.get(0).getNodeName());

        List<Node> emptySublist = operationsHelper.sublistWithName(children, "child3");
        assertTrue(emptySublist.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testChildrenNullNode() {
        operationsHelper.children(null);
    }

    @Test
    public void testFindNonExistentPath() {
        Document document = createTestDocument();
        Node node = operationsHelper.find(document, "root/child3");

        assertNull(node);
    }

    @Test
    public void testNodeWithNameNotFound() {
        List<Node> emptyList = new ArrayList<>();
        Node node = operationsHelper.nodeWithName(emptyList, "child1");

        assertNull(node);
    }

    @Test
    public void testSublistWithNameEmpty() {
        List<Node> emptyList = new ArrayList<>();
        List<Node> sublist = operationsHelper.sublistWithName(emptyList, "child1");

        assertTrue(sublist.isEmpty());
    }

    private Document createTestDocument() {
        // Create a test document with root and two child nodes
        return null;
    }
}

