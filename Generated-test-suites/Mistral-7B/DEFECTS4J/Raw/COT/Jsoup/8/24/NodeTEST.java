package org.junit.tests; // Replace with your test package name

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {
    

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(attributes, node.attributes());
        Assert.assertNull(node.parentNode);
        Assert.assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testNodeAbstractConstructor() {
        String nodeName = "div";
        Node node = new Node() {
            @Override
            public String nodeName() {
                return nodeName;
            }
        };
        Assert.assertEquals(nodeName, node.nodeName());
        Assert.assertNull(node.parentNode);
        Assert.assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullBaseUri() {
        Attributes attributes = new Attributes();
        Node node = new Node("", attributes);
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, null);
    }

    @Test
    public void testAttrMethod() {
        String baseUri = "http://example.com";
        String attributeKey = "class";
        String attributeValue = "example";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, attributeValue);
        Node node = new Node(baseUri, attributes);
        String attrValue = node.attr(attributeKey);
        Assert.assertEquals(attributeValue, attrValue);
        Assert.assertEquals("", node.attr("nonExistentAttribute"));
    }

    @Test
    public void testAbsUrlMethod() {
        String baseUri = "http://example.com";
        String relativeUrl = "css/styles.css";
        Node node = new Node(baseUri, new Attributes());
        String absUrl = node.absUrl(relativeUrl);
        Assert.assertEquals("http://example.com/css/styles.css", absUrl);
    }

    @Test
    public void testAbsUrlWithAbsoluteUrl() {
        String baseUri = "http://example.com";
        String absUrl = "http://another-example.com/image.jpg";
        Node node = new Node(baseUri, new Attributes());
        String absUrlFromNode = node.absUrl(absUrl);
        Assert.assertEquals(absUrl, absUrlFromNode);
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlWithInvalidUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());
        String invalidUrl = "invalid:url";
        node.absUrl(invalidUrl);
    }

    @Test
    public void testChildNodeMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);
        parentNode.addChild(childNode);
        Node retrievedChildNode = parentNode.childNode(0);
        Assert.assertEquals(childNode, retrievedChildNode);
    }

    @Test
    public void testChildNodesMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals(childNode1, childNodes.get(0));
        Assert.assertEquals(childNode2, childNodes.get(1));
    }

    @Test
    public void testParentMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);
        parentNode.addChild(childNode);
        Assert.assertEquals(parentNode, childNode.parent());
        Assert.assertEquals(null, childNode.parent());
    }

    @Test
    public void testOwnerDocumentMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        Document document = new Document();
        document.appendChild(node);
        Assert.assertEquals(document, node.ownerDocument());
        Assert.assertEquals(null, new Node().ownerDocument());
    }

    @Test
    public void testRemoveMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);
        parentNode.addChild(childNode);
        parentNode.remove();
        Assert.assertFalse(parentNode.parentNode == null);
        Assert.assertEquals(Collections.EMPTY_LIST, parentNode.childNodes());
    }

    @Test
    public void testReplaceWithMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node oldChildNode = new Node(baseUri, attributes);
        Node newChildNode = new Node(baseUri, attributes);
        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        Assert.assertEquals(newChildNode, parentNode.childNode(0));
        Assert.assertEquals(Collections.EMPTY_LIST, oldChildNode.childNodes());
    }

    @Test
    public void testSetParentNodeMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        childNode.setParentNode(parentNode);
        Assert.assertEquals(parentNode, childNode.parent());
        Assert.assertEquals(Collections.EMPTY_LIST, childNode.childNodes());
    }

    @Test
    public void testSetSiblingIndexMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri, attributes);
        parentNode.addChildren(node1, node2);
        node1.setSiblingIndex(1);
        Assert.assertEquals(node2, parentNode.childNode(0));
        Assert.assertEquals(node1, parentNode.childNode(1));
    }

    @Test
    public void testOuterHtmlMethod() {
        String baseUri = "http://example.com";
        String htmlContent = "<div class='example'><p>Test</p></div>";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Node(baseUri, attributes) {
            @Override
            protected void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
                accum.append("<div");
                if (attributes.size() > 0) {
                    for (String name : attributes.names()) {
                        accum.append(" ").append(name).append("='").append(attributes.get(name)).append("'");
                    }
                }
                accum.append(">");
                indent(accum, depth, out);
            }

            @Override
            protected void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
                indent(accum, depth, out);
                accum.append("</div>");
            }

            @Override
            void outerHtml(StringBuilder accum, int depth, Document.OutputSettings out) {
                outerHtmlHead(accum, depth, out);
                outerHtmlChildren(accum, depth, out);
                outerHtmlTail(accum, depth, out);
            }

            @Override
            void outerHtmlChildren(StringBuilder accum, int depth, Document.OutputSettings out) {
                Node childNode = new TextNode("Test", baseUri, new Attributes());
                childNode.outerHtml(accum, depth, out);
            }
        };
        Assert.assertEquals(htmlContent, node.outerHtml());
    }

    @Test
    public void testEqualsMethod() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node node1 = new Node(baseUri, attributes1);
        Node node2 = new Node(baseUri, attributes2);
        Node node3 = new Node(baseUri, attributes1);
        Assert.assertEquals(node1, node1);
        Assert.assertEquals(node1, node3);
        Assert.assertNotEquals(node1, node2);
    }

    @Test
    public void testHashCodeMethod() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Node node1 = new Node(baseUri, attributes1);
        Node node2 = new Node(baseUri, new Attributes());
        Assert.assertEquals(node1.hashCode(), node1.hashCode());
        Assert.assertEquals(node1.hashCode(), node1.hashCode());
        Assert.assertNotEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testToStringMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        Assert.assertTrue(node.toString().startsWith(node.outerHtml()));
    }

    @Test
    public void testSiblingNodesMethod() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node parentNode = new Node(baseUri, attributes1);
        Node siblingNode1 = new Node(baseUri, attributes2);
        Node node1 = new Node(baseUri, attributes1);
        Node node2 = new Node(baseUri, attributes1);
        parentNode.addChildren(node1, siblingNode1, node2);
        List<Node> siblings = node1.siblingNodes();
        Assert.assertEquals(2, siblings.size());
        Assert.assertEquals(node2, siblings.get(0));
        Assert.assertEquals(siblingNode1, siblings.get(1));
    }

    @Test
    public void testNextSiblingMethod() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node parentNode = new Node(baseUri, attributes1);
        Node siblingNode1 = new Node(baseUri, attributes2);
        Node node1 = new Node(baseUri, attributes1);
        Node node2 = new Node(baseUri, attributes1);
        parentNode.addChildren(node1, siblingNode1, node2);
        Node nextSibling = node1.nextSibling();
        Assert.assertEquals(siblingNode1, nextSibling);
        Assert.assertEquals(null, node2.nextSibling());
    }

    @Test
    public void testPreviousSiblingMethod() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node parentNode = new Node(baseUri, attributes1);
        Node siblingNode1 = new Node(baseUri, attributes2);
        Node node1 = new Node(baseUri, attributes1);
        Node node2 = new Node(baseUri, attributes1);
        parentNode.addChildren(node1, node2, siblingNode1);
        Node previousSibling = node1.previousSibling();
        Assert.assertEquals(null, previousSibling);
        Assert.assertEquals(node1, siblingNode1.previousSibling());
    }

    private void indent(StringBuilder accum, int depth, Document.OutputSettings out) {
        accum.append("\n").append(StringUtil.padding(depth * out.indentAmount()));
    }

    
}