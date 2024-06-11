
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.*;

public class OperationsHelperImplTest {

  private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
  
  @Test
  public void testChildren() {
    // Test case 1: Empty node
    Node emptyNode = document.createElement("empty");
    List<Node> children = operationsHelper.children(emptyNode);
    assertTrue(children.isEmpty());
    
    // Test case 2: Node with children
    Node parent = document.createElement("parent");
    Node child1 = document.createElement("child1");
    Node child2 = document.createElement("child2");
    parent.appendChild(child1);
    parent.appendChild(child2);
    List<Node> children = operationsHelper.children(parent);
    assertEquals(2, children.size());
    assertTrue(children.contains(child1));
    assertTrue(children.contains(child2));
  }
  
  @Test
  public void testFind() {
    // Test case 1: Path with existing node
    Document document = createDummyDocument();
    Node node = operationsHelper.find(document, "root/child1");
    assertEquals("child1", node.getNodeName());
    
    // Test case 2: Path with non-existing node
    Node node = operationsHelper.find(document, "root/child3");
    assertNull(node);
  }
  
  @Test
  public void testNodeWithName() {
    // Test case 1: List with matching node
    List<Node> nodes = createDummyNodeList();
    Node node = operationsHelper.nodeWithName(nodes, "child1");
    assertEquals("child1", node.getNodeName());
    
    // Test case 2: List with no matching node
    Node node = operationsHelper.nodeWithName(nodes, "child3");
    assertNull(node);
  }
  
  @Test
  public void testSublistWithName() {
    // Test case 1: List with matching nodes
    List<Node> nodes = createDummyNodeList();
    List<Node> sublist = operationsHelper.sublistWithName(nodes, "child1");
    assertEquals(2, sublist.size());
    assertTrue(sublist.contains(nodes.get(0)));
    assertTrue(sublist.contains(nodes.get(2)));
    
    // Test case 2: List with no matching nodes
    List<Node> sublist = operationsHelper.sublistWithName(nodes, "child3");
    assertTrue(sublist.isEmpty());
  }
  
  // Helper methods
  private Document createDummyDocument() {
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element root = document.createElement("root");
    document.appendChild(root);
    Element child1 = document.createElement("child1");
    root.appendChild(child1);
    Element child2 = document.createElement("child2");
    root.appendChild(child2);
    Element grandchild1 = document.createElement("child1");
    child1.appendChild(grandchild1);
    Element grandchild2 = document.createElement("child2");
    child1.appendChild(grandchild2);
    return document;
  }
  
  private List<Node> createDummyNodeList() {
    List<Node> nodes = new ArrayList<Node>();
    Document document = createDummyDocument();
    NodeList nodeList = document.getElementsByTagName("*");
    for (int i = 0; i < nodeList.getLength(); i++) {
      nodes.add(nodeList.item(i));
    }
    return nodes;
  }
}
