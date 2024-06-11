package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

  private Node testNode;

  @Before
  public void setUp() {
    testNode = new TestNode("http://www.example.com", new Attributes());
  }

  @Test
  public void testAttr() {
    testNode.attr("class", "test");
    assertEquals("test", testNode.attr("class"));
  }

  @Test
  public void testAttributes() {
    assertNotNull(testNode.attributes());
  }

  @Test
  public void testHasAttr() {
    testNode.attr("name", "John");
    assertTrue(testNode.hasAttr("name"));
  }

  @Test
  public void testRemoveAttr() {
    testNode.attr("id", "123");
    testNode.removeAttr("id");
    assertFalse(testNode.hasAttr("id"));
  }

  @Test
  public void testBaseUri() {
    assertEquals("http://www.example.com", testNode.baseUri());
  }

  @Test
  public void testSetBaseUri() {
    testNode.setBaseUri("http://www.newexample.com");
    assertEquals("http://www.newexample.com", testNode.baseUri());
  }

  @Test
  public void testChildNode() {
    Node childNode = new TestNode("http://www.example.com", new Attributes());
    testNode.addChildren(childNode);
    assertEquals(childNode, testNode.childNode(0));
  }

  @Test
  public void testChildNodes() {
    Node childNode = new TestNode("http://www.example.com", new Attributes());
    testNode.addChildren(childNode);
    assertEquals(1, testNode.childNodes().size());
  }

  @Test
  public void testParent() {
    Node parentNode = new TestNode("http://www.example.com", new Attributes());
    testNode.setParentNode(parentNode);
    assertEquals(parentNode, testNode.parent());
  }

  @Test
  public void testOwnerDocument() {
    Document document = new Document("http://www.example.com");
    testNode.setParentNode(document);
    assertEquals(document, testNode.ownerDocument());
  }

  @Test
  public void testRemove() {
    Node parentNode = new TestNode("http://www.example.com", new Attributes());
    testNode.setParentNode(parentNode);
    testNode.remove();
    assertNull(testNode.parent());
  }

  @Test
  public void testReplaceWith() {
    Node inNode = new TestNode("http://www.example.com", new Attributes());
    Node parentNode = new TestNode("http://www.example.com", new Attributes());
    testNode.setParentNode(parentNode);
    testNode.replaceWith(inNode);
    assertEquals(inNode, parentNode.childNode(0));
  }

  @Test
  public void testSiblingNodes() {
    Node siblingNode = new TestNode("http://www.example.com", new Attributes());
    Node parentNode = new TestNode("http://www.example.com", new Attributes());
    parentNode.addChildren(testNode, siblingNode);
    assertEquals(2, testNode.siblingNodes().size());
  }

  @Test
  public void testNextSibling() {
    Node siblingNode = new TestNode("http://www.example.com", new Attributes());
    Node parentNode = new TestNode("http://www.example.com", new Attributes());
    parentNode.addChildren(testNode, siblingNode);
    assertEquals(siblingNode, testNode.nextSibling());
  }

  @Test
  public void testPreviousSibling() {
    Node siblingNode = new TestNode("http://www.example.com", new Attributes());
    Node parentNode = new TestNode("http://www.example.com", new Attributes());
    parentNode.addChildren(siblingNode, testNode);
    assertEquals(siblingNode, testNode.previousSibling());
  }

  @Test
  public void testSiblingIndex() {
    Node siblingNode = new TestNode("http://www.example.com", new Attributes());
    Node parentNode = new TestNode("http://www.example.com", new Attributes());
    parentNode.addChildren(siblingNode, testNode);
    assertEquals(1, testNode.siblingIndex().intValue());
  }

  @Test
  public void testOuterHtml() {
    String expectedHtml = "<div id=\"test\"></div>";
    testNode.attr("id", "test");
    assertEquals(expectedHtml, testNode.outerHtml());
  }

  private class TestNode extends Node {

    protected TestNode(String baseUri, Attributes attributes) {
      super(baseUri, attributes);
    }

    @Override
    public String nodeName() {
      return "TestNode";
    }

    @Override
    void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
      accum.append("<div").append(attributes()).append(">");
    }

    @Override
    void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
      accum.append("</div>");
    }
  }
}