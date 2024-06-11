import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;
import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Assertions cannot be made here as startSourceMapping is an abstract method
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Assertions cannot be made here as endSourceMapping is an abstract method
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
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    assertEquals("test", consumer.toString());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.toString());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.toString());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeLineBreak();
    assertEquals("test\n", consumer.toString());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeCutLine();
    assertEquals("test", consumer.toString());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.toString());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{\ntest\n}", consumer.toString());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    assertEquals("{\ntest\n}", consumer.toString());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.toString());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.toString());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test;");
    consumer.endStatement();
    assertEquals("test;\n", consumer.toString());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    assertEquals("test;\n", consumer.toString());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endFunction();
    assertEquals("{\ntest\n}\n", consumer.toString());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endFunction(true);
    assertEquals("{\ntest\n}\n", consumer.toString());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.toString());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    assertEquals(":\n", consumer.toString());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test");
    assertEquals("test", consumer.toString());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    assertEquals("test", consumer.toString());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", false);
    assertEquals("+", consumer.toString());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(42);
    assertEquals("42", consumer.toString());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.toString());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidMethodCall() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.invalidMethod();
  }

  private static class TestCodeConsumer extends CodeConsumer {
    @Override
    void add(String newCode) {
      super.add(newCode);
      assertEquals(newCode, getLastN(toString(), newCode.length()));
    }

    @Override
    void append(String str) {
      super.append(str);
    }

    @Override
    void appendBlockStart() {
      super.appendBlockStart();
      assertEquals("{\n", toString());
    }

    @Override
    void appendBlockEnd() {
      super.appendBlockEnd();
      assertEquals("}\n", toString());
    }

    @Override
    void startNewLine() {
      super.startNewLine();
      assertEquals("\n", toString());
    }

    @Override
    void maybeCutLine() {
      super.maybeCutLine();
      assertEquals(toString(), getLastN(toString(), 1));
    }

    @Override
    void endLine() {
      super.endLine();
      assertEquals("\n", toString());
    }

    @Override
    void notePreferredLineBreak() {
      super.notePreferredLineBreak();
      assertEquals("\n", toString());
    }

    @Override
    void beginBlock() {
      super.beginBlock();
      assertEquals("{\n", toString());
    }

    @Override
    void endBlock() {
      super.endBlock();
      assertEquals("}\n", toString());
    }

    @Override
    void endBlock(boolean shouldEndLine) {
      super.endBlock(shouldEndLine);
      assertEquals(toString(), getLastN(toString(), 1));
    }

    @Override
    void listSeparator() {
      super.listSeparator();
      assertEquals(",\n", toString());
    }

    @Override
    void endStatement() {
      super.endStatement();
      assertEquals(";\n", toString());
    }

    @Override
    void endStatement(boolean needSemiColon) {
      super.endStatement(needSemiColon);
      assertEquals(toString(), getLastN(toString(), 1));
    }

    @Override
    void maybeEndStatement() {
      super.maybeEndStatement();
      assertEquals(";\n", toString());
    }

    @Override
    void endFunction() {
      super.endFunction();
      assertEquals("}\n", toString());
    }

    @Override
    void endFunction(boolean statementContext) {
      super.endFunction(statementContext);
      assertEquals("}\n", toString());
    }

    @Override
    void beginCaseBody() {
      super.beginCaseBody();
      assertEquals(":\n", toString());
    }

    @Override
    void endCaseBody() {
      super.endCaseBody();
      assertEquals("", toString());
    }

    @Override
    void addIdentifier(String identifier) {
      super.addIdentifier(identifier);
      assertEquals(identifier, getLastN(toString(), identifier.length()));
    }

    @Override
    void appendOp(String op, boolean binOp) {
      super.appendOp(op, binOp);
      assertEquals(op, toString());
    }

    @Override
    void addOp(String op, boolean binOp) {
      super.addOp(op, binOp);
      assertEquals(op, getLastN(toString(), op.length()));
    }

    @Override
    void addNumber(double x) {
      super.addNumber(x);
      assertEquals(String.valueOf(x), toString());
    }

    @Override
    boolean shouldPreserveExtraBlocks() {
      return false;
    }

    @Override
    boolean breakAfterBlockFor(Node n, boolean statementContext) {
      return false;
    }

    @Override
    void endFile() {
      super.endFile();
      assertEquals("", toString());
    }

    @Override
    void invalidMethod() {
      throw new AssertionError("Invalid method call.");
    }

    private String getLastN(String str, int n) {
      return str.substring(str.length() - n);
    }
  }
}