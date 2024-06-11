import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Verify no exception was thrown
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Verify no exception was thrown
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.continueProcessing());
    // Verify the method returns false
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");
    assertEquals("abc", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");
    consumer.maybeLineBreak();
    assertEquals("abc\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");
    consumer.maybeCutLine();
    assertEquals("abc", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    // No output is expected
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("def foo() {\n  return 1;\n}");
    consumer.endBlock(true);
    assertEquals("{\n  def foo() {\n    return 1;\n  }\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");
    consumer.listSeparator();
    consumer.append("def foo() {\n  return 1;\n}");
    assertEquals("abc,\ndef foo() {\n  return 1;\n}", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("def foo() {\n  ");
    consumer.endStatement();
    consumer.append("return 1;\n}");
    consumer.endStatement();
    assertEquals("def foo() {\n  return 1;\n}", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("def foo() {\n  ");
    consumer.endStatement(true);
    consumer.append("return 1;\n}");
    consumer.endStatement();
    assertEquals("def foo() {\n  return 1;\n}", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("def foo() {\n  return 1;\n}");
    consumer.maybeEndStatement();
    assertEquals("def foo() {\n  return 1;\n}", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {\n}");
    consumer.endFunction();
    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {\n  return 1;\n}");
    consumer.endFunction(true);
    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("switch (x) {\n  case 1:\n");
    consumer.beginCaseBody();
    consumer.append("return 1;\n");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case 1:\n    return 1;\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("def foo() {\n  ");
    consumer.add("return 1;");
    consumer.endStatement();
    assertEquals("def foo() {\n  return 1;\n}", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("def foo() {\n  ");
    consumer.addOp("+ 1", true);
    consumer.append("x");
    consumer.endStatement();
    assertEquals("def foo() {\n  x += 1;\n}", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("def foo() {\n  ");
    consumer.addNumber(1.23);
    consumer.endStatement();
    assertEquals("def foo() {\n  return 1.23;\n}", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, isNegativeZero(0));
    assertEquals(true, isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('A'));
    assertTrue(isWordChar('Z'));
    assertTrue(isWordChar('0'));
    assertTrue(isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    StubCodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    StubCodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    // No output is expected
  }

  private static class StubCodeConsumer extends CodeConsumer {

    @Override
    void add(String newCode) {
      super.add(newCode);
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
    void beginCaseBody() {
      super.beginCaseBody();
    }

    @Override
    void endCaseBody() {
      super.endCaseBody();
    }

    @Override
    void startSourceMapping(Node node) {
      super.startSourceMapping(node);
    }

    @Override
    void endSourceMapping(Node node) {
      super.endSourceMapping(node);
    }

    @Override
    boolean continueProcessing() {
      return super.continueProcessing();
    }

    @Override
    char getLastChar() {
      return super.getLastChar();
    }

    @Override
    String getCode() {
      return super.getCode();
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
  }
}