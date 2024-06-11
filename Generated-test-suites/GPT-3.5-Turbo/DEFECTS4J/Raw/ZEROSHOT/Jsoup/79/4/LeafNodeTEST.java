package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {
  
  private LeafNode leafNode;
  
  @Before
  public void setup() {
    leafNode = new LeafNode() {
      @Override
      public String nodeName() {
        return "leaf-node";
      }
    };
  }
  
  @Test
  public void testHasAttributes() {
    Assert.assertFalse(leafNode.hasAttributes());
  }
  
  @Test
  public void testAttributes() {
    Assert.assertNull(leafNode.attributes());
  }
  
  @Test
  public void testEnsureAttributes() {
    leafNode.ensureAttributes();
    Assert.assertTrue(leafNode.hasAttributes());
    Assert.assertNotNull(leafNode.attributes());
  }
  
  @Test
  public void testCoreValue() {
    Assert.assertNull(leafNode.coreValue());
    
    String value = "testValue";
    leafNode.coreValue(value);
    Assert.assertEquals(value, leafNode.coreValue());
    
    value = "newValue";
    leafNode.coreValue(value);
    Assert.assertEquals(value, leafNode.coreValue());
  }
  
  @Test
  public void testAttr() {
    String key = "key";
    String value = "value";
    
    Assert.assertNull(leafNode.attr(key));
    
    leafNode.attr(key, value);
    Assert.assertEquals(value, leafNode.attr(key));
    
    value = "newValue";
    leafNode.attr(key, value);
    Assert.assertEquals(value, leafNode.attr(key));
  }
  
  @Test
  public void testHasAttr() {
    String key = "key";
    String value = "value";
    
    Assert.assertFalse(leafNode.hasAttr(key));
    
    leafNode.attr(key, value);
    Assert.assertTrue(leafNode.hasAttr(key));
  }
  
  @Test
  public void testRemoveAttr() {
    String key = "key";
    String value = "value";
    
    leafNode.attr(key, value);
    
    leafNode.removeAttr(key);
    Assert.assertFalse(leafNode.hasAttr(key));
  }
  
  @Test
  public void testAbsUrl() {
    String key = "key";
    String value = "https://www.example.com";
    
    leafNode.attr(key, value);
    
    Assert.assertEquals(value, leafNode.absUrl(key));
  }
  
  @Test
  public void testBaseUri() {
    Assert.assertEquals("", leafNode.baseUri());
  }
  
  @Test(expected=UnsupportedOperationException.class)
  public void testEnsureChildNodes() {
    leafNode.ensureChildNodes();
  }
}