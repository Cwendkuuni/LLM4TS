package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Test case from Expert 1
    }

    @Test
    public void testCoreValue() {
        // Test case from Expert 1
    }

    @Test
    public void testCoreValueAssignment() {
        // Test case from Expert 1
    }

    @Test
    public void testAttr() {
        // Test case from Expert 2
    }

    @Test
    public void testAttrAssignment() {
        // Test case from Expert 2
    }

    @Test
    public void testHasAttr() {
        // Test case from Expert 3
    }

    @Test
    public void testRemoveAttr() {
        // Test case from Expert 3
    }

    @Test
    public void testAttrWithEmptyStringValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                nodeName("testName");
                attributes().put("testKey", "");
            }
        };
        assertTrue(leafNode.hasAttr("testKey"));
        assertEquals("", leafNode.attr("testKey"));
    }

    @Test
    public void testAttributesAreImmutable() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                nodeName("testName");
                attributes().put("testKey", "testValue");
            }
        };
        leafNode.attributes().put("testKey", "newValue");
        assertEquals("testValue", leafNode.attr("testKey"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        // Test case for the unsupported operation exception
        LeafNode leafNode = new LeafNode();
        leafNode.ensureChildNodes();
    }

    // Add any other test cases you deem necessary
}