package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static org.jsoup.nodes.Node.*;

public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(attributes, node.attributes());
        Assert.assertEquals(Collections.emptyList(), node.childNodes());
        Assert.assertNull(node.parentNode());
    }

    @Test
    public void testNodeSimpleConstructor() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertNull(node.attributes());
        Assert.assertEquals(Collections.emptyList(), node.childNodes());
        Assert.assertNull(node.parentNode());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();

        Assert.assertEquals(Collections.EMPTY_LIST, node.childNodes());
        Assert.assertNull(node.attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullBaseUri() {
        String baseUri = null;
        Attributes attributes = new Attributes();
        new Node(baseUri, attributes);
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = null;
        new Node(baseUri, attributes);
    }

    @Test
    public void testNodeNodeName() {
        Node node = new Node();
        Assert.assertNull(node.nodeName());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("element1", node.attr("id"));
        Assert.assertEquals("", node.attr("nonExistentAttr"));

        String absUrl = node.absUrl("href");
        Assert.assertEquals("", absUrl);

        attributes.put("href", "/path/to/file.html");
        absUrl = node.absUrl("href");
        Assert.assertEquals("/path/to/file.html", absUrl);
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Node(baseUri, attributes);

        List<String> keys = new ArrayList<>();
        keys.add("id");
        keys.add("nonExistentAttr");

        for (String key : keys) {
            String value = node.attr(key);
            if (key.equals("id")) {
                Assert.assertEquals("element1", value);
            } else {
                Assert.assertEquals("", value);
            }

            Set<String> keysInAttributes = node.attributes().keySet();
            Assert.assertTrue(keysInAttributes.contains(key));

            String valueFromAttributes = node.attributes().get(key);
            if (key.equals("id")) {
                Assert.assertEquals("element1", valueFromAttributes);
            } else {
                Assert.assertEquals("", valueFromAttributes);
            }
        }

        Assert.assertEquals(2, node.attributes().size());
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Node(baseUri, attributes);

        Assert.assertTrue(node.hasAttr("id"));
        Assert.assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("element1", node.attr("id"));
        Assert.assertTrue(node.hasAttr("id"));
        node.removeAttr("id");
        Assert.assertEquals("", node.attr("id"));
        Assert.assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        Assert.assertEquals(baseUri, node.baseUri());

        String newBaseUri = "http://newexample.com";
        node.setBaseUri(newBaseUri);
        Assert.assertEquals(newBaseUri, node.baseUri());

        String attributeUrl = "relative.html";
        node.attributes().put("href", attributeUrl);
        String absUrl = node.absUrl("href");
        Assert.assertEquals(newBaseUri + "/" + attributeUrl, absUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedBaseUri() {
        String baseUri = "http://example.com:port";
        new Node(baseUri);
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);
        parentNode.addChildren(child1, child2);

        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals(child1, childNodes.get(0));
        Assert.assertEquals(child2, childNodes.get(1));

        Node child3 = new Node(baseUri);
        parentNode.addChild(child3);
        childNodes = parentNode.childNodes();
        Assert.assertEquals(3, childNodes.size());
        Assert.assertEquals(child1, childNodes.get(0));
        Assert.assertEquals(child2, childNodes.get(1));
        Assert.assertEquals(child3, childNodes.get(2));

        Node child4 = new Node(baseUri);
        parentNode.replaceChild(child1, child4);
        childNodes = parentNode.childNodes();
        Assert.assertEquals(3, childNodes.size());
        Assert.assertEquals(child4, childNodes.get(0));
        Assert.assertEquals(child2, childNodes.get(1));
        Assert.assertEquals(child3, childNodes.get(2));

        parentNode.removeChild(child2);
        childNodes = parentNode.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals(child4, childNodes.get(0));
        Assert.assertEquals(child3, childNodes.get(1));

        parentNode.remove();
        Assert.assertNull(parentNode.parent());
        Assert.assertNull(parentNode.childNodes());
    }

    @Test
    public void testParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChild(childNode);

        Assert.assertNull(childNode.parent());
        Assert.assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        Assert.assertNull(node.ownerDocument());

        Node documentNode = new Node(baseUri, attributes);
        documentNode.addChild(node);
        Assert.assertEquals(documentNode, node.ownerDocument());
    }

    @Test
    public void testRemoveAndReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);
        parentNode.addChildren(child1, child2);

        Node newChild = new Node(baseUri);
        parentNode.replaceChild(child1, newChild);
        Assert.assertEquals(newChild, parentNode.childNodes().get(0));
        Assert.assertEquals(child2, parentNode.childNodes().get(1));
        Assert.assertEquals(child1, newChild.previousSibling());

        parentNode.removeChild(child2);
        Assert.assertEquals(newChild, parentNode.childNodes().get(0));
        Assert.assertNull(parentNode.childNodes().get(1));

        parentNode.remove();
        Assert.assertNull(parentNode.parent());
        Assert.assertNull(parentNode.childNodes());
    }

    @Test
    public void testSiblingNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node sibling1 = new Node(baseUri);
        Node node = new Node(baseUri);
        Node sibling2 = new Node(baseUri);
        parentNode.addChildren(sibling1, node, sibling2);

        List<Node> siblingNodes = node.siblingNodes();
        Assert.assertEquals(2, siblingNodes.size());
        Assert.assertEquals(sibling1, siblingNodes.get(0));
        Assert.assertEquals(sibling2, siblingNodes.get(1));

        Assert.assertEquals(sibling1, node.previousSibling());
        Assert.assertEquals(sibling2, node.nextSibling());

        parentNode.removeChild(sibling1);
        siblingNodes = node.siblingNodes();
        Assert.assertEquals(1, siblingNodes.size());
        Assert.assertEquals(sibling2, siblingNodes.get(0));
    }

    @Test
    public void testNextSiblingAndPreviousSibling() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node sibling1 = new Node(baseUri);
        Node node = new Node(baseUri);
        Node sibling2 = new Node(baseUri);
        parentNode.addChildren(sibling1, node, sibling2);

        Assert.assertNull(node.previousSibling());
        Assert.assertEquals(sibling1, node.nextSibling());

        Assert.assertEquals(node, sibling1.nextSibling());
        Assert.assertEquals(sibling2, sibling1.nextSibling());

        parentNode.removeChild(sibling1);
        Assert.assertEquals(sibling2, node.nextSibling());
        Assert.assertNull(sibling2.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node sibling1 = new Node(baseUri);
        Node node = new Node(baseUri);
        Node sibling2 = new Node(baseUri);
        parentNode.addChildren(sibling1, node, sibling2);

        Assert.assertEquals(0, sibling1.siblingIndex());
        Assert.assertEquals(1, node.siblingIndex());
        Assert.assertEquals(2, sibling2.siblingIndex());

        parentNode.removeChild(sibling1);
        Assert.assertEquals(0, node.siblingIndex());
        Assert.assertEquals(0, sibling2.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Element("div", baseUri, attributes);
        node.outerHtmlHead(new StringBuilder(), 0, new Document.OutputSettings());
        node.outerHtmlTail(new StringBuilder(), 0, new Document.OutputSettings());
        String expected = "<div id=\"element1\"></div>";
        Assert.assertEquals(expected, node.outerHtml());
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "element1");
        Node node = new Node(baseUri, attributes);
        Node clonedNode = node.clone();

        Assert.assertNotSame(node, clonedNode);

        Assert.assertEquals(baseUri, clonedNode.baseUri());
        Assert.assertEquals(attributes, clonedNode.attributes());
        Assert.assertEquals(Collections.emptyList(), clonedNode.childNodes());
        Assert.assertNull(clonedNode.parentNode());

        Node child = new Node(baseUri, attributes);
        node.addChild(child);

        List<Node> childNodes = clonedNode.childNodes();
        Assert.assertEquals(0, childNodes.size());

        Node clonedChild = child.clone();
        clonedNode.addChild(clonedChild);
        Assert.assertEquals(1, clonedNode.childNodes().size());
        Assert.assertEquals(clonedChild, clonedNode.childNodes().get(0));
    }
}