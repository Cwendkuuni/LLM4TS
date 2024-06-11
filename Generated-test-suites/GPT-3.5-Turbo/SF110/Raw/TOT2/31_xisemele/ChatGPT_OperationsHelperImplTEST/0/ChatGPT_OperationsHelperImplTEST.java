import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1/><child2/><child3/></root>");
        Node root = document.getDocumentElement();
        List<Node> children = operationsHelper.children(root);
        assertEquals(3, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
        assertEquals("child3", children.get(2).getNodeName());
    }

    @Test
    public void testFind() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1><grandchild/></child1><child2/></root>");
        Node found = operationsHelper.find(document, "/root/child1/grandchild");
        assertNotNull(found);
        assertEquals("grandchild", found.getNodeName());
        found = operationsHelper.find(document, "/root/child2");
        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
        found = operationsHelper.find(document, "/root/child3");
        assertNull(found);
    }

    @Test
    public void testNodeWithName() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1/><child2/><child1/></root>");
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        Node found = operationsHelper.nodeWithName(children, "child2");
        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
        found = operationsHelper.nodeWithName(children, "child3");
        assertNull(found);
    }

    @Test
    public void testSublistWithName() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1/><child2/><child1/><child3/></root>");
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        List<Node> sublist = operationsHelper.sublistWithName(children, "child1");
        assertEquals(2, sublist.size());
        assertEquals("child1", sublist.get(0).getNodeName());
        assertEquals("child1", sublist.get(1).getNodeName());
        sublist = operationsHelper.sublistWithName(children, "child3");
        assertEquals(1, sublist.size());
        assertEquals("child3", sublist.get(0).getNodeName());
    }

    @Test
    public void testSubsetWithNameNotFound() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1/><child2/><child1/><child3/></root>");
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        List<Node> sublist = operationsHelper.sublistWithName(children, "child5");
        assertNotNull(sublist);
        assertEquals(0, sublist.size());
    }

}