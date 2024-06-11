import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;

import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren_WhenNodeHasChildren_ReturnListOfChildrenNodes() {
        Document document = Mockito.mock(Document.class);
        Element element = Mockito.mock(Element.class);
        NodeList nodeList = Mockito.mock(NodeList.class);
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        Node node3 = Mockito.mock(Node.class);

        Mockito.when(node.getChildNodes()).thenReturn(nodeList);
        Mockito.when(nodeList.getLength()).thenReturn(3);
        Mockito.when(nodeList.item(0)).thenReturn(node1);
        Mockito.when(nodeList.item(1)).thenReturn(node2);
        Mockito.when(nodeList.item(2)).thenReturn(node3);
        Mockito.when(node1.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        Mockito.when(node2.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        Mockito.when(node3.getNodeType()).thenReturn(Node.TEXT_NODE);

        List<Node> expectedResult = new ArrayList<>();
        expectedResult.add(node1);
        expectedResult.add(node2);

        List<Node> actualResult = operationsHelper.children(node);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testChildren_WhenNodeHasNoChildren_ReturnEmptyList() {
        Document document = Mockito.mock(Document.class);
        Element element = Mockito.mock(Element.class);
        NodeList nodeList = Mockito.mock(NodeList.class);

        Mockito.when(node.getChildNodes()).thenReturn(nodeList);
        Mockito.when(nodeList.getLength()).thenReturn(0);

        List<Node> expectedResult = new ArrayList<>();

        List<Node> actualResult = operationsHelper.children(node);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFind_WhenPathExists_ReturnFoundNode() {
        Document document = Mockito.mock(Document.class);
        Element element1 = Mockito.mock(Element.class);
        Element element2 = Mockito.mock(Element.class);
        Element element3 = Mockito.mock(Element.class);
        Element element4 = Mockito.mock(Element.class);
        Element element5 = Mockito.mock(Element.class);
        NodeList nodeList1 = Mockito.mock(NodeList.class);
        NodeList nodeList2 = Mockito.mock(NodeList.class);
        NodeList nodeList3 = Mockito.mock(NodeList.class);
        NodeList nodeList4 = Mockito.mock(NodeList.class);

        Mockito.when(document.getDocumentElement()).thenReturn(element1);
        Mockito.when(element1.getNodeName()).thenReturn("root");
        Mockito.when(element1.getChildNodes()).thenReturn(nodeList1);
        Mockito.when(nodeList1.getLength()).thenReturn(1);
        Mockito.when(nodeList1.item(0)).thenReturn(element2);
        Mockito.when(element2.getNodeName()).thenReturn("parent1");
        Mockito.when(element2.getChildNodes()).thenReturn(nodeList2);
        Mockito.when(nodeList2.getLength()).thenReturn(1);
        Mockito.when(nodeList2.item(0)).thenReturn(element3);
        Mockito.when(element3.getNodeName()).thenReturn("child1");
        Mockito.when(element3.getChildNodes()).thenReturn(nodeList3);
        Mockito.when(nodeList3.getLength()).thenReturn(1);
        Mockito.when(nodeList3.item(0)).thenReturn(element4);
        Mockito.when(element4.getNodeName()).thenReturn("grandchild1");
        Mockito.when(element4.getChildNodes()).thenReturn(nodeList4);
        Mockito.when(nodeList4.getLength()).thenReturn(1);
        Mockito.when(nodeList4.item(0)).thenReturn(element5);
        Mockito.when(element5.getNodeName()).thenReturn("greatgrandchild1");

        String path = "root/parent1/child1/grandchild1/greatgrandchild1";

        Node expectedResult = element5;

        Node actualResult = operationsHelper.find(document, path);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFind_WhenPathDoesNotExist_ReturnNull() {
        Document document = Mockito.mock(Document.class);
        Element element1 = Mockito.mock(Element.class);
        Element element2 = Mockito.mock(Element.class);
        Element element3 = Mockito.mock(Element.class);
        NodeList nodeList1 = Mockito.mock(NodeList.class);
        NodeList nodeList2 = Mockito.mock(NodeList.class);
        NodeList nodeList3 = Mockito.mock(NodeList.class);

        Mockito.when(document.getDocumentElement()).thenReturn(element1);
        Mockito.when(element1.getNodeName()).thenReturn("root");
        Mockito.when(element1.getChildNodes()).thenReturn(nodeList1);
        Mockito.when(nodeList1.getLength()).thenReturn(1);
        Mockito.when(nodeList1.item(0)).thenReturn(element2);
        Mockito.when(element2.getNodeName()).thenReturn("parent1");
        Mockito.when(element2.getChildNodes()).thenReturn(nodeList2);
        Mockito.when(nodeList2.getLength()).thenReturn(1);
        Mockito.when(nodeList2.item(0)).thenReturn(element3);
        Mockito.when(element3.getNodeName()).thenReturn("child1");
        Mockito.when(element3.getChildNodes()).thenReturn(nodeList3);
        Mockito.when(nodeList3.getLength()).thenReturn(1);

        String path = "root/parent1/child1/grandchild1/greatgrandchild1";

        Node expectedResult = null;

        Node actualResult = operationsHelper.find(document, path);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testNodeWithName_WhenNodeListContainsNodeWithName_ReturnNode() {
        Element element1 = Mockito.mock(Element.class);
        Element element2 = Mockito.mock(Element.class);
        Element element3 = Mockito.mock(Element.class);
        NodeList nodeList = Mockito.mock(NodeList.class);

        Mockito.when(nodeList.getLength()).thenReturn(3);
        Mockito.when(nodeList.item(0)).thenReturn(element1);
        Mockito.when(nodeList.item(1)).thenReturn(element2);
        Mockito.when(nodeList.item(2)).thenReturn(element3);
        Mockito.when(element1.getNodeName()).thenReturn("node1");
        Mockito.when(element2.getNodeName()).thenReturn("node2");
        Mockito.when(element3.getNodeName()).thenReturn("node3");

        List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);

        String name = "node2";

        Node expectedResult = element2;

        Node actualResult = operationsHelper.nodeWithName(nodes, name);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testNodeWithName_WhenNodeListDoesNotContainNodeWithName_ReturnNull() {
        Element element1 = Mockito.mock(Element.class);
        Element element2 = Mockito.mock(Element.class);
        Element element3 = Mockito.mock(Element.class);
        NodeList nodeList = Mockito.mock(NodeList.class);

        Mockito.when(nodeList.getLength()).thenReturn(3);
        Mockito.when(nodeList.item(0)).thenReturn(element1);
        Mockito.when(nodeList.item(1)).thenReturn(element2);
        Mockito.when(nodeList.item(2)).thenReturn(element3);
        Mockito.when(element1.getNodeName()).thenReturn("node1");
        Mockito.when(element2.getNodeName()).thenReturn("node2");
        Mockito.when(element3.getNodeName()).thenReturn("node3");

        List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);

        String name = "node4";

        Node expectedResult = null;

        Node actualResult = operationsHelper.nodeWithName(nodes, name);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSublistWithName_WhenNodeListContainsNodeWithName_ReturnListOfNodes() {
        Element element1 = Mockito.mock(Element.class);
        Element element2 = Mockito.mock(Element.class);
        Element element3 = Mockito.mock(Element.class);
        Element element4 = Mockito.mock(Element.class);
        NodeList nodeList = Mockito.mock(NodeList.class);

        Mockito.when(nodeList.getLength()).thenReturn(4);
        Mockito.when(nodeList.item(0)).thenReturn(element1);
        Mockito.when(nodeList.item(1)).thenReturn(element2);
        Mockito.when(nodeList.item(2)).thenReturn(element3);
        Mockito.when(nodeList.item(3)).thenReturn(element4);
        Mockito.when(element1.getNodeName()).thenReturn("node1");
        Mockito.when(element2.getNodeName()).thenReturn("node2");
        Mockito.when(element3.getNodeName()).thenReturn("node3");
        Mockito.when(element4.getNodeName()).thenReturn("node2");

        List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);
        nodes.add(element4);

        String name = "node2";

        List<Node> expectedResult = new ArrayList<>();
        expectedResult.add(element2);
        expectedResult.add(element4);

        List<Node> actualResult = operationsHelper.sublistWithName(nodes, name);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSublistWithName_WhenNodeListDoesNotContainNodeWithName_ReturnEmptyList() {
        Element element1 = Mockito.mock(Element.class);
        Element element2 = Mockito.mock(Element.class);
        Element element3 = Mockito.mock(Element.class);
        NodeList nodeList = Mockito.mock(NodeList.class);

        Mockito.when(nodeList.getLength()).thenReturn(3);
        Mockito.when(nodeList.item(0)).thenReturn(element1);
        Mockito.when(nodeList.item(1)).thenReturn(element2);
        Mockito.when(nodeList.item(2)).thenReturn(element3);
        Mockito.when(element1.getNodeName()).thenReturn("node1");
        Mockito.when(element2.getNodeName()).thenReturn("node2");
        Mockito.when(element3.getNodeName()).thenReturn("node3");

        List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);

        String name = "node4";

        List<Node> expectedResult = new ArrayList<>();

        List<Node> actualResult = operationsHelper.sublistWithName(nodes, name);

        assertEquals(expectedResult, actualResult);
    }
}