import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        // ... Test cases for basic functionality and edge cases ...
    }

    @Test
    public void testAttributes() {
        // ... Test cases for basic functionality and edge cases ...
    }

    // Add test cases for other methods here...

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.ensureChildNodes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHasParentAndBaseUri() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.parent().baseUri(); // This should throw an UnsupportedOperationException
    }

    @Test
    public void testCoreValue() {
        // ... Test cases for basic functionality and edge cases ...
    }

    @Test
    public void testCoreValueWithStringValue() {
        TextNode textNode = new TextNode("Test");
        assertEquals("Test", textNode.coreValue());
    }

    @Test
    public void testCoreValueWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test", "attribute");
        TextNode textNode = new TextNode("Test", attributes);
        assertEquals("Test", textNode.coreValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testCoreValueWithNullValue() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue(null);
    }

    @Test
    public void testAttr() {
        // ... Test cases for basic functionality and edge cases ...
    }

    @Test
    public void testAttrWithNullKey() {
        TextNode textNode = new TextNode("Test");
        assertNull(textNode.attr(null));
    }

    @Test
    public void testAttrWithEmptyStringValue() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        assertEquals("", textNode.attr("test"));
    }

    @Test
    public void testAttrWithWhitespaceStringValue() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        assertEquals(" ", textNode.attr("test"));
    }

    @Test
    public void testAttrWithLongStringValue() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        String longString = "This is a very long string that tests the edge case for string length in the attr method";
        textNode.coreValue(longString);
        assertEquals(longString, textNode.attr("test"));
    }

    @Test
    public void testAttrWithNonExistentKey() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        assertNull(textNode.attr("nonExistentKey"));
    }

    @Test
    public void testAttrWithSameKeyAndStringValue() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        textNode.attr("test", "Test");
        assertEquals("Test", textNode.attr("test"));
    }

    @Test
    public void testAttrWithSameKeyAndAttributesValue() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        Attributes attributes = new Attributes();
        attributes.put("test", "Test");
        textNode.value = attributes;
        assertEquals("Test", textNode.attr("test"));
    }

    @Test
    public void testAttrWithSameKeyAndStringValueOverwriting() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        textNode.attr("test", "Attribute");
        assertEquals("Attribute", textNode.attr("test"));
    }

    @Test
    public void testAttrWithDifferentKeyAndStringValue() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        textNode.attr("test", "Test");
        textNode.attr("differentKey", "Attribute");
        assertEquals("Test", textNode.attr("test"));
        assertEquals("Attribute", textNode.attr("differentKey"));
    }

    @Test
    public void testAttrWithNullValue() {
        TextNode textNode = new TextNode("Test");
        textNode.attr("test", null);
        assertNull(textNode.attr("test"));
    }

    @Test
    public void testAttrWithEmptyStringValue() {
        TextNode textNode = new TextNode("Test");
        textNode.attr("test", "");
        assertEquals("", textNode.attr("test"));
    }

    @Test
    public void testAttrWithWhitespaceStringValue() {
        TextNode textNode = new TextNode("Test");
        textNode.attr("test", " ");
        assertEquals(" ", textNode.attr("test"));
    }

    @Test
    public void testAttrWithLongStringValue() {
        TextNode textNode = new TextNode("Test");
        String longString = "This is a very long string that tests the edge case for string length in the attr method";
        textNode.attr("test", longString);
        assertEquals(longString, textNode.attr("test"));
    }

    @Test
    public void testHasAttr() {
        // ... Test cases for basic functionality and edge cases ...
    }

    @Test
    public void testHasAttrWithExistentKey() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        textNode.attr("test", "Test");
        assertTrue(textNode.hasAttr("test"));
    }

    @Test
    public void testHasAttrWithNonExistentKey() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        assertFalse(textNode.hasAttr("nonExistentKey"));
    }

    @Test
    public void testRemoveAttr() {
        // ... Test cases for basic functionality and edge cases ...
    }

    @Test
    public void testRemoveAttrWithExistentKey() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        textNode.attr("test", "Test");
        textNode.removeAttr("test");
        assertFalse(textNode.hasAttr("test"));
    }

    @Test
    public void testRemoveAttrWithNonExistentKey() {
        TextNode textNode = new TextNode("Test");
        textNode.coreValue("Test");
        assertFalse(textNode.hasAttr("nonExistentKey"));
        textNode.removeAttr("nonExistentKey");
        assertFalse(textNode.hasAttr("nonExistentKey"));
    }

    @Test
    public void testAbsUrl() {
        // ... Test cases for basic functionality and edge cases ...
    }

    @Test
    public void testAbsUrlWithNullBaseUri() {
        TextNode textNode = new TextNode("Test");
        assertNull(textNode.absUrl("test"));
    }

    @Test
    public void testAbsUrlWithEmptyBaseUri() {
        TextNode textNode = new TextNode("Test");
        String emptyBaseUri = "";
        assertEquals(emptyBaseUri, textNode.absUrl("test"));
    }

    @Test
    public void testAbsUrlWithBaseUriContainingSlash() {
        TextNode textNode = new TextNode("Test");
        String baseUri = "/test/";
        textNode.parent().baseUri(baseUri);
        assertEquals("/test/test", textNode.absUrl("test"));
    }

    @Test
    public void testAbsUrlWithBaseUriWithoutTrailingSlash() {
        TextNode textNode = new TextNode("Test");
        String baseUri = "/test";
        textNode.parent().baseUri(baseUri);
        assertEquals("/test/test", textNode.absUrl("test"));
    }

    @Test
    public void testAbsUrlWithBaseUriContainingTrailingSlash() {
        TextNode textNode = new Textnode("Test");
        String baseUri = "/test/";
        textNode.parent().baseUri(baseUri);
        assertEquals("/test/test", textNode.absUrl("test"));
    }

    @Test
    public void testBaseUri() {
        // ... Test cases for basic functionality and edge cases ...
    }

    @Test
    public void testBaseUriWithParent() {
        TextNode textNode = new TextNode("Test");
        Node parentNode = new ElementNode("Parent");
        parentNode.add(textNode);
        textNode.parent(parentNode);
        assertEquals("/Parent", textNode.baseUri());
    }

    @Test
    public void testBaseUriWithoutParent() {
        TextNode textNode = new TextNode("Test");
        assertEquals("", textNode.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        // ... Test cases for basic functionality and edge cases ...
    }

    @Test
    public void testChildNodeSizeWithZeroChildren() {
        TextNode textNode = new TextNode("Test");
        assertEquals(0, textNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        TextNode textNode = new TextNode("Test");
        textNode.ensureChildNodes();
    }

    @Test
    public void testDoSetBaseUri() {
        // ... Test cases for basic functionality and edge cases ...
    }

    @Test
    public void testDoSetBaseUriWithNullBaseUri() {
        TextNode textNode = new TextNode("Test");
        textNode.doSetBaseUri(null);
        assertEquals("", textNode.baseUri());
    }

    @Test
    public void testDoSetBaseUriWithEmptyBaseUri() {
        TextNode textNode = new TextNode("Test");
        textNode.doSetBaseUri("");
        assertEquals("", textNode.baseUri());
    }

    @Test
    public void testDoSetBaseUriWithBaseUriContainingSlash() {
        TextNode textNode = new TextNode("Test");
        textNode.doSetBaseUri("/test/");
        assertEquals("/test/", textNode.baseUri());
    }

    @Test
    public void testDoSetBaseUriWithBaseUriWithoutTrailingSlash() {
        TextNode textNode = new TextNode("Test");
        textNode.doSetBaseUri("/test");
        assertEquals("/test/", textNode.baseUri());
    }

    @Test
    public void testDoSetBaseUriWithBaseUriContainingTrailingSlash() {
        TextNode textNode = new TextNode("Test");
        textNode.doSetBaseUri("/test/");
        assertEquals("/test/", textNode.baseUri());
    }
}