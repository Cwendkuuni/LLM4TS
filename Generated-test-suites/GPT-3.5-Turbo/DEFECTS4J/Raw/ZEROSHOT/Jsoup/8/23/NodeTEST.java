import org.jsoup.nodes.*;
import org.junit.*;
import org.junit.runner.*;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class NodeTest {
  
  @Test
  public void testAttr() {
    Node node = new Node("https://www.example.com", new Attributes());
    node.attr("key", "value");
    assertEquals("value", node.attr("key"));
  }
  
  @Test
  public void testAttributes() {
    Node node = new Node("https://www.example.com", new Attributes());
    Attributes attributes = node.attributes();
    assertEquals(0, attributes.size());
  }
  
  @Test
  public void testHasAttr() {
    Node node = new Node("https://www.example.com", new Attributes());
    assertFalse(node.hasAttr("key"));
    node.attr("key", "value");
    assertTrue(node.hasAttr("key"));
  }
  
  @Test
  public void testRemoveAttr() {
    Node node = new Node("https://www.example.com", new Attributes());
    node.attr("key", "value");
    node.removeAttr("key");
    assertFalse(node.hasAttr("key"));
  }
  
  @Test
  public void testBaseUri() {
    Node node = new Node("https://www.example.com", new Attributes());
    assertEquals("https://www.example.com", node.baseUri());
  }
  
  @Test
  public void testSetBaseUri() {
    Node node = new Node("https://www.example.com", new Attributes());
    node.setBaseUri("https://www.google.com");
    assertEquals("https://www.google.com", node.baseUri());
  }
  
  @Test
  public void testChildNode() {
    Node node = new Node("https://www.example.com", new Attributes());
    Node childNode = new Node();
    node.addChildren(childNode);
    assertEquals(childNode, node.childNode(0));
  }
  
  @Test
  public void testChildNodes() {
    Node node = new Node("https://www.example.com", new Attributes());
    Node childNode = new Node();
    node.addChildren(childNode);
    assertEquals(childNode, node.childNodes().get(0));
  }
  
  @Test
  public void testNextSibling() {
    Node parentNode = new Node("https://www.example.com", new Attributes());
    Node node1 = new Node();
    Node node2 = new Node();
    parentNode.addChildren(node1, node2);
    assertEquals(node2, node1.nextSibling());
  }
  
  @Test
  public void testPreviousSibling() {
    Node parentNode = new Node("https://www.example.com", new Attributes());
    Node node1 = new Node();
    Node node2 = new Node();
    parentNode.addChildren(node1, node2);
    assertEquals(node1, node2.previousSibling());
  }
  
  @Test
  public void testSiblingIndex() {
    Node parentNode = new Node("https://www.example.com", new Attributes());
    Node node1 = new Node();
    Node node2 = new Node();
    parentNode.addChildren(node1, node2);
    assertEquals(Integer.valueOf(0), node1.siblingIndex());
    assertEquals(Integer.valueOf(1), node2.siblingIndex());
  }
  
  @Test
  public void testOuterHtml() {
    Node node = new Node("https://www.example.com", new Attributes());
    assertEquals("", node.outerHtml());
  }
  
  @Test
  public void testEquals() {
    Node node1 = new Node("https://www.example.com", new Attributes());
    Node node2 = new Node("https://www.example.com", new Attributes());
    assertFalse(node1.equals(node2));
  }
  
  @Test
  public void testHashCode() {
    Node node = new Node("https://www.example.com", new Attributes());
    assertEquals(0, node.hashCode());
  }
  
  @Test
  public void testToString() {
    Node node = new Node("https://www.example.com", new Attributes());
    assertEquals("", node.toString());
  }
}