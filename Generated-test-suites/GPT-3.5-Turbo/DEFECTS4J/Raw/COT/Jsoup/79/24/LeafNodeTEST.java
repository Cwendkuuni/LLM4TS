
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Given
        LeafNode leafNode = new TestLeafNode();

        // When
        boolean hasAttributes = leafNode.hasAttributes();

        // Then
        assertFalse(hasAttributes);
    }

    @Test
    public void testAttributes() {
        // Given
        LeafNode leafNode = new TestLeafNode();

        // When
        Attributes attributes = leafNode.attributes();

        // Then
        assertNotNull(attributes);
    }
    
    @Test
    public void testCoreValue() {
        // Given
        LeafNode leafNode = new TestLeafNode();

        // When
        String coreValue = leafNode.coreValue();

        // Then
        assertNull(coreValue);
    }

    @Test
    public void testCoreValueWithSetCoreValue() {
        // Given
        LeafNode leafNode = new TestLeafNode();
        String expectedValue = "Test Value";

        // When
        leafNode.coreValue(expectedValue);
        String coreValue = leafNode.coreValue();

        // Then
        assertEquals(expectedValue, coreValue);
    }

    @Test
    public void testAttr() {
        // Given
        LeafNode leafNode = new TestLeafNode();
        String key = "testKey";
        String value = "testValue";

        // When
        leafNode.attr(key, value);
        String attrValue = leafNode.attr(key);

        // Then
        assertEquals(value, attrValue);
    }

    @Test
    public void testAttrWithoutAttributes() {
        // Given
        LeafNode leafNode = new TestLeafNode();
        String key = "testKey";

        // When
        String attrValue = leafNode.attr(key);

        // Then
        assertEquals(key, attrValue);
    }

    @Test
    public void testHasAttr() {
        // Given
        LeafNode leafNode = new TestLeafNode();
        String key = "testKey";
        String value = "testValue";
        leafNode.attr(key, value);

        // When
        boolean hasAttr = leafNode.hasAttr(key);

        // Then
        assertTrue(hasAttr);
    }

    @Test
    public void testRemoveAttr() {
        // Given
        LeafNode leafNode = new TestLeafNode();
        String key = "testKey";
        String value = "testValue";
        leafNode.attr(key, value);

        // When
        Node removedNode = leafNode.removeAttr(key);

        // Then
        assertNotNull(removedNode);
        assertFalse(leafNode.hasAttr(key));
    }

    @Test
    public void testAbsUrl() {
        // Given
        LeafNode leafNode = new TestLeafNode();
        String key = "testKey";
        String value = "http://www.example.com";
        leafNode.attr(key, value);

        // When
        String absUrl = leafNode.absUrl(key);

        // Then
        assertEquals(value, absUrl);
    }

    @Test
    public void testBaseUriWithParent() {
        // Given
        LeafNode leafNode = new TestLeafNode();
        TestNode parent = new TestNode();
        parent.setBaseUri("http://www.example.com");
        leafNode.setParentNode(parent);

        // When
        String baseUri = leafNode.baseUri();

        // Then
        assertEquals("http://www.example.com", baseUri);
    }

    @Test
    public void testBaseUriWithoutParent() {
        // Given
        LeafNode leafNode = new TestLeafNode();

        // When
        String baseUri = leafNode.baseUri();

        // Then
        assertEquals("", baseUri);
    }

    @Test
    public void testDoSetBaseUri() {
        // Given
        LeafNode leafNode = new TestLeafNode();
        String baseUri = "http://www.example.com";

        // When
        leafNode.doSetBaseUri(baseUri);

        // Then
        // No assertion, only testing if the method executes without errors
    }

    @Test
    public void testChildNodeSize() {
        // Given
        LeafNode leafNode = new TestLeafNode();

        // When
        int childNodeSize = leafNode.childNodeSize();

        // Then
        assertEquals(0, childNodeSize);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        // Given
        LeafNode leafNode = new TestLeafNode();

        // When
        leafNode.ensureChildNodes();

        // Then
        // Expecting UnsupportedOperationException to be thrown
    }

    private static class TestLeafNode extends LeafNode {
        // Empty implementation for testing purposes
    }

    private static class TestNode extends Node {
        // Empty implementation for testing purposes
    }

}
