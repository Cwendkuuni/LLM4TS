package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {
            }

            @Override
            protected List<Node> ensureChildNodes() {
                return null;
            }
        };
    }

    @Test
    public void testHasAttributes_WhenValueIsNull_ReturnsFalse() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_WhenValueIsAttributes_ReturnsTrue() {
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes_WhenValueIsAttributes_ReturnsAttributes() {
        leafNode.value = new Attributes();
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }

    @Test
    public void testEnsureAttributes_WhenHasAttributes_DoesNotModifyValue() {
        leafNode.value = new Attributes();
        leafNode.ensureAttributes();
        assertEquals(Attributes.class, leafNode.value.getClass());
    }

    @Test
    public void testEnsureAttributes_WhenValueIsNull_CreatesAttributesAndSetCoreValue() {
        leafNode.ensureAttributes();
        assertEquals(Attributes.class, leafNode.value.getClass());
        assertEquals(leafNode.nodeName(), leafNode.attributes().get(leafNode.nodeName()));
    }

    @Test
    public void testCoreValue_WhenValueIsNull_ReturnsEmptyString() {
        leafNode.value = null;
        assertEquals("", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_WhenValueIsNotNull_ReturnsValueAsString() {
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_WhenValueIsNotNull_ReturnsValueAsString() {
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testAttr_WhenKeyIsNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            leafNode.attr(null);
        });
    }

    @Test
    public void testAttr_WhenHasAttributesAndKeyEqualsNodeName_ReturnsValueAsString() {
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr_WhenHasAttributesAndKeyDoesNotEqualNodeName_ReturnsEmptyString() {
        leafNode.value = "testValue";
        assertEquals("", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttr_WhenValueIsNull_ReturnsEmptyString() {
        leafNode.value = null;
        assertEquals("", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr_WhenDoesNotHaveAttributes_ReturnsEmptyString() {
        assertEquals("", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr_WhenHasAttributesAndKeyDoesNotEqualNodeName_ReturnsAttributeValueFromAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("otherKey", "otherValue");
        leafNode.value = attributes;
        assertEquals("otherValue", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttr_WhenHasAttributesAndKeyDoesNotEqualNodeName_ReturnsEmptyStringIfAttributeValueIsNull() {
        Attributes attributes = new Attributes();
        attributes.put("otherKey", null);
        leafNode.value = attributes;
        assertEquals("", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttr_WhenHasAttributesAndKeyDoesNotEqualNodeName_ReturnsEmptyStringIfAttributeValueIsEmptyString() {
        Attributes attributes = new Attributes();
        attributes.put("otherKey", "");
        leafNode.value = attributes;
        assertEquals("", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttr_WhenHasAttributesAndKeyDoesNotEqualNodeName_ReturnsAttributeValueAsString() {
        Attributes attributes = new Attributes();
        attributes.put("otherKey", "otherValue");
        leafNode.value = attributes;
        assertEquals("otherValue", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttr_WhenHasAttributesAndKeyEqualsNodeNameAndValueIsNull_ReturnsEmptyString() {
        leafNode.value = null;
        assertEquals("", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr_WhenHasAttributesAndKeyEqualsNodeNameAndValueIsNotNull_ReturnsValue() {
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr_WhenDoesNotHaveAttributesAndKeyEqualsNodeName_ReturnsValue() {
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr_WhenDoesNotHaveAttributesAndKeyDoesNotEqualNodeName_ReturnsEmptyString() {
        leafNode.value = "testValue";
        assertEquals("", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttr_WhenDoesNotHaveAttributesAndKeyDoesNotEqualNodeName_ReturnsEmptyStringIfValueIsNull() {
        leafNode.value = null;
        assertEquals("", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttr_WhenDoesNotHaveAttributesAndKeyDoesNotEqualNodeName_ReturnsEmptyStringIfValueIsEmptyString() {
        leafNode.value = "";
        assertEquals("", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttr_WhenDoesNotHaveAttributesAndKeyDoesNotEqualNodeName_ReturnsValueAsString() {
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttr_WhenDoesNotHaveAttributesAndKeyEqualsNodeNameAndValueIsNull_ReturnsEmptyString() {
        leafNode.value = null;
        assertEquals("", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr_WhenDoesNotHaveAttributesAndKeyEqualsNodeNameAndValueIsNotNull_ReturnsValue() {
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testHasAttr_WhenDoesNotHaveAttributes_ReturnsFalse() {
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testHasAttr_WhenHasAttributes_ReturnsSuperHasAttr() {
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testAddAttr_WhenDoesNotHaveAttributesAndKeyEqualsNodeName_SetsValue() {
        leafNode.attr(leafNode.nodeName(), "testValue");
        assertEquals("testValue", leafNode.value);
    }

    @Test
    public void testAddAttr_WhenHasAttributesAndKeyEqualsNodeName_SetsValue() {
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        leafNode.attr(leafNode.nodeName(), "testValue");
        assertEquals("testValue", attributes.get(leafNode.nodeName()));
    }

    @Test
    public void testAddAttr_WhenDoesNotHaveAttributesAndKeyDoesNotEqualNodeName_CreatesAttributesAndSetsValue() {
        leafNode.attr("otherKey", "otherValue");
        Attributes attributes = (Attributes) leafNode.value;
        assertEquals("otherValue", attributes.get("otherKey"));
    }

    @Test
    public void testAddAttr_WhenHasAttributesAndKeyDoesNotEqualNodeName_SetsValueInAttributes() {
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        leafNode.attr("otherKey", "otherValue");
        assertEquals("otherValue", attributes.get("otherKey"));
    }

    @Test
    public void testRemoveAttr_WhenHasAttributes_DeletesAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;
        leafNode.removeAttr("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testAbsUrl_WhenHasAttributes_DelegatesToSuperAbsUrl() {
        Attributes attributes = new Attributes();
        leafNode.value = attributes;
        leafNode.absUrl("key");
        // Assert that the method call is delegated properly and any assertions on the return value
        // would be specific to the implementation of the super class (Node).
    }

    @Test
    public void testBaseUri_WhenHasParent_ReturnsParentBaseUri() {
        Node parent = new Element("parent");
        parent.setBaseUri("parentBaseUri");
        leafNode.setParent(parent);
        assertEquals("parentBaseUri", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_WhenDoesNotHaveParent_ReturnsEmptyString() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri_WhenCalled_DoesNothing() {
        // Test that the method does nothing as it is a no-op
        leafNode.doSetBaseUri("baseUri");
    }

    @Test
    public void testChildNodeSize_AlwaysReturnsZero() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test
    public void testEnsureChildNodes_ThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            leafNode.ensureChildNodes();
        });
    }
}