package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor_statementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.breakAfterBlockFor(null, true));
  }

  @Test
  public void testBreakAfterBlockFor_noStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(null, false));
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testConsumeIdentifiers() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addIdentifier("identifier1");
    consumer.addIdentifier("identifier2");
    consumer.addIdentifier("identifier3");

    String expected = "identifier1identifier2identifier3";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testConsumeStrings() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("string1");
    consumer.append("string2");
    consumer.append("string3");

    String expected = "string1string2string3";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testConsumeNumbers() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addNumber(1.0);
    consumer.addNumber(2.0);
    consumer.addNumber(3.0);

    String expected = "1.02.03.0";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testConsumeOperators() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addOp("+");
    consumer.addOp("*");

    String expected = "+*";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testConsumeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endStatement();
    consumer.endStatement(true);

    String expected = ";";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testConsumeFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginFunction();
    consumer.endFunction();
    consumer.beginFunction(true);
    consumer.endFunction(true);

    String expected = "{}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testConsumeBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.append("blockContent");
    consumer.endBlock();

    String expected = "{\nblockContent\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testConsumeCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginCaseBody();
    consumer.append("caseBodyContent");
    consumer.endCaseBody();

    String expected = ":caseBodyContent\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  private static class DummyCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      return '\u0000';
    }

    @Override
    String getCode() {
      return toString();
    }
  }
}