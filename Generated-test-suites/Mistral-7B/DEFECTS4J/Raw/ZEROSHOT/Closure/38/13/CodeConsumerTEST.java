package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
    // We cannot verify the behavior of this method as it is abstract
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);
    // We cannot verify the behavior of this method as it is abstract
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    when(consumer.continueProcessing()).thenReturn(true);

    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingFalse() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    when(consumer.continueProcessing()).thenReturn(false);

    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");

    char lastChar = consumer.getLastChar();

    assert(lastChar == 'o');
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLastCharThrowsException() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.getLastChar();
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");

    String expectedString = "hello";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.appendBlockStart();

    String expectedString = "{\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.appendBlockEnd();

    String expectedString = "}\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.startNewLine();

    String expectedString = "\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.maybeLineBreak();

    String expectedString = "\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.maybeCutLine();

    String expectedString = "";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endLine();

    String expectedString = "\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.notePreferredLineBreak();

    String expectedString = "";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();

    String expectedString = "{\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();

    String expectedString = "{\ntest\n}";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);

    String expectedString = "{\ntest\n}";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.listSeparator();
    consumer.append("world");

    String expectedString = "hello,\nworld";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("if (true) {");
    consumer.endStatement();
    consumer.append("}");

    String expectedString = "if (true) {\n}\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("if (true) {");
    consumer.endStatement(true);
    consumer.append("}");

    String expectedString = "if (true) {\n};\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.maybeEndStatement();
    consumer.append("world");

    String expectedString = "hello;\nworld";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function test() {");
    consumer.endFunction();

    String expectedString = "function test() {\n}\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function test() {");
    consumer.append("return 1;");
    consumer.endFunction(true);

    String expectedString = "function test() {\n  return 1;\n}\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("switch (1) {");
    consumer.beginCaseBody();
    consumer.append("return 1;");
    consumer.endCaseBody();
    consumer.append("}");

    String expectedString = "switch (1) {\n  case 1:\n    return 1;\n}\n";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.add(" world");

    String expectedString = "hello world";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("1 + 1");

    String expectedString = "1 + 1";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testAddOpWithSpace() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("1 + ");
    consumer.addOp("1", true);

    String expectedString = "1 + 1";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("1 + ");
    consumer.addNumber(1.5);

    String expectedString = "1 + 1.5";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.shouldPreserveExtraBlocks();

    boolean result = consumer.shouldPreserveExtraBlocks();

    assert(result == false);
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);

    boolean result = consumer.breakAfterBlockFor(node, false);

    assert(result == false);
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);

    when(node.getType()).thenReturn(Node.BLOCK);

    boolean result = consumer.breakAfterBlockFor(node, true);

    assert(result == true);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endFile();

    String expectedString = "";

    String actualString = consumer.getContent();

    assert(expectedString.equals(actualString));
  }
}

class TestCodeConsumer extends CodeConsumer {
  private StringBuilder content = new StringBuilder();

  @Override
  public void append(String str) {
    content.append(str);
  }

  @Override
  public char getLastChar() {
    return content.charAt(content.length() - 1);
  }

  @Override
  public String getContent() {
    return content.toString();
  }
}