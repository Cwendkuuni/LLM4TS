import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;

public class NodeTest {
  
  private Node node;
  
  @Before
  public void setUp() {
    node = new TestNode();
  }
  
  @Test
  public void testAttr() {
    assertEquals("", node.attr("attributeKey"));
    node.attr("attributeKey", "attributeValue");
    assertEquals("attributeValue", node.attr("attributeKey"));
  }
  
  @Test
  public void testAttributes() {
    assertEquals(new Attributes(), node.attributes());
  }
  
  @Test
  public void testHasAttr() {
    assertFalse(node.hasAttr("attributeKey"));
    node.attr("attributeKey", "attributeValue");
    assertTrue(node.hasAttr("attributeKey"));
  }
  
  @Test
  public void testRemoveAttr() {
    node.attr("attributeKey", "attributeValue");
    assertTrue(node.hasAttr("attributeKey"));
    node.removeAttr("attributeKey");
    assertFalse(node.hasAttr("attributeKey"));
  }
  
  @Test
  public void testBaseUri() {
    node.setBaseUri("baseUri");
    assertEquals("baseUri", node.baseUri());
  }
  
  @Test
  public void testChildNode() {
    assertNull(node.childNode(0));
    Node childNode = new TestNode();
    node.addChildren(childNode);
    assertEquals(childNode, node.childNode(0));
  }
  
  @Test
  public void testChildNode() {
    assertEquals(Collections.emptyList(), node.childNodes());
    Node childNode = new TestNode();
    node.addChildren(childNode);
    assertEquals(Collections.singletonList(childNode), node.childNodes());
  }
  
  @Test
  public void testParent() {
    assertNull(node.parent());
    Node parentNode = new TestNode();
    node.setParentNode(parentNode);
    assertEquals(parentNode, node.parent());
  }
  
  @Test
  public void testOwnerDocument() {
    assertNull(node.ownerDocument());
    Node document = new TestNode();
    node.setParentNode(document);
    assertEquals(document, node.ownerDocument());
  }
  
  @Test
  public void testRemove() {
    Node parentNode = new TestNode();
    node.setParentNode(parentNode);
    assertEquals(1, parentNode.childNodes().size());
    node.remove();
    assertEquals(0, parentNode.childNodes().size());
  }
  
  @Test
  public void testReplaceWith() {
    Node parentNode = new TestNode();
    node.setParentNode(parentNode);
    assertEquals(1, parentNode.childNodes().size());
    Node newNode = new TestNode();
    node.replaceWith(newNode);
    assertEquals(1, parentNode.childNodes().size());
    assertEquals(newNode, parentNode.childNodes().get(0));
  }
  
  @Test
  public void testSiblingNodes() {
    Node parentNode = new TestNode();
    Node siblingNode = new TestNode();
    parentNode.addChildren(node, siblingNode);
    
    assertEquals(2, node.siblingNodes().size());
    assertTrue(node.siblingNodes().contains(siblingNode));
  }
  
  @Test
  public void testNextSibling() {
    Node parentNode = new TestNode();
    Node siblingNode = new TestNode();
    parentNode.addChildren(node, siblingNode);
    
    assertEquals(siblingNode, node.nextSibling());
  }
  
  @Test
  public void testPreviousSibling() {
    Node parentNode = new TestNode();
    Node siblingNode = new TestNode();
    parentNode.addChildren(node, siblingNode);
    
    assertEquals(node, siblingNode.previousSibling());
  }
  
  @Test
  public void testSiblingIndex() {
    Node parentNode = new TestNode();
    Node siblingNode = new TestNode();
    parentNode.addChildren(siblingNode, node);
    
    assertEquals(1, node.siblingIndex());
  }
  
  @Test
  public void testEquals() {
    assertFalse(node.equals(null));
    assertFalse(node.equals(new Object()));
    assertTrue(node.equals(node));
  }
  
  @Test
  public void testHashCode() {
    Node parentNode = new TestNode();
    node.setParentNode(parentNode);
    assertEquals(31 * parentNode.hashCode(), node.hashCode());
  }
  
  private static class TestNode extends Node {
    
    protected TestNode(String baseUri, Attributes attributes) {
      super(baseUri, attributes);
    }

    protected TestNode(String baseUri) {
      super(baseUri);
    }

    protected TestNode() {
      super();
    }

    @Override
    public String nodeName() {
      return "TestNode";
    }

    @Override
    void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
    }

    @Override
    void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
    }
  }
}