package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {
  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());

    consumer = new OtherDummyCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();

    Node node = Node.newTextNode("if (true) { x = 1; }");
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertTrue(consumer.breakAfterBlockFor(node, false));

    node = Node.newTextNode("for (int i = 0; i < 10; i++) { x = i; }");
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  static class DummyCodeConsumer extends CodeConsumer {
    // Empty implementation of all methods
  }

  static class OtherDummyCodeConsumer extends CodeConsumer {
    // Implementation of shouldPreserveExtraBlocks() as false
  }
}