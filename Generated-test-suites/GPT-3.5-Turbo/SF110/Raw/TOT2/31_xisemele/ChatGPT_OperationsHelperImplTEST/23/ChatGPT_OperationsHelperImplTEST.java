import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.junit.Before; 

public class ChatGPT_OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;
    private Document document;
    
    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
        document = TestUtils.createDocument("<root><child1/><child2/><child3/></root>");
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
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        Node found = operationsHelper.nodeWithName(children, "child2");
        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
        found = operationsHelper.nodeWithName(children, "child3");
        assertNull(found);
    }

    @Test
    public void testSublistWithName() {
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
    public void testExceptions() {
        try {
            document = TestUtils.createDocument("<invalid_xml><child1/></invalid_xml>");
            operationsHelper.children(document.getDocumentElement());
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // exception expected
        }
    }
}