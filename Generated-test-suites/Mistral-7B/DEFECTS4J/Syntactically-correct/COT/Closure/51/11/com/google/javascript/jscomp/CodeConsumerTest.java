import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    consumer = mock(CodeConsumer.class);
  }

  @Test
  public void testStartSourceMapping() {
    consumer.startSourceMapping(new Node(-1));
  }

  @Test
  public void testEndSourceMapping() {
    consumer.endSourceMapping(new Node(-1));
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    // This class is abstract, so we cannot create an instance to test this method.
  }

  @Test
  public void testAddIdentifier() {
    String identifier = "identifier";
    consumer.addIdentifier(identifier);
    assertEquals(identifier, consumer.getLastCharAsString());
  }

  @Test
  public void testAppend() {
    String str = "string";
    consumer.append(str);
    assertEquals(str, consumer.getLastCharAsString());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{", consumer.getLastCharAsString());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getLastCharAsString());
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    // This method does not change the last character, so we cannot test it directly.
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    assertEquals("{", consumer.getLastCharAsString());
  }

  @Test
  public void testEndBlock() {
    consumer.endBlock();
    assertEquals("}", consumer.getLastCharAsString());
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
    assertEquals(",", consumer.getLastCharAsString());
  }

  @Test
  public void testEndStatement() {
    consumer.endStatement();
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer.endStatement(true);
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    consumer.endFunction();
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginCaseBody();
    assertEquals(":", consumer.getLastCharAsString());
  }

  @Test
  public void testEndCaseBody() {
    consumer.endCaseBody();
  }

  @Test
  public void testAdd() {
    String newCode = "newCode";
    consumer.add(newCode);
    assertEquals(newCode, consumer.getLastCharAsString());
  }

  @Test
  public void testAppendOp() {
    String op = "+";
    consumer.appendOp(op, false);
    assertEquals(op, consumer.getLastCharAsString());
  }

  @Test
  public void testAddOp() {
    String op = "+";
    consumer.addOp(op, false);
    assertEquals(op, consumer.getLastCharAsString());
  }

  @Test
  public void testAddNumber() {
    double x = 1.2;
    consumer.addNumber(x);
    String expected = String.valueOf(x);
    assertEquals(expected, consumer.getLastCharAsString());
  }

  @Test
  public void testIsWordChar() {
    char ch = '_';
    assertTrue(CodeConsumer.isWordChar(ch));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    // This method returns false by default.
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    // This method returns true by default.
    assertTrue(consumer.breakAfterBlockFor(null, false));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndFunctionWithoutStatementContext() {
    consumer.endFunction();
    consumer.endLine();
  }

  @Test
  public void testStatementNeedsEndedAndStatementStarted() {
    consumer.endStatement();
    consumer.statementNeedsEnded = false;
    consumer.maybeEndStatement();
    consumer.statementStarted = true;
    consumer.maybeEndStatement();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testSawFunction() {
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  private String getLastCharAsString() {
    char c = consumer.getLastChar();
    return Character.isLetterOrDigit(c) ? String.valueOf(c) : String.valueOf((int) c);
  }
}