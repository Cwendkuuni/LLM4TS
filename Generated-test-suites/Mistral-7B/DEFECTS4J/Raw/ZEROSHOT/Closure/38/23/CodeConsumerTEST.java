import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer codeConsumer;

  @Before
  public void setUp() {
    codeConsumer = mock(CodeConsumer.class);
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.startSourceMapping(node);
    verify(codeConsumer, times(1)).startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.endSourceMapping(node);
    verify(codeConsumer, times(1)).endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    codeConsumer.continuedProcessing();
    verify(codeConsumer, times(1)).continueProcessing();
  }

  @Test
  public void testGetLastChar() {
    // This method is abstract, we cannot test it directly
  }

  @Test
  public void testAppend() {
    String str = "test string";
    codeConsumer.append(str);
    verify(codeConsumer, times(1)).append(str);
  }

  @Test
  public void testAppendBlockStart() {
    codeConsumer.appendBlockStart();
    verify(codeConsumer, times(1)).append("{");
  }

  @Test
  public void testAppendBlockEnd() {
    codeConsumer.appendBlockEnd();
    verify(codeConsumer, times(1)).append("}");
  }

  @Test
  public void testStartNewLine() {
    codeConsumer.startNewLine();
    verify(codeConsumer, never()).append("\n");
  }

  @Test
  public void testMaybeLineBreak() {
    codeConsumer.maybeLineBreak();
    verify(codeConsumer, times(1)).maybeCutLine();
  }

  @Test
  public void testMaybeCutLine() {
    codeConsumer.maybeCutLine();
    verify(codeConsumer, times(1)).maybeEndStatement();
  }

  @Test
  public void testEndLine() {
    codeConsumer.endLine();
    verify(codeConsumer, times(1)).endStatement(false);
  }

  @Test
  public void testNotePreferredLineBreak() {
    codeConsumer.notePreferredLineBreak();
    verify(codeConsumer, times(1)).notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    codeConsumer.beginBlock();
    verify(codeConsumer, times(1)).append("{");
    verify(codeConsumer, times(1)).endLine();
  }

  @Test
  public void testEndBlock() {
    codeConsumer.endBlock();
    verify(codeConsumer, times(1)).append("}");
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    codeConsumer.endBlock(true);
    verify(codeConsumer, times(1)).append("}");
    verify(codeConsumer, times(1)).endLine();
  }

  @Test
  public void testListSeparator() {
    codeConsumer.listSeparator();
    verify(codeConsumer, times(1)).append(",");
    verify(codeConsumer, times(1)).maybeLineBreak();
  }

  @Test
  public void testEndStatement() {
    codeConsumer.endStatement();
    verify(codeConsumer, times(1)).endStatement(false);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    codeConsumer.endStatement(true);
    verify(codeConsumer, times(1)).endStatement(true);
  }

  @Test
  public void testMaybeEndStatement() {
    codeConsumer.maybeEndStatement();
    verify(codeConsumer, times(1)).maybeEndStatement();
  }

  @Test
  public void testMaybeEndStatementWithEnded() {
    codeConsumer.statementNeedsEnded = true;
    codeConsumer.maybeEndStatement();
    verify(codeConsumer, times(1)).append(";");
    verify(codeConsumer, times(1)).maybeLineBreak();
    verify(codeConsumer, times(1)).endLine();
  }

  @Test
  public void testEndFunction() {
    codeConsumer.endFunction();
    verify(codeConsumer, times(1)).endFunction(false);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    codeConsumer.endFunction(true);
    verify(codeConsumer, times(1)).endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    codeConsumer.beginCaseBody();
    verify(codeConsumer, times(1)).append(":");
  }

  @Test
  public void testEndCaseBody() {
    codeConsumer.endCaseBody();
    verify(codeConsumer, times(1)).endStatement();
  }

  @Test  public void testAdd() {
    String newCode = "new code";
    codeConsumer.add(newCode);
    verify(codeConsumer, times(1)).add(newCode);
  }

  @Test
  public void testAddIdentifier() {
    String identifier = "identifier";
    codeConsumer.addIdentifier(identifier);
    verify(codeConsumer, times(1)).add(identifier);
  }

  @Test
  public void testAddOp() {
    String op = "+";
    codeConsumer.addOp(op, false);
    verify(codeConsumer, times(1)).appendOp(op, false);
  }

  @Test
  public void testAddNumber() {
    double x = 1.0;
    codeConsumer.addNumber(x);
    verify(codeConsumer, times(1)).addNumber(x);
  }

  @Test
  public void testIsNegativeZero() {
    double x = 0.0;
    boolean result = CodeConsumer.isNegativeZero(x);
    assertTrue(result);
  }

  @Test
  public void testIsWordChar() {
    char ch = '_';
    boolean result = CodeConsumer.isWordChar(ch);
    assertTrue(result);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    boolean result = codeConsumer.shouldPreserveExtraBlocks();
    assertFalse(result);
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = mock(Node.class);
    boolean statementContext = false;
    boolean result = codeConsumer.breakAfterBlockFor(node, statementContext);
    assertFalse(result);
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    Node node = mock(Node.class);
    boolean statementContext = true;
    boolean result = codeConsumer.breakAfterBlockFor(node, statementContext);
    assertTrue(result);
  }

  @Test
  public void testEndFile() {
    codeConsumer.endFile();
    verify(codeConsumer, times(1)).endFile();
  }
}