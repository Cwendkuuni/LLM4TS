package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for CodeConsumer.
 */
public class CodeConsumerTest {
  

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertNull(consumer.getLastChar());
    consumer.endSourceMapping(node);
    assertNull(consumer.getLastChar());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
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
    assertEquals("", consumer.getCode());
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
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{\ntest\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    assertEquals("test1,\ntest2\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.endStatement();
    assertEquals("test1;\n", consumer.getCode());
    consumer.append("test2");
    consumer.endStatement();
    assertEquals("test1;\ntest2;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.maybeEndStatement();
    consumer.append("test2");
    consumer.maybeEndStatement();
    assertEquals("test1;\ntest2;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("test1();");
    consumer.endFunction();
    assertEquals("function test() {\n  test1();\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("case 1:");
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endBlock();
    assertEquals("switch (x) {\n  case 1:\n}\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("case 1:");
    consumer.append("test1();");
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endBlock();
    assertEquals("switch (x) {\n  case 1:\n    test1();\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.add("test2");
    assertEquals("test1test2\n", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.appendOp("+", false);
    consumer.append("test2");
    assertEquals("test1+test2\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.addOp("+", false);
    consumer.append("test2");
    assertEquals("test1 + test2\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.addNumber(1.2);
    assertEquals("test11.2\n", consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testIsNegativeZero() {
    assertTrue(CodeConsumer.isNegativeZero(-0.0));
  }

  @Test(expected = AssertionError.class)
  public void testIsWordChar() {
    assertFalse(CodeConsumer.isWordChar('@'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testStatementNeedsEndedAndStatementStarted() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier1");
    consumer.append("=");
    consumer.addIdentifier("identifier2");
    consumer.endStatement();
    assertEquals("identifier1 = identifier2;\n", consumer.getCode());
    consumer.statementNeedsEnded = false;
    consumer.statementStarted = true;
    consumer.addIdentifier("identifier3");
    consumer.endStatement();
    assertEquals("identifier1 = identifier2;\nidentifier3;\n", consumer.getCode());
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction();
    assertFalse(consumer.sawFunction);
    consumer.append("function test() {}\n");
    consumer.endFunction(true);
    assertTrue(consumer.sawFunction);
  }

  

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
    String getCode() {
      return code;
    }
  }
}