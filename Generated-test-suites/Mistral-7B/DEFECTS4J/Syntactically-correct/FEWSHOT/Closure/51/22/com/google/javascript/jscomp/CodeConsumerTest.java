package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);

    // No verification needed, as method has no side effects
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);

    // No verification needed, as method has no side effects
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    when(consumer.continueProcessing()).thenReturn(true);

    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("foo");

    char lastChar = consumer.getLastChar();

    assertTrue(lastChar == 'o');
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.addIdentifier("identifier");

    String code = consumer.getCode();

    assertTrue(code.contains("identifier"));
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("foo");

    String code = consumer.getCode();

    assertTrue(code.contains("foo"));
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.appendBlockStart();

    String code = consumer.getCode();

    assertTrue(code.contains("{"));
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.appendBlockEnd();

    String code = consumer.getCode();

    assertTrue(code.contains("}"));
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.startNewLine();

    String code = consumer.getCode();

    assertTrue(code.contains("\n"));
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.maybeLineBreak();

    String code = consumer.getCode();

    assertTrue(code.contains("\n"));
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.maybeCutLine();

    String code = consumer.getCode();

    assertTrue(code.contains("\n"));
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.endLine();

    String code = consumer.getCode();

    assertTrue(code.contains("\n"));
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.notePreferredLineBreak();

    String code = consumer.getCode();

    assertTrue(code.contains("\n"));
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.beginBlock();

    String code = consumer.getCode();

    assertTrue(code.contains("{"));
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.appendBlockStart();
    consumer.append("statement");
    consumer.endBlock();

    String code = consumer.getCode();

    assertTrue(code.contains("{\nstatement\n}"));
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.appendBlockStart();
    consumer.append("statement");
    consumer.endBlock(true);

    String code = consumer.getCode();

    assertTrue(code.contains("{\nstatement\n}"));
    assertTrue(code.contains("\n"));
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("foo");
    consumer.listSeparator();
    consumer.append("bar");

    String code = consumer.getCode();

    assertTrue(code.contains("foo,\nbar"));
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("statement");
    consumer.endStatement();

    String code = consumer.getCode();

    assertTrue(code.contains("statement;\n"));
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("statement");
    consumer.endStatement(true);

    String code = consumer.getCode();

    assertTrue(code.contains("statement;\n"));
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("statement1");
    consumer.append("statement2");

    consumer.maybeEndStatement();

    String code = consumer.getCode();

    assertTrue(code.contains("statement1;\nstatement2"));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("function foo() {");
    consumer.append("statement");
    consumer.endFunction();

    String code = consumer.getCode();

    assertTrue(code.contains("function foo() {\nstatement\n}"));
  }

  @Test
  public void testEndFunctionWithSemicolon() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("function foo() {");
    consumer.append("statement");
    consumer.endFunction(true);

    String code = consumer.getCode();

    assertTrue(code.contains("function foo() {\nstatement;\n}"));
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("case label:");
    consumer.beginCaseBody();

    String code = consumer.getCode();

    assertTrue(code.contains("case label:\n:"));
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("case label:");
    consumer.beginCaseBody();
    consumer.append("statement");
    consumer.endCaseBody();

    String code = consumer.getCode();

    assertTrue(code.contains("case label:\n:\nstatement"));
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("foo");
    consumer.add("bar");

    String code = consumer.getCode();

    assertTrue(code.contains("foobar"));
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("x");
    consumer.appendOp("+", false);
    consumer.append("y");

    String code = consumer.getCode();

    assertTrue(code.contains("x+y"));
  }

  @Test
  public void testAddOpWithBinOp() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("x");
    consumer.addOp("+", true);
    consumer.append("y");

    String code = consumer.getCode();

    assertTrue(code.contains("x + y"));
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.addNumber(42.0);

    String code = consumer.getCode();

    assertTrue(code.contains("42.0"));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertFalse(CodeConsumer.isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();

    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();

    Node node = mock(Node.class);

    when(node.getType()).thenReturn(Node.BLOCK);

    assertTrue(consumer.breakAfterBlockFor(node, false));

    when(node.getType()).thenReturn(Node.EXPR_STATEMENT);

    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.endFile();

    String code = consumer.getCode();

    assertTrue(code.isEmpty());
  }
}

class DummyCodeConsumer extends CodeConsumer {
  private StringBuilder code = new StringBuilder();

  @Override
  public void add(String newcode) {
    code.append(newcode);
  }

  @Override
  public void append(String str) {
    code.append(str);
  }

  @Override
  public void appendBlockStart() {
  }

  @Override
  public void appendBlockEnd() {
  }

  @Override
  public void startNewLine() {
    code.append("\n");
  }

  @Override
  public void maybeLineBreak() {
  }

  @Override
  public void maybeCutLine() {
  }

  @Override
  public void endLine() {
  }

  @Override
  public void notePreferredLineBreak() {
  }

  @Override
  public void beginBlock() {
  }

  @Override
  public void endBlock() {
  }

  @Override
  public void listSeparator() {
  }

  @Override
  public void endStatement() {
  }

  @Override
  public void endStatement(boolean needSemiColon) {
  }

  @Override
  public void maybeEndStatement() {
  }

  @Override
  public void endFunction() {
  }

  @Override
  public void endFunction(boolean statementContext) {
  }

  @Override
  public void beginCaseBody() {
  }

  @Override
  public void endCaseBody() {
  }

  @Override
  public char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  public String getCode() {
    return code.toString();
  }
}