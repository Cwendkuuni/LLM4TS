
import static org.junit.Assert.*;
import org.junit.Test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
    
    @Test
    public void testChildren_withEmptyNode_shouldReturnEmptyList() {
        Document document = createEmptyDocument();
        
        List<Node> result = operationsHelper.children(document);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testChildren_withNodeHavingChildren_shouldReturnListOfChildren() {
        Document document = createEmptyDocument();
        Element element = document.createElement("element");
        document.appendChild(element);
        Element childElement1 = document.createElement("child1");
        element.appendChild(childElement1);
        Element childElement2 = document.createElement("child2");
        element.appendChild(childElement2);
        
        List<Node> result = operationsHelper.children(document);
        
        assertEquals(2, result.size());
        assertEquals(childElement1, result.get(0));
        assertEquals(childElement2, result.get(1));
    }
    
    @Test
    public void testFind_withEmptyDocumentAndEmptyPath_shouldReturnNull() {
        Document document = createEmptyDocument();
        String path = "";
        
        Node result = operationsHelper.find(document, path);
        
        assertNull(result);
    }
    
    @Test
    public void testFind_withDocumentAndRootPath_shouldReturnRootElement() {
        Document document = createEmptyDocument();
        String path = document.getDocumentElement().getNodeName();
        
        Node result = operationsHelper.find(document, path);
        
        assertEquals(document.getDocumentElement(), result);
    }
    
    @Test
    public void testFind_withDocumentAndInvalidPath_shouldReturnNull() {
        Document document = createEmptyDocument();
        String path = "invalid";
        
        Node result = operationsHelper.find(document, path);
        
        assertNull(result);
    }
    
    @Test
    public void testFind_withDocumentAndValidPath_shouldReturnNode() {
        Document document = createEmptyDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        Element child1 = document.createElement("child1");
        root.appendChild(child1);
        Element child2 = document.createElement("child2");
        root.appendChild(child2);
        String path = "root/child2";
        
        Node result = operationsHelper.find(document, path);
        
        assertEquals(child2, result);
    }
    
    @Test
    public void testNodeWithName_withEmptyList_shouldReturnNull() {
        List<Node> nodes = new ArrayList<>();
        String name = "nodeName";
        
        Node result = operationsHelper.nodeWithName(nodes, name);
        
        assertNull(result);
    }
    
    @Test
    public void testNodeWithName_withListContainingNodeWithMatchingName_shouldReturnNode() {
        List<Node> nodes = new ArrayList<>();
        Document document = createEmptyDocument();
        Element element = document.createElement("nodeName");
        nodes.add(element);
        String name = "nodeName";
        
        Node result = operationsHelper.nodeWithName(nodes, name);
        
        assertEquals(element, result);
    }
    
    @Test
    public void testNodeWithName_withListNotContainingNodeWithMatchingName_shouldReturnNull() {
        List<Node> nodes = new ArrayList<>();
        Document document = createEmptyDocument();
        Element element = document.createElement("nodeName");
        nodes.add(element);
        String name = "invalidName";
        
        Node result = operationsHelper.nodeWithName(nodes, name);
        
        assertNull(result);
    }
    
    @Test
    public void testSublistWithName_withEmptyList_shouldReturnEmptyList() {
        List<Node> nodes = new ArrayList<>();
        String name = "nodeName";
        
        List<Node> result = operationsHelper.sublistWithName(nodes, name);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testSublistWithName_withListContainingNodesWithMatchingName_shouldReturnListWithMatchingNodes() {
        List<Node> nodes = new ArrayList<>();
        Document document = createEmptyDocument();
        Element element1 = document.createElement("nodeName");
        nodes.add(element1);
        Element element2 = document.createElement("nodeName");
        nodes.add(element2);
        String name = "nodeName";
        
        List<Node> result = operationsHelper.sublistWithName(nodes, name);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(element1));
        assertTrue(result.contains(element2));
    }
    
    @Test
    public void testSublistWithName_withListNotContainingNodesWithMatchingName_shouldReturnEmptyList() {
        List<Node> nodes = new ArrayList<>();
        Document document = createEmptyDocument();
        Element element = document.createElement("nodeName");
        nodes.add(element);
        String name = "invalidName";
        
        List<Node> result = operationsHelper.sublistWithName(nodes, name);
        
        assertTrue(result.isEmpty());
    }
    
    private Document createEmptyDocument() {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }
}

