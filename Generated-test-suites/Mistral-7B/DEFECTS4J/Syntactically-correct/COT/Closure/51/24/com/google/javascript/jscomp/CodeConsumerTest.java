import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if the method was called
    assertTrue(consumer.wasStartSourceMappingCalled);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
    // Check if the method was called
    assertTrue(consumer.wasEndSourceMappingCalled);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.continueProcessing());
    // Derived classes should return false to stop further processing
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    char lastChar = consumer.getLastChar();
    assertEquals('o', lastChar);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    String expectedCode = "identifier";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    String expectedCode = "hello";
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
    String expectedCode = "}\n";
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
    String expectedCode = "}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndBlockWithShouldEndLineTrue() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    String expectedCode = "}\n";
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
  public void testEndStatementWithNeedSemiColonTrue() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement(true);
    String expectedCode = ";";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeEndStatement();
    String expectedCode = "";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    String expectedCode = "}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContextTrue() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction(true);
    String expectedCode = "\n";
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
    consumer.add("hello world");
    String expectedCode = "hello world";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addOp("+", false);
    String expectedCode = "+";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(123.45);
    String expectedCode = "123.45";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testIsWordCharWithInvalidChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(CodeConsumer.isWordChar((char) 0));
  }

  @Test
  public void testShouldPreserveExtraBlocksWithSingleStatementFalse() {
    CodeConsumer consumer = new StubCodeConsumer();
    boolean result = consumer.shouldPreserveExtraBlocks();
    assertFalse(result);
  }

  @Test
  public void testBreakAfterBlockForWithStatementContextTrue() {
    CodeConsumer consumer = new StubCodeConsumer();
    boolean result = consumer.breakAfterBlockFor(new Node(), true);
    assertTrue(result);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    String expectedCode = "";
    assertEquals(expectedCode, consumer.getCode());
  }

  private static class StubCodeConsumer extends CodeConsumer {
    String code = "";
    boolean wasStartSourceMappingCalled = false;
    boolean wasEndSourceMappingCalled = false;
    boolean wasEndStatementCalled = false;
    boolean wasEndFunctionCalled = false;

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    String getCode() {
      return code;
    }

    @Override
    void startSourceMapping(Node node) {
      wasStartSourceMappingCalled = true;
    }

    @Override
    void endSourceMapping(Node node) {
      wasEndSourceMappingCalled = true;
    }

    @Override
    void endStatement() {
      wasEndStatementCalled = true;
    }

    @Override
    void endStatement(boolean needSemiColon) {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void endFunction() {
      wasEndFunctionCalled = true;
    }

    @Override
    void endFunction(boolean statementContext) {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void beginBlock() {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void endBlock() {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void endBlock(boolean shouldEndLine) {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void listSeparator() {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void beginCaseBody() {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void endCaseBody() {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void add(String newcode) {
      code += newcode;
    }

    @Override
    void append(String str) {
      code += str;
    }

    @Override
    void appendBlockStart() {
      code += "{";
    }

    @Override
    void appendBlockEnd() {
      code += "}\n";
    }

    @Override
    void startNewLine() {
      code += "\n";
    }

    @Override
    void maybeLineBreak() {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void maybeCutLine() {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void endLine() {
      code += "\n";
    }

    @Override
    void notePreferredLineBreak() {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void appendOp(String op, boolean binOp) {
      code += op;
    }

    @Override
    void addOp(String op, boolean binOp) {
      // JUnit does not support testing abstract methods.
    }

    @Override
    void addNumber(double x) {
      code += String.valueOf(x);
    }

    @Override
    boolean shouldPreserveExtraBlocks() {
      return false;
    }

    @Override
    boolean breakAfterBlockFor(Node n, boolean statementContext) {
      return statementContext;
    }

    @Override
    void endFile() {
      // JUnit does not support testing abstract methods.
    }
  }
}