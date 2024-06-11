import org.junit.Test;
import org.w3c.dom.*;

import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

    @Test
    public void testChildren() {
        Document document = createDummyDocument();

        // Test case for empty node
        Node emptyNode = document.createElement("empty");
        NodeList expectedResult = new NodeList();
        List<Node> result = operationsHelper.children(emptyNode);
        assertEquals(expectedResult.getLength(), result.size());

        // Test case for node with children
        Node parentNode = document.createElement("parent");
        Node childNode1 = document.createElement("child1");
        Node childNode2 = document.createElement("child2");
        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);
        expectedResult = new NodeList();
        expectedResult.add(childNode1);
        expectedResult.add(childNode2);
        result = operationsHelper.children(parentNode);
        assertEquals(expectedResult.getLength(), result.size());
        assertEquals(expectedResult.item(0), result.get(0));
        assertEquals(expectedResult.item(1), result.get(1));

        // Test case for node with text node child
        Node textNode1 = document.createTextNode("text1");
        Node textNode2 = document.createTextNode("text2");
        parentNode.appendChild(textNode1);
        parentNode.appendChild(textNode2);
        result = operationsHelper.children(parentNode);
        assertEquals(expectedResult.getLength(), result.size());
        assertEquals(expectedResult.item(0), result.get(0));
        assertEquals(expectedResult.item(1), result.get(1));

        // Test case for null node
        NodeList emptyExpectedResult = new NodeList();
        List<Node> emptyResult = operationsHelper.children(null);
        assertEquals(emptyExpectedResult.getLength(), emptyResult.size());
    }

    @Test
    public void testFind() {
        Document document = createDummyDocument();

        // Test case for empty path
        String emptyPath = "";
        Node result = operationsHelper.find(document, emptyPath);
        assertNull(result);

        // Test case for document with multiple levels
        String testPath = "root/level1/level2";
        Node expectedResult = document.getElementsByTagName("level2").item(0);
        result = operationsHelper.find(document, testPath);
        assertEquals(expectedResult, result);

        // Test case for non-existent path
        String nonExistentPath = "root/level1/level3";
        result = operationsHelper.find(document, nonExistentPath);
        assertNull(result);

        // Test case for null document
        String randomPath = "random/path";
        result = operationsHelper.find(null, randomPath);
        assertNull(result);
    }

    @Test
    public void testNodeWithName() {
        Document document = createDummyDocument();

        // Test case for empty list
        List<Node> emptyNodes = new ArrayList<>();
        String name1 = "name1";
        Node result = operationsHelper.nodeWithName(emptyNodes, name1);
        assertNull(result);

        // Test case for list with one node
        List<Node> nodeList = new ArrayList<>();
        Element element = document.createElement("element");
        nodeList.add(element);
        String elementName = "element";
        result = operationsHelper.nodeWithName(nodeList, elementName);
        assertNotNull(result);
        assertEquals(element, result);

        // Test case for list with multiple nodes
        Element element2 = document.createElement("element2");
        nodeList.add(element2);
        String element2Name = "element2";
        result = operationsHelper.nodeWithName(nodeList, element2Name);
        assertNotNull(result);
        assertEquals(element2, result);

        // Test case for non-existent node name
        String nonExistentName = "nonExistentName";
        result = operationsHelper.nodeWithName(nodeList, nonExistentName);
        assertNull(result);

        // Test case for null node list
        String randomName = "randomName";
        result = operationsHelper.nodeWithName(null, randomName);
        assertNull(result);
    }

    @Test
    public void testSublistWithName() {
        Document document = createDummyDocument();

        // Test case for empty list
        List<Node> emptyNodes = new ArrayList<>();
        String name1 = "name1";
        List<Node> result = operationsHelper.sublistWithName(emptyNodes, name1);
        assertNotNull(result);
        assertEquals(0, result.size());

        // Test case for list with one node
        List<Node> nodeList = new ArrayList<>();
        Element element = document.createElement("element");
        nodeList.add(element);
        String elementName = "element";
        result = operationsHelper.sublistWithName(nodeList, elementName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(element, result.get(0));

        // Test case for list with multiple nodes
        Element element2 = document.createElement("element2");
        nodeList.add(element2);
        String element2Name = "element2";
        result = operationsHelper.sublistWithName(nodeList, element2Name);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(element2, result.get(0));

        // Test case for multiple nodes with same name
        Element element3 = document.createElement("element");
        nodeList.add(element3);
        result = operationsHelper.sublistWithName(nodeList, elementName);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(element, result.get(0));
        assertEquals(element3, result.get(1));

        // Test case for null node list
        String randomName = "randomName";
        result = operationsHelper.sublistWithName(null, randomName);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    private Document createDummyDocument() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        return documentBuilder.newDocument();
    }
}