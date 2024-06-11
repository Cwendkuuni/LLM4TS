
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNodeImpl();
    }

    @Test
    public void testHasAttributes_returnsFalseWhenNoAttributesAreSet() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_returnsTrueWhenAttributesAreSet() {
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes_returnsEmptyAttributesWhenNoAttributesAreSet() {
        assertFalse(leafNode.hasAttributes());
        assertNull(leafNode.attributes());
    }

    @Test
    public void testAttributes_returnsAttributesWhenAttributesAreSet() {
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttributes());

        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testCoreValue_returnsNullWhenNoAttributeIsSet() {
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testCoreValue_returnsAttributeValueWhenAttributeIsSet() {
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_updatesAttributeValueWhenNewValueIsSet() {
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.coreValue());

        leafNode.coreValue("new value");
        assertEquals("new value", leafNode.coreValue());
    }

    @Test
    public void testAttr_returnsAttributeValueWhenAttributeIsSet() {
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testAttr_returnsNullWhenNoAttributeIsSet() {
        assertNull(leafNode.attr("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttr_throwsExceptionWhenNullKeyIsProvided() {
        leafNode.attr(null);
    }

    @Test
    public void testAttr_returnsValueWhenKeyMatchesNodeNameAndNoAttributesAreSet() {
        assertEquals("value", leafNode.attr("value"));
    }

    @Test
    public void testAttr_returnsEmptyStringWhenKeyDoesNotMatchNodeNameAndNoAttributesAreSet() {
        assertEquals("", leafNode.attr("key"));
    }

    @Test
    public void testAttr_returnsValueWhenKeyMatchesNodeNameAndAttributesAreSet() {
        leafNode.attributes().put("value", "attribute value");
        assertEquals("attribute value", leafNode.attr("value"));
    }

    @Test
    public void testAttr_returnsAttributeValueWhenKeyDoesNotMatchNodeNameAndAttributesAreSet() {
        leafNode.attributes().put("key", "attribute value");
        assertEquals("attribute value", leafNode.attr("key"));
    }

    @Test
    public void testAttr_returnsNullWhenKeyDoesNotMatchNodeNameAndNoAttributesAreSet() {
        assertNull(leafNode.attr("non-matching"));
    }

    @Test
    public void testAttr_returnsNullWhenKeyDoesNotMatchNodeNameAndAttributesAreSet() {
        leafNode.attributes().put("key", "attribute value");
        assertNull(leafNode.attr("non-matching"));
    }

    @Test
    public void testAttr_returnsAbsoluteUrlWhenAttributesAreSet() {
        leafNode.attributes().put("href", "/path/to/page");
        leafNode.setBaseUri("http://www.example.com");

        assertEquals("http://www.example.com/path/to/page", leafNode.absUrl("href"));
    }

    @Test
    public void testBaseUri_returnsEmptyStringWhenNoParentIsSet() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_returnsParentBaseUriWhenParentIsSet() {
        Node parent = new Element("div");
        parent.setBaseUri("http://www.example.com");

        leafNode.setParentNode(parent);
        assertEquals("http://www.example.com", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri_doesNotSetBaseUri() {
        leafNode.doSetBaseUri("http://www.example.com");

        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize_returnsZero() {
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes_throwsException() {
        leafNode.ensureChildNodes();
    }

    private static class LeafNodeImpl extends LeafNode {
        @Override
        protected String nodeName() {
            return "test";
        }
    }
}
