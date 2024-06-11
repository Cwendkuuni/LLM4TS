import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Assertions not possible as the method does not return or throw an exception
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Assertions not possible as the methods do not have side effects
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertFalse(consumer.continueProcessing());
    consumer = new AlwaysContinueCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertEquals(' ', consumer.getLastChar()); // default value

    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addIdentifier("identifier");
    String expected = "identifier";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("abc");
    String expected = "abc";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    String expected = "{\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    String expected = "}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.startNewLine();
    String expected = "\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeLineBreak();
    // Assertions not possible as the method does not have side effects
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeCutLine();
    // Assertions not possible as the method does not have side effects
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endLine();
    String expected = "\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
    // Assertions not possible as the method does not have side effects
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    String expected = "{\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    String expected = "}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock(true);
    String expected = "}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.listSeparator();
    String expected = ", ";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("statement");
    consumer.endStatement();
    String expected = "statement;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("statement");
    consumer.endStatement(true);
    String expected = "statement;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("statement1");
    consumer.maybeEndStatement();
    consumer.append("statement2");
    String expected = "statement1;\nstatement2";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.append("function functionName() {");
    consumer.append("return 1;");
    consumer.endFunction();
    String expected = "{\nfunction functionName() {\n  return 1;\n}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithSemicolon() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.append("function functionName() {");
    consumer.append("return 1;");
    consumer.endFunction(true);
    String expected = "{\nfunction functionName() {\n  return 1;\n}\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("case label:");
    consumer.beginCaseBody();
    consumer.append("statement");
    consumer.endCaseBody();
    String expected = "case label:\n  statement\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("string1");
    consumer.add("string2");
    String expected = "string1string2";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("x");
    consumer.addOp("+", false);
    consumer.append("y");
    String expected = "x + y";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddOpBinary() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("x");
    consumer.addOp("+", true);
    consumer.append("y");
    String expected = "x + y";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addNumber(1.234);
    String expected = "1.234";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
    // Assertions not possible as the method does not have side effects
  }

  private static class DummyCodeConsumer extends CodeConsumer {
    private StringBuilder codeBuilder = new StringBuilder();

    @Override
    void add(String newcode) {
      super.add(newcode);
      codeBuilder.append(getCode());
      setCode(codeBuilder.toString());
    }

    @Override
    char getLastChar() {
      return codeBuilder.charAt(codeBuilder.length() - 1);
    }

    @Override
    String getCode() {
      return codeBuilder.toString();
    }

    @Override
    void setCode(String code) {
      codeBuilder.setLength(0);
      codeBuilder.append(code);
    }
  }

  private static class AlwaysContinueCodeConsumer extends CodeConsumer {
    @Override
    boolean continueProcessing() {
      return false;
    }
  }
}