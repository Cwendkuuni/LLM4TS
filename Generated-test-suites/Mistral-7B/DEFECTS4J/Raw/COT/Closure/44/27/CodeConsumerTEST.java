import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

/**
 * Tests for the CodeConsumer class
 */
public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
    verify(consumer).startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
    verify(consumer).endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.assertTrue(consumer.continueProcessing());
    verify(consumer).continueProcessing();
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }
    };
    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    assertEquals("hello", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement();
    assertEquals(";\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement(true);
    assertEquals(";\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeEndStatement();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction(true);
    assertEquals("\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endCaseBody();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", false);
    assertEquals("+\n", consumer.getCode());
    consumer.addOp("+=", false);
    assertEquals("+=\n", consumer.getCode());
    consumer.addOp("*", true);
    assertEquals("*\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1);
    assertEquals("1\n", consumer.getCode());
    consumer.addNumber(-1);
    assertEquals("-1\n", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isNegativeZero(-0));
    assertFalse(consumer.isNegativeZero(0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('Z'));
    assertTrue(consumer.isWordChar('0'));
    assertTrue(consumer.isWordChar('9'));
    assertFalse(consumer.isWordChar(' '));
  }

  @Test(expected = AssertionError.class)
  public void testAddIllegalCharacter() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("X\\0");
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = mock(Node.class);
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  private static class TestCodeConsumer extends CodeConsumer {
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
}