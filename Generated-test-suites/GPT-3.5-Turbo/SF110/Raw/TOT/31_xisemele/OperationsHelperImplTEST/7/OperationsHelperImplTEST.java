
import org.junit.*;
import org.w3c.dom.*;

import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    private Document document;
    private Element rootElement;
    private Element childElement;
    private Text textNode;
    private Comment commentNode;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();

        // Create a sample XML document for testing
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        rootElement = document.createElement("root");
        childElement = document.createElement("child");
        rootElement.appendChild(childElement);
        textNode = document.createTextNode("sample text");
        childElement.appendChild(textNode);
        commentNode = document.createComment("sample comment");
        rootElement.appendChild(commentNode);
        document.appendChild(rootElement);
    }

    @Test
    public void testChildren() {
        List<Node> children = operationsHelper.children(rootElement);

        // Verify that the number of children is correct
        Assert.assertEquals(2, children.size());

        // Verify that the returned list contains the correct children
        Assert.assertTrue(children.contains(childElement));
        Assert.assertTrue(children.contains(commentNode));
    }

    @Test
    public void testChildrenWithNoChildren() {
        List<Node> children = operationsHelper.children(childElement);

        // Verify that the number of children is correct
        Assert.assertEquals(0, children.size());
    }

    @Test
    public void testFind() {
        Node found = operationsHelper.find(document, "root/child");

        // Verify that the correct node is found
        Assert.assertEquals(childElement, found);
    }

    @Test
    public void testFindWithInvalidPath() {
        Node found = operationsHelper.find(document, "root/invalid");

        // Verify that null is returned for invalid path
        Assert.assertNull(found);
    }

    @Test
    public void testFindWithEmptyPath() {
        Node found = operationsHelper.find(document, "");

        // Verify that null is returned for empty path
        Assert.assertNull(found);
    }

    @Test
    public void testNodeWithName() {
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(childElement);
        nodeList.add(commentNode);
        nodeList.add(textNode);

        Node found = operationsHelper.nodeWithName(nodeList, "child");

        // Verify that the correct node is found
        Assert.assertEquals(childElement, found);
    }

    @Test
    public void testNodeWithNameNotFound() {
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(rootElement);

        Node found = operationsHelper.nodeWithName(nodeList, "child");

        // Verify that null is returned when node is not found
        Assert.assertNull(found);
    }

    @Test
    public void testSublistWithName() {
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(childElement);
        nodeList.add(commentNode);
        nodeList.add(textNode);

        List<Node> sublist = operationsHelper.sublistWithName(nodeList, "child");

        // Verify that the correct sublist is obtained
        Assert.assertEquals(1, sublist.size());
        Assert.assertEquals(childElement, sublist.get(0));
    }

    @Test
    public void testSublistWithNameEmptyList() {
        List<Node> nodeList = new ArrayList<>();

        List<Node> sublist = operationsHelper.sublistWithName(nodeList, "child");

        // Verify that the sublist is empty when the node list is empty
        Assert.assertEquals(0, sublist.size());
    }

    @Test
    public void testSublistWithNameNotFound() {
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(rootElement);

        List<Node> sublist = operationsHelper.sublistWithName(nodeList, "child");

        // Verify that an empty sublist is returned when node is not found
        Assert.assertEquals(0, sublist.size());
    }
}

