import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {
  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Assertions can't be made here as this method is abstract
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Assertions can't be made here as this method is abstract
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    assertEquals("test", consumer.toString());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.toString());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.toString());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeLineBreak();
    assertEquals("test\n", consumer.toString());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeCutLine();
    assertEquals("test\n", consumer.toString());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    assertEquals("test\n", consumer.toString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.toString());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{\ntest\n}", consumer.toString());
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    assertEquals("{\ntest\n}", consumer.toString());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.listSeparator();
    consumer.append("anotherTest");
    assertEquals("test,\nanotherTest\n", consumer.toString());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.toString());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    assertEquals("test;\n", consumer.toString());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    consumer.append("anotherTest");
    assertEquals("test;\nanotherTest\n", consumer.toString());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("}");
    consumer.endFunction();
    assertEquals("{\nfunction test() {\n}\n}", consumer.toString());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("}");
    consumer.endFunction(true);
    assertEquals("{\nfunction test() {\n}\n}", consumer.toString());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("case label:");
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    assertEquals("case label:\n  test\n", consumer.toString());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.add(" anotherTest");
    assertEquals("test anotherTest\n", consumer.toString());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier\n", consumer.toString());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", true);
    consumer.append("anotherTest");
    assertEquals("test + anotherTest\n", consumer.toString());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.addNumber(123);
    assertEquals("test 123\n", consumer.toString());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('2'));
    assertFalse(isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.toString());
  }

  private static class StubCodeConsumer extends CodeConsumer {
    @Override
    void add(String newcode) {
      super.add(newcode);
    }

    @Override
    char getLastChar() {
      return super.getLastChar();
    }

    @Override
    void append(String str) {
      super.append(str);
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
    void maybeCutLine() {
      super.maybeCutLine();
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
    void appendOp(String op, boolean binOp) {
      super.appendOp(op, binOp);
    }

    @Override
    void addOp(String op, boolean binOp) {
      super.addOp(op, binOp);
    }

    @Override
    void addNumber(double x) {
      super.addNumber(x);
    }

    @Override
    boolean shouldPreserveExtraBlocks() {
      return super.shouldPreserveExtraBlocks();
    }

    @Override
    boolean breakAfterBlockFor(Node n, boolean statementContext) {
      return super.breakAfterBlockFor(n, statementContext);
    }

    @Override
    void endFile() {
      super.endFile();
    }

    @Override
    void startSourceMapping(Node node) {
      // Empty implementation for testing purposes
    }

    @Override
    void endSourceMapping(Node node) {
      // Empty implementation for testing purposes
    }

    @Override
    boolean continueProcessing() {
      // Empty implementation for testing purposes
      return super.continueProcessing();
    }

    @Override
    void append(Node node) {
      // Empty implementation for testing purposes
    }

    @Override
    void startSourceMapping(int lineNumber, int linePositionInLine) {
      // Empty implementation for testing purposes
    }

    @Override
    void endSourceMapping(int lineNumber, int linePositionInLine) {
      // Empty implementation for testing purposes
    }

    @Override
    void setSourceName(String sourceFileName) {
      // Empty implementation for testing purposes
    }

    @Override
    void setSourceStart(int lineNumber, int linePositionInLine) {
      // Empty implementation for testing purposes
    }

    @Override
    void setSourceEnd(int lineNumber, int linePositionInLine) {
      // Empty implementation for testing purposes
    }
  }
}