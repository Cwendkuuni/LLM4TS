package org.jsoup.nodes;

import org.jsoup.helper.Validate;

import java.util.List;

import org.junit.*;

public class LeafNodeTest {
    
    private LeafNode leafNode;
    
    @Before
    public void setUp() {
        leafNode = new LeafNodeImplementation();
    }
    
    @Test
    public void testHasAttributes_true() {
        // Given
        leafNode.value = new Attributes();
        
        // When
        boolean result = leafNode.hasAttributes();
        
        // Then
        Assert.assertTrue(result);
    }
    
    @Test
    public void testHasAttributes_false() {
        // Given
        leafNode.value = "value";
        
        // When
        boolean result = leafNode.hasAttributes();
        
        // Then
        Assert.assertFalse(result);
    }
    
    @Test
    public void testAttributes() {
        // Given
        leafNode.value = new Attributes();
        
        // When
        Attributes result = leafNode.attributes();
        
        // Then
        Assert.assertNotNull(result);
        Assert.assertEquals(Attributes.class, result.getClass());
        Assert.assertEquals(leafNode.value, result);
    }
    
    @Test
    public void testEnsureAttributes_hasAttributes() {
        // Given
        leafNode.value = new Attributes();
        
        // When
        leafNode.ensureAttributes();
        
        // Then
        Assert.assertEquals(Attributes.class, leafNode.value.getClass());
    }
    
    @Test
    public void testEnsureAttributes_noAttributes() {
        // Given
        leafNode.value = "value";
        
        // When
        leafNode.ensureAttributes();
        
        // Then
        Assert.assertEquals(Attributes.class, leafNode.value.getClass());
        Assert.assertTrue(leafNode.value.containsKey(leafNode.nodeName()));
        Assert.assertEquals("value", leafNode.value.get(leafNode.nodeName()));
    }
    
    @Test
    public void testCoreValue() {
        // Given
        leafNode.attr("nodeName", "value");
        
        // When
        String result = leafNode.coreValue();
        
        // Then
        Assert.assertEquals("value", result);
    }
    
    @Test
    public void testSetCoreValue() {
        // Given
        String value = "value";
        
        // When
        leafNode.coreValue(value);
        
        // Then
        Assert.assertEquals(value, leafNode.attr(leafNode.nodeName()));
    }
    
    @Test
    public void testAttr_hasAttributes() {
        // Given
        leafNode.value = new Attributes();
        leafNode.value.put("key", "value");
        String key = "key";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("value", result);
    }
    
    @Test
    public void testAttr_noAttributes_keyEqualsNodeName() {
        // Given
        leafNode.nodeName = "nodeName";
        leafNode.value = "value";
        String key = "nodeName";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("value", result);
    }
    
