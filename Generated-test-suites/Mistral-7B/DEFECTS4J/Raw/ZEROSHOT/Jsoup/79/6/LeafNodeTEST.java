import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "Some Value";
            }
        };
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeName", "attributeValue");
            }
        };
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "Some Value";
            }
        };
        assertEquals(null, leafNode.attributes());

        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeName", "attributeValue");
            }
        };
        Attributes attributes = leafNodeWithAttributes.attributes();
        assertEquals("attributeValue", attributes.get("attributeName"));
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "Some Value";
            }
        };
        leafNode.ensureAttributes();
        assertEquals(null, leafNode.value);
        assertEquals(0, leafNode.attributes().size());

        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeName", "attributeValue");
            }
        };
        leafNodeWithAttributes.ensureAttributes();
        assertEquals(attributes(), leafNodeWithAttributes.value);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "Some Value";
            }
        };
        assertEquals("Some Value", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeName", "attributeValue");
            }
        };
        assertEquals(nodeName(), leafNodeWithAttributes.coreValue());

        LeafNode textNode = new TextNode("Some Text");
        assertEquals("Some Text", textNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "Some Value";
            }
        };
        leafNode.coreValue("New Value");
        assertEquals("New Value", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeName", "attributeValue");
            }
        };
        leafNodeWithAttributes.coreValue("New Value");
        assertEquals(new Attributes().put(nodeName(), "New Value"), leafNodeWithAttributes.attributes());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "Some Value";
            }
        };
        assertEquals(EmptyString, leafNode.attr("attributeName"));

        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeName", "attributeValue");
            }
        };
        assertEquals("attributeValue", leafNodeWithAttributes.attr("attributeName"));

        LeafNode textNode = new TextNode("Some Text");
        assertEquals(EmptyString, textNode.attr("attributeName"));
    }

    @Test
    public void testAttrSetter() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "Some Value";
            }
        };
        leafNode.attr("attributeName", "attributeValue");
        assertEquals(new Attributes().put("attributeName", "attributeValue"), leafNode.attributes());

        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeName", "attributeValue");
            }
        };
        leafNodeWithAttributes.attr("attributeName", "newValue");
        assertEquals(new Attributes().put("attributeName", "newValue"), leafNodeWithAttributes.attributes());

        LeafNode textNode = new TextNode("Some Text");
        textNode.attr("attributeName", "attributeValue");
        assertEquals(new Attributes().put("attributeName", "attributeValue"), textNode.attributes());
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "Some Value";
            }
        };
        assertFalse(leafNode.hasAttr("attributeName"));

        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeName", "attributeValue");
            }
        };
        assertTrue(leafNodeWithAttributes.hasAttr("attributeName"));

        TextNode textNode = new TextNode("Some Text");
        assertFalse(textNode.hasAttr("attributeName"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = new Attributes().put("attributeName", "attributeValue");
            }
        };
        leafNode.removeAttr("attributeName");
        assertEquals(EmptyAttributes.EMPTY_MAP, leafNode.attributes());

        LeafNode leafNodeWithRemainingAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes().put("attributeName1", "attributeValue1").put("attributeName2", "attributeValue2");
            }
        };
        leafNodeWithRemainingAttributes.removeAttr("attributeName1");
        assertEquals(new Attributes().put("attributeName2", "attributeValue2"), leafNodeWithRemainingAttributes.attributes());

        TextNode textNode = new TextNode("Some Text");
        textNode.attr("attributeName", "attributeValue");
        textNode.removeAttr("attributeName");
        assertEquals(EmptyAttributes.EMPTY_MAP, textNode.attributes());
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode("img") {
            {
                value = new Attributes().put("src", "/path/to/image.jpg");
            }
        };
        assertEquals("/path/to/image.jpg", leafNode.absUrl("src"));

        LeafNode leafNodeWithBaseUri = new LeafNode("base") {
            {
                value = new Attributes().put("href", "relative/path.html");
            }
        };
        LeafNode document = new Document();
        document.appendElement(leafNodeWithBaseUri).text("Base Node");
        assertEquals("http://example.com/relative/path.html", leafNodeWithBaseUri.absUrl("href"));
        assertEquals("http://example.com/path/to/image.jpg", leafNode.absUrl("src"));

        TextNode textNode = new TextNode("Some Text");
        assertEquals(EmptyString, leafNode.absUrl("src"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode("img") {
            {
                value = new Attributes().put("src", "/path/to/image.jpg");
            }
        };
        assertEquals(EmptyString, leafNode.baseUri());

        LeafNode parentNode = new LeafNode("div") {
            {
                value = new Attributes().put("id", "parentDiv");
            }
        };
        LeafNode leafNodeWithParent = new LeafNode("img") {
            {
                this.parent(parentNode);
                value = new Attributes().put("src", "/path/to/image.jpg");
            }
        };
        assertEquals("parentDiv", leafNodeWithParent.baseUri());

        LeafNode document = new Document();
        document.appendElement(parentNode).appendElement(leafNodeWithParent);
        assertEquals("", document.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode("nodeName");
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode("nodeName");
        leafNode.ensureChildNodes();
    }
}