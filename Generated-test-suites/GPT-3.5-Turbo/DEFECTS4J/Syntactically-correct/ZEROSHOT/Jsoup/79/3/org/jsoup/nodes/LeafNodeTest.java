
package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {
  
  private LeafNode leafNode;

  @Before
  public void setUp() {
    leafNode = new LeafNode() {
      @Override
      protected void doSetBaseUri(String baseUri) {
        // noop
      }
    };
  }

  @Test
  public void testHasAttributes() {
    assertFalse(leafNode.hasAttributes());
  }

  @Test
  public void testAttributes() {
    assertFalse(leafNode.hasAttributes());
    Attributes attributes = leafNode.attributes();
    assertTrue(leafNode.hasAttributes());
    assertEquals(attributes, leafNode.attributes());
  }

  @Test
  public void testEnsureAttributes() {
    assertFalse(leafNode.hasAttributes());
    leafNode.ensureAttributes();
    assertTrue(leafNode.hasAttributes());
  }

  @Test
  public void testCoreValue() {
    assertNull(leafNode.coreValue());
    leafNode.coreValue("value");
    assertEquals("value", leafNode.coreValue());
  }

  @Test
  public void testAttrWithValue() {
    assertNull(leafNode.attr("key"));
    leafNode.attr("key", "value");
    assertEquals("value", leafNode.attr("key"));
  }

  @Test
  public void testAttrWithoutValue() {
    assertNull(leafNode.attr("key"));
    leafNode.attr("key");
    assertTrue(leafNode.hasAttr("key"));
  }

  @Test
  public void testHasAttr() {
    assertFalse(leafNode.hasAttr("key"));
    leafNode.attr("key", "value");
    assertTrue(leafNode.hasAttr("key"));
  }

  @Test
  public void testRemoveAttr() {
    assertFalse(leafNode.hasAttr("key"));
    leafNode.attr("key", "value");
    assertTrue(leafNode.hasAttr("key"));
    leafNode.removeAttr("key");
    assertFalse(leafNode.hasAttr("key"));
  }

  @Test
  public void testAbsUrl() {
    assertNull(leafNode.absUrl("key"));
    leafNode.attr("key", "value");
    assertNotNull(leafNode.absUrl("key"));
  }

  @Test
  public void testBaseUri() {
    assertEquals("", leafNode.baseUri());
    LeafNode parent = new LeafNode() {
      @Override
      protected void doSetBaseUri(String baseUri) {
        // noop
      }
    };
    parent.appendChild(leafNode);
    assertEquals("", leafNode.baseUri());
    parent.setBaseUri("https://example.com");
    assertEquals("https://example.com", leafNode.baseUri());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testChildNodeSize() {
    leafNode.childNodeSize();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testEnsureChildNodes() {
    leafNode.ensureChildNodes();
  }

}
