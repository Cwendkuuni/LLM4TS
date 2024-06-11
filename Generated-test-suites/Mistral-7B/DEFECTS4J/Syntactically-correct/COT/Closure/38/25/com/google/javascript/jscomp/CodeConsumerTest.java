package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertNotNull(consumer.sourceMappingStart);
    assertEquals(node, consumer.sourceMappingStart.node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertNull(consumer.sourceMappingStart);
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
    consumer.addIdentifier("test");
    assertEquals("test", consumer.code.toString());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    assertEquals("test", consumer.code.toString());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.code.toString());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.code.toString());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.code.toString());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.code.toString());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    assertEquals("", consumer.code.toString());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.code.toString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.code.toString());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.code.toString());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("}\n", consumer.code.toString());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.code.toString());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.code.toString());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.code.toString());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    assertEquals("test;\n", consumer.code.toString());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    assertEquals("test;\n", consumer.code.toString());
    consumer.append(" ");
    consumer.maybeEndStatement();
    assertEquals("test;\n", consumer.code.toString());
  }

  @Test(expected = AssertionError.class)
  public void testEndStatementWithoutStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement();
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("return 1;");
    consumer.endFunction();
    assertEquals("function test() {\nreturn 1;\n}\n", consumer.code.toString());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("return 1;");
    consumer.endStatement();
    consumer.endFunction(true);
    assertEquals("function test() {\nreturn 1;\n}\n", consumer.code.toString());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case");
    consumer.beginCaseBody();
    assertEquals("case:\n", consumer.code.toString());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case");
    consumer.beginBlock();
    consumer.endCaseBody();
    assertEquals("case:\n}", consumer.code.toString());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.add(" world");
    assertEquals("test world\n", consumer.code.toString());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addOp(" + ", true);
    consumer.append("world");
    assertEquals("test + world\n", consumer.code.toString());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addNumber(123);
    assertEquals("test 123\n", consumer.code.toString());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
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

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.code.toString());
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction();
    assertFalse(consumer.sawFunction);
    consumer.endFunction(true);
    assertTrue(consumer.sawFunction);
  }

  private static class TestCodeConsumer extends CodeConsumer {
    String code = "";
    Node sourceMappingStart;
  }
}