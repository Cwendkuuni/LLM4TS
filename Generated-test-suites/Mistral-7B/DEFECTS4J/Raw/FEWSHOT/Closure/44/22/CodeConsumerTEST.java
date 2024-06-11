package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.Node;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @org.junit.Before
  public void setUp() {
    consumer = new MockCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    assertNull(consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    String identifier = "identifier";
    consumer.addIdentifier(identifier);
    assertEquals(identifier, consumer.getCode());
  }

  @Test
  public void testAppend() {
    String str = "String to append";
    consumer.append(str);
    assertEquals(str, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.endStatement();
    assertEquals(";\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithNewline() {
    consumer.endStatement(true);
    assertEquals(";\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.maybeEndStatement();
    assertTrue(consumer.statementStarted);
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndFunction() {
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
  }

  @Test
  public void testAdd() {
    String newcode = "new code";
    consumer.add(newcode);
    assertEquals("new code", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    String op = "+";
    consumer.addOp(op, false);
    assertEquals("+", consumer.getCode());

    op = "+=";
    consumer.addOp(op, false);
    assertEquals("+=", consumer.getCode());

    op = "-";
    consumer.addOp(op, false);
    assertEquals("-", consumer.getCode());

    op = "-=";
    consumer.addOp(op, false);
    assertEquals("-=", consumer.getCode());

    op = "*";
    consumer.addOp(op, true);
    assertEquals("*", consumer.getCode());

    op = "*=";
    consumer.addOp(op, true);
    assertEquals("*=", consumer.getCode());

    op = "/";
    consumer.addOp(op, true);
    assertEquals("/", consumer.getCode());

    op = "/=";
    consumer.addOp(op, true);
    assertEquals("/=", consumer.getCode());

    op = "%";
    consumer.addOp(op, true);
    assertEquals("%", consumer.getCode());

    op = "%=";
    consumer.addOp(op, true);
    assertEquals("%=", consumer.getCode());

    op = ">>";
    consumer.addOp(op, true);
    assertEquals(">>", consumer.getCode());

    op = ">>=";
    consumer.addOp(op, true);
    assertEquals(">>=", consumer.getCode());

    op = "<<";
    consumer.addOp(op, true);
    assertEquals("<<", consumer.getCode());

    op = "<<=";
    consumer.addOp(op, true);
    assertEquals("<<=", consumer.getCode());

    op = "&";
    consumer.addOp(op, true);
    assertEquals("&", consumer.getCode());

    op = "&=";
    consumer.addOp(op, true);
    assertEquals("&=", consumer.getCode());

    op = "|";
    consumer.addOp(op, true);
    assertEquals("|", consumer.getCode());

    op = "|=";
    consumer.addOp(op, true);
    assertEquals("|=", consumer.getCode());

    op = "^";
    consumer.addOp(op, true);
    assertEquals("^", consumer.getCode());

    op = "^=";
    consumer.addOp(op, true);
    assertEquals("^=", consumer.getCode());

    op = "!";
    consumer.addOp(op, false);
    assertEquals("!", consumer.getCode());

    op = "==";
    consumer.addOp(op, false);
    assertEquals("==", consumer.getCode());

    op = "===";
    consumer.addOp(op, false);
    assertEquals("===", consumer.getCode());

    op = "!=";
    consumer.addOp(op, false);
    assertEquals("!=", consumer.getCode());

    op = "!=";
    consumer.addOp(op, false);
    assertEquals("!=", consumer.getCode());

    op = "<";
    consumer.addOp(op, false);
    assertEquals("<", consumer.getCode());

    op = "<=";
    consumer.addOp(op, false);
    assertEquals("<=", consumer.getCode());

    op = ">";
    consumer.addOp(op, false);
    assertEquals(">", consumer.getCode());

    op = ">=";
    consumer.addOp(op, false);
    assertEquals(">=", consumer.getCode());

    op = "instanceof";
    consumer.addOp(op, false);
    assertEquals("instanceof", consumer.getCode());

    op = "in";
    consumer.addOp(op, false);
    assertEquals("in", consumer.getCode());

    op = "typeof";
    consumer.addOp(op, false);
    assertEquals("typeof", consumer.getCode());

    op = "new";
    consumer.addOp(op, false);
    assertEquals("new", consumer.getCode());

    op = "delete";
    consumer.addOp(op, false);
    assertEquals("delete", consumer.getCode());

    op = "void";
    consumer.addOp(op, false);
    assertEquals("void", consumer.getCode());

    op = "yield";
    consumer.addOp(op, false);
    assertEquals("yield", consumer.getCode());

    op = "return";
    consumer.addOp(op, false);
    assertEquals("return", consumer.getCode());

    op = "throw";
    consumer.addOp(op, false);
    assertEquals("throw", consumer.getCode());

    op = "try";
    consumer.addOp(op, false);
    assertEquals("try", consumer.getCode());

    op = "catch";
    consumer.addOp(op, false);
    assertEquals("catch", consumer.getCode());

    op = "finally";
    consumer.addOp(op, false);
    assertEquals("finally", consumer.getCode());

    op = "with";
    consumer.addOp(op, false);
    assertEquals("with", consumer.getCode());

    op = "break";
    consumer.addOp(op, false);
    assertEquals("break", consumer.getCode());

    op = "continue";
    consumer.addOp(op, false);
    assertEquals("continue", consumer.getCode());

    op = "default";
    consumer.addOp(op, false);
    assertEquals("default", consumer.getCode());

    op = "if";
    consumer.addOp(op, false);
    assertEquals("if", consumer.getCode());

    op = "else";
    consumer.addOp(op, false);
    assertEquals("else", consumer.getCode());

    op = "while";
    consumer.addOp(op, false);
    assertEquals("while", consumer.getCode());

    op = "do";
    consumer.addOp(op, false);
    assertEquals("do", consumer.getCode());

    op = "for";
    consumer.addOp(op, false);
    assertEquals("for", consumer.getCode());

    op = "switch";
    consumer.addOp(op, false);
    assertEquals("switch", consumer.getCode());

    op = "default";
    consumer.addOp(op, false);
    assertEquals("default", consumer.getCode());

    op = "case";
    consumer.addOp(op, false);
    assertEquals("case", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.addNumber(1.5);
    assertEquals("1.5", consumer.getCode());

    consumer.addNumber(-1.5);
    assertEquals("-1.5", consumer.getCode());

    consumer.addNumber(0.0);
    assertEquals("0", consumer.getCode());

    consumer.addNumber(Math.pow(2, 32) - 1);
    assertEquals(Long.toUnsignedString(Long.MAX_VALUE) + "E32", consumer.getCode());

    consumer.addNumber(Math.pow(2, 64) - 1);
    assertEquals(Long.toUnsignedString(Long.MAX_VALUE << 32) + "E64", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, CodeConsumer.isNegativeZero(0.0));
    assertEquals(true, CodeConsumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('z'));
    assertTrue(CodeConsumer.isWordChar('1'));
    assertTrue(CodeConsumer.isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = mock(Node.class);
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}

class MockCodeConsumer extends CodeConsumer {
  private StringBuilder code = new StringBuilder();

  @Override
  public char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  @Override
  public void append(String str) {
    code.append(str);
  }

  @Override
  public void appendBlockStart() {
    code.append("{\n");
  }

  @Override
  public void appendBlockEnd() {
    code.append("}\n");
  }

  @Override
  public void startNewLine() {
    code.append("\n");
  }

  @Override
  public void maybeLineBreak() {
    code.append("\n");
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
    code.append("{\n");
  }

  @Override
  public void endBlock() {
    code.append("}\n");
  }

  @Override
  public void endBlock(boolean shouldEndLine) {
    code.append("}\n");
    if (shouldEndLine) {
      code.append("\n");
    }
  }

  @Override
  public void listSeparator() {
    code.append(",\n");
  }

  @Override
  public void endStatement() {
    code.append(";\n");
  }

  @Override
  public void endStatement(boolean needSemiColon) {
    code.append(";\n");
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
    code.append(":\n");
  }

  @Override
  public void endCaseBody() {
  }

  @Override
  public void add(String newcode) {
    code.append(newcode);
  }

  @Override
  public void addOp(String op, boolean binOp) {
    code.append(op);
  }

  @Override
  public void addNumber(double x) {
    code.append(String.valueOf(x));
  }

  @Override
  public String getCode() {
    return code.toString();
  }
}