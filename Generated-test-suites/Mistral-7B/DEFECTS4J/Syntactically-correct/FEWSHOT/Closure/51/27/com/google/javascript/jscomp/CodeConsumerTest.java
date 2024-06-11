package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testStartSourceMappingEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
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
    assertTrue(consumer.getCode().length() > 0);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    assertTrue(consumer.getCode().length() > 0);
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
    assertTrue(consumer.getCode().length() > 0);
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
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
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
    consumer.append("test");
    consumer.endStatement();
    consumer.append("test2");
    consumer.endLine();
    assertEquals("test;\n test2\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    consumer.append("test2");
    consumer.endLine();
    assertEquals("test;\n test2\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    consumer.append("test2");
    consumer.endLine();
    assertEquals("test;\n test2\n", consumer.getCode());
  }

  @Testpublic void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append("test2");
    consumer.endFunction();
    assertEquals("function test() {\n test2\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append("test2");
    consumer.endFunction(true);
    assertEquals("function test() {\n test2;\n}\n", consumer.getCode());
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
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    assertEquals(":\n test\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("5 + 5");
    consumer.addOp("+", true);
    assertEquals("5 + 5 +\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(5.5);
    assertEquals("5.5\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('A'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertFalse(CodeConsumer.isWordChar('-'));
  }

  private String getCode() {
    return new String(new TestCodeConsumer().getCodeArray());
  }

  private static class TestCodeConsumer extends CodeConsumer {
    char[] codeArray = new char[100];
    int index = 0;

    @Override
    public void add(String newcode) {
      super.add(newcode);
      System.arraycopy(newcode.toCharArray(), 0, codeArray, index, newcode.length());
      index += newcode.length();
    }

    @Override
    public char[] getCodeArray() {
      return codeArray;
    }
  }
}