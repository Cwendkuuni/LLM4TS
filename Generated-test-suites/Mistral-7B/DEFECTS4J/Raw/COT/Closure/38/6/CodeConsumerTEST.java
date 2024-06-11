package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodeConsumer.StatementContext;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = Node.newIntLiteral(1);
    consumer.startSourceMapping(node);
    // Nothing to assert here, just checking the method call
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = Node.newIntLiteral(1);
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Nothing to assert here, just checking the method call
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertNull(consumer.getLastChar());
    consumer.append("abc");
    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addIdentifier("identifier");
    String expected = "identifier";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("Hello World");
    String expected = "Hello World";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    String expected = "{";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    String expected = "}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.startNewLine();
    String expected = "\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeLineBreak();
    // Cannot test the output directly as it depends on the previous code
    String previousCode = "abc";
    consumer.setPreviousCode(previousCode);
    String expected = previousCode.substring(previousCode.length() - 1) + "\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeCutLine();
    // Cannot test the output directly as it depends on the previous code
    String previousCode = "abcdefg";
    consumer.setPreviousCode(previousCode);
    String expected = previousCode.substring(0, previousCode.length() - 1);
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endLine();
    String expected = "\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
    // Cannot test the output directly as it does not affect the code
    assertTrue(consumer.hasPreferredLineBreak());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    String expected = "{";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endBlock();
    String expected = "}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endBlock(true);
    String expected = "}";
    assertEquals(expected, consumer.getCode());
    assertEquals("\n", consumer.getPreviousCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.listSeparator();
    String expected = ", ";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("if (x > 0) { x++; }");
    consumer.endStatement();
    String expected = ";";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("if (x > 0) { x++; }");
    consumer.endStatement(true);
    String expected = ";";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("if (x > 0) { x++; } else { y++; }");
    consumer.maybeEndStatement();
    // Cannot test the output directly as it depends on the previous code
    String previousCode = "if (x > 0) { x++; } else { y++; }";
    assertEquals("}", consumer.getCode());
    assertEquals(previousCode, consumer.getPreviousCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("function foo() { }");
    consumer.endFunction();
    String expected = "}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("function foo() { }");
    consumer.endFunction(StatementContext.BLOCK);
    String expected = "}";
    assertEquals(expected, consumer.getCode());
    assertEquals("\n", consumer.getPreviousCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginCaseBody();
    String expected = ":";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endCaseBody();
    String expected = "";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("foo ");
    consumer.add("bar");
    String expected = "foo bar";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("5 + 3");
    consumer.addOp("+", true);
    String expected = "5 + 3 +";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("1.5 + ");
    consumer.addNumber(2.5);
    String expected = "1.5 + 2.5";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = Node.newBlock("{ x++; }");
    assertTrue(consumer.breakAfterBlockFor(node, StatementContext.IF));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = Node.newBlock("{ x++; }");
    assertFalse(consumer.breakAfterBlockFor(node, StatementContext.WHILE));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
    // Nothing to assert here, just checking the method call
  }

  private static class DummyCodeConsumer extends CodeConsumer {
    @Override
    void add(String newcode) {
      super.add(newcode);
    }

    @Override
    void append(String str) {
      super.append(str);
    }

    @Override
    char getLastChar() {
      return super.getLastChar();
    }

    @Override
    void appendBlockStart() {
      super.appendBlockStart();
    }

    @Override
    void appendBlockEnd() {
      super.appendBlockEnd();
    }

    @Override
    void startNewLine() {
      super.startNewLine();
    }

    @Override
    void maybeLineBreak() {
      super.maybeLineBreak();
    }

    @Override
    void endLine() {
      super.endLine();
    }

    @Override
    void notePreferredLineBreak() {
      super.notePreferredLineBreak();
    }

    @Override
    void beginBlock() {
      super.beginBlock();
    }

    @Override
    void endBlock() {
      super.endBlock();
    }

    @Override
    void endBlock(boolean shouldEndLine) {
      super.endBlock(shouldEndLine);
    }

    @Override
    void listSeparator() {
      super.listSeparator();
    }

    @Override
    void endStatement() {
      super.endStatement();
    }

    @Override
    void endStatement(boolean needSemiColon) {
      super.endStatement(needSemiColon);
    }

    @Override
    void maybeEndStatement() {
      super.maybeEndStatement();
    }

    @Override
    void endFunction() {
      super.endFunction();
    }

    @Override
    void endFunction(boolean statementContext) {
      super.endFunction(statementContext);
    }

    @Override
    void beginCaseBody() {
      super.beginCaseBody();
    }

    @Override
    void endCaseBody() {
      super.endCaseBody();
    }

    @Override
    void addIdentifier(String identifier) {
      super.addIdentifier(identifier);
    }

    @Override
    void addNumber(double x) {
      super.addNumber(x);
    }

    @Override
    void appendOp(String op, boolean binOp) {
      super.appendOp(op, binOp);
    }

    @Override
    void addOp(String op, boolean binOp) {
      super.addOp(op, binOp);
    }

    @Override
    boolean shouldPreserveExtraBlocks() {
      return super.shouldPreserveExtraBlocks();
    }

    @Override
    boolean breakAfterBlockFor(Node n, StatementContext statementContext) {
      return super.breakAfterBlockFor(n, statementContext);
    }

    @Override
    void endFile() {
      super.endFile();
    }

    String getCode() {
      return super.getCode();
    }

    String getPreviousCode() {
      return super.getPreviousCode();
    }

    void setPreviousCode(String previousCode) {
      super.setPreviousCode(previousCode);
    }
  }
}