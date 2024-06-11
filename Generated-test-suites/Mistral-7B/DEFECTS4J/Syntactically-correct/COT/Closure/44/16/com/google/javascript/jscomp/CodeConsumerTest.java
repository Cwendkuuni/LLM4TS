import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    consumer = (CodeConsumer) mock(CodeConsumer.class);
  }

  @Test
  public void testStartSourceMapping() {
    consumer.startSourceMapping(null);
    // Testing for void method, so no assertions here.
  }

  @Test
  public void testEndSourceMapping() {
    consumer.endSourceMapping(null);
    // Testing for void method, so no assertions here.
  }

  @Test
  public void testContinueProcessing() {
    when(consumer.continueProcessing()).thenReturn(true);
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingFalse() {
    when(consumer.continueProcessing()).thenReturn(false);
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    String result = "";
    when(consumer.getLastChar()).thenAnswer(i -> result.charAt(result.length() - 1));
    result += 'a';
    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    String expected = "expected";
    consumer.append(expected);
    verify(consumer).add(expected);
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    verify(consumer).append("{");
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    verify(consumer).append("}");
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    verify(consumer).startLine();
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    verifyZeroInteractions(consumer);
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
    verifyZeroInteractions(consumer);
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    verify(consumer).endStatement();
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
    verifyZeroInteractions(consumer);
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    verify(consumer).beginStatement();
    verify(consumer).append("{");
  }

  @Test
  public void testEndBlock() {
    consumer.endBlock();
    verify(consumer).append("}");
    verify(consumer).endStatement();
  }

  @Test
  public void testEndBlockWithLineBreak() {
    consumer.endBlock(true);
    verify(consumer).append("}");
    verify(consumer).endStatement();
    verify(consumer).endLine();
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
    verify(consumer).add(",");
    verify(consumer).maybeLineBreak();
  }

  @Test
  public void testEndStatement() {
    consumer.endStatement();
    verify(consumer).append(";");
    verify(consumer).maybeLineBreak();
    verify(consumer).endStatement();
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer.endStatement(true);
    verify(consumer).append(";");
    verify(consumer).maybeLineBreak();
    verify(consumer).endStatement();
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.maybeEndStatement();
    verify(consumer).endStatement();
  }

  @Test
  public void testEndFunction() {
    consumer.endFunction();
    verify(consumer).endFunction(false);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.endFunction(true);
    verify(consumer).endFunction(true);
    verify(consumer).endLine();
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginCaseBody();
    verify(consumer).append(":");
  }

  @Test
  public void testEndCaseBody() {
    consumer.endCaseBody();
    verifyZeroInteractions(consumer);
  }

  @Test
  public void testAdd() {
    String str = "string";
    consumer.add(str);
    verify(consumer).append(str);
  }

  @Test
  public void testAddOp() {
    String op = "+";
    consumer.addOp(op, true);
    verify(consumer).append(op);
  }

  @Test
  public void testAddNumber() {
    double number = 123.45;
    consumer.addNumber(number);
    // AddNumber appends the number as a string.
    verify(consumer).append(String.valueOf(number));
  }

  @Test
  public void testIsNegativeZero() {
    double number = -0.0;
    assertTrue(CodeConsumer.isNegativeZero(number));
  }

  @Test
  public void testIsWordChar() {
    char ch = '_';
    assertTrue(CodeConsumer.isWordChar(ch));
  }

  @Test(expected = AssertionError.class)
  public void testShouldPreserveExtraBlocksThrowsException() {
    consumer.shouldPreserveExtraBlocks();
    fail("shouldPreserveExtraBlocks() should not be called directly");
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    Node node = mock(Node.class);
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    Node node = mock(Node.class);
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    verifyZeroInteractions(consumer);
  }
}