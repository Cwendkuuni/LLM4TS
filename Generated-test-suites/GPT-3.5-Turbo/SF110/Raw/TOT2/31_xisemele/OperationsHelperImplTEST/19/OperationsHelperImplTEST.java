import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {
    
    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            root.appendChild(child1);
            root.appendChild(child2);
            Node node = document.appendChild(root);
            List<Node> result = helper.children(node);
            assertEquals(2, result.size());
            assertTrue(result.contains(child1));
            assertTrue(result.contains(child2));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testFind() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            root.appendChild(child1);
            child1.appendChild(child2);
            document.appendChild(root);
            Node result = helper.find(document, "/root/child1/child2");
            assertEquals(child2, result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            root.appendChild(child1);
            root.appendChild(child2);
            document.appendChild(root);
            List<Node> nodes = helper.children(document);
            Node result = helper.nodeWithName(nodes, "child1");
            assertEquals(child1, result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            root.appendChild(child1);
            root.appendChild(child2);
            document.appendChild(root);
            List<Node> nodes = helper.children(document);
            List<Node> result = helper.sublistWithName(nodes, "child1");
            assertEquals(1, result.size());
            assertTrue(result.contains(child1));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}