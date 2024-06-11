import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {
    
    @Test
    public void testChildren() {
        Node node = Mockito.mock(Node.class);
        NodeList nodeList = Mockito.mock(NodeList.class);
        when(node.getChildNodes()).thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(2);
        Node item1 = Mockito.mock(Node.class);
        Node item2 = Mockito.mock(Node.class);
        when(item1.getNodeType()).thenReturn(1);
        when(item2.getNodeType()).thenReturn(2);
        when(nodeList.item(0)).thenReturn(item1);
        when(nodeList.item(1)).thenReturn(item2);
        
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        List<Node> children = operationsHelper.children(node);
        
        assertEquals(1, children.size());
        assertEquals(item1, children.get(0));
        
        verify(node).getChildNodes();
        verify(item1).getNodeType();
        verify(item2).getNodeType();
        verify(nodeList).item(0);
        verify(nodeList).item(1);
    }
    
    @Test
    public void testFind() {
        Document document = Mockito.mock(Document.class);
        Element documentElement = Mockito.mock(Element.class);
        when(document.getDocumentElement()).thenReturn(documentElement);
        when(documentElement.getNodeName()).thenReturn("root");
        
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Node found = operationsHelper.find(document, "root");
        
        assertEquals(documentElement, found);
        
        verify(document).getDocumentElement();
        verify(documentElement).getNodeName();
    }
    
    @Test
    public void testNodeWithName() {
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Node node = operationsHelper.nodeWithName(nodes, "name");
        
        assertEquals(node1, node);
    }
    
    @Test
    public void testSublistWithName() {
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "name");
        
        assertEquals(0, sublist.size());
    }
}