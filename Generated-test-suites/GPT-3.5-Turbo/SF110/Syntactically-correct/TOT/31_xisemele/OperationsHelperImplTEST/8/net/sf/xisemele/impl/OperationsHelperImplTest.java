
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;
    private Document document;
    private Node root;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, SAXException {
        operationsHelper = new OperationsHelperImpl();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        try (InputStream inputStream = getClass().getResourceAsStream("test.xml")) {
            document = dBuilder.parse(inputStream);
        }

        document.getDocumentElement().normalize();
        root = document.getDocumentElement();
    }

    @Test
    public void testChildren_typical() {
        List<Node> children = operationsHelper.children(root);
        assertNotNull("Children should not be null", children);
        assertEquals("There should be 2 children nodes", 2, children.size());
    }

    @Test
    public void testChildren_empty() {
        Node emptyNode = document.createElement("empty");
        List<Node> children = operationsHelper.children(emptyNode);
        assertNotNull("Children should not be null", children);
        assertTrue("Children list should be empty", children.isEmpty());
    }

    @Test
    public void testFind_typical() {
        Node foundNode = operationsHelper.find(document, "root/child/grandchild");
        assertNotNull("Found node should not be null", foundNode);
        assertEquals("Found node should have the correct name", "grandchild", foundNode.getNodeName());
    }

    @Test
    public void testFind_missingRootNode() {
        Node foundNode = operationsHelper.find(document, "missingRoot/child/grandchild");
        assertNull("Found node should be null when root node is missing", foundNode);
    }

    @Test
    public void testFind_missingIntermediateNode() {
        Node foundNode = operationsHelper.find(document, "root/missingNode/grandchild");
        assertNull("Found node should be null when intermediate node is missing", foundNode);
    }

    @Test
    public void testFind_missingLeafNode() {
        Node foundNode = operationsHelper.find(document, "root/child/missingNode");
        assertNull("Found node should be null when leaf node is missing", foundNode);
    }

    @Test
    public void testNodeWithName_typical() {
        List<Node> children = operationsHelper.children(root);
        Node node = operationsHelper.nodeWithName(children, "child");
        assertNotNull("Found node should not be null", node);
        assertEquals("Found node should have the correct name", "child", node.getNodeName());
    }

    @Test
    public void testNodeWithName_invalid() {
        List<Node> children = operationsHelper.children(root);
        Node node = operationsHelper.nodeWithName(children, "invalid");
        assertNull("Found node should be null when node name is invalid", node);
    }

    @Test
    public void testSublistWithName_typical() {
        List<Node> children = operationsHelper.children(root);
        List<Node> sublist = operationsHelper.sublistWithName(children, "child");
        assertNotNull("Sublist should not be null", sublist);
        assertFalse("Sublist should not be empty", sublist.isEmpty());
        assertEquals("Sublist should have the correct size", 1, sublist.size());
        assertEquals("Sublist element should have the correct name", "child", sublist.get(0).getNodeName());
    }

    @Test
    public void testSublistWithName_invalid() {
        List<Node> children = operationsHelper.children(root);
        List<Node> sublist = operationsHelper.sublistWithName(children, "invalid");
        assertNotNull("Sublist should not be null", sublist);
        assertTrue("Sublist should be empty", sublist.isEmpty());
    }
}

