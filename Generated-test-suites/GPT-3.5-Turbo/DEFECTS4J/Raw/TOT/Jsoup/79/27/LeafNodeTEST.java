import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
    public void hasAttributes_shouldReturnFalseWhenValueIsNull() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void hasAttributes_shouldReturnFalseWhenValueIsNotInstanceOfAttributes() {
        leafNode.value = "value";
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void hasAttributes_shouldReturnTrueWhenValueIsInstanceOfAttributes() {
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void attributes_shouldReturnAttributesInstanceIfValueIsNotInstanceOfAttributes() {
        leafNode.value = "value";
        assertFalse(leafNode.hasAttributes());
        assertTrue(leafNode.attributes() instanceof Attributes);
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void attributes_shouldReturnAttributesInstanceIfValueIsInstanceOfAttributes() {
        leafNode.value = new Attributes();
        assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void attr_shouldReturnEmptyStringWhenKeyIsNull() {
        assertEquals("", leafNode.attr(null));
    }

    @Test
    public void attr_shouldReturnValueWhenKeyIsSameAsNodeNameAndValueIsString() {
        leafNode.value = "value";
        assertEquals("value", leafNode.attr("leaf"));
    }

    @Test
    public void attr_shouldReturnEmptyStringWhenKeyIsNotEqualToNodeNameAndValueIsString() {
        leafNode.value = "value";
        assertEquals("", leafNode.attr("other"));
    }

    @Test
    public void attr_shouldDelegateToSuperAttrWhenValueIsInstanceOfAttributes() {
        leafNode.value = new Attributes();
        assertNotNull(leafNode.attr("key"));
    }

    @Test
    public void attr_shouldDelegateToSuperAttrWhenValueIsNotInstanceOfAttributes() {
        leafNode.value = "value";
        assertNotNull(leafNode.attr("key"));
    }

    @Test
    public void attr_shouldReturnEmptyStringWhenValueIsNull() {
        leafNode.value = null;
        assertEquals("", leafNode.attr("key"));
    }

    @Test
    public void attr_shouldDelegateToSuperAttrWhenValueIsNullAndKeyIsSameAsNodeName() {
        leafNode.value = null;
        assertNotNull(leafNode.attr("leaf"));
    }

    @Test
    public void attr_shouldDelegateToSuperAttrWhenValueIsNullAndKeyIsNotEqualToNodeName() {
        leafNode.value = null;
        assertNotNull(leafNode.attr("other"));
    }

    @Test
    public void attr_shouldDelegateToSuperAttrWhenValueIsNullAndKeyIsNull() {
        leafNode.value = null;
        assertNotNull(leafNode.attr(null));
    }

    @Test
    public void attr_shouldReturnInstanceWithUpdatedValueWhenKeyIsSameAsNodeNameAndValueIsNotInstanceOfAttributes() {
        leafNode.value = "value";
        leafNode.attr("leaf", "new value");
        assertEquals("new value", leafNode.attr("leaf"));
    }

    @Test
    public void attr_shouldReturnInstanceWithUpdatedValueWhenValueIsInstanceOfAttributes() {
        leafNode.value = new Attributes();
        leafNode.attr("key", "value");
        assertTrue(leafNode.attr("key").equals("value"));
    }

    @Test
    public void hasAttr_shouldReturnFalseWhenKeyIsNull() {
        assertFalse(leafNode.hasAttr(null));
    }

    @Test
    public void hasAttr_shouldDelegateToSuperHasAttrWhenValueIsNotInstanceOfAttributes() {
        leafNode.value = "value";
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void hasAttr_shouldDelegateToSuperHasAttrWhenValueIsInstanceOfAttributes() {
        leafNode.value = new Attributes();
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void removeAttr_shouldDelegateToSuperRemoveAttr() {
        leafNode.value = new Attributes();
        assertNotNull(leafNode.removeAttr("key"));
    }

    @Test
    public void absUrl_shouldDelegateToSuperAbsUrl() {
        leafNode.value = new Attributes();
        assertNotNull(leafNode.absUrl("key"));
    }

    @Test
    public void baseUri_shouldReturnEmptyStringWhenHasParentIsFalse() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void baseUri_shouldReturnParentBaseUriWhenHasParentIsTrue() {
        Node parent = new LeafNode();
        parent.value = new Attributes();
        leafNode.setParentNode(parent);
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void doSetBaseUri_shouldNotPerformAnyAction() {
        leafNode.doSetBaseUri("baseUri");
        // No assertion as this method does nothing
    }

    @Test
    public void childNodeSize_shouldReturnZero() {
        assertEquals(0, leafNode.childNodeSize());
    }

}