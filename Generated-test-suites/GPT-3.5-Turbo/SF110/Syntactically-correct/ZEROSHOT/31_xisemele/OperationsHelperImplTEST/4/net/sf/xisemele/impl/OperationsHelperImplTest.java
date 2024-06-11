
package net.sf.xisemele.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setup() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        // Create a mock node
        Document document = new DocumentMock();

        // Add child nodes to the mock node
        Node child1 = new NodeMock("child1", Node.ELEMENT_NODE);
        Node child2 = new NodeMock("child2", Node.ELEMENT_NODE);
        NodeList nodeList = new NodeListMock(Arrays.asList(child1, child2));
        ((NodeMock) document).setChildNodes(nodeList);

        // Test the children method
        List<Node> result = operationsHelper.children(document);

        // Verify the result
        Assert.assertEquals("child1", result.get(0).getNodeName());
        Assert.assertEquals("child2", result.get(1).getNodeName());
    }

    @Test
    public void testFind() {
        // Create a mock document
        Document document = new DocumentMock();

        // Add child nodes to the mock document
        Node child1 = new NodeMock("child1", Node.ELEMENT_NODE);
        ((NodeMock) document).setChildNodes(new NodeListMock(Arrays.asList(child1)));

        // Test the find method with a valid path
        Node result1 = operationsHelper.find(document, "child1");
        Assert.assertEquals("child1", result1.getNodeName());

        // Test the find method with an invalid path
        Node result2 = operationsHelper.find(document, "child2");
        Assert.assertEquals(null, result2);
    }

    @Test
    public void testNodeWithName() {
        // Create a list of mock nodes
        List<Node> nodes = new ArrayList<>();
        nodes.add(new NodeMock("child1", Node.ELEMENT_NODE));
        nodes.add(new NodeMock("child2", Node.ELEMENT_NODE));

        // Test the nodeWithName method with an existing name
        Node result1 = operationsHelper.nodeWithName(nodes, "child1");
        Assert.assertEquals("child1", result1.getNodeName());

        // Test the nodeWithName method with a non-existing name
        Node result2 = operationsHelper.nodeWithName(nodes, "child3");
        Assert.assertEquals(null, result2);
    }

    @Test
    public void testSublistWithName() {
        // Create a list of mock nodes
        List<Node> nodes = new ArrayList<>();
        nodes.add(new NodeMock("child1", Node.ELEMENT_NODE));
        nodes.add(new NodeMock("child2", Node.ELEMENT_NODE));
        nodes.add(new NodeMock("child1", Node.ELEMENT_NODE));

        // Test the sublistWithName method with an existing name
        List<Node> result1 = operationsHelper.sublistWithName(nodes, "child1");
        Assert.assertEquals(2, result1.size());
        Assert.assertEquals("child1", result1.get(0).getNodeName());
        Assert.assertEquals("child1", result1.get(1).getNodeName());

        // Test the sublistWithName method with a non-existing name
        List<Node> result2 = operationsHelper.sublistWithName(nodes, "child3");
        Assert.assertEquals(0, result2.size());
    }
}
