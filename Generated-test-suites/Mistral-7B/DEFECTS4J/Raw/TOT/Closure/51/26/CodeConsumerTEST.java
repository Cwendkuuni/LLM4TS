import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setUp() {
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
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("test");
    char lastChar = consumer.getLastChar();
    assertTrue(lastChar == 't');
  }

  @Test
  public void testAppend() {
    consumer.append("This is a test");
    String expected = "This is a test";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    String expected = "{";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockStart();
    consumer.append("test");
    consumer.appendBlockEnd();
    String expected = "{test}";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testStartNewLine() {
    consumer.append("test1");
    consumer.startNewLine();
    consumer.append("test2");
    String expected = "test1\n test2";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.append("test1test2");
    consumer.maybeLineBreak();
    consumer.append("test3");
    String expected = "test1test2\n test3";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testMaybeCutLine() {
    consumer.append("test1test2");
    consumer.maybeCutLine();
    consumer.append("test3");
    String expected = "test1test2test3";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testEndLine() {
    consumer.append("test1");
    consumer.endLine();
    String expected = "test1;\n";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.append("test1");
    consumer.notePreferredLineBreak();
    String expected = "test1;\n";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    String expected = "{";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testEndBlock() {
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expected = "{test}";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testListSeparator() {
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    String expected = "test1,\n test2";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testEndStatement() {
    consumer.append("test1");
    consumer.endStatement();
    String expected = "test1;\n";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function testFunction() {");
    consumer.append("// test function code");
    consumer.endFunction();
    String expected = "function testFunction() {\n // test function code\n}";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("// test code for case");
    consumer.endCaseBody();
    String expected = "switch (x) {\n case:\n // test code for case\n}";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testEndCaseBody() {
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("// test code for case");
    consumer.endCaseBody();
    String expected = "switch (x) {\n case:\n // test code for case\n}";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAdd() {
    consumer.addIdentifier("testIdentifier");
    String expected = "testIdentifier";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAddOp() {
    consumer.append("1");
    consumer.addOp("+", true);
    consumer.append("2");
    String expected = "1 + 2";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAddNumber() {
    consumer.append("1");
    consumer.addNumber(2.5);
    String expected = "1 2.5";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(Character.isLetterOrDigit('a'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = new Node();
    node.setLineNumber(1);
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    consumer.append("test");
    consumer.endFile();
    String expected = "test";
    String actual = consumer.getCode();
    assertTrue(expected.equals(actual));
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  String getCode() {
    return super.code;
  }
}