package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    // Verify no exception was thrown
  }

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingFalse() {
    CodeConsumer consumer = new TestFalseCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    String code = "foo";
    consumer.add(code);
    char lastChar = consumer.getLastChar();
    assertEquals(lastChar, code.charAt(code.length() - 1));
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    String code = "foo";
    consumer.append(code);
    assertEquals(code, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    // No output
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    // No output
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock(false);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true) {");
    consumer.endStatement();
    assertEquals("if (true) {\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true);");
    consumer.endStatement();
    assertEquals("if (true);\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true) {");
    consumer.maybeEndStatement();
    assertEquals("if (true) {\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatementWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true);");
    consumer.maybeEndStatement();
    assertEquals("if (true);\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction();
    assertEquals("function foo() {\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction(true);
    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction(true);
    consumer.endStatement();
    assertEquals("function foo() {\n}\n;", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endCaseBody();
    // No output
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("foo");
    consumer.add("bar");
    assertEquals("foobar\n", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("foo");
    assertEquals("foo\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", false);
    assertEquals("+\n", consumer.getCode());
  }

  @Test
  public void testAddBinaryOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", true);
    assertEquals(" +\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1.23);
    assertEquals("1.23\n", consumer.getCode());
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
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  // Helper class for testing CodeConsumer methods that don't return a value
  static class TestCodeConsumer extends CodeConsumer {
    private String code = "";

    @Override
    void append(String str) {
      code += str;
    }

    @Override
    void add(String newcode) {
      code += newcode;
    }

    @Override
    void addIdentifier(String identifier) {
      add(identifier);
    }

    @Override
    void addOp(String op, boolean binOp) {
      addOp(op, binOp);
    }

    @Override
    void addNumber(double x) {
      addNumber(x);
    }

    @Override
    void startSourceMapping(Node node) {
      // No-op
    }

    @Override
    void endSourceMapping(Node node) {
      // No-op
    }

    @Override
    boolean continueProcessing() {
      return true;
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    String getCode() {
      String output = code;
      code = "";
      return output;
    }
  }

  // Helper class for testing CodeConsumer methods that return false
  static class TestFalseCodeConsumer extends CodeConsumer {
    @Override
    boolean continueProcessing() {
      return false;
    }
  }
}