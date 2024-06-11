import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import static org.jsoup.nodes.Node.EMPTY_STRING; // Assuming EMPTY_STRING is defined in Node class
import static org.jsoup.nodes.Node.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        assertFalse(node.hasAttributes());

        node.coreValue("testValue");
        node.attr("testAttr", "testValue");
        assertTrue(node.hasAttributes());
    }

    @Test
    public void testGetAttributes() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        node.coreValue("testValue");
        node.attr("testAttr1", "testValue1");
        node.attr("testAttr2", "testValue2");

        Attributes attributes = node.attributes();
        assertNotNull(attributes);
        assertEquals("testValue", attributes.get("testAttr1"));
        assertEquals("testValue2", attributes.get("testAttr2"));
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        node.coreValue("testValue");

        node.ensureAttributes();
        assertTrue(node.hasAttributes());

        node.ensureAttributes(); // already has attributes, should not change value

        Attributes attributes = node.attributes();
        assertEquals("testValue", attributes.get("testTag"));
    }

    @Test
    public void testCoreValue() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        node.coreValue("testValue");
        assertEquals("testValue", node.coreValue());

        node.coreValue("newTestValue");
        assertEquals("newTestValue", node.coreValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreValueNull() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        node.coreValue(null);
    }

    @Test
    public void testAttr() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        node.attr("testAttr", "testValue");
        assertEquals("testValue", node.attr("testAttr"));

        node.attr("testAttr", "newTestValue");
        assertEquals("newTestValue", node.attr("testAttr"));

        node.attr("nonexistentAttr", "testValue"); // should not change existing value
        assertEquals("testValue", node.attr("testAttr"));

        // Test with empty string value
        node.attr("testAttr", EMPTY_STRING);
        assertEquals(EMPTY_STRING, node.attr("testAttr"));

        // Test with null key
        node.coreValue("testTag");
        assertNull(node.attr(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttrNullValue() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        node.attr("testAttr", null);
    }

    @Test
    public void testRemoveAttr() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        node.attr("testAttr1", "testValue1");
        node.attr("testAttr2", "testValue2");

        node.removeAttr("testAttr1");
        assertNull(node.attr("testAttr1"));

        node.removeAttr("testAttr2");
        assertNull(node.attr("testAttr2"));

        node.removeAttr("nonexistentAttr");
        assertNull(node.attr("nonexistentAttr"));
    }

    @Test
    public void testAttrGetWithParent() {
        Element parent = new Elements.SingleTagElement("parentTag");
        LeafNode child = new Elements.SingleTagElement("testTag");
        parent.appendChild(child);

        child.attr("testAttr", "testValue");
        assertEquals("testValue", child.attr("testAttr"));
        assertEquals("testValue", parent.attr("testTag/testAttr"));
    }

    @Test
    public void testAttrGetWithNoParent() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        node.attr("testAttr", "testValue");
        assertEquals("testValue", node.attr("testAttr"));
        assertEquals(EMPTY_STRING, node.attr("nonexistentAttr"));
    }

    @Test
    public void testBaseUri() {
        Element parent = new Elements.SingleTagElement("parentTag");
        LeafNode node = new Elements.SingleTagElement("testTag");
        parent.appendChild(node);

        String baseUri = "http://test.com";
        parent.baseUri(baseUri);
        assertEquals(baseUri, node.baseUri());

        node = new Elements.SingleTagElement("testTag");
        assertEquals(EMPTY_STRING, node.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        LeafNode node = new Elements.SingleTagElement("testTag");
        assertEquals(0, node.childNodeSize());
    }
}