import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // TODO: Verify that the source mapping was started correctly
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    assertEquals("abc", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
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
    consumer.append("abc");
    consumer.maybeLineBreak();
    assertEquals("abc\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("ab");
    consumer.maybeCutLine();
    assertEquals("a\nb", consumer.getCode());
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
    // TODO: Verify that a line break was added
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
    consumer.append("abc");
    consumer.endBlock();
    assertEquals("abc}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.endBlock(true);
    assertEquals("abc}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.listSeparator();
    assertEquals("abc,\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.endStatement();
    assertEquals("abc;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.endStatement(true);
    assertEquals("abc;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.maybeEndStatement();
    assertEquals("abc", consumer.getCode());
    consumer.append(";");
    consumer.maybeEndStatement();
    assertEquals("abc;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {\n");
    consumer.endFunction();
    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @Test 
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {\nreturn 42;\n}");
    consumer.endFunction(true);
    assertEquals("function foo() {\nreturn 42;\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case foo:\n");
    consumer.beginCaseBody();
    assertEquals("case foo:\n{\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case foo:\n{\nreturn 42;\n}");
    consumer.endCaseBody();
    assertEquals("case foo:\n{\nreturn 42;\n}\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.add("def");
    assertEquals("abcdef", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.addIdentifier("def");
    assertEquals("abcdef", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.addOp("+", false);
    consumer.add("def");
    consumer.addOp("+", false);
    assertEquals("abc + def +", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.addNumber(123);
    assertEquals("abc 123", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('Z'));
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('z'));
    assertFalse(consumer.isWordChar(' '));
    assertFalse(consumer.isWordChar('.'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node statementNode = new Node();
    boolean result = consumer.breakAfterBlockFor(statementNode, true);
    assertTrue(result);
  }

  @Test
  public void testBreakAfterBlockForStatementFalse() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node statementNode = new Node();
    boolean result = consumer.breakAfterBlockFor(statementNode, false);
    assertFalse(result);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    // TODO: Verify that no code was written to the output
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {}\n");
    assertTrue(consumer.sawFunction);
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  public void getCode() {
    throw new UnsupportedOperationException();
  }
}