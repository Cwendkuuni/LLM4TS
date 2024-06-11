import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OperationsHelperImplTest {

    private Document document;
    private OperationsHelperImpl operationsHelper;

    @Before
    public void setup() {
        document = createDocument();
        operationsHelper = new OperationsHelperImpl();
    }

    @After
    public void teardown() {
        document = null;
        operationsHelper = null;
    }

    @Test
    public void testChildren() {
        // Case 1: Test with empty node
        NodeList nodeList = document.getElementsByTagName("EmptyNode");
        List<Node> expectedChildren = new ArrayList<>();
        List<Node> actualChildren = operationsHelper.children(nodeList.item(0));
        assertEquals(expectedChildren, actualChildren);

        // Case 2: Test with node having children
        nodeList = document.getElementsByTagName("ParentNode");
        expectedChildren.add(document.createElement("Child1"));
        expectedChildren.add(document.createElement("Child2"));
        actualChildren = operationsHelper.children(nodeList.item(0));
        assertEquals(expectedChildren, actualChildren);
    }

    @Test
    public void testFind() {
        // Case 1: Test with valid path
        String path = "RootElement/ParentNode/Child1";
        Node expectedNode = document.getElementsByTagName("Child1").item(0);
        Node actualNode = operationsHelper.find(document, path);
        assertEquals(expectedNode, actualNode);

        // Case 2: Test with invalid path
        path = "RootElement/InvalidNode";
        expectedNode = null;
        actualNode = operationsHelper.find(document, path);
        assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testNodeWithName() {
        // Case 1: Test with existing node name
        List<Node> nodes = new ArrayList<>();
        nodes.add(document.createElement("Node1"));
        nodes.add(document.createElement("Node2"));
        String name = "Node2";
        Node expectedNode = document.createElement("Node2");
        Node actualNode = operationsHelper.nodeWithName(nodes, name);
        assertEquals(expectedNode, actualNode);

        // Case 2: Test with non-existing node name
        name = "Node3";
        expectedNode = null;
        actualNode = operationsHelper.nodeWithName(nodes, name);
        assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testSublistWithName() {
        // Case 1: Test with existing node name
        List<Node> nodes = new ArrayList<>();
        nodes.add(document.createElement("Node1"));
        nodes.add(document.createElement("Node2"));
        nodes.add(document.createElement("Node2"));
        String name = "Node2";
        List<Node> expectedSublist = new ArrayList<>();
        expectedSublist.add(document.createElement("Node2"));
        expectedSublist.add(document.createElement("Node2"));
        List<Node> actualSublist = operationsHelper.sublistWithName(nodes, name);
        assertEquals(expectedSublist, actualSublist);

        // Case 2: Test with non-existing node name
        name = "Node3";
        expectedSublist = new ArrayList<>();
        actualSublist = operationsHelper.sublistWithName(nodes, name);
        assertEquals(expectedSublist, actualSublist);
    }

    private Document createDocument() {
        Document document = new OperationsHelperImpl().createDocument();

        Element rootElement = document.createElement("RootElement");
        document.appendChild(rootElement);

        Element parentNode = document.createElement("ParentNode");
        rootElement.appendChild(parentNode);

        Element child1 = document.createElement("Child1");
        Element child2 = document.createElement("Child2");
        parentNode.appendChild(child1);
        parentNode.appendChild(child2);

        return document;
    }
}