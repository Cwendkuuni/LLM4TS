import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer codeConsumer = mock(CodeConsumer.class);

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    boolean result = codeConsumer.continueProcessing();
    assertTrue(result);
  }

  @Test
  public void testGetLastChar() {
    String code = "";
    when(codeConsumer.getLastChar()).thenReturn((char) code.charAt(code.length() - 1));

    code += "a";
    assertEquals('a', codeConsumer.getLastChar());

    code += "bc";
    assertEquals('c', codeConsumer.getLastChar());
  }

  @Test
  public void testAppend() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.append("Hello, world!");
    expectedCode.append("Hello, world!");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.appendBlockStart();
    expectedCode.append("{");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.appendBlockEnd();
    expectedCode.append("}");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.startNewLine();
    expectedCode.append("\n");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.maybeLineBreak();
    expectedCode.append(System.lineSeparator());
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    // Not testable in isolation
  }

  @Test
  public void testEndLine() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.endLine();
    assertEquals(System.lineSeparator(), codeConsumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    // Not testable in isolation
  }

  @Test
  public void testBeginBlock() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.beginBlock();
    expectedCode.append("{\n");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testEndBlock() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.endBlock();
    expectedCode.append("}\n");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.endBlock(true);
    expectedCode.append("}\n");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testListSeparator() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.listSeparator();
    expectedCode.append(",\n");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testEndStatement() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.endStatement();
    expectedCode.append(";\n");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testEndStatementWithNewline() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.endStatement(true);
    expectedCode.append(";\n");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.maybeEndStatement();
    if (codeConsumer.statementNeedsEnded) {
      expectedCode.append(";\n");
    }
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testEndFunction() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.endFunction();
    expectedCode.append("}\n");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testEndFunctionWithNewline() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.endFunction(true);
    expectedCode.append("}\n");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.beginCaseBody();
    expectedCode.append(":\n");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    // Not testable in isolation
  }

  @Test
  public void testAdd() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.add("Hello, world!");
    expectedCode.append("Hello, world!");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.addIdentifier("identifier");
    expectedCode.append("identifier");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testAddOp() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.addOp("+", false);
    expectedCode.append("+");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());

    codeConsumer.addOp("+", true);
    expectedCode.append(" + ");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());

    codeConsumer.addOp("*", false);
    expectedCode.append("*");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());

    codeConsumer.addOp("*", true);
    expectedCode.append(" * ");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testAddNumber() {
    StringBuilder expectedCode = new StringBuilder();
    codeConsumer.addNumber(123.456);
    expectedCode.append("123.456");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());

    codeConsumer.addNumber(-123.456);
    expectedCode.append("-123.456");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());

    codeConsumer.addNumber(123);
    expectedCode.append("123");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());

    codeConsumer.addNumber(-123);
    expectedCode.append("-123");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());

    codeConsumer.addNumber(0);
    expectedCode.append("0");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());

    codeConsumer.addNumber(-0);
    expectedCode.append("-0");
    assertEquals(expectedCode.toString(), codeConsumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, CodeConsumer.isNegativeZero(0.0));
    assertEquals(true, CodeConsumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('A'));
    assertTrue(CodeConsumer.isWordChar('B'));
    assertTrue(CodeConsumer.isWordChar('1'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertFalse(codeConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    // Not testable in isolation
  }

  @Test
  public void testEndFile() {
    // Not testable in isolation
  }

  private String getCode() {
    return codeConsumer.getCode();
  }
}