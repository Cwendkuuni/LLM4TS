import static org.junit.Assert.*;
import org.junit.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        // Create a test Node
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element rootElement = document.createElement("root");
        Element childElement1 = document.createElement("child1");
        Element childElement2 = document.createElement("child2");
        Element childElement3 = document.createElement("child3");
        rootElement.appendChild(childElement1);
        rootElement.appendChild(childElement2);
        rootElement.appendChild(childElement3);
        document.appendChild(rootElement);
        
        // Test the children method
        List<Node> children = operationsHelper.children(document);
        assertEquals(3, children.size());
        assertTrue(children.contains(childElement1));
        assertTrue(children.contains(childElement2));
        assertTrue(children.contains(childElement3));
    }

    @Test
    public void testFind() {
        // Create a test Document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element rootElement = document.createElement("root");
        Element childElement = document.createElement("child");
        rootElement.appendChild(childElement);
        document.appendChild(rootElement);

        // Test the find method when the path is valid
        Node found = operationsHelper.find(document, "root/child");
        assertNotNull(found);
        assertEquals(childElement, found);

        // Test the find method when the path is invalid
        Node notFound = operationsHelper.find(document, "root/nonexistent");
        assertNull(notFound);
    }

    @Test
    public void testNodeWithName() {
        // Create a test NodeList
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element rootElement = document.createElement("root");
        Element childElement1 = document.createElement("child");
        Element childElement2 = document.createElement("child");
        Element childElement3 = document.createElement("child");
        rootElement.appendChild(childElement1);
        rootElement.appendChild(childElement2);
        rootElement.appendChild(childElement3);
        document.appendChild(rootElement);

        // Test the nodeWithName method
        List<Node> children = operationsHelper.children(document);
        Node node = operationsHelper.nodeWithName(children, "child");
        assertNotNull(node);
        assertTrue(node.getNodeName().equals("child"));
    }

    @Test
    public void testSublistWithName() {
        // Create a test NodeList
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element rootElement = document.createElement("root");
        Element childElement1 = document.createElement("child");
        Element childElement2 = document.createElement("child");
        Element childElement3 = document.createElement("child");
        rootElement.appendChild(childElement1);
        rootElement.appendChild(childElement2);
        rootElement.appendChild(childElement3);
        document.appendChild(rootElement);

        // Test the sublistWithName method
        List<Node> children = operationsHelper.children(document);
        List<Node> sublist = operationsHelper.sublistWithName(children, "child");
        assertEquals(3, sublist.size());
        assertTrue(sublist.contains(childElement1));
        assertTrue(sublist.contains(childElement2));
        assertTrue(sublist.contains(childElement3));
    }

}