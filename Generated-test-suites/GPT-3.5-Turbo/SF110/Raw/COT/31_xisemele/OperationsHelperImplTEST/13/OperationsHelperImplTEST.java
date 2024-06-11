
import static org.junit.Assert.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        // Create a test document with child nodes
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement = document.createElement("root");
        Element childElement1 = document.createElement("child1");
        Element childElement2 = document.createElement("child2");
        rootElement.appendChild(childElement1);
        rootElement.appendChild(childElement2);
        document.appendChild(rootElement);

        // Test the children method
        List<Node> children = operationsHelper.children(document);
        assertEquals(2, children.size());
        assertEquals(childElement1, children.get(0));
        assertEquals(childElement2, children.get(1));
    }

    @Test
    public void testFind() {
        // Create a test document with nested elements
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement = document.createElement("root");
        Element childElement1 = document.createElement("child1");
        Element childElement2 = document.createElement("child2");
        Element nestedElement = document.createElement("nested");
        Element nestedChild = document.createElement("nestedChild");
        nestedElement.appendChild(nestedChild);
        childElement2.appendChild(nestedElement);
        rootElement.appendChild(childElement1);
        rootElement.appendChild(childElement2);
        document.appendChild(rootElement);

        // Test the find method
        Node foundNode = operationsHelper.find(document, "root/child2/nested");
        assertNotNull(foundNode);
        assertEquals(nestedElement, foundNode);

        foundNode = operationsHelper.find(document, "root/child2/nested/nestedChild");
        assertNotNull(foundNode);
        assertEquals(nestedChild, foundNode);
    }

    @Test
    public void testNodeWithName() {
        // Create a test document with nodes
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement = document.createElement("root");
        Element childElement1 = document.createElement("child1");
        Element childElement2 = document.createElement("child2");
        rootElement.appendChild(childElement1);
        rootElement.appendChild(childElement2);
        document.appendChild(rootElement);

        // Test the nodeWithName method
        List<Node> children = operationsHelper.children(document);
        Node nodeWithName = operationsHelper.nodeWithName(children, "child2");
        assertNotNull(nodeWithName);
        assertEquals(childElement2, nodeWithName);
    }

    @Test
    public void testSublistWithName() {
        // Create a test document with nodes
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement = document.createElement("root");
        Element childElement1 = document.createElement("child1");
        Element childElement2 = document.createElement("child2");
        rootElement.appendChild(childElement1);
        rootElement.appendChild(childElement2);
        document.appendChild(rootElement);

        // Test the sublistWithName method
        List<Node> children = operationsHelper.children(document);
        List<Node> sublist = operationsHelper.sublistWithName(children, "child2");
        assertEquals(1, sublist.size());
        assertEquals(childElement2, sublist.get(0));
    }
}