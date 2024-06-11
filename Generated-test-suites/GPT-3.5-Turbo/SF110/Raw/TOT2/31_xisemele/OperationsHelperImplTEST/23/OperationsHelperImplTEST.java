import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        // Test case for children method
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("root");
        Element child1 = doc.createElement("child1");
        Element child2 = doc.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);
        List<Node> children = helper.children(root);
        assertEquals(2, children.size());
    }

    @Test
    public void testFind() {
        // Test case for find method
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("root");
        Element child1 = doc.createElement("child1");
        Element child2 = doc.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);
        doc.appendChild(root);
        Node found = helper.find(doc, "root/child1");
        assertEquals(child1, found);
    }

    @Test
    public void testNodeWithName() {
        // Test case for nodeWithName method
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("root");
        Element child1 = doc.createElement("child1");
        Element child2 = doc.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);
        doc.appendChild(root);
        List<Node> nodes = helper.children(root);
        Node node = helper.nodeWithName(nodes, "child2");
        assertEquals(child2, node);
    }

    @Test
    public void testSublistWithName() {
        // Test case for sublistWithName method
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("root");
        Element child1 = doc.createElement("child1");
        Element child2 = doc.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);
        doc.appendChild(root);
        List<Node> nodes = helper.children(root);
        List<Node> sublist = helper.sublistWithName(nodes, "child2");
        assertEquals(1, sublist.size());
    }
}