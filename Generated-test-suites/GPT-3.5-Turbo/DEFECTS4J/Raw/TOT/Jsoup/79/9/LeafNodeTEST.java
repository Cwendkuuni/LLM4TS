import org.junit.Test;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParentNode;

public class LeafNodeTest {

    @Test
    public void hasAttributes_validAttributes_returnsTrue() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();

        // Act
        boolean result = leafNode.hasAttributes();

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasAttributes_noAttributes_returnsFalse() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        leafNode.value = null;

        // Act
        boolean result = leafNode.hasAttributes();

        // Assert
        assertFalse(result);
    }

    @Test
    public void attributes_validAttributes_returnsAttributes() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        Attributes expectedAttributes = new Attributes();
        leafNode.value = expectedAttributes;

        // Act
        Attributes result = leafNode.attributes();

        // Assert
        assertEquals(expectedAttributes, result);
    }

    @Test
    public void attributes_noAttributes_createsAndReturnsAttributes() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        leafNode.value = null;

        // Act
        Attributes result = leafNode.attributes();

        // Assert
        assertNotNull(result);
        assertTrue(leafNode.hasAttributes());
        assertEquals(1, result.size());
        assertEquals(leafNode.nodeName(), result.asList().get(0).getKey());
    }

    @Test
    public void coreValue_defaultValue_returnsCorrectCoreValue() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        String expectedValue = "";

        // Act
        String result = leafNode.coreValue();

        // Assert
        assertEquals(expectedValue, result);
    }

    @Test
    public void coreValue_customValue_returnsCorrectCoreValue() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        String expectedValue = "custom";

        // Act
        leafNode.coreValue(expectedValue);
        String result = leafNode.coreValue();

        // Assert
        assertEquals(expectedValue, result);
    }

    @Test
    public void attr_noAttributesAndKeyEqualToNodeName_returnsCoreValue() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        leafNode.value = "coreValue";
        String key = leafNode.nodeName();
        String expectedValue = "coreValue";

        // Act
        String result = leafNode.attr(key);

        // Assert
        assertEquals(expectedValue, result);
    }

    @Test
    public void attr_noAttributesAndKeyNotEqualToNodeName_returnsEmptyString() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        leafNode.value = "coreValue";
        String key = "notNodeName";
        String expectedValue = "";

        // Act
        String result = leafNode.attr(key);

        // Assert
        assertEquals(expectedValue, result);
    }

    @Test
    public void attr_hasAttributes_delegatesToSuperAttr() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        leafNode.value = new Attributes();
        String key = "key";
        String value = "value";
        leafNode.attr(key, value);

        // Act
        String result = leafNode.attr(key);

        // Assert
        assertEquals(value, result);
    }

    @Test
    public void hasAttr_noAttributes_returnsFalse() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        leafNode.value = null;
        String key = "key";

        // Act
        boolean result = leafNode.hasAttr(key);

        // Assert
        assertFalse(result);
    }

    @Test
    public void hasAttr_hasAttributes_returnsResultFromSuperHasAttr() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        leafNode.value = new Attributes();
        String key = "key";
        String value = "value";
        leafNode.attr(key, value);

        // Act
        boolean result = leafNode.hasAttr(key);

        // Assert
        assertTrue(result);
    }

    @Test
    public void removeAttr_noAttributes_returnsNodeWithoutChanges() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        leafNode.value = null;
        String key = "key";
        leafNode.attr(key, "value");

        // Act
        Node result = leafNode.removeAttr(key);

        // Assert
        assertEquals(leafNode, result);
        assertFalse(leafNode.hasAttributes());
        assertFalse(leafNode.hasAttr(key));
    }

    @Test
    public void removeAttr_hasAttributes_returnsNodeWithAttributesRemoved() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        leafNode.value = new Attributes();
        String key = "key";
        String value = "value";
        leafNode.attr(key, value);

        // Act
        Node result = leafNode.removeAttr(key);

        // Assert
        assertEquals(leafNode, result);
        assertTrue(leafNode.hasAttributes());
        assertFalse(leafNode.hasAttr(key));
    }

    @Test
    public void absUrl_hasParent_returnsParentAbsUrl() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        ParentNode parent = new ParentNodeMock();
        leafNode.parentNode = parent;
        parent.absUrl = "http://example.com";
        String key = "key";
        String expectedUrl = parent.absUrl + "/" + key;

        // Act
        String result = leafNode.absUrl(key);

        // Assert
        assertEquals(expectedUrl, result);
    }

    @Test
    public void absUrl_noParent_returnsEmptyString() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        String key = "key";
        String expectedUrl = "";

        // Act
        String result = leafNode.absUrl(key);

        // Assert
        assertEquals(expectedUrl, result);
    }

    @Test
    public void baseUri_hasParent_returnsParentBaseUri() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        ParentNode parent = new ParentNodeMock();
        leafNode.parentNode = parent;
        parent.baseUri = "http://example.com";
        String expectedBaseUri = parent.baseUri;

        // Act
        String result = leafNode.baseUri();

        // Assert
        assertEquals(expectedBaseUri, result);
    }

    @Test
    public void baseUri_noParent_returnsEmptyString() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();
        String expectedBaseUri = "";

        // Act
        String result = leafNode.baseUri();

        // Assert
        assertEquals(expectedBaseUri, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void ensureChildNodes_leafNode_throwsException() {
        // Arrange
        LeafNode leafNode = new LeafNodeMock();

        // Act
        leafNode.ensureChildNodes();

        // Assert (Exception expected)
    }
    
    class LeafNodeMock extends LeafNode {
        @Override
        protected String nodeName() {
            return "mock";
        }
    }
    
    class ParentNodeMock extends ParentNode {
        String absUrl;
        String baseUri;
        
        @Override
        protected String nodeName() {
            return "mock";
        }
        
        @Override
        protected void updateAbsUrl(String key, String value) {
            // noop
        }
        
        @Override
        protected void updateBaseUri(String baseUri) {
            // noop
        }
    }

}