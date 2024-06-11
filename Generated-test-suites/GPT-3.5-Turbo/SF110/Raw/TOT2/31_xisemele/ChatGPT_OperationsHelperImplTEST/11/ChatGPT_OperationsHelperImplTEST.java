import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.junit.Before;
import org.junit.After;

public class ChatGPT_OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;
    private Document document;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
        document = TestUtils.createDocument("<root><child1/><child2/><child3/></root>");
    }

    @After
    public void tearDown() {
        operationsHelper = null;
        document = null;
    }

    @Test
    public void testChildren_basic() {
        Node root = document.getDocumentElement();
        List<Node> children = operationsHelper.children(root);
        assertEquals(3, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
        assertEquals("child3", children.get(2).getNodeName());
    }

    @Test
    public void testFind_basic() {
        Node found = operationsHelper.find(document, "/root/child1");
        assertNotNull(found);
        assertEquals("child1", found.getNodeName());
    }

    @Test
    public void testNodeWithName_basic() {
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        Node found = operationsHelper.nodeWithName(children, "child2");
        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
    }

    @Test
    public void testSublistWithName_basic() {
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        List<Node> sublist = operationsHelper.sublistWithName(children, "child1");
        assertEquals(1, sublist.size());
        assertEquals("child1", sublist.get(0).getNodeName());
    }

    @Test
    public void testFind_notFound() {
        Node notFound = operationsHelper.find(document, "/root/child4");
        assertNull(notFound);
    }

    @Test
    public void testNodeWithName_notFound() {
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        Node notFound = operationsHelper.nodeWithName(children, "child4");
        assertNull(notFound);
    }

    @Test
    public void testSublistWithName_empty() {
        List<Node> children = new ArrayList<>();
        List<Node> emptyList = operationsHelper.sublistWithName(children, "child1");
        assertEquals(0, emptyList.size());
    }
}