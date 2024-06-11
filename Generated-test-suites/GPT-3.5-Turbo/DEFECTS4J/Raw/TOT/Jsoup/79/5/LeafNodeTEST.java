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
            public String nodeName() {
                return "testNode";
            }
        };
    }

    @Test
    public void hasAttributes_withNoAttributes_ReturnsFalse() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void hasAttributes_withAttributes_ReturnsTrue() {
        leafNode.attr("attrName", "attrValue");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void attributes_withNoAttributes_ReturnsEmptyAttributes() {
        assertEquals(0, leafNode.attributes().size());
    }

    @Test
    public void attributes_withAttributes_ReturnsCorrectAttributes() {
        leafNode.attr("attrName", "attrValue");
        assertEquals(1, leafNode.attributes().size());
        assertEquals("attrValue", leafNode.attributes().get("attrName"));
    }

    @Test
    public void coreValue_withNoAttributes_ReturnsEmptyString() {
        assertEquals("", leafNode.coreValue());
    }

    @Test
    public void coreValue_withAttributes_ReturnsCorrectValue() {
        leafNode.attr("testNode", "attrValue");
        assertEquals("attrValue", leafNode.coreValue());
    }

    @Test
    public void coreValue_withSetCoreValue_SetsCorrectValue() {
        leafNode.coreValue("newValue");
        assertEquals("newValue", leafNode.coreValue());
    }

    @Test
    public void attr_withNoAttributesAndMatchingKey_ReturnsAttributeValue() {
        assertEquals("attrValue", leafNode.attr("testNode"));
    }

    @Test
    public void attr_withNoAttributesAndNonMatchingKey_ReturnsEmptyString() {
        assertEquals("", leafNode.attr("nonMatchingKey"));
    }

    @Test
    public void attr_withAttributesAndNonMatchingKey_ReturnsAttributeValue() {
        leafNode.attr("attrName", "attrValue");
        assertEquals("attrValue", leafNode.attr("nonMatchingKey"));
    }

    @Test
    public void attr_withAttributesAndMatchingKey_ReturnsAttributeValue() {
        leafNode.attr("attrName", "attrValue");
        assertEquals("attrValue", leafNode.attr("attrName"));
    }

    @Test
    public void attr_withAttributesAndMatchingKeyButNotNodeName_ReturnsEmptyString() {
        leafNode.attr("nonMatchingKey", "attrValue");
        assertEquals("", leafNode.attr("testNode"));
    }

    @Test
    public void attr_withAttributesAndMatchingKeyButNotNodeName_ReturnsAttributeValue() {
        leafNode.attr("attrName", "attrValue");
        leafNode.attr("nonMatchingKey", "nonMatchingValue");
        assertEquals("attrValue", leafNode.attr("attrName"));
    }

    @Test
    public void attr_withAttributesAndMatchingKeyButNotNodeName_ReturnsAttributeValueOfNodeName() {
        leafNode.attr("attrName", "attrValue");
        leafNode.attr("nonMatchingKey", "nonMatchingValue");
        assertEquals("attrValue", leafNode.attr("testNode"));
    }

    @Test
    public void hasAttr_withNoAttributes_ReturnsFalse() {
        assertFalse(leafNode.hasAttr("attrName"));
    }

    @Test
    public void hasAttr_withAttributes_ReturnsTrue() {
        leafNode.attr("attrName", "attrValue");
        assertTrue(leafNode.hasAttr("attrName"));
    }

    @Test
    public void removeAttr_withNoAttributes_DoesNotThrowException() {
        leafNode.removeAttr("attrName");
    }

    @Test
    public void removeAttr_withAttributes_RemovesAttribute() {
        leafNode.attr("attrName", "attrValue");
        leafNode.removeAttr("attrName");
        assertFalse(leafNode.hasAttr("attrName"));
    }

    @Test
    public void absUrl_withNoAttributes_ReturnsEmptyString() {
        assertEquals("", leafNode.absUrl("attrName"));
    }

    @Test
    public void absUrl_withAttributes_CallsParentAbsUrl() {
        leafNode.attr("attrName", "attrValue");
        assertEquals("", leafNode.absUrl("attrName"));
    }

    @Test
    public void baseUri_withNoParent_ReturnsEmptyString() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void baseUri_withParent_ReturnsParentBaseUri() {
        LeafNode parent = new LeafNode() {
            @Override
            public String nodeName() {
                return "parentNode";
            }

            @Override
            public String baseUri() {
                return "http://example.com";
            }
        };
        leafNode.parentNode = parent;
        assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void doSetBaseUri_AlwaysThrowsUnsupportedOperationException() {
        leafNode.doSetBaseUri("http://example.com");
    }

    @Test
    public void childNodeSize_AlwaysReturnsZero() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void ensureChildNodes_AlwaysThrowsUnsupportedOperationException() {
        leafNode.ensureChildNodes();
    }
}