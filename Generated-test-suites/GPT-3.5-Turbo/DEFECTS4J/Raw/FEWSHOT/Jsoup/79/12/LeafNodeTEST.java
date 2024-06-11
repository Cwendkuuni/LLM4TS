package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        boolean result = leafNode.hasAttributes();

        // Verify the result
        assertEquals(false, result);
    }
    
    @Test
    public void testAttributes() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        Object result = leafNode.attributes();

        // Verify the result
        assertEquals(null, result);
    }
    
    @Test
    public void testEnsureAttributes() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        leafNode.ensureAttributes();
        
        // Verify the result
        assertEquals("{attributes={}}", leafNode);
    }
    
    @Test
    public void testCoreValue() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        String result = leafNode.coreValue();

        // Verify the result
        assertEquals("", result);
    }
    
    @Test
    public void testCoreValueWithString() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        leafNode.coreValue("test");

        // Verify the result
        assertEquals("{attributes={}, value=test}", leafNode);
    }
    
    @Test
    public void testAttr() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        String result = leafNode.attr("key");

        // Verify the result
        assertEquals("", result);
    }
    
    @Test
    public void testAttrWithValue() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        leafNode.attr("key", "value");

        // Verify the result
        assertEquals("{attributes={key=value}}", leafNode);
    }

    @Test
    public void testHasAttr() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        boolean result = leafNode.hasAttr("key");

        // Verify the result
        assertEquals(false, result);
    }
    
    @Test
    public void testRemoveAttr() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        leafNode.removeAttr("key");

        // Verify the result
        assertEquals("{attributes={}}", leafNode);
    }
    
    @Test
    public void testAbsUrl() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();
        
        // Execute the test
        String result = leafNode.absUrl("key");

        // Verify the result
        assertEquals("", result);
    }
    
    @Test
    public void testBaseUri() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();
        
        // Execute the test
        String result = leafNode.baseUri();

        // Verify the result
        assertEquals("", result);
    }
    
    @Test
    public void testDoSetBaseUri() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        leafNode.doSetBaseUri("baseUri");

        // Verify the result
        assertEquals("{attributes={}, baseUri=baseUri}", leafNode);
    }
    
    @Test
    public void testChildNodeSize() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        int result = leafNode.childNodeSize();

        // Verify the result
        assertEquals(0, result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        // Set up test data
        LeafNode leafNode = new MockLeafNode();

        // Execute the test
        leafNode.ensureChildNodes();
    }
}

class MockLeafNode extends LeafNode {
    @Override
    public String nodeName() {
        return "mockNode";
    } 
}