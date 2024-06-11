import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check that no code was added to the consumer
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Check that no code was added to the consumer
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
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
  public void testAppendBlockStartAndEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    consumer.appendBlockEnd();
    assertEquals("{\n}", consumer.getCode());
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
    consumer.startNewLine();
    consumer.maybeLineBreak();
    assertEquals("\n\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("ab");
    consumer.maybeCutLine();
    assertEquals("ab\n", consumer.getCode());
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
    assertEquals("\n", consumer.getCode());
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
    assertEquals("{\n}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock(true);
    assertEquals("{\n}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("ab");
    consumer.endStatement();
    assertEquals("ab;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("ab");
    consumer.endStatement(true);
    assertEquals("ab;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("ab");
    consumer.maybeEndStatement();
    assertEquals("ab\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.endFunction();
    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {\nreturn 1;\n}");
    consumer.endFunction(true);
    assertEquals("function foo() {\n  return 1;\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("case 1:");
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endBlock();
    assertEquals("switch (x) {\n  case 1:\n}\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("case 1:");
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endBlock();
    assertEquals("switch (x) {\n  case 1:\n}\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("ab");
    consumer.add("cd");
    assertEquals("abcd\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("ab");
    consumer.addOp("+", true);
    consumer.add("cd");
    assertEquals("ab + cd\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("ab");
    consumer.addNumber(123);
    assertEquals("ab 123\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(new StubCodeConsumer().shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (x > 0) { x++; }");
    assertTrue(consumer.breakAfterBlockFor(new Node(), false));
    assertEquals("if (x > 0) {\n  x++;\n}", consumer.getCode());
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
    assertEquals("if (x > 0) {\n  x++;\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testThrowAssertionError() {
    CodeConsumer consumer = new ThrowAssertionErrorConsumer();
    consumer.append("ab");
  }

  private static class StubCodeConsumer extends CodeConsumer {
    @Override
    protected char getLastChar() {
      return getCode().charAt(getCode().length() - 1);
    }

    @Override
    protected void add(String newcode) {
      super.add(newcode);
    }

    @Override
    protected void append(String str) {
      super.append(str);
    }

    @Override
    public String getCode() {
      return code;
    }

    @Override
    public void endFile() {
      // Do nothing
    }

    private String code = "";
  }

  private static class ThrowAssertionErrorConsumer extends CodeConsumer {
    @Override
    public void append(String str) {
      throw new AssertionError("Unexpected append call");
    }
  }
}