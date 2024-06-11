import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer codeConsumer;

  @Before
  public void setup() {
    codeConsumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.startSourceMapping(node);
    // Verify no exception is thrown
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.endSourceMapping(node);
    // Verify no exception is thrown
  }

  @Test
  public void testContinueProcessing() {
    when(codeConsumer.continueProcessing()).thenReturn(true);
    boolean result = codeConsumer.continueProcessing();
    assertEquals(true, result);
  }

  @Test
  public void testGetLastChar() {
    String code = "testCode";
    codeConsumer = new TestCodeConsumer();
    codeConsumer.append(code);
    char result = codeConsumer.getLastChar();
    assertEquals(code.charAt(code.length() - 1), result);
  }

  @Test
  public void testAppend() {
    String code = "testCode";
    codeConsumer = new TestCodeConsumer();
    codeConsumer.append(code);
    assertEquals(code, codeConsumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.appendBlockStart();
    String expected = "{";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.appendBlockEnd();
    String expected = "}";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.startNewLine();
    String expected = "\n";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.maybeLineBreak();
    // Verify no explicit line is added
    assertEquals("", codeConsumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.maybeCutLine();
    // Verify no explicit line is cut
    assertEquals("", codeConsumer.getCode());
  }

  @Test
  public void testEndLine() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.endLine();
    String expected = "\n";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.notePreferredLineBreak();
    // Verify no explicit line break is added
    assertEquals("", codeConsumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.beginBlock();
    String expected = "{";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testEndBlock() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.beginBlock();
    codeConsumer.append("test");
    codeConsumer.endBlock();
    String expected = "{\ntest\n}";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.beginBlock();
    codeConsumer.append("test");
    codeConsumer.endBlock(true);
    String expected = "{\ntest\n}";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testListSeparator() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.listSeparator();
    String expected = ", ";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testEndStatement() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.append("test");
    codeConsumer.endStatement();
    String expected = "test;\n";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testEndStatementNoSemiColon() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.append("test");
    codeConsumer.endStatement(false);
    String expected = "test";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.append("test");
    codeConsumer.maybeEndStatement();
    String expected = "test";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testEndFunction() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.append("function test() {");
    codeConsumer.appendBlockStart();
    codeConsumer.append("testFunctionBody");
    codeConsumer.endFunction();
    String expected = "function test() {\n  testFunctionBody\n}";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testEndFunctionWithNewline() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.append("function test() {");
    codeConsumer.appendBlockStart();
    codeConsumer.append("testFunctionBody");
    codeConsumer.endFunction(true);
    String expected = "function test() {\n  testFunctionBody\n}";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.beginCaseBody();
    String expected = ":";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.beginCaseBody();
    codeConsumer.append("test");
    codeConsumer.endCaseBody();
    String expected = ": test\n";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testAdd() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.append("test");
    codeConsumer.add(" CodeConsumer");
    String expected = "test CodeConsumer";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testAddOp() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.append("x");
    codeConsumer.addOp("+", true);
    codeConsumer.append("y");
    String expected = "x + y";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testAddNumber() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.append("x");
    codeConsumer.addNumber(1);
    String expected = "x 1";
    assertEquals(expected, codeConsumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    codeConsumer = new TestCodeConsumer();
    assertEquals(false, codeConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    codeConsumer = new TestCodeConsumer();
    Node node = mock(Node.class);
    assertEquals(false, codeConsumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    codeConsumer = new TestCodeConsumer();
    Node node = mock(Node.class);
    assertEquals(true, codeConsumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    codeConsumer = new TestCodeConsumer();
    codeConsumer.endFile();
    // Verify no explicit line is added at the end
    assertEquals("", codeConsumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
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