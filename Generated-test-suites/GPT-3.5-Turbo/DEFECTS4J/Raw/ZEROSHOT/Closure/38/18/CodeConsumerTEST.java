import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodeConsumerTest {
  
  private CodeConsumer codeConsumer;
  
  @Before
  public void setUp() {
    codeConsumer = new CodeConsumer() {
      @Override
      char getLastChar() {
        return ' ';
      }
      @Override
      void append(String str) { }
    };
  }
  
  @Test
  public void testStartSourceMapping() {
    Node node = null;
    codeConsumer.startSourceMapping(node);
  }
  
  @Test
  public void testEndSourceMapping() {
    Node node = null;
    codeConsumer.endSourceMapping(node);
  }
  
  @Test
  public void testContinueProcessing() {
    assertTrue(codeConsumer.continueProcessing());
  }
  
  @Test
  public void testAddIdentifier() {
    String identifier = "identifier";
    codeConsumer.addIdentifier(identifier);
  }
  
  @Test
  public void testAppend() {
    String str = "append";
    codeConsumer.append(str);
  }
  
  @Test
  public void testAppendBlockStart() {
    codeConsumer.appendBlockStart();
  }
  
  @Test
  public void testAppendBlockEnd() {
    codeConsumer.appendBlockEnd();
  }
  
  @Test
  public void testStartNewLine() {
    codeConsumer.startNewLine();
  }
  
  @Test
  public void testMaybeLineBreak() {
    codeConsumer.maybeLineBreak();
  }
  
  @Test
  public void testMaybeCutLine() {
    codeConsumer.maybeCutLine();
  }
  
  @Test
  public void testEndLine() {
    codeConsumer.endLine();
  }
  
  @Test
  public void testNotePreferredLineBreak() {
    codeConsumer.notePreferredLineBreak();
  }
  
  @Test
  public void testBeginBlock() {
    codeConsumer.beginBlock();
  }
  
  @Test
  public void testEndBlock() {
    codeConsumer.endBlock();
  }
  
  @Test
  public void testEndBlockWithShouldEndLine() {
    boolean shouldEndLine = true;
    codeConsumer.endBlock(shouldEndLine);
  }
  
  @Test
  public void testListSeparator() {
    codeConsumer.listSeparator();
  }
  
  @Test
  public void testEndStatement() {
    codeConsumer.endStatement();
  }
  
  @Test
  public void testEndStatementWithNeedSemiColon() {
    boolean needSemiColon = true;
    codeConsumer.endStatement(needSemiColon);
  }
  
  @Test
  public void testMaybeEndStatement() {
    codeConsumer.maybeEndStatement();
  }
  
  @Test
  public void testEndFunction() {
    codeConsumer.endFunction();
  }
  
  @Test
  public void testEndFunctionWithStatementContext() {
    boolean statementContext = true;
    codeConsumer.endFunction(statementContext);
  }
  
  @Test
  public void testBeginCaseBody() {
    codeConsumer.beginCaseBody();
  }
  
  @Test
  public void testEndCaseBody() {
    codeConsumer.endCaseBody();
  }
  
  @Test
  public void testAddOp() {
    String op = "+";
    boolean binOp = true;
    codeConsumer.addOp(op, binOp);
  }
  
  @Test
  public void testAddNumber() {
    double x = 10.5;
    codeConsumer.addNumber(x);
  }
  
  @Test
  public void testShouldPreserveExtraBlocks() {
    assertFalse(codeConsumer.shouldPreserveExtraBlocks());
  }
  
  @Test
  public void testBreakAfterBlockFor() {
    Node n = null;
    boolean statementContext = true;
    assertFalse(codeConsumer.breakAfterBlockFor(n, statementContext));
  }
  
  @Test
  public void testEndFile() {
    codeConsumer.endFile();
  }
}