import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMappingAndEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    when(consumer.continueProcessing()).thenReturn(true);

    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessingReturnsFalse() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    when(consumer.continueProcessing()).thenReturn(false);

    // Test that continueProcessing() call stops further processing
    CodeConsumer testConsumer = new TestCodeConsumer(consumer);
    testConsumer.beginBlock();
    testConsumer.append("test");
    assert(testConsumer.getLastChar() == 't');
    testConsumer.endBlock();

    // continueProcessing() should have been called, but returns false
    assert(testConsumer.continueProcessing() == false);
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer(null);
    for (char c = 'a'; c <= 'z'; c++) {
      consumer.append(String.valueOf(c));
    }
    assert(consumer.getLastChar() == 'z');
  }

  @Test
  public void testAppendBlockStartAndBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();

    String expectedOutput = "{\ntest\n}";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testStartNewLineAndMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.append("test");
    consumer.startNewLine();
    consumer.append("another test");

    String expectedOutput = "test\nanother test\n";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.append("test").append("long line");
    consumer.maybeCutLine();

    String expectedOutput = "test\nlong line\n";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.append("test");
    consumer.endLine();

    String expectedOutput = "test\n";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.append("test");
    consumer.notePreferredLineBreak();

    String expectedOutput = "test\n";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testBeginBlockAndEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();

    String expectedOutput = "{\ntest\n}";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.append("test");
    consumer.listSeparator();
    consumer.append("another test");

    String expectedOutput = "test,\nanother test\n";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.append("test");
    consumer.endStatement();

    String expectedOutput = "test;\n";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append(" test();");
    consumer.endFunction();

    String expectedOutput = "{\nfunction test() {\n test();\n}\n}";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testBeginCaseBodyAndEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append(" test();");
    consumer.endCaseBody();
    consumer.append("}");

    String expectedOutput = "switch (x) {\n test();\n}";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.addIdentifier("test");

    String expectedOutput = "test\n";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.append("5");
    consumer.appendOp("+", false);
    consumer.append("3");

    String expectedOutput = "5+\n3\n";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer(null);

    consumer.addNumber(10.5);

    String expectedOutput = "10.5\n";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer(mock(CodeConsumer.class));

    Node statementNode = mock(Node.class);
    Node loopNode = mock(Node.class);

    when(consumer.continueProcessing()).thenReturn(true);
    when(consumer.breakAfterBlockFor(statementNode, false)).thenReturn(true);
    when(consumer.breakAfterBlockFor(loopNode, true)).thenReturn(false);

    consumer.shouldPreserveExtraBlocks();

    String expectedOutput = "{ test;\n}";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));

    consumer.beginBlock();
    consumer.append("for (int i = 0; i < 10; ++i) { test(); }");
    consumer.endBlock();

    expectedOutput += "\nfor (int i = 0; i < 10; ++i) {\n test();\n}";
    actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer(null);
    consumer.endFile();

    String expectedOutput = "";
    String actualOutput = consumer.getCode();
    assert(expectedOutput.equals(actualOutput));
  }
}

class TestCodeConsumer extends CodeConsumer {
  private CodeConsumer delegate;

  public TestCodeConsumer(CodeConsumer delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean continueProcessing() {
    return delegate.continueProcessing();
  }

  @Override
  public char getLastChar() {
    return delegate.getLastChar();
  }

  @Override
  public String getCode() {
    return delegate.getCode();
  }
}