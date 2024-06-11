import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        root.appendChild(child1);

        Element child2 = document.createElement("child2");
        root.appendChild(child2);

        List<Node> result = operationsHelper.children(root);

        assertEquals(2, result.size());
        assertTrue(result.contains(child1));
        assertTrue(result.contains(child2));
    }

    @Test
    public void testFind() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element element1 = document.createElement("element1");
        root.appendChild(element1);

        Element element2 = document.createElement("element2");
        root.appendChild(element2);

        Node result = operationsHelper.find(document, "/root/element1");

        assertEquals(element1, result);
    }

    @Test
    public void testNodeWithName() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        root.appendChild(child1);

        Element child2 = document.createElement("child2");
        root.appendChild(child2);

        List<Node> nodes = operationsHelper.children(root);

        Node result = operationsHelper.nodeWithName(nodes, "child1");

        assertEquals(child1, result);
    }

    @Test
    public void testSublistWithName() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child");
        root.appendChild(child1);

        Element child2 = document.createElement("child");
        root.appendChild(child2);

        List<Node> nodes = operationsHelper.children(root);

        List<Node> result = operationsHelper.sublistWithName(nodes, "child");

        assertEquals(2, result.size());
        assertTrue(result.contains(child1));
        assertTrue(result.contains(child2));
    }

}