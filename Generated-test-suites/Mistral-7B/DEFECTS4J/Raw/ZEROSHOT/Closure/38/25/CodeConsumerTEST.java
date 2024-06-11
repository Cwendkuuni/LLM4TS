import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.isWordChar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new TestCodeConsumer();
  }

  @After
  public void tearDown() {
    consumer = null;
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertNull(consumer.getLastChar());
  }

  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertNull(consumer.getLastChar());
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("identifier");
    consumer.endLine();
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    consumer.append("abc");
    consumer.endLine();
    assertEquals("abc", consumer.getCode());
  }

  @Test
  public void testAddBlockStart() {
    consumer.beginBlock();
    consumer.endLine();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAddBlockEnd() {
    consumer.beginBlock();
    consumer.append("abc");
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\nabc\n}", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.append("abc");
    consumer.startNewLine();
    consumer.endLine();
    assertEquals("abc\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.append("ab");
    consumer.maybeLineBreak();
    consumer.append("c");
    consumer.endLine();
    assertEquals("ab\nc", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.append("ab");
    consumer.maybeCutLine();
    consumer.append("c");
    consumer.endLine();
    assertEquals("ac", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    consumer.append("ab");
    consumer.endLine();
    assertEquals("ab\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
    consumer.append("abc");
    consumer.endLine();
    assertEquals("abc\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    consumer.append("abc");
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\nabc\n}", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.beginBlock();
    consumer.append("abc");
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\nabc\n}", consumer.getCode());
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\nabc\n}", consumer.getCode());
    consumer.endBlock(true);
    consumer.endLine();
    assertEquals("{\nabc\n}", consumer.getCode());
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\nabc\n}", consumer.getCode());
    consumer.endBlock(true);
    consumer.endLine();
    assertEquals("{\nabc\n}", consumer.getCode());
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\nabc\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer.append("ab");
    consumer.listSeparator();
    consumer.append("cd");
    consumer.endLine();
    assertEquals("ab,\ncd\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("ab");
    consumer.endStatement();
    consumer.append("cd");
    consumer.endLine();
    assertEquals("ab;\ncd\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer.append("ab");
    consumer.endStatement(true);
    consumer.append("cd");
    consumer.endLine();
    assertEquals("ab;\ncd\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("ab");
    consumer.maybeEndStatement();
    consumer.append("cd");
    consumer.endLine();
    assertEquals("abcd\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("}");
    consumer.endFunction();
    consumer.endLine();
    assertEquals("{\nfunction test() {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("return 1;");
    consumer.endFunction(true);
    consumer.endLine();
    assertEquals("{\nfunction test() {\n  return 1;\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginBlock();
    consumer.append("switch (1) {");
    consumer.beginCaseBody();
    consumer.append("case 1:");
    consumer.endLine();
    consumer.append("break;");
    consumer.endCaseBody();
    consumer.append("}");
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\nswitch (1) {\n  case 1:\n    break;\n}\n}", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    consumer.beginBlock();
    consumer.append("switch (1) {");
    consumer.beginCaseBody();
    consumer.append("case 1:");
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\nswitch (1) {\n  case 1:\n}\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.append("ab");
    consumer.add("cd");
    consumer.endLine();
    assertEquals("abcd\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.append("ab");
    consumer.addOp("+", false);
    consumer.append("cd");
    consumer.endLine();
    assertEquals("ab+cd\n", consumer.getCode());
  }

  @Test
  public void testAddOpBinary() {
    consumer.append("ab");
    consumer.addOp("+", true);
    consumer.append("cd");
    consumer.endLine();
    assertEquals("ab + cd\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.append("ab");
    consumer.addNumber(1.0);
    consumer.endLine();
    assertEquals("ab 1.0\n", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, CodeConsumerTest.isNegativeZero(0.0));
    assertEquals(false, CodeConsumerTest.isNegativeZero(1.0));
    assertEquals(true, CodeConsumerTest.isNegativeZero(-0.0));
    assertEquals(true, CodeConsumerTest.isNegativeZero(-1.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('z'));
    assertTrue(isWordChar('0'));
    assertTrue(isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testSawFunction() {
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  String getCode() {
    StringBuilder sb = new StringBuilder();
    sb.append(super.getCode());
    consumer = null;
    return sb.toString();
  }
}