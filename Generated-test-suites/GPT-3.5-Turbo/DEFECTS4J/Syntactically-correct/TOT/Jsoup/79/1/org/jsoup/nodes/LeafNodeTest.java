import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

public class LeafNodeTest {

    private static class MockLeafNode extends LeafNode {
        public MockLeafNode() {
            super();
        }
    }

    @Test
    public void testHasAttributes_withAttributes_returnsTrue() {
        LeafNode leafNode = new MockLeafNode();
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_withoutAttributes_returnsFalse() {
        LeafNode leafNode = new MockLeafNode();
        leafNode.value = null;
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes_withAttributes_returnsAttributes() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testAttributes_withoutAttributes_createsAttributesAndReturns() {
        LeafNode leafNode = new MockLeafNode();
        leafNode.value = null;
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertEquals(0, attributes.size());
    }

    @Test
    public void testEnsureAttributes_withoutAttributes_createsAttributes() {
        LeafNode leafNode = new MockLeafNode();
        leafNode.value = null;
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testEnsureAttributes_withAttributes_doesNotCreateAttributes() {
        LeafNode leafNode = new MockLeafNode();
        leafNode.value = new Attributes();
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testEnsureAttributes_withNonNullCoreValue_addsCoreValueToAttributes() {
        LeafNode leafNode = new MockLeafNode();
        leafNode.value = "value";
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());

        Attributes attributes = (Attributes) leafNode.value;
        assertEquals("value", attributes.get(leafNode.nodeName()));
    }

    @Test
    public void testCoreValue_returnsAttributeValueForKey() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        attributes.put(leafNode.nodeName(), "value");
        leafNode.value = attributes;

        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_setsAttributeValueForKey() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        leafNode.value = attributes;

        leafNode.coreValue("value");
        assertEquals("value", attributes.get(leafNode.nodeName()));
    }

    @Test
    public void testAttr_withoutAttributes_returnsAttributeValueForKey() {
        LeafNode leafNode = new MockLeafNode();
        leafNode.value = null;

        assertEquals("key", leafNode.attr("key"));
    }

    @Test
    public void testAttr_withAttributes_returnsAttributeValueForKey() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttr_withAttributesAndProvidedKey_returnsAttributeValueForProvidedKey() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        assertEquals("value", leafNode.attr("key"));
        assertEquals("value", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr_withAttributesAndInvalidKey_returnsEmptyString() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        assertEquals("", leafNode.attr("invalid"));
    }

    @Test
    public void testAttr_withAttributesAndNullKey_throwsException() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        assertThrows(NullPointerException.class, () -> leafNode.attr(null));
    }

    @Test
    public void testAttr_withAttributesAndInvalidAndNullKey_throwsException() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        assertThrows(NullPointerException.class, () -> leafNode.attr(null));
    }

    @Test
    public void testAttr_withAttributesAndProvidedKeyAndValue_callSuperAttr() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        leafNode.value = attributes;

        leafNode.attr("key", "value");

        assertEquals("value", attributes.get("key"));
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttr_withoutAttributes_returnsFalse() {
        LeafNode leafNode = new MockLeafNode();
        leafNode.value = null;

        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testHasAttr_withAttributesAndKey_returnsTrue() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        assertTrue(leafNode.hasAttr("key"));
    }
  
    @Test
    public void testHasAttr_withAttributesAndInvalidKey_returnsFalse() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        assertFalse(leafNode.hasAttr("invalid"));
    }

    @Test
    public void testRemoveAttr_withoutAttributes_returnsNull() {
        LeafNode leafNode = new MockLeafNode();
        leafNode.value = null;

        assertNull(leafNode.removeAttr("key"));
    }

    @Test
    public void testRemoveAttr_withAttributesAndKey_returnsRemovedValue() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        String removedValue = leafNode.removeAttr("key");
        assertEquals("value", removedValue);
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAbsUrl_withAttributes_returnsAttributeValueForKey() {
        LeafNode leafNode = new MockLeafNode();
        Attributes attributes = new Attributes();
        attributes.put("key", "http://example.com/");
        leafNode.value = attributes;

        assertEquals("http://example.com/", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri_withParent_returnsParentBaseUri() {
        LeafNode leafNode = new MockLeafNode();
        Node parentNode = new Node();
        parentNode.setBaseUri("http://parent.com/");
        leafNode.setParentNode(parentNode);

        assertEquals("http://parent.com/", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_withoutParent_returnsEmptyString() {
        LeafNode leafNode = new MockLeafNode();

        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri_needsToBeNoop() {
        LeafNode leafNode = new MockLeafNode();

        leafNode.doSetBaseUri("http://example.com/");

        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize_alwaysReturnsZero() {
        LeafNode leafNode = new MockLeafNode();

        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes_alwaysThrowsUnsupportedOperationException() {
        LeafNode leafNode = new MockLeafNode();

        leafNode.ensureChildNodes();
    }

}