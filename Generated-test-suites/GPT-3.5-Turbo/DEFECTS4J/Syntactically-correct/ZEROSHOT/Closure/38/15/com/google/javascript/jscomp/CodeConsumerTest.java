import org.junit.Test;
import org.mockito.Mockito;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.startSourceMapping(Mockito.any(Node.class));
    Mockito.verify(codeConsumer, Mockito.times(1)).startSourceMapping(Mockito.any(Node.class));
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.endSourceMapping(Mockito.any(Node.class));
    Mockito.verify(codeConsumer, Mockito.times(1)).endSourceMapping(Mockito.any(Node.class));
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    boolean result = codeConsumer.continueProcessing();
    Mockito.verify(codeConsumer, Mockito.times(1)).continueProcessing();
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    char result = codeConsumer.getLastChar();
    Mockito.verify(codeConsumer, Mockito.times(1)).getLastChar();
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.addIdentifier(Mockito.anyString());
    Mockito.verify(codeConsumer, Mockito.times(1)).addIdentifier(Mockito.anyString());
  }

  @Test
  public void testAppend() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.append(Mockito.anyString());
    Mockito.verify(codeConsumer, Mockito.times(1)).append(Mockito.anyString());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.appendBlockStart();
    Mockito.verify(codeConsumer, Mockito.times(1)).appendBlockStart();
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.appendBlockEnd();
    Mockito.verify(codeConsumer, Mockito.times(1)).appendBlockEnd();
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.startNewLine();
    Mockito.verify(codeConsumer, Mockito.times(1)).startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.maybeLineBreak();
    Mockito.verify(codeConsumer, Mockito.times(1)).maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.maybeCutLine();
    Mockito.verify(codeConsumer, Mockito.times(1)).maybeCutLine();
  }

  @Test
  public void testEndLine() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.endLine();
    Mockito.verify(codeConsumer, Mockito.times(1)).endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.notePreferredLineBreak();
    Mockito.verify(codeConsumer, Mockito.times(1)).notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.beginBlock();
    Mockito.verify(codeConsumer, Mockito.times(1)).beginBlock();
  }

  @Test
  public void testEndBlock() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.endBlock();
    Mockito.verify(codeConsumer, Mockito.times(1)).endBlock();
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.endBlock(Mockito.anyBoolean());
    Mockito.verify(codeConsumer, Mockito.times(1)).endBlock(Mockito.anyBoolean());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.listSeparator();
    Mockito.verify(codeConsumer, Mockito.times(1)).listSeparator();
  }

  @Test
  public void testEndStatement() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.endStatement();
    Mockito.verify(codeConsumer, Mockito.times(1)).endStatement();
  }

  @Test
  public void testEndStatementWithNeedSemiColon() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.endStatement(Mockito.anyBoolean());
    Mockito.verify(codeConsumer, Mockito.times(1)).endStatement(Mockito.anyBoolean());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.maybeEndStatement();
    Mockito.verify(codeConsumer, Mockito.times(1)).maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.endFunction();
    Mockito.verify(codeConsumer, Mockito.times(1)).endFunction();
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.endFunction(Mockito.anyBoolean());
    Mockito.verify(codeConsumer, Mockito.times(1)).endFunction(Mockito.anyBoolean());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.beginCaseBody();
    Mockito.verify(codeConsumer, Mockito.times(1)).beginCaseBody();
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.endCaseBody();
    Mockito.verify(codeConsumer, Mockito.times(1)).endCaseBody();
  }

  @Test
  public void testAdd() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.add(Mockito.anyString());
    Mockito.verify(codeConsumer, Mockito.times(1)).add(Mockito.anyString());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.appendOp(Mockito.anyString(), Mockito.anyBoolean());
    Mockito.verify(codeConsumer, Mockito.times(1)).appendOp(Mockito.anyString(), Mockito.anyBoolean());
  }

  @Test
  public void testAddOp() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.addOp(Mockito.anyString(), Mockito.anyBoolean());
    Mockito.verify(codeConsumer, Mockito.times(1)).addOp(Mockito.anyString(), Mockito.anyBoolean());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.addNumber(Mockito.anyDouble());
    Mockito.verify(codeConsumer, Mockito.times(1)).addNumber(Mockito.anyDouble());
  }

  @Test
  public void testIsNegativeZero() {
    boolean result = CodeConsumer.isNegativeZero(Mockito.anyDouble());
  }

  @Test
  public void testIsWordChar() {
    boolean result = CodeConsumer.isWordChar(Mockito.anyChar());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer codeConsumer = new CodeConsumer();
    boolean result = codeConsumer.shouldPreserveExtraBlocks();
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer codeConsumer = new CodeConsumer();
    boolean result = codeConsumer.breakAfterBlockFor(Mockito.any(Node.class), Mockito.anyBoolean());
  }

  @Test
  public void testEndFile() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class);
    codeConsumer.endFile();
    Mockito.verify(codeConsumer, Mockito.times(1)).endFile();
  }

}