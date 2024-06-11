package net.sf.xisemele.impl;

import org.junit.*;
import org.w3c.dom.*;

import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        // Test when node has children
        Element element = document.createElement("element");
        element.appendChild(document.createElement("child1"));
        element.appendChild(document.createElement("child2"));

        List<Node> children = operationsHelper.children(element);

        Assert.assertEquals(2, children.size());
        Assert.assertEquals("child1", children.get(0).getNodeName());
        Assert.assertEquals("child2", children.get(1).getNodeName());

        // Test when node has no children
        element = document.createElement("element");

        children = operationsHelper.children(element);

        Assert.assertEquals(0, children.size());
    }

    @Test
    public void testFind() {
        Document document = createDocument();

        // Test when path is valid
        Node found = operationsHelper.find(document, "root/child1/child2");

        Assert.assertNotNull(found);
        Assert.assertEquals("child2", found.getNodeName());

        // Test when path is invalid
        found = operationsHelper.find(document, "root/child1/child3");

        Assert.assertNull(found);
    }

    @Test
    public void testNodeWithName() {
        List<Node> nodes = createNodes();

        // Test when node with name exists
        Node node = operationsHelper.nodeWithName(nodes, "child1");

        Assert.assertNotNull(node);
        Assert.assertEquals("child1", node.getNodeName());

        // Test when node with name does not exist
        node = operationsHelper.nodeWithName(nodes, "child3");

        Assert.assertNull(node);
    }

    @Test
    public void testSublistWithName() {
        List<Node> nodes = createNodes();

        // Test when sublist with name exists
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child1");

        Assert.assertEquals(2, sublist.size());
        Assert.assertEquals("child1", sublist.get(0).getNodeName());
        Assert.assertEquals("child1", sublist.get(1).getNodeName());

        // Test when sublist with name does not exist
        sublist = operationsHelper.sublistWithName(nodes, "child3");

        Assert.assertEquals(0, sublist.size());
    }

    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            Element child3 = document.createElement("child3");

            child1.appendChild(child2);
            rootElement.appendChild(child1);
            rootElement.appendChild(child3);

            document.appendChild(rootElement);

            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Node> createNodes() {
        List<Node> nodes = new ArrayList<>();

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child1");
        Element child3 = document.createElement("child2");

        nodes.add(child1);
        nodes.add(child2);
        nodes.add(child3);

        return nodes;
    }
}