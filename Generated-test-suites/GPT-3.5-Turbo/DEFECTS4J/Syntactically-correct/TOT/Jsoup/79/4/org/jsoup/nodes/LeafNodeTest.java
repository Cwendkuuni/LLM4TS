import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LeafNodeTest {

    @Mock
    private Node parentNode;

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNodeMock();
        leafNode.setBaseUri("http://example.com");
        leafNode.attr("attribute1", "value1");
        leafNode.attr("attribute2", "value2");
    }

    // Test case for hasAttributes() method

    @Test
    public void testHasAttributesWhenAttributesExist() {
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributesWhenNoAttributesExist() {
        // Remove all attributes from the leafNode
        leafNode.removeAttr("attribute1");
        leafNode.removeAttr("attribute2");

        assertFalse(leafNode.hasAttributes());
    }

    // Test cases for attributes() method

    @Test
    public void testAttributesWhenHasAttributes() {
        Attributes attributes = leafNode.attributes();
        assertEquals("value1", attributes.get("attribute1"));
        assertEquals("value2", attributes.get("attribute2"));
    }

    @Test
    public void testAttributesWhenNoAttributes() {
        // Remove all attributes from the leafNode
        leafNode.removeAttr("attribute1");
        leafNode.removeAttr("attribute2");

        Attributes attributes = leafNode.attributes();
        assertEquals(0, attributes.size());
    }

    // Test cases for attr(String key) method

    @Test
    public void testAttrWhenAttributeExists() {
        String attributeValue = leafNode.attr("attribute1");

        assertEquals("value1", attributeValue);
    }

    @Test
    public void testAttrWhenAttributeDoesNotExist() {
        String attributeValue = leafNode.attr("nonExistingAttribute");

        assertEquals("", attributeValue);
    }

    // Test cases for attr(String key, String value) method

    @Test
    public void testSetAttrWhenAttributeExists() {
        leafNode.attr("attribute1", "newValue");

        String attributeValue = leafNode.attr("attribute1");
        assertEquals("newValue", attributeValue);
    }

    @Test
    public void testSetAttrWhenAttributeDoesNotExist() {
        leafNode.attr("newAttribute", "value");

        String attributeValue = leafNode.attr("newAttribute");
        assertEquals("value", attributeValue);
    }

    // Test cases for hasAttr(String key) method

    @Test
    public void testHasAttrWhenAttributeExists() {
        assertTrue(leafNode.hasAttr("attribute1"));
    }

    @Test
    public void testHasAttrWhenAttributeDoesNotExist() {
        assertFalse(leafNode.hasAttr("nonExistingAttribute"));
    }

    // Test cases for removeAttr(String key) method

    @Test
    public void testRemoveAttrWhenAttributeExists() {
        leafNode.removeAttr("attribute1");

        assertFalse(leafNode.hasAttr("attribute1"));
    }

    @Test
    public void testRemoveAttrWhenAttributeDoesNotExist() {
        leafNode.removeAttr("nonExistingAttribute");

        assertFalse(leafNode.hasAttr("nonExistingAttribute"));
    }

    // Test cases for absUrl(String key) method

    @Test
    public void testAbsUrlWhenHasParent() {
        given(parentNode.baseUri()).willReturn("http://parent.com");

        String absUrl = leafNode.absUrl("attribute1");

        assertEquals("http://parent.com", absUrl);
    }

    @Test
    public void testAbsUrlWhenNoParent() {
        leafNode = new LeafNodeMockWithoutParent();

        String absUrl = leafNode.absUrl("attribute1");

        assertEquals("", absUrl);
    }

    // Test case for baseUri() method

    @Test
    public void testBaseUriWhenHasParent() {
        given(parentNode.baseUri()).willReturn("http://parent.com");

        String baseUri = leafNode.baseUri();

        assertEquals("http://parent.com", baseUri);
    }

    @Test
    public void testBaseUriWhenNoParent() {
        leafNode = new LeafNodeMockWithoutParent();

        String baseUri = leafNode.baseUri();

        assertEquals("", baseUri);
    }

    // Test cases for childNodeSize() method

    @Test
    public void testChildNodeSize() {
        int size = leafNode.childNodeSize();

        assertEquals(0, size);
    }

    // Test case for ensureChildNodes() method

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }

    // Mock class for LeafNode with parent node
    private class LeafNodeMock extends LeafNode {
        @Override
        public boolean hasParent() {
            return true;
        }

        @Override
        public Node parent() {
            return parentNode;
        }

        @Override
        public String nodeName() {
            return "leafNode";
        }
    }

    // Mock class for LeafNode without parent node
    private class LeafNodeMockWithoutParent extends LeafNode {
        @Override
        public boolean hasParent() {
            return false;
        }

        @Override
        public String nodeName() {
            return "leafNode";
        }
    }
}