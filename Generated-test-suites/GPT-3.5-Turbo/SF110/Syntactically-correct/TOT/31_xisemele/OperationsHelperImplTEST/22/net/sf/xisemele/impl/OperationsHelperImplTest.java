import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl helper;
    private Document document;

    @Before
    public void setUp() {
        helper = new OperationsHelperImpl();
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }

    /*
    * Test for the children() method
    */

    // Typical case: node has multiple children
    @Test
    public void testChildren_typicalCase() {
        Element parent = document.createElement("parent");
        Element child1 = document.createElement("child1");
        parent.appendChild(child1);
        Element child2 = document.createElement("child2");
        parent.appendChild(child2);
        Element child3 = document.createElement("child3");
        parent.appendChild(child3);

        List<Node> result = helper.children(parent);

        assertEquals("Expected number of children is not matching", 3, result.size());
        assertEquals("The first child is not matching", child1, result.get(0));
        assertEquals("The second child is not matching", child2, result.get(1));
        assertEquals("The third child is not matching", child3, result.get(2));
    }

    // Edge case: node has no children
    @Test
    public void testChildren_emptyNode() {
        Element emptyNode = document.createElement("emptyNode");

        List<Node> result = helper.children(emptyNode);

        assertTrue("Expected list to be empty", result.isEmpty());
    }

    // Edge case: node has only non-element children
    @Test
    public void testChildren_noElementChildren() {
        Element parent = document.createElement("parent");
        Text textNode = document.createTextNode("Text node");
        parent.appendChild(textNode);
        Comment comment = document.createComment("Comment node");
        parent.appendChild(comment);

        List<Node> result = helper.children(parent);

        assertTrue("Expected list to be empty", result.isEmpty());
    }

    /*
    * Test for the find() method
    */

    // Typical case: path points to an existing element
    @Test
    public void testFind_typicalCase() {
        Element rootNode = document.createElement("root");
        document.appendChild(rootNode);
        Element element1 = document.createElement("element1");
        rootNode.appendChild(element1);
        Element element2 = document.createElement("element2");
        element1.appendChild(element2);
        Element element3 = document.createElement("element3");
        element2.appendChild(element3);

        Node result = helper.find(document, "root/element1/element2/element3");

        assertEquals("Expected element is not matching", element3, result);
    }

    // Error scenario: path points to a non-existing element
    @Test
    public void testFind_nonExistingPath() {
        Element rootNode = document.createElement("root");
        document.appendChild(rootNode);
        Element element1 = document.createElement("element1");
        rootNode.appendChild(element1);
        Element element2 = document.createElement("element2");
        element1.appendChild(element2);

        Node result = helper.find(document, "root/element1/element3");

        assertNull("Expected result to be null", result);
    }

    // Edge case: path is empty
    @Test
    public void testFind_emptyPath() {
        Element rootNode = document.createElement("root");
        document.appendChild(rootNode);

        Node result = helper.find(document, "");

        assertEquals("Expected root element is not matching", rootNode, result);
    }

    /*
    * Test for the nodeWithName() method
    */

    // Typical case: list contains an element with the given name
    @Test
    public void testNodeWithName_typicalCase() {
        Element element1 = document.createElement("element1");
        Element element2 = document.createElement("element2");
        Element element3 = document.createElement("element3");
        Element element4 = document.createElement("element4");
        Element element5 = document.createElement("element5");

        List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);
        nodes.add(element4);
        nodes.add(element5);

        Node result = helper.nodeWithName(nodes, "element3");

        assertEquals("Expected element is not matching", element3, result);
    }

    // Edge case: list does not contain any element with the given name
    @Test
    public void testNodeWithName_nonExistingElement() {
        Element element1 = document.createElement("element1");
        Element element2 = document.createElement("element2");
        Element element3 = document.createElement("element3");
        Element element4 = document.createElement("element4");
        Element element5 = document.createElement("element5");

        List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);
        nodes.add(element4);
        nodes.add(element5);

        Node result = helper.nodeWithName(nodes, "nonexisting");

        assertNull("Expected result to be null", result);
    }

    // Edge case: list is empty
    @Test
    public void testNodeWithName_emptyList() {
        List<Node> nodes = new ArrayList<>();

        Node result = helper.nodeWithName(nodes, "element");

        assertNull("Expected result to be null", result);
    }

    /*
    * Test for the sublistWithName() method
    */

    // Typical case: list contains multiple element with the given name
    @Test
    public void testSublistWithName_typicalCase() {
        Element element1 = document.createElement("element");
        Element element2 = document.createElement("element");
        Element element3 = document.createElement("element");
        Element element4 = document.createElement("element");
        Element element5 = document.createElement("element");

        List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);
        nodes.add(element4);
        nodes.add(element5);

        List<Node> result = helper.sublistWithName(nodes, "element");

        assertEquals("Expected number of elements is not matching", 5, result.size());
        assertEquals("The first element is not matching", element1, result.get(0));
        assertEquals("The second element is not matching", element2, result.get(1));
        assertEquals("The third element is not matching", element3, result.get(2));
        assertEquals("The fourth element is not matching", element4, result.get(3));
        assertEquals("The fifth element is not matching", element5, result.get(4));
    }

    // Edge case: list does not contain any element with the given name
    @Test
    public void testSublistWithName_nonExistingElement() {
        Element element1 = document.createElement("element1");
        Element element2 = document.createElement("element2");
        Element element3 = document.createElement("element3");
        Element element4 = document.createElement("element4");
        Element element5 = document.createElement("element5");

        List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);
        nodes.add(element4);
        nodes.add(element5);

        List<Node> result = helper.sublistWithName(nodes, "nonexisting");

        assertTrue("Expected list to be empty", result.isEmpty());
    }

    // Edge case: list is empty
    @Test
    public void testSublistWithName_emptyList() {
        List<Node> nodes = new ArrayList<>();

        List<Node> result = helper.sublistWithName(nodes, "element");

        assertTrue("Expected list to be empty", result.isEmpty());
    }

}