import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

    @Test
    public void testChildren_typicalCase() {
        // Typical case: testing if children are correctly retrieved from a node
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);
        NodeList nodeList = root.getChildNodes();
        
        assertEquals(operationsHelper.children(root).getLength(), nodeList.getLength());
        assertEquals(operationsHelper.children(root).item(0).getNodeName(), nodeList.item(0).getNodeName());
        assertEquals(operationsHelper.children(root).item(1).getNodeName(), nodeList.item(1).getNodeName());
        assertEquals(operationsHelper.children(root).item(2).getNodeName(), nodeList.item(2).getNodeName());
    }

    @Test
    public void testFind_typicalCase() {
        // Typical case: testing if node is correctly found in the document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        root.appendChild(child1);
        child1.appendChild(child2);
        child2.appendChild(child3);

        assertEquals(operationsHelper.find(document, "/root/child1/child2"), child2);
    }

    @Test
    public void testFind_edgeCase() {
        // Edge case: testing if the root node is correctly returned when path includes root node
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);

        assertEquals(operationsHelper.find(document, "/root"), root);
    }

    @Test
    public void testFind_errorCase() {
        // Error case: testing if null is returned when node is not found
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);

        assertNull(operationsHelper.find(document, "/child"));
    }

    @Test
    public void testNodeWithName_typicalCase() {
        // Typical case: testing if the correct node is returned from a list of nodes
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);
        NodeList nodeList = root.getChildNodes();

        assertEquals(operationsHelper.nodeWithName(nodeList, "child2"), child2);
    }

    @Test
    public void testNodeWithName_errorCase() {
        // Error case: testing if null is returned when node is not found in the list of nodes
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        root.appendChild(child1);
        root.appendChild(child3);
        NodeList nodeList = root.getChildNodes();

        assertNull(operationsHelper.nodeWithName(nodeList, "child2"));
    }

    @Test
    public void testSublistWithName_typicalCase() {
        // Typical case: testing if the correct sublist is returned from a list of nodes
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2a = document.createElement("child2");
        Element child2b = document.createElement("child2");
        Element child3a = document.createElement("child3");
        Element child3b = document.createElement("child3");
        root.appendChild(child1);
        root.appendChild(child2a);
        root.appendChild(child2b);
        root.appendChild(child3a);
        root.appendChild(child3b);
        NodeList nodeList = root.getChildNodes();

        assertEquals(operationsHelper.sublistWithName(nodeList, "child2").size(), 2);
        assertEquals(operationsHelper.sublistWithName(nodeList, "child3").size(), 2);
    }

    @Test
    public void testSublistWithName_errorCase() {
        // Error case: testing if an empty sublist is returned when no node with the given name is found in the list of nodes
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child3a = document.createElement("child3");
        Element child3b = document.createElement("child3");
        root.appendChild(child1);
        root.appendChild(child3a);
        root.appendChild(child3b);
        NodeList nodeList = root.getChildNodes();

        assertEquals(operationsHelper.sublistWithName(nodeList, "child2").size(), 0);
    }
}