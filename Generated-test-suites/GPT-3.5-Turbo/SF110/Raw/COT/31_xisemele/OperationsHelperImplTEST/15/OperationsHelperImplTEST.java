
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Mock
    private Node parentNode;
    private NodeList childNodes;
    private Document document;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
        childNodes = new NodeListMock();
        document = Mockito.mock(Document.class);
        Element documentElement = Mockito.mock(Element.class);
        Mockito.when(document.getDocumentElement()).thenReturn(documentElement);
        Mockito.when(documentElement.getNodeName()).thenReturn("root");
        Mockito.when(parentNode.getChildNodes()).thenReturn(childNodes);
    }

    @Test
    public void testChildren_emptyNodeList() {
        List<Node> nodeList = operationsHelper.children(parentNode);
        assertEquals(0, nodeList.size());
    }

    @Test
    public void testChildren_onlyElementNodes() {
        childNodes = new NodeListMock(Mockito.mock(Node.class), Mockito.mock(Element.class), Mockito.mock(Node.class));
        Mockito.when(parentNode.getChildNodes()).thenReturn(childNodes);
        List<Node> nodeList = operationsHelper.children(parentNode);
        assertEquals(1, nodeList.size());
        assertEquals(Element.class, nodeList.get(0).getClass());
    }

    @Test
    public void testFind_rootElementNotFound() {
        Mockito.when(document.getDocumentElement().getNodeName()).thenReturn("notRoot");
        Node foundNode = operationsHelper.find(document, "root");
        assertNull(foundNode);
    }

    @Test
    public void testFind_emptyPath() {
        Node foundNode = operationsHelper.find(document, "");
        assertEquals(document.getDocumentElement(), foundNode);
    }

    @Test
    public void testFind_nodeNotFound() {
        childNodes = new NodeListMock(Mockito.mock(Node.class));
        Mockito.when(parentNode.getChildNodes()).thenReturn(childNodes);
        Node foundNode = operationsHelper.find(document, "root/child");
        assertNull(foundNode);
    }

    @Test
    public void testFind_nodeFound() {
        Node childNode = Mockito.mock(Node.class);
        Mockito.when(childNode.getNodeName()).thenReturn("child");
        childNodes = new NodeListMock(childNode);
        Mockito.when(parentNode.getChildNodes()).thenReturn(childNodes);
        Node foundNode = operationsHelper.find(document, "root");
        assertEquals(childNode, foundNode);
    }

    @Test
    public void testNodeWithName_nodeNotFound() {
        List<Node> nodes = new ArrayList<>();
        Node foundNode = operationsHelper.nodeWithName(nodes, "name");
        assertNull(foundNode);
    }

    @Test
    public void testNodeWithName_nodeFound() {
        Node node = Mockito.mock(Node.class);
        Mockito.when(node.getNodeName()).thenReturn("name");
        List<Node> nodes = new ArrayList<>();
        nodes.add(Mockito.mock(Node.class));
        nodes.add(node);
        nodes.add(Mockito.mock(Node.class));
        Node foundNode = operationsHelper.nodeWithName(nodes, "name");
        assertEquals(node, foundNode);
    }

    @Test
    public void testSublistWithName_noNodesWithName() {
        List<Node> nodes = new ArrayList<>();
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "name");
        assertEquals(0, sublist.size());
    }

    @Test
    public void testSublistWithName_nodesWithName() {
        Node node1 = Mockito.mock(Node.class);
        Mockito.when(node1.getNodeName()).thenReturn("name");
        Node node2 = Mockito.mock(Node.class);
        Mockito.when(node2.getNodeName()).thenReturn("name");
        Node node3 = Mockito.mock(Node.class);
        Mockito.when(node3.getNodeName()).thenReturn("name");
        List<Node> nodes = new ArrayList<>();
        nodes.add(Mockito.mock(Node.class));
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(Mockito.mock(Node.class));
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "name");
        assertEquals(3, sublist.size());
        for (Node node : sublist) {
            assertEquals("name", node.getNodeName());
        }
    }

    private class NodeListMock implements NodeList {

        private List<Node> nodes;

        public NodeListMock(Node... nodes) {
            this.nodes = new ArrayList<>();
            for (Node node : nodes) {
                this.nodes.add(node);
            }
        }

        @Override
        public Node item(int index) {
            return nodes.get(index);
        }

        @Override
        public int getLength() {
            return nodes.size();
        }

    }
}
