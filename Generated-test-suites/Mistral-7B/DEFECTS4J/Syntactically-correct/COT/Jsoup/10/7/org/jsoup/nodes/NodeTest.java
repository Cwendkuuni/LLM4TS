package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.empty;

/**
 * Test suite for Node class
 */
public class NodeTest {

    private Node node;
    private Attributes attributes;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        attributes = new Attributes();
        node = new Element("div", baseUri, attributes);
    }

    

    // Test methods that don't need instance

    @Test
    public void testAbstractClass() {
        Assert.assertTrue(Node.class.isAbstract());
    }

    @Test
    public void testCloneable() {
        Assert.assertTrue(Node.class.isAssignableFrom(Cloneable.class));
    }

    @Test
    public void testToString() {
        Assert.assertNotNull(node.toString());
    }

    @Test
    public void testEquals() {
        Node other = new Element("div", node.baseUri(), node.attributes());
        Assert.assertThat(node, is(equalTo(other)));

        Element other2 = new Element("div", "other_uri", node.attributes());
        Assert.assertThat(node, is(not(equalTo(other2))));

        assertFalse(node.equals("not a Node"));
    }

    @Test
    public void testHashCode() {
        Node other = new Element("div", node.baseUri(), node.attributes());
        Assert.assertThat(node.hashCode(), is(equalTo(other.hashCode())));

        Element other2 = new Element("div", "other_uri", node.attributes());
        Assert.assertThat(node.hashCode(), is(not(equalTo(other2.hashCode()))));
    }

    // Test methods that require instance

    @Test
    public void testNodeName() {
        Assert.assertNotNull(node.nodeName());
        Assert.assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        node.attr("test", "testValue");
        Assert.assertEquals("testValue", node.attr("test"));
        Assert.assertEquals(empty(), node.childNodes());
    }

    @Test(expected = NullPointerException.class)
    public void testAttrNullAttributeKey() {
        node.attr(null, "testValue");
    }

    @Test(expected = NullPointerException.class)
    public void testAttrNullAttributeValue() {
        node.attr("test", null);
    }

    @Test
    public void testAttributes() {
        Assert.assertNotNull(node.attributes());
        Assert.assertThat(node.attributes(), is(instanceOf(Attributes.class)));
    }

    @Test
    public void testHasAttr() {
        node.attr("test", "testValue");
        Assert.assertTrue(node.hasAttr("test"));
        Assert.assertFalse(node.hasAttr("nonExistingAttr"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("test", "testValue");
        node.removeAttr("test");
        Assert.assertFalse(node.hasAttr("test"));
    }

    @Test
    public void testBaseUri() {
        Assert.assertEquals(node.baseUri(), "http://example.com");
        node.setBaseUri("http://other_example.com");
        Assert.assertEquals(node.baseUri(), "http://other_example.com");
    }

    @Test
    public void testAbsUrl() {
        String relUrl = "http://example.com/relativeUrl";
        String expectedAbsUrl = "http://example.com/relativeUrl";
        node.attr("href", relUrl);
        Assert.assertEquals(expectedAbsUrl, node.absUrl("href"));

        node.setBaseUri("http://other_example.com");
        String expectedAbsUrl2 = "http://other_example.com/relativeUrl";
        Assert.assertEquals(expectedAbsUrl2, node.absUrl("href"));

        Assert.assertEquals("", node.absUrl("nonExistingAttr"));
    }

    @Test(expected = NullPointerException.class)
    public void testAbsUrlNullAttributeKey() {
        node.absUrl(null);
    }

    @Test
    public void testChildNode() {
        Node child = new TextNode("childText", node);
        node.addChild(child);
        Assert.assertEquals(child, node.childNode(0));
        Assert.assertEquals(1, node.childNodes().size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildNodeNegativeIndex() {
        node.addChild(new TextNode("childText", node));
        node.addChild(new TextNode("childText2", node));
        node.childNode(-1);
    }

    @Test
    public void testChildNodes() {
        Node child1 = new TextNode("childText1", node);
        Node child2 = new TextNode("childText2", node);
        node.addChildren(child1, child2);
        Assert.assertEquals(2, node.childNodes().size());
        Assert.assertEquals(child1, node.childNodes().get(0));
        Assert.assertEquals(child2, node.childNodes().get(1));
    }

    @Test
    public void testChildNodesEmptyList() {
        Assert.assertEquals(0, node.childNodes().size());
    }

    @Test
    public void testParent() {
        Node parent = new Element("parent", node.baseUri(), new Attributes());
        node.setParentNode(parent);
        Assert.assertEquals(parent, node.parent());
        Assert.assertEquals(node, parent.childNodes().get(0));
    }

    @Test
    public void testParentNull() {
        node.setParentNode(null);
        Assert.assertEquals(null, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document doc = new Document();
        node.setOwnerDocument(doc);
        Assert.assertEquals(doc, node.ownerDocument());
    }

    @Test
    public void testOwnerDocumentNull() {
        node.setOwnerDocument(null);
        Assert.assertEquals(null, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        node.addChild(new TextNode("childText", node));
        Assert.assertEquals(1, node.childNodes().size());
        node.remove();
        Assert.assertEquals(0, node.childNodes().size());
    }

    @Test
    public void testReplaceWith() {
        Node child = new TextNode("childText", node);
        node.addChild(child);
        Node newChild = new TextNode("newChildText", node);
        node.replaceWith(newChild);
        Assert.assertEquals(newChild, node.childNodes().get(0));
        Assert.assertEquals(0, child.siblingIndex());
        Assert.assertEquals(1, newChild.siblingIndex());
    }

    @Test(expected = NullPointerException.class)
    public void testReplaceWithNullChild() {
        node.addChild(new TextNode("childText", node));
        node.replaceWith(null);
    }

    @Test
    public void testSetParentNode() {
        Node newParent = new Element("newParent", node.baseUri(), new Attributes());
        node.setParentNode(newParent);
        Assert.assertEquals(newParent, node.parent());
        Assert.assertEquals(node, newParent.childNodes().get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testSetParentNodeNull() {
        node.setParentNode(null);
    }

    @Test
    public void testReplaceChild() {
        Node child1 = new TextNode("childText1", node);
        Node child2 = new TextNode("childText2", node);
        node.addChildren(child1, child2);
        node.replaceChild(child1, new TextNode("newChildText", node));
        Assert.assertEquals(1, node.childNodes().size());
        Assert.assertEquals("newChildText", node.childNodes().get(0).toString());
        Assert.assertEquals(0, child2.siblingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceChildNullChild() {
        Node child1 = new TextNode("childText1", node);
        Node child2 = new TextNode("childText2", node);
        node.addChildren(child1, child2);
        node.replaceChild(null, new TextNode("newChildText", node));
    }

    @Test
    public void testRemoveChild() {
        Node child = new TextNode("childText", node);
        node.addChild(child);
        node.removeChild(child);
        Assert.assertEquals(0, node.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveChildNullChild() {
        Node child = new TextNode("childText", node);
        node.addChild(child);
        node.removeChild(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenNullChildren() {
        node.addChildren((Node[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenEmptyArray() {
        node.addChildren(new Node[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenNullChild() {
        node.addChildren(new Node[]{null});
    }

    @Test
    public void testAddChildren() {
        Node child1 = new TextNode("childText1", node);
        Node child2 = new TextNode("childText2", node);
        node.addChildren(child1, child2);
        Assert.assertEquals(2, node.childNodes().size());
        Assert.assertEquals(child1, node.childNodes().get(0));
        Assert.assertEquals(child2, node.childNodes().get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node child1 = new TextNode("childText1", node);
        Node child2 = new TextNode("childText2", node);
        node.addChildren(1, child1, child2);
        Assert.assertEquals(2, node.childNodes().size());
        Assert.assertEquals(child1, node.childNodes().get(1));
        Assert.assertEquals(child2, node.childNodes().get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddChildrenNegativeIndex() {
        Node child = new TextNode("childText", node);
        node.addChild(child);
        node.addChildren(-1, new TextNode("newChildText", node));
    }

    @Test
    public void testAddChildrenWithIndexOutOfBounds() {
        Node child = new TextNode("childText", node);
        node.addChild(child);
        node.addChildren(3, new TextNode("newChildText", node));
    }

    @Test
    public void testAddChildrenWithIndexValid() {
        Node child1 = new TextNode("childText1", node);
        Node child2 = new TextNode("childText2", node);
        node.addChildren(1, new TextNode("newChildText1", node));
        node.addChildren(2, child1, child2);
        Assert.assertEquals(4, node.childNodes().size());
        Assert.assertEquals(newTextNode("newChildText1"), node.childNodes().get(1));
        Assert.assertEquals(child1, node.childNodes().get(2));
        Assert.assertEquals(child2, node.childNodes().get(3));
    }

    @Test
    public void testGetSiblingIndex() {
        Node sibling = new TextNode("sibling", node);
        node.addChild(sibling);
        Assert.assertEquals(0, sibling.siblingIndex());
        node.addChild(new TextNode("newSibling", node));
        Assert.assertEquals(1, sibling.siblingIndex());
    }

    @Test
    public void testSetSiblingIndex() {
        Node sibling = new TextNode("sibling", node);
        node.addChild(sibling);
        sibling.setSiblingIndex(1);
        Assert.assertEquals(1, sibling.siblingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSiblingIndexNegativeIndex() {
        Node sibling = new TextNode("sibling", node);
        sibling.setSiblingIndex(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSiblingIndexNonExistingIndex() {
        Node sibling = new TextNode("sibling", node);
        sibling.setSiblingIndex(10);
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlMalformedUrl() {
        node.attr("abs:href", "jsoup://example.com");
        Assert.assertEquals("", node.absUrl("abs:href"));
    }

    @Test(expected = NullPointerException.class)
    public void testAbsUrlNullAttributeKey() {
        node.absUrl(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributesNullAttributes() {
        node.setAttributes(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributesNullBaseUri() {
        node.setAttributes(new Attributes());
        node.setBaseUri(null);
    }

    @Test
    public void testClone() {
        Node clonedNode = node.clone();
        Assert.assertEquals(node, clonedNode.parent());
        Assert.assertEquals(node.attributes(), clonedNode.attributes());
        Assert.assertEquals(node.childNodesAsArray(), clonedNode.childNodesAsArray());

        // test cloning an orphan node
        node.setParentNode(null);
        Node clonedOrphanNode = node.clone();
        Assert.assertNull(clonedOrphanNode.parent());
    }

    
}