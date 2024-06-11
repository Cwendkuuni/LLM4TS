import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    assertEquals('t', consumer.getLastChar());
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
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    assertEquals("", consumer.getCode());
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
    assertEquals("", consumer.getCode());
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
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    consumer.endStatement(true);
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test1");
    consumer.addIdentifier("test2");
    consumer.maybeEndStatement();
    assertEquals("test2;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    consumer.endFunction();
    assertEquals("test();\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    consumer.endFunction(true);
    assertEquals("test();\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endCaseBody();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test1");
    consumer.addIdentifier("test2");
    consumer.add("+");
    consumer.addIdentifier("test3");
    assertEquals("test1 test2 + test3;\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test1");
    consumer.addIdentifier("test2");
    consumer.addOp("+", true);
    consumer.addIdentifier("test3");
    assertEquals("test1 test2 + test3;\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(123.456);
    assertEquals("123.456;\n", consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testIsNegativeZero() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(-0.0);
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('z'));
    assertFalse(consumer.isWordChar(' '));
    assertFalse(consumer.isWordChar('0'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForSingleStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBreakAfterBlockForMultipleStatements() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node1 = new Node();
    Node node2 = new Node();
    consumer.appendBlockStart();
    consumer.addIdentifier("statement1");
    consumer.addOp(";", true);
    consumer.appendBlockStart();
    consumer.addIdentifier("statement2");
    consumer.addOp(";", true);
    consumer.appendBlockEnd();
    assertTrue(consumer.breakAfterBlockFor(node1, false));
    assertEquals("statement1;\n", consumer.getCode());
    assertFalse(consumer.breakAfterBlockFor(node2, false));
    assertEquals("statement1;\nstatement2;\n", consumer.getCode());
  }

  @Test
  public void testBreakAfterBlockForMultipleStatementsWithNewlines() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node1 = new Node();
    Node node2 = new Node();
    consumer.appendBlockStart();
    consumer.addIdentifier("statement1");
    consumer.addOp(";", true);
    consumer.appendBlockStart();
    consumer.addIdentifier("statement2");
    consumer.addOp(";", true);
    consumer.appendBlockEnd();
    assertTrue(consumer.breakAfterBlockFor(node1, true));
    assertEquals("statement1;\n", consumer.getCode());
    assertTrue(consumer.breakAfterBlockFor(node2, true));
    assertEquals("statement1;\n\nstatement2;\n", consumer.getCode());
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  static class TestCodeConsumer extends CodeConsumer {
    char[] code = new char[128];
    int codeLength = 0;

    @Override
    void add(String newcode) {
      super.add(newcode);
      code = new char[codeLength + newcode.length() + 1];
      System.arraycopy(code, 0, code, 0, codeLength);
      System.arraycopy(newcode.toCharArray(), 0, code, codeLength, newcode.length());
      codeLength += newcode.length();
    }

    @Override
    char getLastChar() {
      return code[codeLength - 1];
    }

    @Override
    String getCode() {
      return new String(code, 0, codeLength);
    }
  }
}