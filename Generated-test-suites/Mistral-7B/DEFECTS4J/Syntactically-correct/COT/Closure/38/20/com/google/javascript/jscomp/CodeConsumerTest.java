package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodeConsumer.StatementContext;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = Node.newIntLiteral(0);
    consumer.startSourceMapping(node);
    assertNotNull(consumer.getLastSourceMapping());
  }

  @Test
  public void testEndSourceMapping() {
    Node node = Node.newIntLiteral(0);
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertNull(consumer.getLastSourceMapping());
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("Hello");
    assertEquals('o', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getLastText());
  }

  @Test
  public void testAppend() {
    consumer.append("Hello");
    assertEquals("Hello", consumer.getLastText());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getLastText());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockStart();
    consumer.append("Statement");
    consumer.appendBlockEnd();
    assertEquals("{\nStatement\n}", consumer.getLastText());
  }

  @Test
  public void testStartNewLine() {
    consumer.append("Hello");
    consumer.startNewLine();
    assertEquals("\nHello", consumer.getLastText());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.append("Hello");
    consumer.maybeLineBreak();
    assertEquals("\nHello", consumer.getLastText());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.append("Hello");
    consumer.maybeCutLine();
    assertEquals("Hello", consumer.getLastText());
  }

  @Test
  public void testEndLine() {
    consumer.append("Hello");
    consumer.endLine();
    assertEquals("Hello\n", consumer.getLastText());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.append("Hello");
    consumer.notePreferredLineBreak();
    assertEquals("Hello\n", consumer.getLastText());
  }

  @Test
  public void testBeginBlock() {
    consumer.append("Statement");
    consumer.beginBlock();
    assertEquals("Statement{\n", consumer.getLastText());
  }

  @Test
  public void testEndBlock() {
    consumer.append("Statement");
    consumer.beginBlock();
    consumer.append("Statement2");
    consumer.endBlock();
    assertEquals("Statement{\nStatement2\n}", consumer.getLastText());
  }

  @Test
  public void testEndBlockWithNewline() {
    consumer.append("Statement");
    consumer.beginBlock();
    consumer.append("Statement2");
    consumer.endBlock(true);
    assertEquals("Statement{\nStatement2\n}", consumer.getLastText());
  }

  @Test
  public void testListSeparator() {
    consumer.append("Statement1");
    consumer.listSeparator();
    consumer.append("Statement2");
    assertEquals("Statement1,\nStatement2", consumer.getLastText());
  }

  @Test
  public void testEndStatement() {
    consumer.append("Statement");
    consumer.endStatement();
    assertEquals("Statement;\n", consumer.getLastText());
  }

  @Test
  public void testEndStatementWithNewline() {
    consumer.append("Statement");
    consumer.endStatement(true);
    assertEquals("Statement;\n", consumer.getLastText());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("Statement");
    consumer.maybeEndStatement();
    assertEquals("Statement;", consumer.getLastText());
  }

  @Test
  public void testEndFunction() {
    consumer.append("Statement");
    consumer.beginBlock();
    consumer.append("FunctionBody");
    consumer.endFunction();
    assertEquals("Statement{\nFunctionBody\n}\n", consumer.getLastText());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("Statement");
    consumer.beginCaseBody();
    assertEquals("Statement:\n", consumer.getLastText());
  }

  @Test
  public void testEndCaseBody() {
    consumer.append("Statement");
    consumer.beginCaseBody();
    consumer.append("Statement2");
    consumer.endCaseBody();
    assertEquals("Statement:\nStatement2", consumer.getLastText());
  }

  @Test
  public void testAdd() {
    consumer.append("Statement1");
    consumer.add("Statement2");
    assertEquals("Statement1Statement2", consumer.getLastText());
  }

  @Test
  public void testAddOp() {
    consumer.append("Statement1");
    consumer.addOp("+", false);
    consumer.append("Statement2");
    assertEquals("Statement1+Statement2", consumer.getLastText());
  }

  @Test
  public void testAddOpBinary() {
    consumer.append("Statement1");
    consumer.addOp("*", true);
    consumer.append("Statement2");
    assertEquals("Statement1 * Statement2", consumer.getLastText());
  }

  @Test
  public void testAddNumber() {
    consumer.append("Statement1");
    consumer.addNumber(1.2);
    assertEquals("Statement11.2", consumer.getLastText());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    assertFalse(consumer.breakAfterBlockFor(new Node(), StatementContext.NONE));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    assertEquals("", consumer.getLastText());
  }

}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  String getLastText() {
    return super.getOutputText();
  }
}