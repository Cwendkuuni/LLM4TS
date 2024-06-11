package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertFalse(consumer.isWordChar('!'));
    assertFalse(consumer.isWordChar('%'));
    assertFalse(consumer.isWordChar('^'));
    assertTrue(Character.isLetterOrDigit('a'));
    assertTrue(Character.isLetterOrDigit('0'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isNegativeZero(0.0));
    assertFalse(consumer.isNegativeZero(1.0));
    assertFalse(consumer.isNegativeZero(3.14));
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    String codeBefore = consumer.getCode();
    consumer.endStatement();
    String codeAfter = consumer.getCode();
    assertTrue(codeAfter.endsWith(";"));
    assertEquals(codeBefore.length() + 1, codeAfter.length());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("return 1;");
    consumer.maybeEndStatement();
    String codeAfter = consumer.getCode();
    assertTrue(codeAfter.endsWith(";"));
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String codeAfter = consumer.getCode();
    assertTrue(codeAfter.endsWith("{"));
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.add("test();");
    consumer.endBlock();
    String codeAfter = consumer.getCode();
    assertTrue(codeAfter.endsWith("}"));
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test1,");
    consumer.listSeparator();
    consumer.add("test2");
    String codeAfter = consumer.getCode();
    assertEquals("test1,\n test2", codeAfter);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    String codeAfter = consumer.getCode();
    assertEquals("test", codeAfter);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    String codeAfter = consumer.getCode();
    assertEquals("test", codeAfter);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test");
    consumer.startNewLine();
    String codeAfter = consumer.getCode();
    assertEquals("\ntest", codeAfter);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test");
    consumer.maybeCutLine();
    String codeAfter = consumer.getCode();
    assertFalse(codeAfter.endsWith("\n"));
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test");
    consumer.endLine();
    String codeAfter = consumer.getCode();
    assertTrue(codeAfter.endsWith("\n"));
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    String codeAfter = consumer.getCode();
    assertFalse(codeAfter.endsWith("\n"));
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test");
    consumer.maybeLineBreak();
    String codeAfter = consumer.getCode();
    assertFalse(codeAfter.endsWith("\n"));
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    String codeAfter = consumer.getCode();
    assertEquals("{\n", codeAfter);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.add("test");
    consumer.appendBlockEnd();
    String codeAfter = consumer.getCode();
    assertEquals("{\n test\n}", codeAfter);
  }

  @Test
  public void testMaybeEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("function test() {}");
    consumer.maybeEndFunction();
    String codeAfter = consumer.getCode();
    assertTrue(codeAfter.endsWith("}"));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("function test() {}");
    consumer.endFunction();
    String codeAfter = consumer.getCode();
    assertTrue(codeAfter.endsWith("}"));
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    String codeAfter = consumer.getCode();
    assertEquals(":\n", codeAfter);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    consumer.add("test");
    consumer.endCaseBody();
    String codeAfter = consumer.getCode();
    assertEquals(":\n test\n", codeAfter);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test");
    consumer.addOp("+", false);
    consumer.add("test2");
    String codeAfter = consumer.getCode();
    assertEquals("test + test2", codeAfter);
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test");
    consumer.appendOp("+", false);
    consumer.add("test2");
    String codeAfter = consumer.getCode();
    assertEquals("test + test2", codeAfter);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1.0);
    String codeAfter = consumer.getCode();
    assertEquals("1.0", codeAfter);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(null, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
  }

  @Test
  public void testStatementNeedsEnded() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement(true);
    consumer.add("test");
    consumer.maybeEndStatement();
    String codeAfter = consumer.getCode();
    assertTrue(codeAfter.endsWith(";"));
    assertTrue(codeAfter.contains("test"));
    assertFalse(codeAfter.contains("test;"));
  }
}

class TestCodeConsumer extends CodeConsumer {
  private StringBuilder code = new StringBuilder();

  @Override
  void append(String str) {
    code.append(str);
  }

  @Override
  void add(String newcode) {
    code.append(newcode);
  }

  @Override
  char getLastChar() {
    if (code.length() > 0) {
      return code.charAt(code.length() - 1);
    } else {
      return '\u0000';
    }
  }

  @Override
  String getCode() {
    return code.toString();
  }
}