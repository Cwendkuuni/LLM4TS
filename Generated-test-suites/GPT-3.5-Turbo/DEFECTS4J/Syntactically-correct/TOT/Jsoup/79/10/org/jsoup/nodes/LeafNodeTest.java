import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.junit.Assert;

import java.util.List;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "leaf";
            }
        };
    }

    @Test
    public void hasAttributes_WhenNoAttributesSet_ReturnsFalse() {
        Assert.assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void hasAttributes_WhenAttributesSet_ReturnsTrue() {
        leafNode.value = new Attributes();
        Assert.assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void attributes_WhenNoAttributesSet_ReturnsEmptyAttributes() {
        Attributes attributes = leafNode.attributes();
        Assert.assertNotNull(attributes);
        Assert.assertTrue(attributes.isEmpty());
    }

    @Test
    public void attributes_WhenAttributesSet_ReturnsCorrectAttributes() {
        leafNode.value = new Attributes();
        Attributes attributes = leafNode.attributes();
        Assert.assertNotNull(attributes);
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void coreValue_WhenNoAttributesSet_ReturnsNull() {
        String coreValue = leafNode.coreValue();
        Assert.assertNull(coreValue);
    }

    @Test
    public void coreValue_WhenAttributeSet_ReturnsAttributeValue() {
        leafNode.value = "value";
        String coreValue = leafNode.coreValue();
        Assert.assertEquals("value", coreValue);
    }

    @Test
    public void coreValue_WhenMultipleAttributesSet_ReturnsNull() {
        leafNode.value = new Attributes();
        String coreValue = leafNode.coreValue();
        Assert.assertNull(coreValue);
    }

    @Test
    public void coreValue_WhenAttributesAndAttributeSetNameMatch_ReturnsAttributeValue() {
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("leaf", "value");
        String coreValue = leafNode.coreValue();
        Assert.assertEquals("value", coreValue);
    }

    @Test
    public void coreValue_WhenAttributesAndAttributeSetNameDoNotMatch_ReturnsNull() {
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("other", "value");
        String coreValue = leafNode.coreValue();
        Assert.assertNull(coreValue);
    }

    @Test
    public void coreValue_WhenAttributeSet_ReturnsUpdatedAttributeValue() {
        leafNode.coreValue("value");
        String coreValue = leafNode.coreValue();
        Assert.assertEquals("value", coreValue);
    }

    @Test
    public void attr_WhenNoAttributesSetAndKeyMatchesNodeName_ReturnsNodeValue() {
        leafNode.value = "value";
        String attrValue = leafNode.attr("leaf");
        Assert.assertEquals("value", attrValue);
    }

    @Test
    public void attr_WhenNoAttributesSetAndKeyDoesNotMatchNodeName_ReturnsEmptyString() {
        leafNode.value = "value";
        String attrValue = leafNode.attr("other");
        Assert.assertEquals("", attrValue);
    }

    @Test
    public void attr_WhenAttributesSet_ReturnsAttributeValueFromAttributes() {
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("attr1", "value1");
        ((Attributes) leafNode.value).put("attr2", "value2");
        String attrValue = leafNode.attr("attr2");
        Assert.assertEquals("value2", attrValue);
    }

    @Test
    public void attr_WhenAttributesSetAndKeyDoesNotMatch_ReturnsEmptyString() {
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("attr1", "value1");
        ((Attributes) leafNode.value).put("attr2", "value2");
        String attrValue = leafNode.attr("other");
        Assert.assertEquals("", attrValue);
    }

    @Test
    public void attr_WhenKeyIsNull_ThrowsIllegalArgumentException() {
        try {
            leafNode.attr(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Key must not be null", e.getMessage());
        }
    }

    @Test
    public void attr_WhenKeyAndValueMatchNodeNameAndUpdateValueSet_ReturnsUpdatedValue() {
        leafNode.value = "value";
        leafNode.attr("leaf", "new value");
        String attrValue = leafNode.attr("leaf");
        Assert.assertEquals("new value", attrValue);
    }

    @Test
    public void attr_WhenKeyAndValueDoNotMatchNodeNameAndUpdateValueSet_ReturnsEmptyString() {
        leafNode.value = "value";
        leafNode.attr("other", "new value");
        String attrValue = leafNode.attr("other");
        Assert.assertEquals("", attrValue);
    }

    @Test
    public void hasAttr_WhenNoAttributesSet_ReturnsFalse() {
        Assert.assertFalse(leafNode.hasAttr("attr1"));
    }

    @Test
    public void hasAttr_WhenKeyDoesNotMatch_ReturnsFalse() {
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("attr1", "value1");
        Assert.assertFalse(leafNode.hasAttr("other"));
    }

    @Test
    public void hasAttr_WhenKeyMatches_ReturnsTrue() {
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("attr1", "value1");
        Assert.assertTrue(leafNode.hasAttr("attr1"));
    }

    @Test
    public void removeAttr_WhenNoAttributesSet_ReturnsLeafNodeWithoutChanges() {
        Node result = leafNode.removeAttr("attr1");
        Assert.assertSame(leafNode, result);
    }

    @Test
    public void removeAttr_WhenAttributeToRemoveDoesNotExist_ReturnsLeafNodeWithoutChanges() {
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("attr1", "value1");
        Node result = leafNode.removeAttr("other");
        Assert.assertSame(leafNode, result);
    }

    @Test
    public void removeAttr_WhenAttributeToRemoveExists_RemovesAttributeAndReturnsLeafNode() {
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("attr1", "value1");
        Node result = leafNode.removeAttr("attr1");
        Assert.assertSame(leafNode, result);
        Assert.assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void absUrl_WhenNoAttributesSet_ReturnsEmptyString() {
        String absUrl = leafNode.absUrl("attr1");
        Assert.assertEquals("", absUrl);
    }

    @Test
    public void absUrl_WhenAttributeSet_DelegatesToSuperAndReturnsResult() {
        leafNode.value = new Attributes();
        ((Attributes) leafNode.value).put("attr1", "value1");
        String absUrl = leafNode.absUrl("attr1");
        Assert.assertEquals("abs_value1", absUrl);
    }

    @Test
    public void baseUri_WhenHasParent_ReturnsParentBaseUri() {
        Node parent = new Node() {
            @Override
            public String nodeName() {
                return "parent";
            }

            @Override
            public String baseUri() {
                return "parentBaseUri";
            }
        };
        leafNode.parent = parent;
        String baseUri = leafNode.baseUri();
        Assert.assertEquals("parentBaseUri", baseUri);
    }

    @Test
    public void baseUri_WhenNoParent_ReturnsEmptyString() {
        String baseUri = leafNode.baseUri();
        Assert.assertEquals("", baseUri);
    }

    @Test
    public void doSetBaseUri_WhenCalled_LeavesBaseUriUnchanged() {
        leafNode.doSetBaseUri("newBaseUri");
        String baseUri = leafNode.baseUri();
        Assert.assertEquals("", baseUri);
    }

    @Test
    public void childNodeSize_AlwaysReturnsZero() {
        int childSize = leafNode.childNodeSize();
        Assert.assertEquals(0, childSize);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void ensureChildNodes_AlwaysThrowsUnsupportedOperationException() {
        leafNode.ensureChildNodes();
    }
}