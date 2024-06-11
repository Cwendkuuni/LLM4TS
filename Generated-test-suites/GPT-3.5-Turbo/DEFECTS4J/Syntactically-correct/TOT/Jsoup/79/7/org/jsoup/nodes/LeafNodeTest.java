import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
    }

    // Test hasAttributes() method

    @Test
    public void testHasAttributes_NoAttributes_ReturnsFalse() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_AttributesSet_ReturnsTrue() {
        leafNode.attr("attribute1", "value1");
        assertTrue(leafNode.hasAttributes());
    }

    // Test attributes() method

    @Test
    public void testAttributes_NoAttributes_ReturnsEmptyAttributes() {
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAttributes_AttributesSet_ReturnsCorrectAttributes() {
        leafNode.attr("attribute1", "value1");
        Attributes attributes = leafNode.attributes();

        assertNotNull(attributes);
        assertTrue(attributes.hasKey("attribute1"));
        assertEquals("value1", attributes.get("attribute1"));
    }

    // Test coreValue() method

    @Test
    public void testCoreValue_NoAttributes_ReturnsEmptyString() {
        String coreValue = leafNode.coreValue();
        assertEquals("", coreValue);
    }

    @Test
    public void testCoreValue_AttributeSet_ReturnsAttributeValue() {
        leafNode.attr("attribute1", "value1");
        String coreValue = leafNode.coreValue();
        assertEquals("value1", coreValue);
    }

    // Test coreValue(String value) method

    @Test
    public void testCoreValue_SetAttribute_ReturnsUpdatedCoreValue() {
        leafNode.attr("attribute1", "value1");

        leafNode.coreValue("newValue");
        String coreValue = leafNode.coreValue();

        assertEquals("newValue", coreValue);
    }

    // Test attr(String key) method

    @Test
    public void testAttr_NoAttributes_ReturnsEmptyString() {
        String attrValue = leafNode.attr("attribute1");
        assertEquals("", attrValue);
    }

    @Test
    public void testAttr_AttributeSet_ReturnsAttributeValue() {
        leafNode.attr("attribute1", "value1");

        String attrValue = leafNode.attr("attribute1");
        assertEquals("value1", attrValue);
    }

    // Test attr(String key, String value) method

    @Test
    public void testAttr_SetAttributeWithExistingKey_UpdatesAttributeValue() {
        leafNode.attr("attribute1", "value1");
        leafNode.attr("attribute1", "newValue");

        String attrValue = leafNode.attr("attribute1");
        assertEquals("newValue", attrValue);
    }

    @Test
    public void testAttr_SetAttributeWithNewKey_AddsAttribute() {
        leafNode.attr("attribute1", "value1");
        leafNode.attr("attribute2", "value2");

        assertTrue(leafNode.hasAttr("attribute2"));
        assertEquals("value2", leafNode.attr("attribute2"));
    }

    // Test hasAttr(String key) method

    @Test
    public void testHasAttr_NoAttributes_ReturnsFalse() {
        assertFalse(leafNode.hasAttr("attribute1"));
    }

    @Test
    public void testHasAttr_AttributeSet_ReturnsTrue() {
        leafNode.attr("attribute1", "value1");
        assertTrue(leafNode.hasAttr("attribute1"));
    }

    // Test removeAttr(String key) method

    @Test
    public void testRemoveAttr_AttributeExists_RemovesAttribute() {
        leafNode.attr("attribute1", "value1");
        assertTrue(leafNode.hasAttr("attribute1"));

        leafNode.removeAttr("attribute1");
        assertFalse(leafNode.hasAttr("attribute1"));
    }

    // Test absUrl(String key) method

    @Test
    public void testAbsUrl_NoAttributes_ReturnsEmptyString() {
        String absUrl = leafNode.absUrl("attribute1");
        assertEquals("", absUrl);
    }

    // Test baseUri() method

    @Test
    public void testBaseUri_HasParent_ReturnsParentBaseUri() {
        Node parent = new Element("parent");
        parent.setBaseUri("http://example.com");

        leafNode.setParentNode(parent);
        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_NoParent_ReturnsEmptyString() {
        assertEquals("", leafNode.baseUri());
    }

    // Test doSetBaseUri(String baseUri) method

    @Test
    public void testDoSetBaseUri_Always_Noop() {
        leafNode.doSetBaseUri("http://example.com");

        assertEquals("", leafNode.baseUri());
    }

    // Test childNodeSize() method

    @Test
    public void testChildNodeSize_Always_ReturnsZero() {
        assertEquals(0, leafNode.childNodeSize());
    }

    // Test ensureChildNodes() method

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes_Always_ThrowsUnsupportedOperationException() {
        leafNode.ensureChildNodes();
    }

}