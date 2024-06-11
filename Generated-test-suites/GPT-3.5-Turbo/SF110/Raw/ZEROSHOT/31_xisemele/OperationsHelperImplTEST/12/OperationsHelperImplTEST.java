import org.junit.*;
import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        // Test case 1
        Node node1 = createNodeWithChildren();
        List<Node> expectedResult1 = createChildrenList(node1);
        List<Node> actualResult1 = OperationsHelperImpl.children(node1);
        assertEquals(expectedResult1, actualResult1);
        
        // Test case 2
        Node node2 = createNodeWithoutChildren();
        List<Node> expectedResult2 = createEmptyList();
        List<Node> actualResult2 = OperationsHelperImpl.children(node2);
        assertEquals(expectedResult2, actualResult2);
    }
    
    @Test
    public void testFind() {
        // Test case 1
        Document document1 = createDocument("root>element1>element2");
        String path1 = "root/element1/element2";
        Node expectedResult1 = document1.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0);
        Node actualResult1 = OperationsHelperImpl.find(document1, path1);
        assertEquals(expectedResult1, actualResult1);
        
        // Test case 2
        Document document2 = createDocument("root>element1>element2>element3");
        String path2 = "root/element1/element2/element3";
        Node expectedResult2 = document2.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0);
        Node actualResult2 = OperationsHelperImpl.find(document2, path2);
        assertEquals(expectedResult2, actualResult2);
        
        // Test case 3
        Document document3 = createDocument("root>element1>element2");
        String path3 = "root/element1/element3";
        Node expectedResult3 = null;
        Node actualResult3 = OperationsHelperImpl.find(document3, path3);
        assertEquals(expectedResult3, actualResult3);
    }
    
    @Test
    public void testNodeWithName() {
        // Test case 1
        List<Node> nodes1 = createNodesList("element1", "element2", "element3");
        String name1 = "element2";
        Node expectedResult1 = nodes1.get(1);
        Node actualResult1 = OperationsHelperImpl.nodeWithName(nodes1, name1);
        assertEquals(expectedResult1, actualResult1);
        
        // Test case 2
        List<Node> nodes2 = createEmptyList();
        String name2 = "element";
        Node expectedResult2 = null;
        Node actualResult2 = OperationsHelperImpl.nodeWithName(nodes2, name2);
        assertEquals(expectedResult2, actualResult2);
    }
    
    @Test
    public void testSublistWithName() {
        // Test case 1
        List<Node> nodes1 = createNodesList("element1", "element2", "element1");
        String name1 = "element1";
        List<Node> expectedResult1 = nodes1.subList(0, 2);
        List<Node> actualResult1 = OperationsHelperImpl.sublistWithName(nodes1, name1);
        assertEquals(expectedResult1, actualResult1);
        
        // Test case 2
        List<Node> nodes2 = createNodesList("element1", "element2", "element3");
        String name2 = "element4";
        List<Node> expectedResult2 = createEmptyList();
        List<Node> actualResult2 = OperationsHelperImpl.sublistWithName(nodes2, name2);
        assertEquals(expectedResult2, actualResult2);
    }
    
    private Node createNodeWithChildren() {
        Element node = document.createElement("parent");
        node.appendChild(document.createElement("child1"));
        node.appendChild(document.createElement("child2"));
        return node;
    }
    
    private Node createNodeWithoutChildren() {
        return document.createElement("node");
    }
    
    private List<Node> createChildrenList(Node node) {
        List<Node> children = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                children.add(item);
            }
        }
        return children;
    }
    
    private Document createDocument(String hierarchy) {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>" + hierarchy.replace(">", "><") + "</root>")));
        return doc;
    }
    
    private List<Node> createNodesList(String... names) {
        List<Node> nodes = new ArrayList<>();
        for (String name : names) {
            Node node = document.createElement(name);
            nodes.add(node);
        }
        return nodes;
    }
    
    private List<Node> createEmptyList() {
        return new ArrayList<>();
    }
    
}