import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertNull(consumer.getLastSourceMapping());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertEquals(node, consumer.getLastSourceMapping());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    assertEquals('o', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    assertEquals("hello", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getCode());
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
    consumer.maybeLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    assertEquals("", consumer.getCode());
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
    assertEquals("", consumer.getCode());
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
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
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
    consumer.append("hello");
    consumer.endStatement();
    assertEquals("hello;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithoutSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    consumer.endStatement(false);
    assertEquals("hello", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    consumer.append(" ");
    consumer.maybeEndStatement();
    assertEquals("hello;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction();
    assertEquals("function foo() {\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {\n  return 1;\n}");
    consumer.endFunction(true);
    assertEquals("function foo() {\n  return 1;\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    consumer.add(" world");
    assertEquals("hello world", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("5");
    consumer.addOp("+", true);
    consumer.append("3");
    assertEquals("5 + 3", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(5.0);
    assertEquals("5.0", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  private static class StubCodeConsumer extends CodeConsumer {

    private Node lastSourceMapping;
    private String code = "";

    @Override
    public void startSourceMapping(Node node) {
      lastSourceMapping = node;
    }

    @Override
    public void endSourceMapping(Node node) {
      super.endSourceMapping(node);
    }

    @Override
    public char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    public void add(String newcode) {
      super.add(newcode);
      code += newcode;
    }

    @Override
    public void append(String str) {
      super.append(str);
      code += str;
    }

    @Override
    public void appendBlockStart() {
      super.appendBlockStart();
      code += "{";
    }

    @Override
    public void appendBlockEnd() {
      super.appendBlockEnd();
      code += "}";
    }

    @Override
    public void startNewLine() {
      super.startNewLine();
      code += "\n";
    }

    @Override
    public void endStatement(boolean needSemiColon) {
      super.endStatement(needSemiColon);
      if (needSemiColon) {
        code += ";";
      }
    }

    @Override
    public void endFunction(boolean statementContext) {
      super.endFunction(statementContext);
      if (statementContext) {
        code += "}\n";
      }
    }

    @Override
    public void beginBlock() {
      super.beginBlock();
      code += "{\n";
    }

    @Override
    public void endBlock(boolean shouldEndLine) {
      super.endBlock(shouldEndLine);
      if (shouldEndLine) {
        code += "}\n";
      }
    }

    @Override
    public void maybeEndStatement() {
      super.maybeEndStatement();
      code += ";";
    }

    @Override
    public void addIdentifier(String identifier) {
      super.addIdentifier(identifier);
      code += identifier;
    }

    @Override
    public void addOp(String op, boolean binOp) {
      super.addOp(op, binOp);
      code += op;
    }

    @Override
    public void addNumber(double x) {
      super.addNumber(x);
      code += String.valueOf(x);
    }

    @Override
    public void notePreferredLineBreak() {
      super.notePreferredLineBreak();
    }

    @Override
    public void beginCaseBody() {
      super.beginCaseBody();
      code += ":";
    }

    @Override
    public void endCaseBody() {
      super.endCaseBody();
    }

    @Override
    public String getCode() {
      return code;
    }
  }
}