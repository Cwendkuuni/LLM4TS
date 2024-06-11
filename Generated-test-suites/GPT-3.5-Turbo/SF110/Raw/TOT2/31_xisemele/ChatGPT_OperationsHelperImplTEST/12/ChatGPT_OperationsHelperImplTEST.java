import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.List;

public class ChatGPT_OperationsHelperImplTest {

    @Test
    public void testChildren() {
        // Typical use case
        ChatGPT_OperationsHelperImpl operationsHelper = new ChatGPT_OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1/><child2/><child3/></root>");
        Node root = document.getDocumentElement();
        List<Node> children = operationsHelper.children(root);
        
        // Check if all children are retrieved
        assertEquals(3, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
        assertEquals("child3", children.get(2).getNodeName());
        
        // Edge case: Empty root node
        document = TestUtils.createDocument("<root></root>");
        root = document.getDocumentElement();
        children = operationsHelper.children(root);
        assertEquals(0, children.size());
    }

    @Test
    public void testFind() {
        // Typical use case
        ChatGPT_OperationsHelperImpl operationsHelper = new ChatGPT_OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1><grandchild/></child1><child2/></root>");
        
        // Check if correct node is found
        Node found = operationsHelper.find(document, "/root/child1/grandchild");
        assertNotNull(found);
        assertEquals("grandchild", found.getNodeName());
        
        // Check if correct node is found with incorrect path
        found = operationsHelper.find(document, "/root/child2");
        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
        
        // Check if null is returned for non-existent node
        found = operationsHelper.find(document, "/root/child3");
        assertNull(found);
    }

    @Test
    public void testNodeWithName() {
        // Typical use case
        ChatGPT_OperationsHelperImpl operationsHelper = new ChatGPT_OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1/><child2/><child1/></root>");
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        
        // Check if correct node with name is found
        Node found = operationsHelper.nodeWithName(children, "child2");
        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
        
        // Check if null is returned for non-existent node name
        found = operationsHelper.nodeWithName(children, "child3");
        assertNull(found);
    }

    @Test
    public void testSublistWithName() {
        // Typical use case
        ChatGPT_OperationsHelperImpl operationsHelper = new ChatGPT_OperationsHelperImpl();
        Document document = TestUtils.createDocument("<root><child1/><child2/><child1/><child3/></root>");
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        
        // Check if sublist with given name is retrieved correctly
        List<Node> sublist = operationsHelper.sublistWithName(children, "child1");
        assertEquals(2, sublist.size());
        assertEquals("child1", sublist.get(0).getNodeName());
        assertEquals("child1", sublist.get(1).getNodeName());
        
        // Check if sublist is empty for non-existent name
        sublist = operationsHelper.sublistWithName(children, "child3");
        assertEquals(1, sublist.size());
        assertEquals("child3", sublist.get(0).getNodeName());
    }
}