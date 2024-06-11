import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();

    assertEquals(true, consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();

    for (char c = 'a'; c <= 'z'; c++) {
      consumer.add("abc");
      assertEquals(c, consumer.getLastChar());
    }
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("hello world");
    assertEquals("hello world", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockShouldEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);

    assertEquals("{\n test\n}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementShouldAddSemiColon() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("test");
    consumer.endStatement(true);
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.append("test");
    consumer.maybeEndStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.endFunction();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionShouldAddStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.beginBlock();
    consumer.append("test");
    consumer.endFunction(true);

    assertEquals("{\n test\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.endCaseBody();
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.add("abc");
    assertEquals("abc", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.addIdentifier("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.appendOp("+", false);
    assertEquals("+", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.addOp("+", false);
    assertEquals("+", consumer.getCode());
  }

  @Test
  public void testAddOpBinary() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.addOp("+", true);
    assertEquals(" + ", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.addNumber(1.0);
    assertEquals("1.0\n", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new DummyCodeConsumer();

    assertEquals(false, consumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new DummyCodeConsumer();

    for (char c = 'a'; c <= 'z'; c++) {
      assertEquals(true, consumer.isWordChar(c));
    }

    assertEquals(true, consumer.isWordChar('_'));
    assertEquals(true, consumer.isWordChar('$'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();

    assertEquals(false, consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();

    Node node = mock(Node.class);
    boolean statementContext = true;

    assertEquals(statementContext, consumer.breakAfterBlockFor(node, statementContext));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();

    consumer.endFile();
  }

  static class DummyCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    void add(String newcode) {
      code.append(newcode);
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    void append(String str) {
      code.append(str);
    }

    @Override
    String getCode() {
      return code.toString();
    }
  }
}