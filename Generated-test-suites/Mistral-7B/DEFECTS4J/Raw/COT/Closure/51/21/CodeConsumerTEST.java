import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if startSourceMapping method was called
    assertTrue(consumer.statementStarted);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Check if startSourceMapping and endSourceMapping methods were called
    assertFalse(consumer.statementStarted);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    // Test continueProcessing method with true return value
    assertTrue(consumer.continueProcessing());

    // Test continueProcessing method with false return value
    consumer = new TestContinueProcessingFalseConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("TestString");
    assertEquals('T', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("TestString");
    assertEquals("TestString", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertTrue(consumer.getCode().startsWith("{"));
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    assertTrue(consumer.getCode().endsWith("}"));
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("TestString");
    consumer.startNewLine();
    assertEquals("\nTestString", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("TestString");
    consumer.maybeLineBreak();
    assertEquals("TestString\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("TestString");
    consumer.maybeCutLine();
    assertEquals("TestString", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("TestString");
    consumer.endLine();
    assertEquals("TestString\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    // NotePreferredLineBreak method does not change the code, so there is no need to check the result
  }

  @Test(expected = AssertionError.class)
  public void testGetLastCharWhenCodeIsEmpty() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertEquals(' ', consumer.getLastChar());
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
    consumer.beginCaseBody();
    consumer.endCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("TestString");
    consumer.add(" AnotherTestString");
    assertEquals("TestString AnotherTestString", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("x");
    consumer.appendOp("+", false);
    consumer.append("y");
    assertEquals("x + y", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("x");
    consumer.addOp("+", false);
    consumer.append("y");
    assertEquals("x + y", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(123.456);
    assertEquals("123.456", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('A'));
    assertTrue(isWordChar('0'));
    assertFalse(isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    // endFile method does not change the code, so there is no need to check the result
  }
}

class TestCodeConsumer extends CodeConsumer {
  private StringBuilder code = new StringBuilder();

  @Override
  public char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  @Override
  public String getCode() {
    return code.toString();
  }
}

class TestContinueProcessingFalseConsumer extends CodeConsumer {
  @Override
  public boolean continueProcessing() {
    return false;
  }
}