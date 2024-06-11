import org.junit.*; 

public class OperationsHelperImplTest {

    OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

    @Test
    public void testChildren() {
        // Test case 1: Testing with a node that has no children
        Node emptyNode = createEmptyNode();
        List<Node> emptyChildren = operationsHelper.children(emptyNode);
        assertTrue(emptyChildren.isEmpty());

        // Test case 2: Testing with a node that has multiple children
        Node parentNode = createParentNode();
        List<Node> children = operationsHelper.children(parentNode);
        assertEquals(2, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());

        // Test case 3: Testing with a node that contains non-element nodes
        Node mixedNode = createMixedNode();
        List<Node> mixedChildren = operationsHelper.children(mixedNode);
        assertEquals(2, mixedChildren.size());
        assertEquals("child1", mixedChildren.get(0).getNodeName());
        assertEquals("child2", mixedChildren.get(1).getNodeName());
    }

    private Node createEmptyNode() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        return document.createElement("emptyNode");
    }

    private Node createParentNode() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        Element parent = document.createElement("parent");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");

        parent.appendChild(child1);
        parent.appendChild(child2);

        document.appendChild(parent);

        return parent;
    }

    private Node createMixedNode() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        Element mixed = document.createElement("mixed");
        Element child1 = document.createElement("child1");
        Text textNode = document.createTextNode("text node");
        Element child2 = document.createElement("child2");

        mixed.appendChild(child1);
        mixed.appendChild(textNode);
        mixed.appendChild(child2);

        document.appendChild(mixed);

        return mixed;
    }

    @Test
    public void testFind() {
        // Test case 1: Testing with an empty document and empty path
        Document emptyDocument = createEmptyDocument();
        Node foundNode = operationsHelper.find(emptyDocument, "");
        assertNull(foundNode);

        // Test case 2: Testing with a document and valid path
        Document document = createDocumentWithElements();
        Node foundNode1 = operationsHelper.find(document, "root/child1");
        assertNotNull(foundNode1);
        assertEquals("child1", foundNode1.getNodeName());

        Node foundNode2 = operationsHelper.find(document, "root/child2");
        assertNotNull(foundNode2);
        assertEquals("child2", foundNode2.getNodeName());

        // Test case 3: Testing with a document and invalid path
        Node invalidNode = operationsHelper.find(document, "root/invalid");
        assertNull(invalidNode);
    }

    private Document createEmptyDocument() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.newDocument();
    }

    private Document createDocumentWithElements() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");

        root.appendChild(child1);
        root.appendChild(child2);

        document.appendChild(root);

        return document;
    }

    @Test
    public void testNodeWithName() {
        // Test case 1: Testing with an empty list of nodes
        List<Node> emptyList = new ArrayList<>();
        Node foundNode = operationsHelper.nodeWithName(emptyList, "name");
        assertNull(foundNode);

        // Test case 2: Testing with a list of nodes containing the target node
        List<Node> nodeList = createNodeList();
        Node foundNode1 = operationsHelper.nodeWithName(nodeList, "node1");
        assertNotNull(foundNode1);
        assertEquals("node1", foundNode1.getNodeName());

        Node foundNode2 = operationsHelper.nodeWithName(nodeList, "node2");
        assertNotNull(foundNode2);
        assertEquals("node2", foundNode2.getNodeName());

        // Test case 3: Testing with a list of nodes without the target node
        Node invalidNode = operationsHelper.nodeWithName(nodeList, "invalid");
        assertNull(invalidNode);
    }

    private List<Node> createNodeList() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        Element node1 = document.createElement("node1");
        Element node2 = document.createElement("node2");

        List<Node> nodeList = new ArrayList<>();
        nodeList.add(node1);
        nodeList.add(node2);

        return nodeList;
    }

    @Test
    public void testSublistWithName() {
        // Test case 1: Testing with an empty list of nodes
        List<Node> emptyList = new ArrayList<>();
        List<Node> sublist = operationsHelper.sublistWithName(emptyList, "name");
        assertTrue(sublist.isEmpty());

        // Test case 2: Testing with a list of nodes containing the target nodes
        List<Node> nodeList = createNodeListWithDuplicates();
        List<Node> foundNodes1 = operationsHelper.sublistWithName(nodeList, "node1");
        assertEquals(2, foundNodes1.size());
        assertEquals("node1", foundNodes1.get(0).getNodeName());
        assertEquals("node1", foundNodes1.get(1).getNodeName());

        List<Node> foundNodes2 = operationsHelper.sublistWithName(nodeList, "node2");
        assertEquals(1, foundNodes2.size());
        assertEquals("node2", foundNodes2.get(0).getNodeName());

        // Test case 3: Testing with a list of nodes without the target nodes
        List<Node> invalidNodes = operationsHelper.sublistWithName(nodeList, "invalid");
        assertTrue(invalidNodes.isEmpty());
    }

    private List<Node> createNodeListWithDuplicates() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        Element node1 = document.createElement("node1");
        Element duplicateNode1 = document.createElement("node1");
        Element node2 = document.createElement("node2");

        List<Node> nodeList = new ArrayList<>();
        nodeList.add(node1);
        nodeList.add(duplicateNode1);
        nodeList.add(node2);

        return nodeList;
    }

}