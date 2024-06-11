import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setup() {
    consumer = mock(CodeConsumer.class);
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    when(consumer.continueProcessing()).thenReturn(true);
    boolean result = consumer.continueProcessing();
    assertEquals(true, result);
  }

  @Test
  public void testGetLastChar() {
    // This method is abstract, so cannot be tested directly.
  }

  @Test
  public void testAppend() {
    String str = "test";
    consumer.append(str);
    String expected = "test";
    char lastChar = expected.charAt(expected.length() - 1);
    char actual = consumer.getLastChar();
    assertEquals(lastChar, actual);
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    String expected = "{";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    String expected = "}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    String expected = "\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeLineBreak() {
    // This method is abstract, so cannot be tested directly.
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    String expected = "\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
    // This method has no return value, so cannot be directly tested.
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    String expected = "{\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlock() {
    consumer.endBlock();
    String expected = "}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlockWithNewline() {
    consumer.endBlock(true);
    String expected = "}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
    String expected = ", ";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatement() {
    consumer.endStatement();
    String expected = "; ";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatementWithSemicolon() {
    consumer.endStatement(true);
    String expected = "; ";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.maybeEndStatement();
    String expected = "";
    String actual = consumer.getCode();
    assertEquals(expected, actual);

    consumer.statementStarted = true;
    consumer.maybeEndStatement();
    expected = "; ";
    actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunction() {
    consumer.endFunction();
    String expected = "}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.endFunction(true);
    String expected = "\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginCaseBody();
    String expected = ": ";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testEndCaseBody() {
    consumer.endCaseBody();
    String expected = "";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAdd() {
    String identifier = "testIdentifier";
    consumer.addIdentifier(identifier);
    String expected = identifier;
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddOp() {
    String op = "+";
    consumer.addOp(op, false);
    String expected = op;
    String actual = consumer.getCode();
    assertEquals(expected, actual);

    op = "+";
    consumer.addOp(op, true);
    expected = " + ";
    actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testAddNumber() {
    double x = 123.45;
    consumer.addNumber(x);
    String expected = "123.45";
    String actual = consumer.getCode();
    assertEquals(expected, actual);

    x = -123.45;
    consumer.addNumber(x);
    expected = "-123.45";
    actual = consumer.getCode();
    assertEquals(expected, actual);

    x = 123;
    consumer.addNumber(x);
    expected = "123";
    actual = consumer.getCode();
    assertEquals(expected, actual);

    x = -123;
    consumer.addNumber(x);
    expected = "-123";
    actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    when(consumer.shouldPreserveExtraBlocks()).thenReturn(true);
    boolean result = consumer.shouldPreserveExtraBlocks();
    assertEquals(true, result);
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node n = mock(Node.class);
    boolean statementContext = true;
    boolean result = consumer.breakAfterBlockFor(n, statementContext);
    assertEquals(statementContext, result);
  }

  @Test(expected = AssertionError.class)
  public void testGetCodeThrowsException() {
    consumer.getCode();
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    String expected = "";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }
}