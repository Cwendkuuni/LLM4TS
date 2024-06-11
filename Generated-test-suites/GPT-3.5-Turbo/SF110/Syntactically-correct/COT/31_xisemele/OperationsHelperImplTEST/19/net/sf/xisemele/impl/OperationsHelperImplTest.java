
import static org.junit.Assert.*;
import org.junit.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {
    private OperationsHelperImpl operationsHelper;
    private Document document;
    private List<Node> nodes;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
        document = createDocument();
        nodes = createNodes();
    }
    
    @Test
    public void testChildren() {
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        assertEquals(2, children.size());
        assertTrue(children.contains(document.getElementsByTagName("child1").item(0)));
        assertTrue(children.contains(document.getElementsByTagName("child2").item(0)));
    }
    
    @Test
    public void testFind() {
        Node foundNode = operationsHelper.find(document, "/root/child1/grandchild");
        assertNotNull(foundNode);
        assertEquals("grandchild", foundNode.getNodeName());
    }
    
    @Test
    public void testFind_NotFound() {
        Node foundNode = operationsHelper.find(document, "/root/child1/nonexistent");
        assertNull(foundNode);
    }
    
    @Test
    public void testNodeWithName() {
        Node foundNode = operationsHelper.nodeWithName(nodes, "child1");
        assertNotNull(foundNode);
        assertEquals("child1", foundNode.getNodeName());
    }
    
    @Test
    public void testNodeWithName_NotFound() {
        Node foundNode = operationsHelper.nodeWithName(nodes, "nonexistent");
        assertNull(foundNode);
    }
    
    @Test
    public void testSublistWithName() {
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child1");
        assertEquals(2, sublist.size());
        assertTrue(sublist.contains(nodes.get(0)));
        assertTrue(sublist.contains(nodes.get(1)));
    }
    
    @Test
    public void testSublistWithName_NotFound() {
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "nonexistent");
        assertTrue(sublist.isEmpty());
    }
    
    // Helper method to create a sample document for testing
    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElem = document.createElement("root");
            Element child1Elem = document.createElement("child1");
            Element child2Elem = document.createElement("child2");
            Element grandchildElem = document.createElement("grandchild");
            
            child1Elem.appendChild(grandchildElem);
            rootElem.appendChild(child1Elem);
            rootElem.appendChild(child2Elem);
            document.appendChild(rootElem);
            
            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Helper method to create a sample list of nodes for testing
    private List<Node> createNodes() {
        List<Node> nodes = new ArrayList<Node>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElem = document.createElement("root");
            Element child1Elem1 = document.createElement("child1");
            Element child1Elem2 = document.createElement("child1");
            Element child2Elem = document.createElement("child2");
            Element grandchildElem = document.createElement("grandchild");
            
            child1Elem1.appendChild(grandchildElem);
            child1Elem2.appendChild(grandchildElem);
            rootElem.appendChild(child1Elem1);
            rootElem.appendChild(child1Elem2);
            rootElem.appendChild(child2Elem);
            document.appendChild(rootElem);
            
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                nodes.add(nodeList.item(i));
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        
        return nodes;
    }
}

