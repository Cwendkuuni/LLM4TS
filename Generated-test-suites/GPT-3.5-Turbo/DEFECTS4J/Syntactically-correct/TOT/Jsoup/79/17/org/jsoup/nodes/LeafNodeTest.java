package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {};
    }

    @Test
    public void testHasAttributes_WhenAttributesExist_ReturnTrue() {
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_WhenAttributesDoNotExist_ReturnFalse() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes_WhenAttributesExist_ReturnAttributes() {
        leafNode.value = new Attributes();
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }

    @Test
    public void testAttributes_WhenAttributesDoNotExist_ReturnEmptyAttributes() {
        assertNull(leafNode.attributes().getClass());
    }

    @Test
    public void testEnsureAttributes_WhenAttributesDoNotExist_CreateAttributes() {
        leafNode.ensureAttributes();
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }

    @Test
    public void testEnsureAttributes_WhenAttributesExist_DoNotCreateAttributes() {
        leafNode.value = new Attributes();
        leafNode.ensureAttributes();
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }

    @Test
    public void testCoreValue_WhenValueisSet_ReturnCoreValue() {
        String expected = "value";
        leafNode.value = expected;
        assertEquals(expected, leafNode.coreValue());
    }

    @Test
    public void testCoreValue_WhenValueisNotSet_ReturnNull() {
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testCoreValue_WhenValueisSet_UpdateCoreValue() {
        String expected = "value";
        String newValue = "new value";
        leafNode.value = expected;
        leafNode.coreValue(newValue);
        assertEquals(newValue, leafNode.coreValue());
    }

    @Test
    public void testAttr_WhenAttributesDoNotExist_ReturnAttributeValueIfKeyMatchesNodeName() {
        String expected = "value";
        leafNode.value = expected;
        assertEquals(expected, leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr_WhenAttributesDoNotExist_ReturnEmptyStringIfKeyDoesNotMatchNodeName() {
        leafNode.value = "value";
        assertEquals("", leafNode.attr("key"));
    }

    @Test
    public void testAttr_WhenAttributesExist_ReturnAttributeValue() {
        String key = "key";
        String value = "value";
        Attributes attributes = new Attributes();
        attributes.put(key, value);
        leafNode.value = attributes;
        assertEquals(value, leafNode.attr(key));
    }

    @Test
    public void testAttr_WhenAttributesDoNotExistAndKeyMatchesNodeName_SetValueAsCoreValue() {
        String key = leafNode.nodeName();
        String value = "value";
        leafNode.attr(key, value);
        assertEquals(value, leafNode.coreValue());
    }

    @Test
    public void testAttr_WhenAttributesDoNotExistAndKeyDoesNotMatchNodeName_CreateAttributesAndSetValue() {
        String key = "key";
        String value = "value";
        leafNode.attr(key, value);
        assertTrue(leafNode.hasAttributes());
        assertEquals(value, leafNode.attributes().get(key));
    }

    @Test
    public void testAttr_WhenAttributesExistAndKeyMatchesNodeName_UpdateCoreValue() {
        String key = leafNode.nodeName();
        String value = "value";
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        leafNode.attr(key, value);
        assertEquals(value, leafNode.coreValue());
    }

    @Test
    public void testAttr_WhenAttributesExistAndKeyDoesNotMatchNodeName_UpdateAttributeValue() {
        String key = "key";
        String value = "value";
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        leafNode.attr(key, value);
        assertEquals(value, leafNode.attributes().get(key));
    }

    @Test
    public void testHasAttr_WhenAttributesDoNotExist_ReturnFalse() {
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testHasAttr_WhenAttributesExistAndKeyExists_ReturnTrue() {
        String key = "key";
        String value = "value";
        Attributes attributes = new Attributes();
        attributes.put(key, value);
        leafNode.value = attributes;
        assertTrue(leafNode.hasAttr(key));
    }

    @Test
    public void testHasAttr_WhenAttributesExistAndKeyDoesNotExist_ReturnFalse() {
        String key = "key";
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        assertFalse(leafNode.hasAttr(key));
    }

    @Test
    public void testRemoveAttr_WhenAttributesExistAndKeyExists_RemoveAttribute() {
        String key = "key";
        String value = "value";
        Attributes attributes = new Attributes();
        attributes.put(key, value);
        leafNode.value = attributes;
        leafNode.removeAttr(key);
        assertFalse(leafNode.hasAttr(key));
    }

    @Test
    public void testRemoveAttr_WhenAttributesExistAndKeyDoesNotExist_DoesNotRemoveAnyAttribute() {
        String key = "key";
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        leafNode.removeAttr(key);
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAbsUrl_WhenAttributesDoNotExist_ReturnEmptyString() {
        assertEquals("", leafNode.absUrl("key"));
    }

    @Test
    public void testAbsUrl_WhenAttributesExist_ReturnAbsoluteUrl() {
        String baseUrl = "http://example.com";
        String key = "key";
        String value = "/path";
        Attributes attributes = new Attributes();
        attributes.put(key, value);
        leafNode.value = attributes;

        assertEquals(baseUrl + value, leafNode.absUrl(key));
    }

    @Test
    public void testBaseUri_WhenLeafNodeHasParent_ReturnParentBaseUri() {
        String baseUri = "http://example.com";
        Node parent = new Element("parent");
        parent.setBaseUri(baseUri);
        leafNode.parentNode = parent;

        assertEquals(baseUri, leafNode.baseUri());
    }

    @Test
    public void testBaseUri_WhenLeafNodeDoesNotHaveParent_ReturnEmptyString() {
        assertEquals("", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoSetBaseUri_ThrowsUnsupportedOperationException() {
        leafNode.doSetBaseUri("http://example.com");
    }

    @Test
    public void testChildNodeSize_ReturnZero() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes_ThrowsUnsupportedOperationException() {
        leafNode.ensureChildNodes();
    }
}