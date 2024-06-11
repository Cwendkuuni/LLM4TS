package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CodeConsumer.isWordChar;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('z'));
    assertTrue(isWordChar('0'));
    assertTrue(isWordChar('9'));
  }

  @Test
  public void testEmptyCodeConsumer() {
    consumer = new EmptyCodeConsumer();
    assertTrue(consumer instanceof CodeConsumer);
  }

  @Test
  public void testBasicFunctions() {
    consumer = new BasicCodeConsumer();
    testAddIdentifier("identifier");
    testAppend(" string ");
    testAppendBlockStart();
    testAppendBlockEnd();
    testStartNewLine();
    testMaybeLineBreak();
    testMaybeCutLine();
    testEndLine();
    testNotePreferredLineBreak();
    testBeginBlock();
    testEndBlock();
    testListSeparator();
  }

  @Test
  public void testStatementFunctions() {
    consumer = new BasicCodeConsumer();
    testEndStatement();
    testEndStatement(true);
    testMaybeEndStatement();
  }

  @Test
  public void testFunctionFunctions() {
    consumer = new BasicCodeConsumer();
    testEndFunction();
    testEndFunction(true);
  }

  @Test
  public void testBlockStartAndEndFunctions() {
    consumer = new BasicCodeConsumer();
    testBeginBlock();
    testEndBlock();
    testEndBlock(true);
  }

  @Test
  public void testSourceMappingFunctions() {
    consumer = new BasicCodeConsumer();
    testStartSourceMapping(null);
    testEndSourceMapping(null);
  }

  @Test
  public void testContinueProcessingFunction() {
    consumer = new BasicCodeConsumer();
    testContinueProcessing();
  }

  @Test
  public void testGetLastCharFunction() {
    consumer = new EmptyCodeConsumer();
    assertNull(consumer.getLastChar());
  }

  @Test
  public void testAddIdentifierFunction() {
    consumer = new BasicCodeConsumer();
    testAddIdentifier("identifier");
  }

  @Test
  public void testAppendFunction() {
    consumer = new BasicCodeConsumer();
    testAppend("string");
  }

  @Test
  public void testAppendOpFunction() {
    consumer = new BasicCodeConsumer();
    testAddOp("+", true);
    testAddOp("+", false);
    testAddOp("-", true);
    testAddOp("-", false);
    testAddOp("*", true);
    testAddOp("/", true);
    testAddOp("=", false);
    testAddOp("==", false);
    testAddOp("!=", false);
    testAddOp(">", false);
    testAddOp(">=", false);
    testAddOp("<", false);
    testAddOp("<=", false);
    testAddOp("!", false);
    testAddOp("&&", true);
    testAddOp("||", true);
  }

  @Test
  public void testAddNumberFunction() {
    consumer = new BasicCodeConsumer();
    testAddNumber(1);
    testAddNumber(1.2);
    testAddNumber(-1.2);
  }

  @Test
  public void testShouldPreserveExtraBlocksFunction() {
    consumer = new BasicCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForFunction() {
    consumer = new BasicCodeConsumer();
    Node node = new Node_Constant();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFileFunction() {
    consumer = new BasicCodeConsumer();
    consumer.endFile();
  }

  private void testAdd(String newcode) {
    consumer.append("before");
    consumer.add(newcode);
    assertEquals("before" + newcode, consumer.getCode());
  }

  private void testAppend(String string) {
    consumer.append("before");
    consumer.append(string);
    assertEquals("before" + string, consumer.getCode());
  }

  private void testAddIdentifier(String identifier) {
    consumer.addIdentifier(identifier);
    assertEquals(identifier, consumer.getLastIdentifier());
  }

  private void testContinueProcessing() {
    assertFalse(consumer.continueProcessing());
  }

  private void testEndLine() {
    consumer.append("before");
    consumer.endLine();
    assertEquals("\nbefore", consumer.getCode());
  }

  private void testGetCode() {
    assertEquals("", consumer.getCode());
  }

  private void testGetLastChar() {
    consumer.append("ab");
    assertEquals('b', consumer.getLastChar());
  }

  private void testGetLastIdentifier() {
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getLastIdentifier());
  }

  private void testIsStatementNeedsEnded() {
    assertFalse(consumer.isStatementNeedsEnded());
  }

  private void testIsStatementStarted() {
    assertFalse(consumer.isStatementStarted());
  }

  private void testSawFunction() {
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  private String getCode() {
    return consumer.getCode();
  }

  private static class EmptyCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      return '\u0000';
    }
  }

  private static class BasicCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      return getCode().charAt(getCode().length() - 1);
    }
  }
}