package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Test
  public void testStartSourceMapping() {
    consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot test the current position in this test, as it's not accessible
  }

  @Test
  public void testEndSourceMapping() {
    consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingFalse() {
    consumer = new FalseContinueProcessingCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer = new StubCodeConsumer();
    for (int i = 0; i < 10; i++) {
      consumer.append("x");
    }
    for (int i = 0; i < 10; i++) {
      assertEquals('x', consumer.getLastChar());
      consumer.maybeLineBreak();
    }
  }

  @Test
  public void testAppend() {
    consumer = new StubCodeConsumer();
    consumer.append("abc");
    assertEquals("abc", consumer.toString());
  }

  @Test
  public void testAppendWithLineBreak() {
    consumer = new StubCodeConsumer();
    consumer.append("abc");
    consumer.maybeLineBreak();
    consumer.append("def");
    assertEquals("abc\ndef", consumer.toString());
  }

  @Test
  public void testAppendBlockStart() {
    consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.toString());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("abc");
    consumer.appendBlockEnd();
    assertEquals("{\nabc}\n", consumer.toString());
  }

  @Test
  public void testStartNewLine() {
    consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer = new StubCodeConsumer();
    consumer.append("abc");
    consumer.maybeLineBreak();
    consumer.append("def");
    assertEquals("abc\ndef", consumer.toString());
  }

  @Test
  public void testMaybeCutLine() {
    consumer = new StubCodeConsumer();
    consumer.append("abc");
    consumer.maybeCutLine();
    consumer.append("def");
    assertEquals("abc\ndef", consumer.toString());

    consumer.append("abc");
    consumer.maybeCutLine();
    consumer.append("def");
    assertEquals("abc\ndef", consumer.toString());
  }

  @Test
  public void testEndLine() {
    consumer = new StubCodeConsumer();
    consumer.append("abc");
    consumer.endLine();
    assertEquals("abc\n", consumer.toString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testBeginBlock() {
    consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.toString());
  }

  @Test
  public void testEndBlock() {
    consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("abc");
    consumer.endBlock();
    assertEquals("{\nabc}\n", consumer.toString());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("abc");
    consumer.endBlock(true);
    assertEquals("{\nabc}\n", consumer.toString());
  }

  @Test
  public void testListSeparator() {
    consumer = new StubCodeConsumer();
    consumer.append("abc");
    consumer.listSeparator();
    consumer.append("def");
    assertEquals("abc,\ndef", consumer.toString());
  }

  @Test
  public void testEndStatement() {
    consumer = new StubCodeConsumer();
    consumer.append("if (true) {");
    consumer.append("}");
    consumer.endStatement();
    assertEquals("if (true) {\n}\n", consumer.toString());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer = new StubCodeConsumer();
    consumer.append("if (true) {");
    consumer.append("}");
    consumer.endStatement(true);
    assertEquals("if (true) {\n};\n", consumer.toString());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer = new StubCodeConsumer();
    consumer.append("if (true) {");
    consumer.append("}");
    consumer.maybeEndStatement();
    consumer.append("else if (false) {");
    assertEquals("if (true) {\n}\nelse if (false) {\n}", consumer.toString());
  }

  @Test
  public void testEndFunction() {
    consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.endFunction();
    consumer.append("}");
    consumer.endBlock();
    assertEquals("{\nfunction foo() {\n}\n}", consumer.toString());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append("}");
    consumer.endFunction(true);
    consumer.append("}");
    assertEquals("{\nfunction foo() {\n}\n}", consumer.toString());
  }

  @Test
  public void testBeginCaseBody() {
    consumer = new StubCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("case 1:");
    assertEquals("switch (x) {\n  case 1:\n}", consumer.toString());
  }

  @Test
  public void testEndCaseBody() {
    consumer = new StubCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("case 1:");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case 1:\n}", consumer.toString());
  }

  @Test
  public void testAdd() {
    consumer = new StubCodeConsumer();
    consumer.append("abc");
    consumer.add("def");
    assertEquals("abcdef", consumer.toString());
  }

  @Test
  public void testAddWithSpaces() {
    consumer = new StubCodeConsumer();
    consumer.append("return x");
    consumer.add(" + 5;");
    assertEquals("return x + 5;\n", consumer.toString());
  }

  @Test
  public void testAddOp() {
    consumer = new StubCodeConsumer();
    consumer.append("return x");
    consumer.addOp(" + ", true);
    consumer.append("5;");
    assertEquals("return x + 5;\n", consumer.toString());
  }

  @Test
  public void testAddNumber() {
    consumer = new StubCodeConsumer();
    consumer.append("return ");
    consumer.addNumber(1.23);
    assertEquals("return 1.23;\n", consumer.toString());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    consumer = new StubCodeConsumer_ShouldPreserveExtraBlocks();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForSingleStatement() {
    consumer = new StubCodeConsumer();
    Node singleStatementNode = new Node();
    singleStatementNode.setType(Node.STMT);
    singleStatementNode.setLastChild(new Node("return 1;"));
    assertTrue(consumer.breakAfterBlockFor(singleStatementNode, false));
  }

  @Test
  public void testBreakAfterBlockForMultipleStatements() {
    consumer = new StubCodeConsumer();
    Node multipleStatementsNode = new Node();
    multipleStatementsNode.setType(Node.STMTLIST);
    multipleStatementsNode.setLastChild(new Node("return 1;"));
    multipleStatementsNode.setPreviousSibling(new Node("var x = 5;"));
    assertFalse(consumer.breakAfterBlockFor(multipleStatementsNode, false));
  }

  @Test
  public void testBreakAfterBlockForStatementContext() {
    consumer = new StubCodeConsumer();
    Node singleStatementNode = new Node();
    singleStatementNode.setType(Node.STMT);
    singleStatementNode.setLastChild(new Node("return 1;"));
    assertTrue(consumer.breakAfterBlockFor(singleStatementNode, true));
  }

  @Test
  public void testEndFile() {
    consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.toString());
  }
}

class StubCodeConsumer extends CodeConsumer {
  // Empty implementation of all abstract methods
}

class FalseContinueProcessingCodeConsumer extends CodeConsumer {
  @Override
  public boolean continueProcessing() {
    return false;
  }
}

class StubCodeConsumer_ShouldPreserveExtraBlocks extends CodeConsumer {
  @Override
  public boolean shouldPreserveExtraBlocks() {
    return true;
  }
}