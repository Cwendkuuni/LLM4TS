import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot assert on private fields directly, so let's check if the method was called
    assertTrue(consumer.sourceMappingStarted);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertFalse(consumer.sourceMappingStarted);
  }

  @Test
  public void testContinueProcessing() {
    assertFalse(consumer.continueProcessing());

    consumer.statementNeedsEnded = true;
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("foo");
    assertEquals('f', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    consumer.append("bar");
    assertEquals("bar", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.append("foo");
    consumer.startNewLine();
    consumer.append("bar");
    assertEquals("foo\nbar", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.append("foo");
    consumer.maybeLineBreak();
    consumer.append("bar");
    assertEquals("foo\nbar", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    consumer.append("foo");
    consumer.endLine();
    consumer.append("bar");
    assertEquals("foo\nbar", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.append("foo");
    consumer.notePreferredLineBreak();
    consumer.append("bar");
    assertEquals("foo\nbar", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.append("foo");
    consumer.beginBlock();
    consumer.append("bar");
    consumer.endBlock();
    consumer.append("baz");
    assertEquals("foo\n{\nbar\n}\nbaz", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer.append("foo");
    consumer.listSeparator();
    consumer.append("bar");
    assertEquals("foo,\nbar", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("foo");
    consumer.endStatement();
    consumer.append("bar");
    assertEquals("foo;\nbar", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer.append("foo");
    consumer.endStatement(true);
    consumer.append("bar");
    assertEquals("foo;\nbar", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("foo");
    consumer.maybeEndStatement();
    consumer.append("bar");
    assertEquals("foo;\nbar", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.append("function foo() {");
    consumer.endFunction();
    consumer.append("}");
    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.append("function foo() {\n  bar;\n}");
    consumer.endFunction(true);
    consumer.append("baz");
    assertEquals("function foo() {\n  bar;\n}\nbaz", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (x) {\n  case 1:\n    foo;\n}");
    consumer.beginCaseBody();
    consumer.append("bar");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case 1:\n    foo;\n    bar\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.add("foo");
    consumer.append("bar");
    assertEquals("foo\nbar", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("foo");
    consumer.append("bar");
    assertEquals("foo\nbar", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.append("foo");
    consumer.addOp("+", true);
    consumer.append("bar");
    assertEquals("foo + bar\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.addNumber(123);
    assertEquals("123\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('0'));
    assertFalse(isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());

    consumer = new TestCodeConsumerFalse();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    assertTrue(consumer.breakAfterBlockFor(new Node(), false));

    consumer = new TestCodeConsumerTrueStatementContext();
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    consumer.append("foo");
    consumer.endFile();
    assertEquals("foo\n", consumer.getCode());
  }

  @Test
  public void testSawFunction() {
    consumer.append("function foo() {}", 0);
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  private String getCode() {
    return consumer.getCode().getCode();
  }
}

class TestCodeConsumer extends CodeConsumer {
  private boolean sourceMappingStarted = false;

  @Override
  void startSourceMapping(Node node) {
    super.startSourceMapping(node);
    sourceMappingStarted = true;
  }

  @Override
  void endSourceMapping(Node node) {
    super.endSourceMapping(node);
    sourceMappingStarted = false;
  }
}

class TestCodeConsumerFalse extends CodeConsumer {
  @Override
  boolean shouldPreserveExtraBlocks() {
    return false;
  }
}

class TestCodeConsumerTrueStatementContext extends CodeConsumer {
  @Override
  boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return statementContext;
  }
}