import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNodeMock();
    }

    @Test
    public void testHasAttributes_shouldReturnFalseWhenNoAttributes() {
        assertFalse("hasAttributes() should return false when no attributes are present", leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_shouldReturnTrueWhenAttributesPresent() {
        leafNode.value = new Attributes();
        assertTrue("hasAttributes() should return true when attributes are present", leafNode.hasAttributes());
    }

    @Test
    public void testAttributes_shouldReturnEmptyAttributesWhenNoAttributes() {
        assertTrue("attributes() should return an empty Attributes object when no attributes are present", leafNode.attributes().isEmpty());
    }

    @Test
    public void testAttributes_shouldReturnCorrectAttributesWhenAttributesPresent() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;
        assertEquals("attributes() should return the correct Attributes object when attributes are present", attributes, leafNode.attributes());
    }

    @Test
    public void testAttributes_shouldCreateAttributesIfNotPresent() {
        assertNull("value should be null initially", leafNode.value);
        leafNode.ensureAttributes();
        assertNotNull("value should not be null after ensureAttributes()", leafNode.value);
        assertTrue("value should be an instance of Attributes after ensureAttributes()", leafNode.value instanceof Attributes);
    }

    @Test
    public void testCoreValue_shouldReturnAttributeValueWhenNoAttributes() {
        leafNode.value = "value";
        assertEquals("coreValue() should return the correct attribute value when no attributes are present", "value", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_shouldReturnAttributeValueWhenAttributesPresent() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;
        assertEquals("coreValue() should return the correct attribute value when attributes are present", "value", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_shouldSetAttributeValueWhenNoAttributes() {
        leafNode.coreValue("value");
        assertEquals("coreValue() should set the correct attribute value when no attributes are present", "value", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_shouldSetAttributeValueWhenAttributesPresent() {
        leafNode.value = new Attributes();
        leafNode.coreValue("value");
        assertEquals("coreValue() should set the correct attribute value when attributes are present", "value", leafNode.coreValue());
    }

    @Test
    public void testAttr_shouldReturnDefaultValueWhenNoAttributes() {
        leafNode.value = "value";
        assertEquals("attr() should return the default value when no attributes are present and key is not nodeName()", "", leafNode.attr("key"));
    }

    @Test
    public void testAttr_shouldReturnAttributeValueWhenNoAttributesAndKeyIsNodeName() {
        leafNode.value = "value";
        assertEquals("attr() should return the attribute value when no attributes are present and key is nodeName()", "value", leafNode.attr(leafNode.nodeName()));
    }

    @Test
    public void testAttr_shouldReturnAttributeValueWhenAttributesPresent() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        leafNode.value = attributes;
        assertEquals("attr() should return the correct attribute value when attributes are present", "value", leafNode.attr("key"));
    }

    @Test
    public void testAttr_shouldSetAttributeValueWhenNoAttributesAndKeyIsNodeName() {
        leafNode.value = "value";
        leafNode.attr(leafNode.nodeName(), "new value");
        assertEquals("attr() should set the correct attribute value when no attributes are present and key is nodeName()", "new value", leafNode.coreValue());
    }

    @Test
    public void testAttr_shouldSetAttributeValueWhenNoAttributesAndKeyIsNotNodeName() {
        leafNode.coreValue("value");
        leafNode.attr("key", "new value");
        assertTrue("hasAttributes() should return true after attr() is called and attributes are created", leafNode.hasAttributes());
        assertEquals("attr() should set the correct attribute value when no attributes are present and key is not nodeName()", "new value", leafNode.attr("key"));
    }

    @Test
    public void testAttr_shouldSetAttributeValueWhenAttributesPresent() {
        leafNode.value = new Attributes();
        leafNode.attr("key", "value");
        assertEquals("attr() should set the correct attribute value when attributes are present", "value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr_shouldReturnFalseWhenNoAttributes() {
        assertFalse("hasAttr() should return false when no attributes are present", leafNode.hasAttr("key"));
    }

    @Test
    public void testHasAttr_shouldReturnTrueWhenAttributesPresent() {
        leafNode.value = new Attributes();
        leafNode.attr("key", "value");
        assertTrue("hasAttr() should return true when attributes are present", leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr_shouldRemoveAttributeWhenAttributesPresent() {
        leafNode.value = new Attributes();
        leafNode.attr("key", "value");
        leafNode.removeAttr("key");
        assertFalse("removeAttr() should remove the attribute when attributes are present", leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl_shouldReturnEmptyStringWhenNoParent() {
        assertEquals("absUrl() should return an empty string when no parent is present", "", leafNode.absUrl("key"));
    }

    @Test
    public void testAbsUrl_shouldDelegateToParentWhenParentPresent() {
        Node parent = new NodeMock();
        parent.baseUri = "http://example.com";
        leafNode.parent = parent;
        assertEquals("absUrl() should delegate to parent when parent is present", "http://example.com/myPath", leafNode.absUrl("myPath"));
    }

    @Test
    public void testBaseUri_shouldReturnEmptyStringWhenNoParent() {
        assertEquals("baseUri() should return an empty string when no parent is present", "", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_shouldDelegateToParentWhenParentPresent() {
        Node parent = new NodeMock();
        parent.baseUri = "http://example.com";
        leafNode.parent = parent;
        assertEquals("baseUri() should delegate to parent when parent is present", "http://example.com", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri_shouldDoNothing() {
        leafNode.doSetBaseUri("http://example.com");
        // No assertions as it is a no-op
    }

    @Test
    public void testChildNodeSize_shouldReturnZero() {
        assertEquals("childNodeSize() should always return zero for LeafNode", 0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes_shouldThrowUnsupportedOperationException() {
        leafNode.ensureChildNodes();
    }
}