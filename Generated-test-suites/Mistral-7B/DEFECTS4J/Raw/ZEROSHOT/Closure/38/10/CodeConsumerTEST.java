import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @Test
  public void testStartSourceMapping() {
    consumer = new StubCodeConsumer();
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    consumer = new StubCodeConsumer();
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer = new StubCodeConsumer();
    assertEquals(' ', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    consumer = new StubCodeConsumer();
    String identifier = "identifier";
    consumer.addIdentifier(identifier);
    assertEquals(identifier, consumer.getCode());
  }

  @Test
  public void testAppend() {
    consumer = new StubCodeConsumer();
    String str = "string";
    consumer.append(str);
    assertEquals(str, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
  }

  @Test
  public void testEndLine() {
    consumer = new StubCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer = new StubCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer = new StubCodeConsumer();
    consumer.endStatement();
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer = new StubCodeConsumer();
    String code = "int x = 5;";
    consumer.setCode(code);
    consumer.endStatement(true);
    assertEquals("int x = 5;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer = new StubCodeConsumer();
    consumer.maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    consumer = new StubCodeConsumer();
    consumer.endFunction();
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer = new StubCodeConsumer();
    consumer.endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    consumer = new StubCodeConsumer();
    consumer.endCaseBody();
  }

  @Test
  public void testAdd() {
    consumer = new StubCodeConsumer();
    String newcode = "newcode";
   consumer.add(newcode);
    assertEquals(newcode, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer = new StubCodeConsumer();
    String op = "+";
    consumer.addOp(op, false);
    assertEquals(" + ", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer = new StubCodeConsumer();
    consumer.addNumber(5.5);
    assertEquals("5.5\n", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    consumer = new StubCodeConsumer();
    assertEquals(false, CodeConsumerTest.class.getInstance().isNegativeZero(0));
    assertEquals(true, CodeConsumerTest.class.getInstance().isNegativeZero(-0));
  }

  @Test
  public void testIsWordChar() {
    consumer = new StubCodeConsumer();
    assertEquals(true, CodeConsumerTest.class.getInstance().isWordChar('_'));
    assertEquals(true, CodeConsumerTest.class.getInstance().isWordChar('$'));
    assertEquals(true, CodeConsumerTest.class.getInstance().isWordChar('a'));
    assertEquals(true, CodeConsumerTest.class.getInstance().isWordChar('1'));
    assertEquals(false, CodeConsumerTest.class.getInstance().isWordChar('.'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    consumer = new StubCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    consumer = new StubCodeConsumer();
    Node node = mock(Node.class);
    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    consumer = new StubCodeConsumer();
    consumer.endFile();
  }

  private static class StubCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    public void add(String newcode) {
      super.add(newcode);
      code.append(getCode());
      setCode(code.toString());
    }

    @Override
    public void append(String str) {
      super.append(str);
      code.append(str);
    }

    @Override
    public void appendOp(String op, boolean binOp) {
      super.appendOp(op, binOp);
      code.append(getCode());
      setCode(code.toString());
    }

    @Override
    public void addNumber(double x) {
      super.addNumber(x);
      code.append(getCode());
      setCode(code.toString());
    }

    @Override
    public void startSourceMapping(Node node) {
      super.startSourceMapping(node);
      code.append("startSourceMapping(node)\n");
    }

    @Override
    public void endSourceMapping(Node node) {
      super.endSourceMapping(node);
      code.append("endSourceMapping(node)\n");
    }

    @Override
    public void continueProcessing() {
      super.continueProcessing();
      code.append("continueProcessing()\n");
    }

    @Override
    public void endFile() {
      super.endFile();
      code.append("endFile()\n");
    }

    @Override
    public char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    public void setCode(String code) {
      super.setCode(code);
      this.code = new StringBuilder(code);
    }

    @Override
    public boolean shouldPreserveExtraBlocks() {
      return false;
    }

    @Override
    public boolean breakAfterBlockFor(Node n, boolean statementContext) {
      return false;
    }
  }

}