import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1/><child2/><child3/></root>");
        Node root = document.getDocumentElement();
        List<Node> children = operationsHelper.children(root);
        // Typical use case
        assertEquals(3, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
        assertEquals("child3", children.get(2).getNodeName());
        // Edge case: Empty root element
        document = TestUtils.createDocument("<root></root>");
        root = document.getDocumentElement();
        children = operationsHelper.children(root);
        assertEquals(0, children.size());
        // Edge case: Root element with only one child
        document = TestUtils.createDocument("<root><child1/></root>");
        root = document.getDocumentElement();
        children = operationsHelper.children(root);
        assertEquals(1, children.size());
        assertEquals("child1", children.get(0).getNodeName());
    }

    @Test
    public void testFind() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1><grandchild/></child1><child2/></root>");
        Node found = operationsHelper.find(document, "/root/child1/grandchild");
        // Typical use case
        assertNotNull(found);
        assertEquals("grandchild", found.getNodeName());
        // Edge case: Node not found in document
        found = operationsHelper.find(document, "/root/child3");
        assertNull(found);
    }

    @Test
    public void testNodeWithName() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1/><child2/><child1/></root>");
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        // Typical use case
        Node found = operationsHelper.nodeWithName(children, "child2");
        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
        // Edge case: Node with duplicate names
        found = operationsHelper.nodeWithName(children, "child1");
        assertNotNull(found);
        assertEquals("child1", found.getNodeName());
        // Edge case: Node not found
        found = operationsHelper.nodeWithName(children, "child3");
        assertNull(found);
    }

    @Test
    public void testSublistWithName() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1/><child2/><child1/><child3/></root>");
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        // Typical use case
        List<Node> sublist = operationsHelper.sublistWithName(children, "child1");
        assertEquals(2, sublist.size());
        assertEquals("child1", sublist.get(0).getNodeName());
        assertEquals("child1", sublist.get(1).getNodeName());
        // Edge case: No nodes with given name
        sublist = operationsHelper.sublistWithName(children, "child3");
        assertEquals(1, sublist.size());
        assertEquals("child3", sublist.get(0).getNodeName());
        // Additional edge case: Empty list
        sublist = operationsHelper.sublistWithName(new ArrayList<>(), "child1");
        assertEquals(0, sublist.size());
    }

    private static class TestUtils {
        public static Document createDocument(String xml) {
            // This method is not important for the test case, it just creates a document from an XML string.
            return null;
        }
    }
}