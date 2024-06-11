
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = spy(LeafNode.class);
    }

    @Test
    public void testHasAttributes_WhenAttributesAreSet_ReturnsTrue() {
        Attributes attributes = new Attributes();
        leafNode.value = attributes;

        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_WhenAttributesAreNotSet_ReturnsFalse() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes_WhenAttributesAreSet_ReturnsAttributesObject() {
        Attributes attributes = new Attributes();
        leafNode.value = attributes;

        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testAttributes_WhenAttributesAreNotSet_ReturnsEmptyAttributesObject() {
        Attributes emptyAttributes = new Attributes();
        when(leafNode.nodeName()).thenReturn("nodeName");

        assertEquals(emptyAttributes, leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes_WhenAttributesAreNotSetAndCoreValueIsNull_CreatesEmptyAttributesObject() {
        when(leafNode.nodeName()).thenReturn("nodeName");
        leafNode.value = null;

        leafNode.ensureAttributes();

        assertTrue(leafNode.hasAttributes());
        assertEquals("nodeName", ((Attributes) leafNode.value).get("nodeName"));
    }

    @Test
    public void testEnsureAttributes_WhenAttributesAreNotSetAndCoreValueIsNotNull_CreatesAttributesObjectWithCoreValue() {
        when(leafNode.nodeName()).thenReturn("nodeName");
        leafNode.value = "coreValue";

        leafNode.ensureAttributes();

        assertTrue(leafNode.hasAttributes());
        assertEquals("nodeName", ((Attributes) leafNode.value).getKey(0));
        assertEquals("coreValue", ((Attributes) leafNode.value).get("nodeName"));
    }

    @Test
    public void testCoreValue_WhenAttributesAreNotSetAndKeyEqualsNodeName_ReturnsCoreValue() {
        when(leafNode.nodeName()).thenReturn("nodeName");
        leafNode.value = "coreValue";

        assertEquals("coreValue", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_WhenAttributesAreNotSetAndKeyDoesNotEqualNodeName_ReturnsEmptyString() {
        when(leafNode.nodeName()).thenReturn("nodeName");
        leafNode.value = "coreValue";

        assertEquals("", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_WhenAttributesAreSetAndKeyEqualsNodeName_ReturnsAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("nodeName", "attribute");
        leafNode.value = attributes;
        when(leafNode.nodeName()).thenReturn("nodeName");

        assertEquals("attribute", leafNode.coreValue());
    }

    @Test
    public void testAttr_WhenAttributesAreNotSetAndKeyEqualsNodeName_ReturnsValue() {
        when(leafNode.nodeName()).thenReturn("nodeName");
        leafNode.value = "value";

        assertEquals("value", leafNode.attr("nodeName"));
    }

    @Test
    public void testAttr_WhenAttributesAreNotSetAndKeyDoesNotEqualNodeName_ReturnsEmptyString() {
        when(leafNode.nodeName()).thenReturn("nodeName");
        leafNode.value = "value";

        assertEquals("", leafNode.attr("notNodeName"));
    }

    @Test
    public void testAttr_WhenAttributesAreSet_ReturnsAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttr_WhenKeyIsNull_ThrowsException() {
        assertThrows(NullPointerException.class, () -> leafNode.attr(null));
    }

    @Test
    public void testHasAttr_WhenAttributesAreSet_ReturnsTrue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testHasAttr_WhenAttributesAreNotSet_ReturnsFalse() {
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr_WhenAttributesAreSetAndHasAttribute_ReturnsNodeWithoutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        LeafNode node = (LeafNode) leafNode.removeAttr("key");

        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr_WhenAttributesAreSetAndDoesNotHaveAttribute_ReturnsNodeWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;

        LeafNode node = (LeafNode) leafNode.removeAttr("notKey");

        assertTrue(node.hasAttr("key"));
    }

    @Test
    public void testAbsUrl_WhenAttributesAreSet_ReturnsAbsoluteUrl() {
        when(leafNode.baseUri()).thenReturn("https://example.com");
        Attributes attributes = new Attributes();
        attributes.put("href", "/path");
        leafNode.value = attributes;

        assertEquals("https://example.com/path", leafNode.absUrl("href"));
    }

    @Test
    public void testBaseUri_WhenParentIsSet_ReturnsParentBaseUri() {
        Node parent = mock(Node.class);
        when(leafNode.hasParent()).thenReturn(true);
        when(leafNode.parent()).thenReturn(parent);
        when(parent.baseUri()).thenReturn("https://example.com");

        assertEquals("https://example.com", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_WhenParentIsNotSet_ReturnsEmptyString() {
        when(leafNode.hasParent()).thenReturn(false);

        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri_WhenCalled_DoesNothing() {
        leafNode.doSetBaseUri("https://example.com");

        // No assertion. If the method throws an exception, the test will fail.
    }

    @Test
    public void testChildNodeSize_Always_ReturnsZero() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test
    public void testEnsureChildNodes_Always_ThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> leafNode.ensureChildNodes());
    }

}

