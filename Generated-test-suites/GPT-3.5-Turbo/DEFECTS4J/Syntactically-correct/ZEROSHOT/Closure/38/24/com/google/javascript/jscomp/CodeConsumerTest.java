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
        return 'a';
      }
      
      @Override
      void append(String str) {
        // do nothing
      }
    };
  }
  
  @Test
  public void testStartSourceMapping() {
    codeConsumer.startSourceMapping(null);
  }
  
  @Test
  public void testEndSourceMapping() {
    codeConsumer.endSourceMapping(null);
  }
  
  @Test
  public void testContinueProcessing() {
    boolean result = codeConsumer.continueProcessing();
    assertTrue(result);
  }
  
  @Test
  public void testAddIdentifier() {
    codeConsumer.addIdentifier("identifier");
  }
  
  @Test
  public void testAppend() {
    codeConsumer.append("string");
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
  public void testEndBlockWithParameter() {
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
  public void testEndStatementWithParameter() {
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
  public void testEndFunctionWithParameter() {
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
    codeConsumer.add("newcode");
  }
  
  @Test
  public void testAppendOp() {
    codeConsumer.appendOp("op", true);
  }
  
  @Test
  public void testAddOp() {
    codeConsumer.addOp("op", true);
  }
  
  @Test
  public void testAddNumber() {
    codeConsumer.addNumber(1.0);
  }
  
  @Test
  public void testIsNegativeZero() {
    boolean result = CodeConsumer.isNegativeZero(0.0);
    assertFalse(result);
  }
  
  @Test
  public void testIsWordChar() {
    boolean result = CodeConsumer.isWordChar('a');
    assertTrue(result);
  }
  
  @Test
  public void testShouldPreserveExtraBlocks() {
    boolean result = codeConsumer.shouldPreserveExtraBlocks();
    assertFalse(result);
  }
  
  @Test
  public void testBreakAfterBlockFor() {
    boolean result = codeConsumer.breakAfterBlockFor(null, true);
    assertTrue(result);
  }
  
  @Test
  public void testEndFile() {
    codeConsumer.endFile();
  }
  
}