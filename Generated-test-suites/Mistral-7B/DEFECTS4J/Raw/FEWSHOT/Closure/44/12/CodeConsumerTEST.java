package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot assert anything here, as this method is abstract
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
    // Cannot assert anything here, as this method is abstract
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());

    consumer = new FakeCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    String code = "";
    consumer.append(code);
    char lastChar = consumer.getLastChar();
    assertTrue(Character.isWhitespace(lastChar));

    code = "foo";
    consumer.append(code);
    lastChar = consumer.getLastChar();
    assertEquals('f', lastChar);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    String identifier = "foo";
    consumer.addIdentifier(identifier);
    String expectedCode = identifier;
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    String str = "foo";
    consumer.append(str);
    String expectedCode = str;
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    String expectedCode = "{\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    String expectedCode = "\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    String expectedCode = "\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    String expectedCode = "";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    String expectedCode = "";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    String expectedCode = "\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    String expectedCode = "";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    String expectedCode = "{\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock();
    String expectedCode = "\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    String expectedCode = "\n}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    String expectedCode = ", ";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement();
    String expectedCode = ";";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndStatementWithLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement(true);
    String expectedCode = ";;\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    String code = ";";
    consumer.maybeEndStatement();
    consumer.add(code);
    String expectedCode = code;
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    String expectedCode = "\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction(true);
    String expectedCode = "\n}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    String expectedCode = ": ";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    String expectedCode = "";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    String code = "foo";
    consumer.add(code);
    String expectedCode = code;
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddStringWithSpaces() {
    CodeConsumer consumer = new StubCodeConsumer();
    String code = "foo bar";
    consumer.add(code);
    String expectedCode = code;
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    String op = "+";
    consumer.addOp(op, false);
    String expectedCode = op;
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddOpWithSpace() {
    CodeConsumer consumer = new StubCodeConsumer();
    String op = "+";
    consumer.addOp(op, true);
    String expectedCode = " " + op;
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    double number = 123.45;
    consumer.addNumber(number);
    String expectedCode = Double.toString(number);
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new StubCodeConsumer();
    double number = -0.0;
    assertTrue(consumer.isNegativeZero(number));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    char ch = '_';
    assertTrue(consumer.isWordChar(ch));

    ch = 'a';
    assertTrue(consumer.isWordChar(ch));

    ch = '1';
    assertTrue(consumer.isWordChar(ch));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));

    consumer = new FakeCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    String expectedCode = "";
    assertEquals(expectedCode, consumer.getCode());
  }
}

class StubCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return ' ';
  }

  @Override
  String getCode() {
    return "";
  }
}

class FakeCodeConsumer extends CodeConsumer {
  @Override
  boolean continueProcessing() {
    return false;
  }
}