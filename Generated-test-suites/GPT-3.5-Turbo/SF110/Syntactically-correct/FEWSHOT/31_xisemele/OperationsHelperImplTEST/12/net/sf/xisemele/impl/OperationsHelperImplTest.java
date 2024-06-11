import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }
    
    @Test
    public void testChildren() {
        Document document = createDocument();
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        Node node3 = document.createElement("node3");
        Node node4 = document.createTextNode("text");
        
        node1.appendChild(node2);
        node1.appendChild(node4);
        node2.appendChild(node3);
        
        List<Node> children = operationsHelper.children(node1);
        
        assertEquals(2, children.size());
        assertEquals(node2, children.get(0));
        assertEquals(node4, children.get(1));
    }
    
    @Test
    public void testFind() {
        Document document = createDocument();
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        Node node3 = document.createTextNode("text");
        
        document.appendChild(node1);
        node1.appendChild(node2);
        node2.appendChild(node3);
        
        Node found = operationsHelper.find(document, "node1/node2");
        
        assertNotNull(found);
        assertEquals(node2, found);
    }
    
    @Test
    public void testFind_notFound() {
        Document document = createDocument();
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        Node node3 = document.createTextNode("text");
        
        document.appendChild(node1);
        node1.appendChild(node2);
        node2.appendChild(node3);
        
        Node found = operationsHelper.find(document, "node2/node3");
        
        assertNull(found);
    }
    
    @Test
    public void testNodeWithName() {
        Document document = createDocument();
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        Node node3 = document.createTextNode("text");
        
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        
        Node found = operationsHelper.nodeWithName(nodes, "node2");
        
        assertNotNull(found);
        assertEquals(node2, found);
    }
    
    @Test
    public void testNodeWithName_notFound() {
        Document document = createDocument();
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        Node node3 = document.createTextNode("text");
        
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        
        Node found = operationsHelper.nodeWithName(nodes, "node4");
        
        assertNull(found);
    }
    
    @Test
    public void testSublistWithName() {
        Document document = createDocument();
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        Node node3 = document.createTextNode("text");
        
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "node2");
        
        assertEquals(1, sublist.size());
        assertEquals(node2, sublist.get(0));
    }
    
    @Test
    public void testSublistWithName_empty() {
        Document document = createDocument();
        Node node1 = document.createElement("node1");
        Node node2 = document.createElement("node2");
        Node node3 = document.createTextNode("text");
        
        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "node4");
        
        assertEquals(0, sublist.size());
    }
    
    private Document createDocument() {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        return document;
    }
}