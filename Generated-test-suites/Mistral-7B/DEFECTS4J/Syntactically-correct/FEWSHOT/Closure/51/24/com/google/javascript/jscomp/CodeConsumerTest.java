package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('z'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertTrue(CodeConsumer.isWordChar('9'));

    assertFalse(CodeConsumer.isWordChar(' '));
    assertFalse(CodeConsumer.isWordChar('.'));
    assertFalse(CodeConsumer.isWordChar('('));
    assertFalse(CodeConsumer.isWordChar(')'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());

    consumer = new AnotherStubCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.breakAfterBlockFor(null, false));
    assertTrue(consumer.breakAfterBlockFor(null, true));

    // We don't have access to the actual implementation of the method,
    // so we can't test it comprehensively.
  }

  static class StubCodeConsumer extends CodeConsumer {
    // Empty implementation for testing
  }

  static class AnotherStubCodeConsumer extends CodeConsumer {
    // Empty implementation for testing
  }
}