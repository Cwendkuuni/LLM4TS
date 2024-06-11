package org.jsoup.nodes;

import org.jsoup.helper.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeConstructorWithBaseUriAndAttributes() {
 Node node = new Node("http://example.com", new Attributes().put("id", "test"));
 assertEquals("test", node.attr("id"));
 assertEquals("http://example.com", node.baseUri());
 }

 @Test
 public void testNodeConstructorWithBaseUriOnly() {
 Node node = new Node("http://example.com");
 assertEquals("http://example.com", node.baseUri());
 }

 @Test
 public void testDefaultConstructor() {
 Node node = new Node();
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.attributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 try {
 new Node(null, new Attributes());
 fail("Should throw IllegalArgumentException for null baseUri");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributes() {
 try {
 new Node("http://example.com", null);
 fail("Should throw IllegalArgumentException for null attributes");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNodeNameAbstractMethod() {
 try {
 new Node() {
 @Override
 public String nodeName() {
 throw new UnsupportedOperationException();
 }
 };
 fail("Should throw UnsupportedOperationException for abstract nodeName() method");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testAttrMethodWithValidKey() {
 Node node = new Node("http://example.com", new Attributes().put("id", "test"));
 assertEquals("test", node.attr("id"));
 }

 @Test
 public void testAttrMethodWithInvalidKey() {
 Node node = new Node("http://example.com");
 assertEquals("", node.attr("invalidKey"));
 }

 @Test
 public void testAttrMethodWithAbsUrlKey() {
 Node node = new Node("http://example.com") {
 @Override
 public String baseUri() {
 return "http://example2.com";
 }
 };
 assertEquals("http://example2.com/testPage/testLink", node.absUrl("testPage/testLink"));
 }

 @Test
 public void testAttributesMethod() {
 Node node = new Node("http://example.com", new Attributes().put("id", "test"));
 Attributes attributes = node.attributes();
 assertTrue(attributes.containsKey("id"));
 }

 @Test
 public void testAttrMethodWithEmptyAttributes() {
 Node node = new Node("http://example.com");
 assertEquals("", node.attr("invalidKey"));
 }

 @Test
 public void testSetAttrMethod() {
 Node node = new Node("http://example.com");
 node.attr("id", "test");
 assertEquals("test", node.attr("id"));
 }

 @Test
 public void testHasAttrMethod() {
 Node node = new Node("http://example.com", new Attributes().put("id", "test"));
 assertTrue(node.hasAttr("id"));
 }

 @Test
 public void testRemoveAttrMethod() {
 Node node = new Node("http://example.com", new Attributes().put("id", "test"));
 node.removeAttr("id");
 assertFalse(node.hasAttr("id"));
 }

 @Test
 public void testAbsUrlMethodWithValidRelativeUrl() {
 Node node = new Node("http://example.com") {
 @Override
 public String baseUri() {
 return "http://example2.com";
 }
 };
 assertEquals("http://example2.com/testPage/testLink", node.absUrl("testPage/testLink"));
 }

 @Test
 public void testAbsUrlMethodWithValidAbsoluteUrl() {
 Node node = new Node("http://example.com") {
 @Override
 public String baseUri() {
 return "http://example2.com";
 }
 };
 assertEquals("http://example2.com/testLink", node.absUrl("http://example.com/testLink"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributeKeyForAbsUrl() {
 try {
 new Node("http://example.com") {
 @Override
 public String baseUri() {
 return "http://example2.com";
 }
 }.absUrl(null);
 fail("Should throw IllegalArgumentException for null attributeKey in absUrl() method");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testChildNodeMethod() {
 Node parentNode = new Node("http://example.com");
 Node childNode = new Node("http://child.com");
 parentNode.addChild(childNode);
 assertEquals(childNode, parentNode.childNode(0));
 }

 @Test
 public void testChildNodesMethod() {
 Node parentNode = new Node("http://example.com");
 Node childNode1 = new Node("http://child1.com");
 Node childNode2 = new Node("http://child2.com");
 parentNode.addChildren(childNode1, childNode2);
 assertEquals(2, parentNode.childNodes().size());
 }

 @Test
 public void testParentMethod() {
 Node childNode = new Node("http://child.com");
 Node parentNode = new Node("http://example.com");
 childNode.setParentNode(parentNode);
 assertEquals(parentNode, childNode.parent());
 }

 @Test
 public void testOwnerDocumentMethod() {
 Node childNode = new Node("http://child.com");
 Node parentNode = new Node("http://example.com");
 childNode.setParentNode(parentNode);
 assertEquals(parentNode, childNode.ownerDocument());
 }

 @Test
 public void testRemoveMethod() {
 Node parentNode = new Node("http://example.com");
 Node childNode = new Node("http://child.com");
 parentNode.addChild(childNode);
 parentNode.remove(); // Removing the parent node will remove the child node as well
 assertTrue(parentNode.childNodes().isEmpty());
 }

 @Test
 public void testReplaceWithMethod() {
 Node parentNode = new Node("http://example.com");
 Node childNode1 = new Node("http://child1.com");
 Node childNode2 = new Node("http://child2.com");
 parentNode.addChildren(childNode1, childNode2);
 parentNode.replaceChild(childNode1, childNode2);
 assertEquals(childNode2, parentNode.childNode(0));
 }

 @Test
 public void testSetParentNodeMethod() {
 Node childNode = new Node("http://child.com");
 Node parentNode1 = new Node("http://parent1.com");
 Node parentNode2 = new Node("http://parent2.com");
 childNode.setParentNode(parentNode1);
 assertEquals(parentNode1, childNode.parent());
 childNode.setParentNode(parentNode2);
 assertEquals(parentNode2, childNode.parent());
 }

 @Test
 public void testReplaceChildMethod() {
 Node parentNode = new Node("http://example.com");
 Node outNode = new Node("http://outNode.com");
 Node inNode = new Node("http://inNode.com");
 parentNode.addChildren(outNode);
 parentNode.replaceChild(outNode, inNode);
 assertEquals(inNode, parentNode.childNode(0));
 }

 @Test
 public void testRemoveChildMethod() {
 Node parentNode = new Node("http://example.com");
 Node childNode = new Node("http://child.com");
 parentNode.addChild(childNode);
 parentNode.removeChild(childNode);
 assertTrue(parentNode.childNodes().isEmpty());
 }

 @Test
 public void testAddChildrenMethod() {
 Node parentNode = new Node("http://example.com");
 Node childNode1 = new Node("http://child1.com");
 Node childNode2 = new Node("http://child2.com");
 parentNode.addChildren(childNode1, childNode2);
 assertEquals(2, parentNode.childNodes().size());
 }

 @Test
 public void testAddChildrenMethodWithIndex() {
 Node parentNode = new Node("http://example.com");
 Node childNode1 = new Node("http://child1.com");
 Node childNode2 = new Node("http://child2.com");
 parentNode.addChildren(1, childNode1, childNode2);
 assertEquals(childNode1, parentNode.childNode(1));
 }

 @Test
 public void testSiblingIndexMethod() {
 Node parentNode = new Node("http://example.com");
 Node childNode1 = new Node("http://child1.com");
 Node childNode2 = new Node("http://child2.com");
 parentNode.addChildren(childNode1, childNode2);
 assertEquals(0, childNode1.siblingIndex());
 assertEquals(1, childNode2.siblingIndex());
 }

 @Test
 public void testSetSiblingIndexMethod() {
 Node parentNode = new Node("http://example.com");
 Node childNode1 = new Node("http://child1.com");
 Node childNode2 = new Node("http://child2.com");
 parentNode.addChildren(childNode1, childNode2);
 childNode2.setSiblingIndex(3);
 assertEquals(3, childNode2.siblingIndex());
 }

 @Test
 public void testOuterHtmlMethod() {
 Node node = new Node("http://example.com") {
 @Override
 protected void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
 accum.append("<html><head><title>Test</title></head>");
 }

 @Override
 protected void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
 accum.append("</html>");
 }
 };
 assertEquals("<html><head><title>Test</title></head><body></body></html>", node.outerHtml());
 }

 @Test
 public void testToStringMethod() {
 Node node = new Node("http://example.com");
 assertEquals("<html><body></body></html>", node.toString());
 }

 @Test
 public void testCloneMethod() {
 Node node = new Node("http://example.com");
 Node clonedNode = node.clone();
 assertEquals(node.childNodes().size(), clonedNode.childNodes().size());
 }
}