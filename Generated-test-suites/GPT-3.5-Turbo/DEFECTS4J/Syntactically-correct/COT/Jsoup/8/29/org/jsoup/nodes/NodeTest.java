import org.junit.Test;

public class NodeTest {

  @Test
  public void testNodeName() {
    // Create a Node instance
    Node node = new Node() {
      @Override
      public String nodeName() {
        return "testNode";
      }
      @Override
      void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
      @Override
      void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
    };

    // Test the nodeName() method
    assertEquals("testNode", node.nodeName());
  }

  @Test
  public void testAttr() {
    // Create a Node instance
    Node node = new Node() {
      @Override
      public String nodeName() {
        return "";
      }
      @Override
      void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
      @Override
      void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
    };

    // Set an attribute
    node.attr("testAttr", "testValue");

    // Test the attr() method
    assertEquals("testValue", node.attr("testAttr"));
  }

  @Test
  public void testHasAttr() {
    // Create a Node instance
    Node node = new Node() {
      @Override
      public String nodeName() {
        return "";
      }
      @Override
      void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
      @Override
      void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
    };

    // Set an attribute
    node.attr("testAttr", "testValue");

    // Test the hasAttr() method
    assertTrue(node.hasAttr("testAttr"));
    assertFalse(node.hasAttr("otherAttr"));
  }

  @Test
  public void testRemoveAttr() {
    // Create a Node instance
    Node node = new Node() {
      @Override
      public String nodeName() {
        return "";
      }
      @Override
      void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
      @Override
      void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
    };

    // Set an attribute
    node.attr("testAttr", "testValue");

    // Remove the attribute
    node.removeAttr("testAttr");

    // Test the hasAttr() method
    assertFalse(node.hasAttr("testAttr"));
  }

  @Test
  public void testBaseUri() {
    // Create a Node instance
    Node node = new Node() {
      @Override
      public String nodeName() {
        return "";
      }
      @Override
      void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
      @Override
      void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
    };

    // Set the base URI
    node.setBaseUri("http://example.com");

    // Test the baseUri() method
    assertEquals("http://example.com", node.baseUri());
  }
  
}