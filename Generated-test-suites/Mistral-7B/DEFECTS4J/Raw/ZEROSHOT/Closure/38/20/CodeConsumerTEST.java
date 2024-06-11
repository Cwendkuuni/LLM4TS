import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node("");
    consumer.startSourceMapping(node);
    // Cannot assert anything here as there is no implementation in the given abstract class
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node("");
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Cannot assert anything here as there is no implementation in the given abstract class
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcd");
    assertEquals('d', consumer.getLastChar());
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
    consumer.append("abcd");
    assertEquals("abcd", consumer.getCode());
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
    consumer.append("abcd");
    consumer.maybeLineBreak();
    assertEquals("abcd\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcd");
    consumer.maybeCutLine();
    assertEquals("abcd\n", consumer.getCode());
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
    // Cannot assert anything here as there is no implementation in the given abstract class
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
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("abcd");
    consumer.endBlock(true);
    assertEquals("{\naborcd}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcd");
    consumer.listSeparator();
    assertEquals("abcd,\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcd");
    consumer.endStatement();
    assertEquals("abcd;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcd");
    consumer.endStatement(true);
    assertEquals("abcd;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcd");
    consumer.maybeEndStatement();
    assertEquals("abcd;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatementWithNoSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcd");
    consumer.maybeEndStatement();
    assertEquals("abcd", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction();
    assertEquals("function foo() {\n}:\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() { return 1; }");
    consumer.endFunction(true);
    assertEquals("function foo() {\n  return 1;\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case label:");
    consumer.beginCaseBody();
    assertEquals("case label:\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case label:\n");
    consumer.beginCaseBody();
    consumer.append("return 1;");
    consumer.endCaseBody();
    assertEquals("case label:\n  return 1;\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcd");
    consumer.add("efg");
    assertEquals("abcdefg", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("1 + 2");
    consumer.addOp("+", true);
    assertEquals("1 + 2 +", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("1 + ");
    consumer.addNumber(2.5);
    assertEquals("1 + 2.5", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node("");
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  private static class TestCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

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
}