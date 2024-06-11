import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        // Test case 1
        Node node1 = createNode("parent");
        Node child1 = createNode("child1");
        Node child2 = createNode("child2");
        node1.appendChild(child1);
        node1.appendChild(child2);
        
        List<Node> expected1 = new ArrayList<>();
        expected1.add(child1);
        expected1.add(child2);
        assertEquals(expected1, helper.children(node1));
        
        // Test case 2
        Node node2 = createNode("parent");
        
        List<Node> expected2 = new ArrayList<>();
        assertEquals(expected2, helper.children(node2));
    }
    
    @Test
    public void testFind() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        // Test case 1
        Document document1 = createDocument();
        
        Node found1 = helper.find(document1, "/root");
        assertEquals(document1.getDocumentElement(), found1);
        
        // Test case 2
        Document document2 = createDocument();
        
        Node found2 = helper.find(document2, "/root/child1");
        assertEquals(null, found2);
    }
    
    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        // Test case 1
        Node node1 = createNode("parent");
        Node child1 = createNode("child1");
        Node child2 = createNode("child2");
        node1.appendChild(child1);
        node1.appendChild(child2);
        
        List<Node> nodes1 = new ArrayList<>();
        nodes1.add(child1);
        nodes1.add(child2);
        
        Node found1 = helper.nodeWithName(nodes1, "child1");
        assertEquals(child1, found1);
        
        // Test case 2
        Node node2 = createNode("parent");
        Node child3 = createNode("child3");
        node2.appendChild(child3);
        
        List<Node> nodes2 = new ArrayList<>();
        nodes2.add(child3);
        
        Node found2 = helper.nodeWithName(nodes2, "child1");
        assertEquals(null, found2);
    }
    
    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        // Test case 1
        Node node1 = createNode("parent");
        Node child1 = createNode("child1");
        Node child2 = createNode("child2");
        Node child3 = createNode("child1");
        node1.appendChild(child1);
        node1.appendChild(child2);
        node1.appendChild(child3);
        
        List<Node> nodes1 = new ArrayList<>();
        nodes1.add(child1);
        nodes1.add(child2);
        nodes1.add(child3);
        
        List<Node> expected1 = new ArrayList<>();
        expected1.add(child1);
        expected1.add(child3);
        assertEquals(expected1, helper.sublistWithName(nodes1, "child1"));
        
        // Test case 2
        Node node2 = createNode("parent");
        Node child4 = createNode("child3");
        node2.appendChild(child4);
        
        List<Node> nodes2 = new ArrayList<>();
        nodes2.add(child4);
        
        List<Node> expected2 = new ArrayList<>();
        assertEquals(expected2, helper.sublistWithName(nodes2, "child1"));
    }
    
    private Node createNode(String name) {
        Document document = createDocument();
        return document.createElement(name);
    }
    
    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}