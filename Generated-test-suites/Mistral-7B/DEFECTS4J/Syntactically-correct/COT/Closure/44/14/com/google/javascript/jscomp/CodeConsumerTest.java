import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = createTestee();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // We cannot check the position here as it's not accessible
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = createTestee();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = createTestee();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = createTestee();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetLastCharBeforeAppending() {
    CodeConsumer consumer = createTestee();
    assertEquals(Character.MIN_VALUE, consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = createTestee();
    consumer.addIdentifier("identifier");
    assertNotNull(consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = createTestee();
    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = createTestee();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = createTestee();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = createTestee();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = createTestee();
    consumer.append("test");
    consumer.maybeLineBreak();
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = createTestee();
    consumer.append("test");
    consumer.maybeCutLine();
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = createTestee();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = createTestee();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = createTestee();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = createTestee();
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = createTestee();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    assertEquals("{\n test\n}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = createTestee();
    consumer.append("test");
    consumer.listSeparator();
    assertEquals("test,\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = createTestee();
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementNoSemiColon() {
    CodeConsumer consumer = createTestee();
    consumer.append("test");
    consumer.endStatement(false);
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = createTestee();
    consumer.append("test");
    consumer.maybeEndStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = createTestee();
    consumer.beginBlock();
    consumer.append("function test() { }");
    consumer.endFunction();
    assertEquals("function test() { }\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = createTestee();
    consumer.beginBlock();
    consumer.append("function test() { }");
    consumer.endFunction(true);
    assertEquals("function test() { }\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = createTestee();
    consumer.append("case test:");
    consumer.beginCaseBody();
    assertEquals("case test:\n{\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = createTestee();
    consumer.append("case test:");
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    assertEquals("case test:\n{\n test\n}\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = createTestee();
    consumer.append("test");
    consumer.add("test2");
    assertEquals("testtest2\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = createTestee();
    consumer.append("test");
    consumer.addOp("+", true);
    consumer.append("test2");
    assertEquals("test + test2\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = createTestee();
    consumer.append("test");
    consumer.addNumber(1.2);
    assertEquals("test 1.2\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = createTestee();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForStatementContextTrue() {
    CodeConsumer consumer = createTestee();
    Node n = new Node();
    assertTrue(consumer.breakAfterBlockFor(n, true));
  }

  @Test
  public void testBreakAfterBlockForStatementContextFalse() {
    CodeConsumer consumer = createTestee();
    Node n = new Node();
    assertFalse(consumer.breakAfterBlockFor(n, false));
  }

  @Test
  public void testBreakAfterBlockForExpressionContextTrue() {
    CodeConsumer consumer = createTestee();
    Node n = new Node();
    assertTrue(consumer.breakAfterBlockFor(n, false));
  }

  @Test
  public void testBreakAfterBlockForExpressionContextFalse() {
    CodeConsumer consumer = createTestee();
    Node n = new Node();
    assertFalse(consumer.breakAfterBlockFor(n, true));
  }

  private CodeConsumer createTestee() {
    return new StubCodeConsumer();
  }

  static class StubCodeConsumer extends CodeConsumer {
    @Override
    public String getCode() {
      return new String(codeBuffer);
    }
  }
}