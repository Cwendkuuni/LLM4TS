package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('z'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertTrue(CodeConsumer.isWordChar('9'));
    assertFalse(CodeConsumer.isWordChar('!'));
    assertFalse(CodeConsumer.isWordChar('('));
    assertFalse(CodeConsumer.isWordChar(')'));
    assertFalse(CodeConsumer.isWordChar('['));
    assertFalse(CodeConsumer.isWordChar(']'));
    assertFalse(CodeConsumer.isWordChar('{'));
    assertFalse(CodeConsumer.isWordChar('}'));
    assertFalse(CodeConsumer.isWordChar(';'));
    assertFalse(CodeConsumer.isWordChar(','));
    assertFalse(CodeConsumer.isWordChar('.'));
    assertFalse(CodeConsumer.isWordChar('/'));
    assertFalse(CodeConsumer.isWordChar('\\'));
  }

  @Test
  public void testUtilCodeConsumer() {
    CodeConsumer consumer = new UtilCodeConsumer();

    consumer.beginBlock();
    consumer.addIdentifier("function foo() {");
    consumer.endStatement();
    consumer.append("return 42;");
    consumer.endStatement();
    consumer.append("}");
    consumer.endBlock();

    String code = consumer.getCode();

    assertEquals("function foo() {\n  return 42;\n}", code);

    consumer = new UtilCodeConsumer();
    consumer.beginBlock();
    consumer.addIdentifier("function foo() {");
    consumer.endStatement();
    consumer.append("return 42;");
    consumer.endStatement();
    consumer.appendBlockEnd();

    code = consumer.getCode();

    assertEquals("function foo() {\n}", code);
    assertEquals("function foo() {\n  return 42;\n}", consumer.getCode(true));

    consumer.beginBlock();
    consumer.addIdentifier("function foo() {");
    consumer.endStatement();
    consumer.appendBlockEnd();

    code = consumer.getCode();

    assertEquals("function foo() {\n}", code);

    consumer = new UtilCodeConsumer();

    consumer.append("function foo() {");
    consumer.endStatement();
    consumer.append("return 42;");
    consumer.endStatement();

    code = consumer.getCode();

    assertEquals("function foo() {\n  return 42;\n}", code);

    consumer = new UtilCodeConsumer();

    consumer.append("function foo() {");
    consumer.endBlock();

    code = consumer.getCode();

    assertEquals("function foo() {\n}", code);
  }

  @Test
  public void testStatementConsumer() {
    StatementConsumer consumer = new StatementConsumer();

    consumer.beginBlock();
    consumer.addIdentifier("function foo() {");
    consumer.endStatement();
    consumer.append("return 42;");
    consumer.endStatement();
    consumer.appendBlockEnd();

    String code = consumer.getCode();

    assertEquals("function foo() {\n  return 42;\n}", code);

    consumer = new StatementConsumer();

    consumer.append("function foo() {");
    consumer.endStatement();
    consumer.append("return 42;");

    code = consumer.getCode();

    assertEquals("function foo() {\n  return 42;\n}", code);

    consumer = new StatementConsumer();

    consumer.append("function foo() {");
    consumer.endBlock();

    code = consumer.getCode();

    assertEquals("function foo() {\n}", code);
  }

  @Test
  public void testBlockConsumer() {
    BlockConsumer consumer = new BlockConsumer();

    consumer.beginBlock();
    consumer.addIdentifier("function foo() {");
    consumer.endStatement();
    consumer.append("return 42;");
    consumer.endStatement();
    consumer.appendBlockEnd();

    String code = consumer.getCode();

    assertEquals("function foo() {\n  return 42;\n}", code);

    consumer = new BlockConsumer();

    consumer.append("function foo() {");
    consumer.endBlock();

    code = consumer.getCode();

    assertEquals("function foo() {\n}", code);

    consumer = new BlockConsumer();

    consumer.append("function foo() {");
    consumer.endBlock();
    consumer.appendBlockEnd();

    code = consumer.getCode();

    assertEquals("function foo() {\n}", code);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.append("return 42;");
    consumer.maybeEndStatement();
    consumer.append(";");
    consumer.maybeEndStatement();

    assertEquals(2, consumer.statementCount());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.append("return 42;\n");
    consumer.notePreferredLineBreak();
    consumer.append("return 43;");

    String code = consumer.getCode();

    assertEquals("return 42;\nreturn 43;\n", code);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.append("return ");
    consumer.append("42;\n");
    consumer.maybeCutLine();
    consumer.append("return ");
    consumer.append("43;\n");

    String code = consumer.getCode();

    assertEquals("return 42;\nreturn 43;\n", code);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.append("return 42;");
    consumer.maybeLineBreak();
    consumer.append("return 43;");

    String code = consumer.getCode();

    assertEquals("return 42;\nreturn 43;\n", code);
  }

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StatementConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.append("return 42;");
    consumer.endSourceMapping(node);

    String code = consumer.getCode();

    assertEquals("return 42;\n", code);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StatementConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.append("return 42;");
    consumer.endSourceMapping(node);
    consumer.startSourceMapping(node);

    String code = consumer.getCode();

    assertEquals("return 42;\n", code);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StatementConsumer();

    assertFalse(consumer.continueProcessing());

    consumer = new StatementConsumer();

    consumer.append("return 42;");

    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StatementConsumer();

    consumer.append("return 42;");

    assertEquals(';', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.addIdentifier("foo");
    consumer.addIdentifier("bar");

    String code = consumer.getCode();

    assertEquals("foo bar", code);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.add("foo");
    consumer.add("bar");

    String code = consumer.getCode();

    assertEquals("foobar", code);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.append("foo");
    consumer.append("bar");

    String code = consumer.getCode();

    assertEquals("foobar", code);
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.appendOp("+");
    consumer.appendOp("+");

    String code = consumer.getCode();

    assertEquals("++", code);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.addOp("+", true);
    consumer.addOp("+", true);

    String code = consumer.getCode();

    assertEquals("++", code);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.addNumber(42.0);

    String code = consumer.getCode();

    assertEquals("42.0", code);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.append("function foo() {");
    consumer.endFunction();

    String code = consumer.getCode();

    assertEquals("function foo() {\n", code);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.append("function foo() {");
    consumer.endFunction(true);

    String code = consumer.getCode();

    assertEquals("function foo() {\n}", code);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.append("case");
    consumer.beginCaseBody();

    String code = consumer.getCode();

    assertEquals("case:\n", code);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.append("case");
    consumer.beginCaseBody();
    consumer.append("return 42;");
    consumer.endCaseBody();

    String code = consumer.getCode();

    assertEquals("case:\nreturn 42;\n", code);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StatementConsumer();
    consumer.listSeparator();
    consumer.listSeparator();

    String code = consumer.getCode();

    assertEquals(",\n,", code);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StatementConsumer();

    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StatementConsumer();

    assertTrue(consumer.breakAfterBlockFor(null, false));
    assertTrue(consumer.breakAfterBlockFor(null, true));
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
    assertFalse(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StatementConsumer();

    consumer.endFile();

    String code = consumer.getCode();

    assertEquals("", code);
  }
}

class UtilCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }
}

class StatementConsumer extends CodeConsumer {
  private int statementCount = 0;

  @Override
  void beginBlock() {
    super.beginBlock();
    statementCount++;
  }

  @Override
  void endBlock() {
    super.endBlock();
    statementCount--;
  }

  @Override
  void endStatement() {
    statementCount++;
  }

  @Override
  void endStatement(boolean needSemiColon) {
    super.endStatement(needSemiColon);
    statementCount++;
  }

  @Override
  void maybeEndStatement() {
    super.maybeEndStatement();
    statementCount++;
  }

  @Override
  void endFunction() {
    super.endFunction();
    statementCount--;
  }

  @Override
  void endFunction(boolean statementContext) {
    super.endFunction(statementContext);
    statementCount--;
  }

  @Override
  void beginCaseBody() {
    super.beginCaseBody();
    statementCount++;
  }

  @Override
  void endCaseBody() {
    super.endCaseBody();
    statementCount--;
  }

  @Override
  void add(String newcode) {
    super.add(newcode);
    statementCount += newcode.split("\n").length;
  }

  @Override
  void append(String str) {
    super.append(str);
    statementCount++;
  }

  @Override
  void appendBlockStart() {
    super.appendBlockStart();
    statementCount++;
  }

  @Override
  void appendBlockEnd() {
    super.appendBlockEnd();
    statementCount--;
  }

  @Override
  void startNewLine() {
    super.startNewLine();
    statementCount++;
  }

  @Override
  void maybeLineBreak() {
    super.maybeLineBreak();
    statementCount++;
  }

  @Override
  void maybeCutLine() {
    super.maybeCutLine();
    statementCount++;
  }

  @Override
  void endLine() {
    super.endLine();
    statementCount--;
  }

  @Override
  void notePreferredLineBreak() {
    super.notePreferredLineBreak();
    statementCount++;
  }

  @Override
  void beginBlock() {
    super.beginBlock();
    if (statementCount > 0) {
      append("{");
    }
  }

  @Override
  void endBlock() {
    super.endBlock();
    statementCount = Math.max(statementCount - 1, 0);
    if (statementCount == 0) {
      append("}");
    }
  }

  @Override
  void endBlock(boolean shouldEndLine) {
    super.endBlock(shouldEndLine);
    statementCount = Math.max(statementCount - 1, 0);
    if (statementCount == 0) {
      endLine();
    }
  }

  @Override
  void listSeparator() {
    super.listSeparator();
    endLine();
    statementCount = Math.max(statementCount - 1, 0);
  }

  @Override
  void addNumber(double x) {
    super.addNumber(x);
    statementCount++;
  }

  int statementCount() {
    return statementCount;
  }
}

class BlockConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }
}