import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setup() {
    consumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    assertNull(consumer.getLastChar());

    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    consumer.append("abc");
    consumer.append("def");
    assertEquals("abcdef", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.append("abc");
    consumer.appendBlockStart();
    consumer.append("def");
    consumer.appendBlockEnd();
    assertEquals("abc{\ndef\n}", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.append("abc");
    consumer.startNewLine();
    consumer.append("def");
    assertEquals("abc\ndef", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.append("abc");
    consumer.maybeLineBreak();
    consumer.append("def");
    assertEquals("abc\ndef", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    consumer.append("abc");
    consumer.endLine();
    consumer.append("def");
    assertEquals("abc\ndef", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.append("abc");
    consumer.notePreferredLineBreak();
    consumer.append("def");
    assertEquals("abc\ndef", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.append("abc");
    consumer.beginBlock();
    consumer.append("def");
    consumer.endBlock();
    assertEquals("abc{\ndef\n}", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.append("abc");
    consumer.beginBlock();
    consumer.append("def");
    consumer.endBlock();
    assertEquals("abc{\ndef\n}", consumer.getCode());
    consumer.endBlock(true);
    assertEquals("abc{\ndef\n}", consumer.getCode());
    consumer.endBlock(false);
    assertEquals("abc{\ndef}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer.append("abc");
    consumer.listSeparator();
    consumer.append("def");
    assertEquals("abc,\ndef", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("abc");
    consumer.endStatement();
    consumer.append("def");
    assertEquals("abc;\ndef", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("abc");
    consumer.maybeEndStatement();
    consumer.append("def");
    assertEquals("abcdef", consumer.getCode());
    consumer.endStatement();
    consumer.append("ghi");
    assertEquals("abcdef;\nghi", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.append("function foo() {}\n");
    consumer.sawFunction = false;
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (x) {\n case 1:\n");
    consumer.beginCaseBody();
    consumer.append("y = 4;\n");
    consumer.endCaseBody();
    consumer.append("\n}");
    assertEquals("switch (x) {\n case 1:\n y = 4;\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.append("abc");
    consumer.add("def");
    assertEquals("abcdef", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.append("x = 5 + 3");
    consumer.addOp("+", true);
    consumer.append("y = 10 - 2");
    consumer.addOp("-", true);
    assertEquals("x = 5 + 3;\n y = 8", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.append("x = ");
    consumer.addNumber(3.14159265358979323846);
    assertEquals("x = 3.14159265358979323846", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, isNegativeZero(0.0));
    assertEquals(true, isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('z'));
    assertFalse(isWordChar(' '));
    assertFalse(isWordChar('('));
    assertFalse(isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(new TestCodeConsumerWithShouldPreserveExtraBlocks().shouldPreserveExtraBlocks());
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    assertTrue(consumer.breakAfterBlockFor(null, false));
    assertFalse(consumer.breakAfterBlockFor(null, true));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return super.getLastChar();
  }

  @Override
  public void append(String str) {
    super.add(str);
  }

  @Override
  public String getCode() {
    return super.code;
  }
}

class TestCodeConsumerWithShouldPreserveExtraBlocks extends TestCodeConsumer {
  @Override
  public boolean shouldPreserveExtraBlocks() {
    return true;
  }
}