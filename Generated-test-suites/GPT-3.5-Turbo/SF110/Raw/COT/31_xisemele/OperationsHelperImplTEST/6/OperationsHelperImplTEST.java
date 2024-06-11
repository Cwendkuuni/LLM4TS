import org.junit.*;
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
        // Create a test document element
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        Document document;
        try {
            db = dbf.newDocumentBuilder();
            document = db.newDocument();
            Element rootElement = document.createElement("root");
            Element child1 = document.createElement("child1");
            rootElement.appendChild(child1);
            Element child2 = document.createElement("child2");
            rootElement.appendChild(child2);
            
            // Test the children method
            List<Node> result = operationsHelper.children(rootElement);
            
            // Assert the result
            Assert.assertEquals(2, result.size());
            Assert.assertEquals(child1, result.get(0));
            Assert.assertEquals(child2, result.get(1));
        } catch (Exception e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testFind() {
        // Create a test document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        Document document;
        try {
            db = dbf.newDocumentBuilder();
            document = db.newDocument();
            Element rootElement = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            rootElement.appendChild(child1);
            rootElement.appendChild(child2);
            document.appendChild(rootElement);
            
            // Test the find method
            Node result = operationsHelper.find(document, "root/child1");
            
            // Assert the result
            Assert.assertEquals(child1, result);
        } catch (Exception e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testNodeWithName() {
        // Create a test document element
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        Document document;
        try {
            db = dbf.newDocumentBuilder();
            document = db.newDocument();
            Element rootElement = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            rootElement.appendChild(child1);
            rootElement.appendChild(child2);
            
            // Create a test list of nodes
            List<Node> nodes = new ArrayList<>();
            nodes.add(rootElement);
            nodes.add(child1);
            nodes.add(child2);
            
            // Test the nodeWithName method
            Node result = operationsHelper.nodeWithName(nodes, "child1");
            
            // Assert the result
            Assert.assertEquals(child1, result);
        } catch (Exception e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSublistWithName() {
        // Create a test document element
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        Document document;
        try {
            db = dbf.newDocumentBuilder();
            document = db.newDocument();
            Element rootElement = document.createElement("root");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            Element child3 = document.createElement("child1");
            rootElement.appendChild(child1);
            rootElement.appendChild(child2);
            rootElement.appendChild(child3);
            
            // Create a test list of nodes
            List<Node> nodes = new ArrayList<>();
            nodes.add(rootElement);
            nodes.add(child1);
            nodes.add(child2);
            nodes.add(child3);
            
            // Test the sublistWithName method
            List<Node> result = operationsHelper.sublistWithName(nodes, "child1");
            
            // Assert the result
            Assert.assertEquals(2, result.size());
            Assert.assertEquals(child1, result.get(0));
            Assert.assertEquals(child3, result.get(1));
        } catch (Exception e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }
}