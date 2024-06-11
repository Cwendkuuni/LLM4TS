package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.nodes.Attributes.fromMap;
import static org.jsoup.nodes.Attributes.toMap;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new AbstractElement() {
        };
        Assert.assertFalse(node.hasAttributes());

        LeafNode nodeWithAttributes = new AbstractElement() {
            {
                this.value = fromMap(Map.of("class", "test"));
            }
        };
        Assert.assertTrue(nodeWithAttributes.hasAttributes());
    }

    @Test
    public void testGetAttributes() {
        LeafNode node = new AbstractElement() {
            {
                this.value = "Hello World";
            }
        };
        Assert.assertNull(node.attributes());

        LeafNode nodeWithAttributes = new AbstractElement() {
            {
                this.value = fromMap(Map.of("class", "test", "id", "element-id"));
            }
        };
        Attributes attributes = nodeWithAttributes.attributes();
        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("test", attributes.get("class"));
        Assert.assertEquals("element-id", attributes.get("id"));
    }

    @Test
    public void testSetAttributes() {
        LeafNode node = new AbstractElement() {
            {
                this.value = "Hello World";
            }
        };
        node.attr("class", "test");
        node.attr("id", "element-id");
        Attributes attributes = node.attributes();
        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("test", attributes.get("class"));
        Assert.assertEquals("element-id", attributes.get("id"));

        node.coreValue("New Value");
        Assert.assertEquals("New Value", node.attr("test"));
    }

    @Test
    public void testHasAttr() {
        LeafNode node = new AbstractElement() {
            {
                this.value = fromMap(Map.of("class", "test", "id", "element-id"));
            }
        };
        Assert.assertTrue(node.hasAttr("class"));
        Assert.assertTrue(node.hasAttr("id"));
        Assert.assertFalse(node.hasAttr("unknown"));

        LeafNode nodeWithoutAttributes = new AbstractElement() {
            {
                this.value = "Hello World";
            }
        };
        Assert.assertFalse(nodeWithoutAttributes.hasAttr("class"));
        Assert.assertFalse(nodeWithoutAttributes.hasAttr("id"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode nodeWithAttributes = new AbstractElement() {
            {
                this.value = fromMap(Map.of("class", "test", "id", "element-id"));
            }
        };
        nodeWithAttributes.removeAttr("class");
        Attributes attributes = nodeWithAttributes.attributes();
        Assert.assertEquals(1, attributes.size());
        Assert.assertNull(attributes.get("class"));
        Assert.assertEquals("element-id", attributes.get("id"));

        nodeWithAttributes.removeAttr("id");
        attributes = nodeWithAttributes.attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertNull(nodeWithAttributes.attr("class"));
        Assert.assertNull(nodeWithAttributes.attr("id"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode node = new AbstractElement() {
            {
                this.value = "http://example.com";
            }
        };
        Assert.assertEquals("http://example.com", node.absUrl(""));

        LeafNode nodeWithBase = new Elements.SingleElement<Element>("base") {
            {
                absUrl("http://example.org");
            }
        };
        LeafNode leafNode = new AbstractElement() {
            {
                this.parent = nodeWithBase;
                this.value = "relative-path.html";
            }
        };
        Assert.assertEquals("http://example.org/relative-path.html", leafNode.absUrl(""));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNodeWithParent = new Elements.SingleElement<Element>("parent") {
            {
                this.value = "http://example.com";
            }
        };
        LeafNode leafNode = new AbstractElement() {
            {
                this.parent = leafNodeWithParent;
            }
        };
        Assert.assertEquals("http://example.com", leafNode.baseUri());

        LeafNode rootNode = new Document();
        leafNode.detach();
        rootNode.appendChild(leafNode);
        Assert.assertEquals("", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBaseUri() {
        LeafNode node = new AbstractElement() {
        };
        node.setBaseUri("http://example.com");
    }

    @Test
    public void testChildNodeSize() {
        LeafNode node = new AbstractElement() {
        };
        Assert.assertEquals(0, node.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode node = new AbstractElement() {
        };
        node.ensureChildNodes();
    }
}