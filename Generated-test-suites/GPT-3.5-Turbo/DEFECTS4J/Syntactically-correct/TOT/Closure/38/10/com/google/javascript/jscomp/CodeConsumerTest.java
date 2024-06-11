import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer codeConsumer;

  @Before
  public void setUp() {
    codeConsumer = spy(CodeConsumer.class);
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.startSourceMapping(node);
    verify(codeConsumer, never()).append(anyString());
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.endSourceMapping(node);
    verify(codeConsumer, never()).append(anyString());
  }

  @Test
  public void testContinueProcessing() {
    boolean result = codeConsumer.continueProcessing();
    assertEquals(true, result);
  }

  @Test
  public void testGetLastChar() {
    char result = codeConsumer.getLastChar();
    assertEquals(true, Character.isWhitespace(result));
  }

  @Test
  public void testAddIdentifier() {
    String identifier = "identifier";
    codeConsumer.addIdentifier(identifier);
    verify(codeConsumer).add(identifier);
  }

  @Test
  public void testAppend() {
    String str = "string";
    codeConsumer.append(str);
    verify(codeConsumer).append(str);
  }

  @Test
  public void testAppendBlockStart() {
    codeConsumer.appendBlockStart();
    verify(codeConsumer).append("{");
  }

  @Test
  public void testAppendBlockEnd() {
    codeConsumer.appendBlockEnd();
    verify(codeConsumer).append("}");
  }

  @Test
  public void testStartNewLine() {
    codeConsumer.startNewLine();
    verify(codeConsumer, never()).append(anyString());
  }

  @Test
  public void testMaybeLineBreak() {
    codeConsumer.maybeLineBreak();
    verify(codeConsumer, never()).maybeCutLine();
  }

  @Test
  public void testMaybeCutLine() {
    codeConsumer.maybeCutLine();
    verify(codeConsumer, never()).append(anyString());
  }

  @Test
  public void testEndLine() {
    codeConsumer.endLine();
    verify(codeConsumer, never()).append(anyString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    codeConsumer.notePreferredLineBreak();
    verify(codeConsumer, never()).append(anyString());
  }

  @Test
  public void testBeginBlock() {
    codeConsumer.beginBlock();
    verify(codeConsumer).endLine();
    verify(codeConsumer).statementNeedsEnded = false;
  }

  @Test
  public void testEndBlock() {
    codeConsumer.endBlock();
    verify(codeConsumer).appendBlockEnd();
    verify(codeConsumer).endLine();
    verify(codeConsumer).statementNeedsEnded = false;
  }

  @Test
  public void testEndBlock_shouldEndLine() {
    codeConsumer.endBlock(true);
    verify(codeConsumer).appendBlockEnd();
    verify(codeConsumer).endLine();
    verify(codeConsumer).statementNeedsEnded = false;
  }

  @Test
  public void testListSeparator() {
    codeConsumer.listSeparator();
    verify(codeConsumer).add(",");
    verify(codeConsumer).maybeLineBreak();
  }

  @Test
  public void testEndStatement() {
    codeConsumer.endStatement();
    verify(codeConsumer).endStatement(false);
  }

  @Test
  public void testEndStatement_needSemiColon() {
    codeConsumer.endStatement(true);
    verify(codeConsumer).append(";");
    verify(codeConsumer).maybeLineBreak();
    verify(codeConsumer).statementNeedsEnded = false;
  }

  @Test
  public void testMaybeEndStatement() {
    codeConsumer.statementStarted = true;
    codeConsumer.maybeEndStatement();
    verify(codeConsumer).append(";");
    verify(codeConsumer).maybeLineBreak();
    verify(codeConsumer).endLine();
    verify(codeConsumer).statementNeedsEnded = false;
  }

  @Test
  public void testMaybeEndStatement_statementStartedFalse() {
    codeConsumer.statementStarted = false;
    codeConsumer.maybeEndStatement();
    verify(codeConsumer).statementStarted = true;
  }

  @Test
  public void testEndFunction() {
    codeConsumer.endFunction();
    verify(codeConsumer).endLine();
  }

  @Test
  public void testEndFunction_statementContext() {
    codeConsumer.endFunction(true);
    verify(codeConsumer).endLine();
  }

  @Test
  public void testBeginCaseBody() {
    codeConsumer.beginCaseBody();
    verify(codeConsumer).append(":");
  }

  @Test
  public void testEndCaseBody() {
    codeConsumer.endCaseBody();
    verify(codeConsumer, never()).append(anyString());
  }

  @Test
  public void testAdd() {
    String newcode = "new code";
    codeConsumer.add(newcode);
    verify(codeConsumer).maybeEndStatement();
    verify(codeConsumer).append(newcode);
  }

  @Test
  public void testAppendOp() {
    String op = "+";
    boolean binOp = true;
    codeConsumer.appendOp(op, binOp);
    verify(codeConsumer).append(op);
  }

  @Test
  public void testAddOp() {
    String op = "+";
    boolean binOp = true;
    codeConsumer.addOp(op, binOp);
    verify(codeConsumer).maybeEndStatement();
    verify(codeConsumer).append(op);
    verify(codeConsumer).maybeCutLine();
  }

  @Test
  public void testAddNumber() {
    double x = 5.0;
    codeConsumer.addNumber(x);
    verify(codeConsumer).add(String.valueOf(x));
  }

  @Test
  public void testIsNegativeZero() {
    double x = 0.0;
    boolean result = CodeConsumer.isNegativeZero(x);
    assertEquals(false, result);
  }

  @Test
  public void testIsWordChar() {
    char ch = 'a';
    boolean result = CodeConsumer.isWordChar(ch);
    assertEquals(true, result);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    boolean result = codeConsumer.shouldPreserveExtraBlocks();
    assertEquals(false, result);
  }

  @Test
  public void testBreakAfterBlockFor_statementContextTrue() {
    Node n = mock(Node.class);
    boolean statementContext = true;
    boolean result = codeConsumer.breakAfterBlockFor(n, statementContext);
    assertEquals(true, result);
  }

  @Test
  public void testBreakAfterBlockFor_statementContextFalse() {
    Node n = mock(Node.class);
    boolean statementContext = false;
    boolean result = codeConsumer.breakAfterBlockFor(n, statementContext);
    assertEquals(false, result);
  }

  @Test
  public void testEndFile() {
    codeConsumer.endFile();
    verify(codeConsumer, never()).append(anyString());
  }
}