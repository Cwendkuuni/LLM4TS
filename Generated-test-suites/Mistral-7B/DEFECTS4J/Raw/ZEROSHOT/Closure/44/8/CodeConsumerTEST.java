import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer codeConsumer;

  @Before
  public void setUp() {
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
    assert (codeConsumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    String testString = "";
    codeConsumer.append(testString);
    assertEquals('', codeConsumer.getLastChar());

    testString = "a";
    codeConsumer.append(testString);
    assertEquals('a', codeConsumer.getLastChar());

    testString = "ab";
    codeConsumer.append(testString);
    assertEquals('b', codeConsumer.getLastChar());
  }

  @Test
  public void testAppend() {
    String testString = "";
    codeConsumer.append(testString);
    assertEquals(0, codeConsumer.codeLength());

    testString = "ab";
    codeConsumer.append(testString);
    assertEquals("ab", codeConsumer.getCode());

    testString = "abc";
    codeConsumer.append(testString);
    assertEquals("ab\nabc", codeConsumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    codeConsumer.appendBlockStart();
    assertEquals("{\n", codeConsumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    codeConsumer.appendBlockStart();
    codeConsumer.append("abc");
    codeConsumer.appendBlockEnd();
    assertEquals("{\nabc}\n", codeConsumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    codeConsumer.append("ab");
    codeConsumer.startNewLine();
    assertEquals("ab\n", codeConsumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    codeConsumer.maybeLineBreak();
    // Verify no change in code
  }

  @Test
  public void testMaybeCutLine() {
    codeConsumer.maybeCutLine();
    // Verify no change in code
  }

  @Test
  public void testEndLine() {
    codeConsumer.append("ab");
    codeConsumer.endLine();
    assertEquals("ab\n", codeConsumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    codeConsumer.notePreferredLineBreak();
    // Verify no change in code
  }

  @Test
  public void testBeginBlock() {
    codeConsumer.beginBlock();
    assertEquals("{\n", codeConsumer.getCode());
  }

  @Test
  public void testEndBlock() {
    codeConsumer.beginBlock();
    codeConsumer.append("abc");
    codeConsumer.endBlock();
    assertEquals("{\nabc}\n", codeConsumer.getCode());
  }

  @Test
  public void testEndBlockWithNewLine() {
    codeConsumer.beginBlock();
    codeConsumer.append("abc");
    codeConsumer.endBlock(true);
    assertEquals("{\nabc}\n", codeConsumer.getCode());
  }

  @Test
  public void testListSeparator() {
    codeConsumer.append("ab");
    codeConsumer.listSeparator();
    assertEquals("ab,\n", codeConsumer.getCode());
  }

  @Test
  public void testEndStatement() {
    codeConsumer.append("if (true);");
    codeConsumer.endStatement();
    assertEquals("if (true);\n", codeConsumer.getCode());

    codeConsumer.append("if (true) {}\n");
    codeConsumer.endStatement();
    assertEquals("if (true) {}\n", codeConsumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    codeConsumer.append("if (true);");
    codeConsumer.endStatement(true);
    assertEquals("if (true);\n", codeConsumer.getCode());

    codeConsumer.append("if (true) {}\n");
    codeConsumer.endStatement(true);
    assertEquals("if (true) {\n};\n", codeConsumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    codeConsumer.append("if (true)");
    codeConsumer.maybeEndStatement();
    assertEquals("if (true);\n", codeConsumer.getCode());

    codeConsumer.append("if (true) {}\n");
    codeConsumer.maybeEndStatement();
    assertEquals("if (true) {\n};\n", codeConsumer.getCode());
  }

  @Test
  public void testEndFunction() {
    codeConsumer.append("function testFunction() {}\n");
    codeConsumer.endFunction();
    assertEquals("function testFunction() {\n}\n", codeConsumer.getCode());

    codeConsumer.append("function testFunction() {\n  return 1;\n}", false);
    codeConsumer.endFunction(true);
    assertEquals("function testFunction() {\n  return 1;\n}\n", codeConsumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    codeConsumer.beginCaseBody();
    assertEquals(":\n", codeConsumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    codeConsumer.beginCaseBody();
    codeConsumer.append("return 1;");
    codeConsumer.endCaseBody();
    assertEquals(":\nreturn 1;\n", codeConsumer.getCode());
  }

  @Test
  public void testAdd() {
    codeConsumer.append("if (true) {");
    codeConsumer.add("x = 1;");
    assertEquals("if (true) {\n  x = 1;\n}", codeConsumer.getCode());

    codeConsumer.append("if (true) {\n  x = 1;\n}");
    codeConsumer.add("else {\n y = 2;\n}");
    assertEquals("if (true) {\n  x = 1;\n}\nelse {\n  y = 2;\n}", codeConsumer.getCode());
  }

  @Test
  public void testAddOp() {
    codeConsumer.append("if (true) {");
    codeConsumer.addOp("+ x, true");
    assertEquals("if (true) {\n  + x;\n}", codeConsumer.getCode());

    codeConsumer.append("if (true) {\n  + x;\n}");
    codeConsumer.addOp("+ x, false");
    assertEquals("if (true) {\n  + x;\n}\n+ x;\n", codeConsumer.getCode());
  }

  @Test
  public void testAddNumber() {
    codeConsumer.append("if (true) {");
    codeConsumer.addNumber(1);
    assertEquals("if (true) {\n  1;\n}", codeConsumer.getCode());

    codeConsumer.append("if (true) {\n  1;\n}");
    codeConsumer.addNumber(-1.5);
    assertEquals("if (true) {\n  1;\n}\n-1.5;\n", codeConsumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, CodeConsumer.isNegativeZero(0));
    assertEquals(true, CodeConsumer.isNegativeZero(-0));
  }

  @Test
  public void testIsWordChar() {
    assertEquals(true, CodeConsumer.isWordChar('a'));
    assertEquals(true, CodeConsumer.isWordChar('_'));
    assertEquals(true, CodeConsumer.isWordChar('$'));
    assertEquals(true, CodeConsumer.isWordChar('A'));
    assertEquals(true, CodeConsumer.isWordChar('Z'));
    assertEquals(true, CodeConsumer.isWordChar('0'));
    assertEquals(true, CodeConsumer.isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertEquals(false, codeConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = mock(Node.class);
    boolean statementContext = true;
    assertEquals(statementContext, codeConsumer.breakAfterBlockFor(node, statementContext));

    statementContext = false;
    assertEquals(statementContext, codeConsumer.breakAfterBlockFor(node, statementContext));
  }

  @Test
  public void testEndFile() {
    codeConsumer.endFile();
    // Verify no change in code
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  void append(String str) {
    super.append(str);
    code += str;
  }

  @Override
  char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  String code = "";
}