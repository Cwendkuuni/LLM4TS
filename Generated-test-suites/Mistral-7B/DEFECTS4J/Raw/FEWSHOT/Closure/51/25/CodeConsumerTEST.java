package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testIsWordChar() {
    char[] chars = new char[]{'_', '$', 'A', 'B', '0', '1', '2'};
    for (char c : chars) {
      assertTrue(CodeConsumer.isWordChar(c));
    }
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = null; // Replace this with a valid Node for testing
    boolean statementContext = true;
    assertTrue(consumer.breakAfterBlockFor(node, statementContext));
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingReturnsFalse() {
    CodeConsumer consumer = new TestCodeConsumerWithContinueProcessingFalse();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("foo");
    char lastChar = consumer.getLastChar();
    assertEquals('o', lastChar);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    consumer.append(" = ");
    consumer.addNumber(1);
    String expectedCode = "identifier = 1";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1.2);
    String expectedCode = "1.2";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddString() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("str");
    consumer.append(" = ");
    consumer.addString("\"foo\"");
    String expectedCode = "str = \"foo\"";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    String expectedCode = "{\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    String expectedCode = "}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.appendOp("+", true);
    consumer.append("b");
    String expectedCode = "a + b\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.addOp("+", true);
    consumer.append("b");
    String expectedCode = "a + b\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String expectedCode = "{\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("print(\"Hello\")");
    consumer.endBlock();
    String expectedCode = "{\nprint(\"Hello\");\n}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("print(\"Hello\")");
    consumer.endBlock(true);
    String expectedCode = "{\nprint(\"Hello\");\n}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a, ");
    consumer.append("b");
    String expectedCode = "a, b\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("print(\"Hello\")");
    consumer.endStatement();
    String expectedCode = "print(\"Hello\");\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("print(\"Hello\")");
    consumer.endStatement(true);
    String expectedCode = "print(\"Hello\");\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("print(\"Hello\")");
    consumer.maybeEndStatement();
    String expectedCode = "print(\"Hello\");\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("print(\"Hello\")\nprint(\"World\")");
    consumer.maybeCutLine();
    String expectedCode = "print(\"Hello\");\nprint(\"World\")\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("print(\"Hello\")");
    consumer.maybeLineBreak();
    String expectedCode = "print(\"Hello\")\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("print(\"Hello\")");
    consumer.notePreferredLineBreak();
    String expectedCode = "print(\"Hello\");\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    String expectedCode = ": \n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    consumer.append("print(\"Hello\")");
    consumer.endCaseBody();
    String expectedCode = ": \nprint(\"Hello\")\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.add(" b");
    String expectedCode = "a b\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {\n");
    consumer.append(" print(\"Hello\")\n");
    consumer.endFunction();
    String expectedCode = "function foo() {\nprint(\"Hello\");\n}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {\n");
    consumer.append(" print(\"Hello\")\n");
    consumer.append("}\n");
    consumer.endFunction(true);
    String expectedCode = "function foo() {\nprint(\"Hello\");\n}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatementAfterFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {\n");
    consumer.append(" print(\"Hello\")\n");
    consumer.maybeEndStatement();
    String expectedCode = "function foo() {\nprint(\"Hello\");\n}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  private abstract static class TestCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    void add(String newcode) {
      code.append(newcode);
    }

    @Override
    void append(String str) {
      code.append(str);
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    String getCode() {
      return code.toString();
    }
  }

  private static class TestCodeConsumerWithContinueProcessingFalse extends TestCodeConsumer {
    @Override
    boolean continueProcessing() {
      return false;
    }
  }
}