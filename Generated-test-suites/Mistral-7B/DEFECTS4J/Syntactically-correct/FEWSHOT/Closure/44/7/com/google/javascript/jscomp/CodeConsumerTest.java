package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('1'));
    assertTrue(CodeConsumer.isWordChar('Z'));
    assertFalse(CodeConsumer.isWordChar(' '));
    assertFalse(CodeConsumer.isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  static class TestCodeConsumer extends CodeConsumer {
    @Override
    void startSourceMapping(Node node) {
      // Empty implementation
    }

    @Override
    void endSourceMapping(Node node) {
      // Empty implementation
    }

    @Override
    char getLastChar() {
      return '\u0000';
    }

    @Override
    void add(String newcode) {
      // Empty implementation
    }

    @Override
    void appendOp(String op, boolean binOp) {
      // Empty implementation
    }

    @Override
    void addNumber(double x) {
      // Empty implementation
    }
  }
}

abstract class AbstractCodeConsumerTest extends CodeConsumerTest {
  abstract CodeConsumer getCodeConsumerUnderTest();

  @Test
  public void testAppend() {
    String str = "test";
    getCodeConsumerUnderTest().append(str);
    assertEquals(getCodeConsumerUnderTest().getLastChar(), str.charAt(str.length() - 1));
  }

  @Test
  public void testMaybeEndStatement() {
    getCodeConsumerUnderTest().append("foo");
    getCodeConsumerUnderTest().maybeEndStatement();
    assertTrue(getCodeConsumerUnderTest().statementStarted);
    assertTrue(getCodeConsumerUnderTest().statementNeedsEnded);

    getCodeConsumerUnderTest().append(";");
    getCodeConsumerUnderTest().maybeEndStatement();
    assertFalse(getCodeConsumerUnderTest().statementNeedsEnded);
  }

  @Test
  public void testBeginBlock() {
    getCodeConsumerUnderTest().beginBlock();
    assertTrue(getCodeConsumerUnderTest().statementNeedsEnded);
  }

  @Test
  public void testEndBlock() {
    getCodeConsumerUnderTest().beginBlock();
    getCodeConsumerUnderTest().append("foo");
    getCodeConsumerUnderTest().endBlock();
    assertFalse(getCodeConsumerUnderTest().statementNeedsEnded);
  }

  @Test
  public void testListSeparator() {
    getCodeConsumerUnderTest().append("foo");
    getCodeConsumerUnderTest().listSeparator();
    getCodeConsumerUnderTest().append("bar");
    assertEquals("foo,", getCodeConsumerUnderTest().getLastCode());
  }

  @Test
  public void testEndFunction() {
    getCodeConsumerUnderTest().beginBlock();
    getCodeConsumerUnderTest().append("function foo() {");
    getCodeConsumerUnderTest().append("}");
    getCodeConsumerUnderTest().endFunction();
    assertEquals("function foo() {}\n}", getCodeConsumerUnderTest().getLastCode());
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    getCodeConsumerUnderTest().startSourceMapping(node);
    assertEquals(node, getCodeConsumerUnderTest().getLastNode());
  }

  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    getCodeConsumerUnderTest().startSourceMapping(node);
    getCodeConsumerUnderTest().endSourceMapping(node);
    assertNull(getCodeConsumerUnderTest().getLastNode());
  }
}