    @Test
    public void testAttr_noAttributes_keyNotEqualsNodeName() {
        // Given
        leafNode.nodeName = "nodeName";
        leafNode.value = "value";
        String key = "key";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testAttr_valueIsNull() {
        // Given
        leafNode.value = null;
        String key = "key";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertNull(result);
    }
    
    @Test
    public void testAttr_valueIsNull_keyEqualsNodeName() {
        // Given
        leafNode.value = null;
        leafNode.nodeName = "nodeName";
        String key = "nodeName";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertNull(result);
    }
    
    @Test
    public void testAttr_valueIsNull_keyNotEqualsNodeName() {
        // Given
        leafNode.value = null;
        leafNode.nodeName = "nodeName";
        String key = "key";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testAttr_valueIsString() {
        // Given
        leafNode.value = "value";
        String key = "key";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testAttr_valueIsString_keyEqualsNodeName() {
        // Given
        leafNode.value = "value";
        leafNode.nodeName = "nodeName";
        String key = "nodeName";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("value", result);
    }
    
    @Test
    public void testAttr_valueIsString_keyNotEqualsNodeName() {
        // Given
        leafNode.value = "value";
        leafNode.nodeName = "nodeName";
        String key = "key";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testAttr_valueIsAttributes() {
        // Given
        leafNode.value = new Attributes();
        leafNode.value.put("key", "value");
        String key = "key";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("value", result);
    }
    
    @Test
    public void testAttr_valueIsAttributes_keyEqualsNodeName() {
        // Given
        leafNode.value = new Attributes();
        leafNode.value.put("nodeName", "value");
        leafNode.nodeName = "nodeName";
        String key = "nodeName";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("value", result);
    }
    
    @Test
    public void testAttr_valueIsAttributes_keyNotEqualsNodeName() {
        // Given
        leafNode.value = new Attributes();
        leafNode.value.put("key", "value");
        leafNode.nodeName = "nodeName";
        String key = "key";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("value", result);
    }
    
    @Test
    public void testAttr_valueIsAttributes_noValueForKey() {
        // Given
        leafNode.value = new Attributes();
        leafNode.value.put("key", "value");
        String key = "anotherKey";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testAttr_valueIsStringAttributes() {
        // Given
        leafNode.value = "value";
        leafNode.value = new Attributes();
        leafNode.value.put("key", "value");
        String key = "key";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testAttr_valueIsStringAttributes_keyEqualsNodeName() {
        // Given
        leafNode.value = "value";
        leafNode.nodeName = "nodeName";
        leafNode.value = new Attributes();
        leafNode.value.put("nodeName", "value");
        String key = "nodeName";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("value", result);
    }
    
    @Test
    public void testAttr_valueIsStringAttributes_keyNotEqualsNodeName() {
        // Given
        leafNode.value = "value";
        leafNode.nodeName = "nodeName";
        leafNode.value = new Attributes();
        leafNode.value.put("key", "value");
        String key = "key";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testAttr_valueIsStringAttributes_noValueForKey() {
        // Given
        leafNode.value = "value";
        leafNode.value = new Attributes();
        leafNode.value.put("key", "value");
        String key = "anotherKey";
        
        // When
        String result = leafNode.attr(key);
        
        // Then
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testHasAttr_true() {
        // Given
        leafNode.value = new Attributes();
        leafNode.value.put("key", "value");
        String key = "key";
        
        // When
        boolean result = leafNode.hasAttr(key);
        
        // Then
        Assert.assertTrue(result);
    }
    
    @Test
    public void testHasAttr_false() {
        // Given
        leafNode.value = new Attributes();
        String key = "key";
        
        // When
        boolean result = leafNode.hasAttr(key);
        
        // Then
        Assert.assertFalse(result);
    }
    
    @Test
    public void testRemoveAttr() {
        // Given
        leafNode.value = new Attributes();
        leafNode.value.put("key", "value");
        String key = "key";
        
        // When
        Node result = leafNode.removeAttr(key);
        
        // Then
        Assert.assertNotNull(result);
        Assert.assertEquals(LeafNodeImplementation.class, result.getClass());
        Assert.assertEquals("value", result.attr(key));
    }
    
    @Test
    public void testAbsUrl() {
        // Given
        leafNode.value = new Attributes();
        leafNode.value.put("key", "value");
        String key = "key";
        
        // When
        String result = leafNode.absUrl(key);
        
        // Then
        Assert.assertNotNull(result);
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testBaseUri_hasParent() {
        // Given
        Node parent = new NodeImplementation();
        parent.value = "http://example.com";
        leafNode.parentNode = parent;
        
        // When
        String result = leafNode.baseUri();
        
        // Then
        Assert.assertEquals("http://example.com", result);
    }
    
    @Test
    public void testBaseUri_noParent() {
        // Given
        leafNode.parentNode = null;
        
        // When
        String result = leafNode.baseUri();
        
        // Then
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testDoSetBaseUri() {
        // Given
        String baseUri = "http://example.com";
        
        // When
        leafNode.doSetBaseUri(baseUri);
        
        // Then
        Assert.assertEquals("", leafNode.baseUri());
    }
    
    @Test
    public void testChildNodeSize() {
        // When
        int result = leafNode.childNodeSize();
        
        // Then
        Assert.assertEquals(0, result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        // When
        leafNode.ensureChildNodes();
    }
    
    private class LeafNodeImplementation extends LeafNode {
        private String nodeName = "nodeName";
        
        @Override
        protected void doSetBaseUri(String baseUri) {
            // Do nothing
        }
        
        @Override
        protected List<Node> ensureChildNodes() {
            throw new UnsupportedOperationException("Leaf Nodes do not have child nodes.");
        }
    }
}