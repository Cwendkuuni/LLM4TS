package net.sf.xisemele.impl;

import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        // Setup
        Node node = mock(Node.class);
        NodeList nodeList = mock(NodeList.class);
        when(node.getChildNodes()).thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(3);
        when(nodeList.item(0)).thenReturn(mock(Node.class));
        when(nodeList.item(1)).thenReturn(mock(Node.class));
        when(nodeList.item(2)).thenReturn(mock(Node.class));

        OperationsHelperImpl operationsHelperImpl = new OperationsHelperImpl();

        // Execute
        List<Node> children = operationsHelperImpl.children(node);

        // Verify
        assertEquals(3, children.size());
    }

    @Test
    public void testFind() {
        // Setup
        Document document = mock(Document.class);
        Node documentElement = mock(Node.class);
        when(document.getDocumentElement()).thenReturn(documentElement);
        when(documentElement.getNodeName()).thenReturn("root");
        NodeList nodeList1 = mock(NodeList.class);
        NodeList nodeList2 = mock(NodeList.class);
        when(documentElement.getChildNodes()).thenReturn(nodeList1);
        when(nodeList1.getLength()).thenReturn(2);
        when(nodeList1.item(0)).thenReturn(mock(Node.class));
        when(nodeList1.item(1)).thenReturn(mock(Node.class));
        when(nodeList2.item(0)).thenReturn(mock(Node.class));
        when(nodeList2.item(1)).thenReturn(mock(Node.class));
        when(nodeList2.getLength()).thenReturn(2);
        when(nodeList1.item(0).getChildNodes()).thenReturn(nodeList2);
        when(nodeList1.item(1).getChildNodes()).thenReturn(nodeList2);
        when(nodeList1.item(0).getNodeName()).thenReturn("child1");
        when(nodeList1.item(1).getNodeName()).thenReturn("child2");
        when(nodeList2.item(0).getNodeName()).thenReturn("grandchild1");
        when(nodeList2.item(1).getNodeName()).thenReturn("grandchild2");

        OperationsHelperImpl operationsHelperImpl = new OperationsHelperImpl();

        // Execute
        Node found = operationsHelperImpl.find(document, "root/child1/grandchild1");

        // Verify
        assertEquals("grandchild1", found.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        // Setup
        List<Node> nodes = new ArrayList<>();
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        Node node3 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("node1");
        when(node2.getNodeName()).thenReturn("node2");
        when(node3.getNodeName()).thenReturn("node3");
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);

        OperationsHelperImpl operationsHelperImpl = new OperationsHelperImpl();

        // Execute
        Node found = operationsHelperImpl.nodeWithName(nodes, "node2");

        // Verify
        assertEquals("node2", found.getNodeName());
    }

    @Test
    public void testSublistWithName() {
        // Setup
        List<Node> nodes = new ArrayList<>();
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        Node node3 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("node");
        when(node2.getNodeName()).thenReturn("node");
        when(node3.getNodeName()).thenReturn("otherNode");
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);

        OperationsHelperImpl operationsHelperImpl = new OperationsHelperImpl();

        // Execute
        List<Node> sublist = operationsHelperImpl.sublistWithName(nodes, "node");

        // Verify
        assertEquals(2, sublist.size());
    }
}