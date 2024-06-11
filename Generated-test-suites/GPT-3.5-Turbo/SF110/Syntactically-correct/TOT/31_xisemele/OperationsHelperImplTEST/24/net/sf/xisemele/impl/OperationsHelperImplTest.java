import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

    @Test
    public void testChildren() {
        // Typical use case
        Element rootElement = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        rootElement.appendChild(child3);

        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(child1);
        expectedChildren.add(child2);
        expectedChildren.add(child3);

        List<Node> actualChildren = operationsHelper.children(rootElement);

        assertEquals(expectedChildren, actualChildren);

        // Edge case: empty element
        Element emptyElement = document.createElement("empty");
        List<Node> expectedEmptyChildren = new ArrayList<>();
        List<Node> actualEmptyChildren = operationsHelper.children(emptyElement);

        assertEquals(expectedEmptyChildren, actualEmptyChildren);

        // Error scenario: null input
        List<Node> actualNullChildren = operationsHelper.children(null);

        assertNull(actualNullChildren);
    }

    @Test
    public void testFind() {
        // Typical use case
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        rootElement.appendChild(child1);
        child1.appendChild(child2);
        child2.appendChild(child3);
        document.appendChild(rootElement);

        Node expectedNode = child3;
        Node actualNode = operationsHelper.find(document, "/root/child1/child2/child3");

        assertEquals(expectedNode, actualNode);

        // Edge case: empty path
        Node expectedEmptyNode = rootElement;
        Node actualEmptyNode = operationsHelper.find(document, "");

        assertEquals(expectedEmptyNode, actualEmptyNode);

        // Error scenario: path does not exist
        Node actualNonExistentNode = operationsHelper.find(document, "/nonexistent");

        assertNull(actualNonExistentNode);
    }

    @Test
    public void testNodeWithName() {
        // Typical use case
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        List<Node> nodes = new ArrayList<>();
        nodes.add(child1);
        nodes.add(child2);

        Node expectedNode = child1;
        Node actualNode = operationsHelper.nodeWithName(nodes, "child1");

        assertEquals(expectedNode, actualNode);

        // Edge case: empty node list
        List<Node> emptyNodes = new ArrayList<>();

        Node expectedEmptyNode = null;
        Node actualEmptyNode = operationsHelper.nodeWithName(emptyNodes, "child1");

        assertEquals(expectedEmptyNode, actualEmptyNode);

        // Error scenario: node with given name does not exist
        Node actualNonExistentNode = operationsHelper.nodeWithName(nodes, "nonexistent");

        assertNull(actualNonExistentNode);
    }

    @Test
    public void testSublistWithName() {
        // Typical use case
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child1");
        List<Node> nodes = new ArrayList<>();
        nodes.add(child1);
        nodes.add(child2);
        nodes.add(child3);

        List<Node> expectedSublist = new ArrayList<>();
        expectedSublist.add(child1);
        expectedSublist.add(child3);

        List<Node> actualSublist = operationsHelper.sublistWithName(nodes, "child1");

        assertEquals(expectedSublist, actualSublist);

        // Edge case: empty node list
        List<Node> emptyNodes = new ArrayList<>();

        List<Node> expectedEmptySublist = new ArrayList<>();
        List<Node> actualEmptySublist = operationsHelper.sublistWithName(emptyNodes, "child1");

        assertEquals(expectedEmptySublist, actualEmptySublist);

        // Error scenario: node with given name does not exist
        List<Node> actualNonExistentSublist = operationsHelper.sublistWithName(nodes, "nonexistent");

        assertTrue(actualNonExistentSublist.isEmpty());
    }
}