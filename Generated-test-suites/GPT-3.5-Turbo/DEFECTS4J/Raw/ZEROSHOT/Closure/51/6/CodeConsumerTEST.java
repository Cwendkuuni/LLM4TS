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
        return '\0';
      }
      
      @Override
      void append(String str) {
      }
    };
  }
  
  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    codeConsumer.startSourceMapping(node);
  }
  
  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    codeConsumer.endSourceMapping(node);
  }
  
  @Test
  public void testContinueProcessing() {
    boolean result = codeConsumer.continueProcessing();
    assertTrue(result);
  }
  
  @Test
  public void testAddIdentifier() {
    String identifier = "identifier";
    codeConsumer.addIdentifier(identifier);
  }
  
  @Test
  public void testAppend() {
    String str = "test";
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
    codeConsumer.endBlock(true);
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
    codeConsumer.endStatement(true);
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
    codeConsumer.endFunction(true);
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
  public void testAdd() {
    String newcode = "new code";
    codeConsumer.add(newcode);
  }
  
  @Test
  public void testAppendOp() {
    String op = "+";
    boolean binOp = true;
    codeConsumer.appendOp(op, binOp);
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
  public void testIsWordChar() {
    char ch = 'a';
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
    Node node = new Node();
    boolean statementContext = true;
    boolean result = codeConsumer.breakAfterBlockFor(node, statementContext);
    assertTrue(result);
  }
  
  @Test
  public void testEndFile() {
    codeConsumer.endFile();
  }
  
}