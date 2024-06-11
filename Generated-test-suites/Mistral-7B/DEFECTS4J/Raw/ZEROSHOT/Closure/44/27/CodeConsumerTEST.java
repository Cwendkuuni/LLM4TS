import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Assertions not possible here as startSourceMapping does not have any effect
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Assertions not possible here as endSourceMapping does not have any effect
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    // Assertions not possible here as maybeLineBreak does not have any effect
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    // Assertions not possible here as maybeCutLine does not have any effect
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    // Assertions not possible here as notePreferredLineBreak does not have any effect
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{\ntest\n}", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    assertEquals("{\ntest\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    assertEquals("test1,\ntest2", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    consumer.append("test2");
    assertEquals("test;\ntest2", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    consumer.append("test2");
    assertEquals("test;\ntest2", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    consumer.append("test2");
    assertEquals("test;\ntest2", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {}");
    consumer.endFunction();
    assertEquals("{\nfunction test() {\n}\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() { test(); }");
    consumer.endFunction(true);
    assertEquals("{\nfunction test() {\n  test();\n}\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (1) { case 1: }");
    assertEquals("switch (1) {\n case 1:\n}", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (1) { case 1: test(); }");
    consumer.endCaseBody();
    assertEquals("switch (1) {\n case 1:\n  test();\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.add(" test2");
    assertEquals("test test2", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", true);
    consumer.append("test2");
    assertEquals("test + test2", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", true);
    consumer.append("test2");
    assertEquals("test + test2", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addNumber(123);
    assertEquals("test 123", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, isNegativeZero(0.0));
    assertEquals(true, isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('1'));
    assertFalse(isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    node.setType(Node.BLOCK);
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    // Assertions not possible here as endFile does not have any effect
  }

  private static class TestCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    protected void append(String str) {
      code.append(str);
    }

    @Override
    protected char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    protected String getCode() {
      return code.toString();
    }
  }
}