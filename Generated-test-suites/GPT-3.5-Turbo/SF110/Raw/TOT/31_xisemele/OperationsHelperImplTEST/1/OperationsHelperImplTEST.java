import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    // Test for the children() method

    @Test
    public void testChildren_typicalCase() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        NodeList nodeList = root.getChildNodes();
        List<Node> expected = new ArrayList<>();
        expected.add(child1);
        expected.add(child2);

        List<Node> actual = operationsHelper.children(root);
        assertEquals(expected, actual);
    }

    @Test
    public void testChildren_noChildren() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        NodeList nodeList = root.getChildNodes();
        List<Node> expected = new ArrayList<>();

        List<Node> actual = operationsHelper.children(root);
        assertEquals(expected, actual);
    }

    // Test for the find() method

    @Test
    public void testFind_typicalCase() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        Node expected = child1;

        Node actual = operationsHelper.find(document, "root/child1");
        assertEquals(expected, actual);
    }

    @Test
    public void testFind_noMatch() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        Node expected = null;

        Node actual = operationsHelper.find(document, "root/child3");
        assertEquals(expected, actual);
    }

    // Test for the nodeWithName() method

    @Test
    public void testNodeWithName_typicalCase() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        List<Node> nodes = operationsHelper.children(root);
        Node expected = child1;

        Node actual = operationsHelper.nodeWithName(nodes, "child1");
        assertEquals(expected, actual);
    }

    @Test
    public void testNodeWithName_noMatch() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        List<Node> nodes = operationsHelper.children(root);
        Node expected = null;

        Node actual = operationsHelper.nodeWithName(nodes, "child3");
        assertEquals(expected, actual);
    }

    // Test for the sublistWithName() method

    @Test
    public void testSublistWithName_typicalCase() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child");
        Element child2 = document.createElement("child");
        root.appendChild(child1);
        root.appendChild(child2);

        List<Node> nodes = operationsHelper.children(root);
        List<Node> expected = new ArrayList<>();
        expected.add(child1);
        expected.add(child2);

        List<Node> actual = operationsHelper.sublistWithName(nodes, "child");
        assertEquals(expected, actual);
    }

    @Test
    public void testSublistWithName_noMatch() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        List<Node> nodes = operationsHelper.children(root);
        List<Node> expected = new ArrayList<>();

        List<Node> actual = operationsHelper.sublistWithName(nodes, "child3");
        assertEquals(expected, actual);
    }

}