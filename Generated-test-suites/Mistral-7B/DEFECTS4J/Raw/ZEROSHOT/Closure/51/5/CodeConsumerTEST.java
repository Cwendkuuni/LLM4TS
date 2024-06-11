import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot assert on private field, so just check if no exception is thrown
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("abcd");
    assertEquals('d', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("abc");
    assertEquals("abc", consumer.code);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.code);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.code);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.code);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeLineBreak();
    // Cannot assert on private field, so just check if no exception is thrown
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.code);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
    // Cannot assert on private field, so just check if no exception is thrown
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.code);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("}\n", consumer.code);
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.code);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.code);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("foo");
    consumer.endStatement();
    assertEquals("foo;\n", consumer.code);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("foo");
    consumer.endStatement(true);
    assertEquals("foo;\n", consumer.code);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    assertEquals("foo", consumer.code);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() { }");
    consumer.endFunction();
    assertEquals("function foo() {\n}\n", consumer.code);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() { return 1; }");
    consumer.endFunction(true);
    assertEquals("function foo() {\n  return 1;\n}\n", consumer.code);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("case 1:");
    consumer.beginCaseBody();
    assertEquals("case 1:\n", consumer.code);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginCaseBody();
    consumer.endCaseBody();
    assertEquals("", consumer.code);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("foo");
    consumer.add("bar");
    assertEquals("foobar", consumer.code);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.code);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("foo");
    consumer.addOp("+", false);
    consumer.append("bar");
    assertEquals("foo+bar", consumer.code);
  }

  @Test
  public void testAddOpWithBinOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("foo");
    consumer.addOp("+", true);
    consumer.append("bar");
    assertEquals("foo + bar", consumer.code);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("foo");
    consumer.addNumber(42.0);
    assertEquals("foonumbers:42.0", consumer.code);
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('B'));
    assertTrue(isWordChar('1'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.code);
  }

  private static class DummyCodeConsumer extends CodeConsumer {
    String code = "";

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    void append(String str) {
      code += str;
    }

    @Override
    void startNewLine() {
      code += "\n";
    }

    @Override
    void maybeCutLine() {
      // Cannot assert on private field, so just check if no exception is thrown
    }

    @Override
    void endLine() {
      code += "\n";
    }

    @Override
    void notePreferredLineBreak() {
      // Cannot assert on private field, so just check if no exception is thrown
    }

    @Override
    void beginBlock() {
      super.beginBlock();
      code = "{\n" + code;
    }

    @Override
    void endBlock() {
      super.endBlock();
      code += "\n}";
    }

    @Override
    void endFunction() {
      super.endFunction();
      code += "\n";
    }

    @Override
    void beginCaseBody() {
      super.beginCaseBody();
      code += ":";
    }

    @Override
    void endCaseBody() {
      super.endCaseBody();
      // Cannot assert on private field, so just check if no exception is thrown
    }

    @Override
    void add(String newcode) {
      super.add(newcode);
      code += newcode;
    }

    @Override
    void addIdentifier(String identifier) {
      super.addIdentifier(identifier);
      code += identifier;
    }

    @Override
    void addOp(String op, boolean binOp) {
      super.addOp(op, binOp);
      code += op;
    }

    @Override
    void addNumber(double x) {
      super.addNumber(x);
      code += x;
    }
  }
}