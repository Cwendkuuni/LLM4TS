import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
    assertTrue(codeConsumer.continueProcessing());
  }
  
  @Test
  public void testAddIdentifier() {
    codeConsumer.addIdentifier("identifier");
  }
  
  @Test
  public void testAppend() {
    codeConsumer.append("str");
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
    codeConsumer.add("newcode");
  }
  
  @Test
  public void testAppendOp() {
    codeConsumer.appendOp("op", false);
  }
  
  @Test
  public void testAddOp() {
    codeConsumer.addOp("op", false);
  }
  
  @Test
  public void testAddNumber() {
    codeConsumer.addNumber(1.0);
  }
  
  @Test
  public void testIsNegativeZero() {
    assertTrue(CodeConsumer.isNegativeZero(0.0));
  }
  
  @Test
  public void testIsWordCharWithUnderscore() {
    assertTrue(CodeConsumer.isWordChar('_'));
  }
  
  @Test
  public void testIsWordCharWithDollarSign() {
    assertTrue(CodeConsumer.isWordChar('$'));
  }
  
  @Test
  public void testIsWordCharWithLetter() {
    assertTrue(CodeConsumer.isWordChar('a'));
  }
  
  @Test
  public void testIsWordCharWithDigit() {
    assertTrue(CodeConsumer.isWordChar('1'));
  }
  
  @Test
  public void testIsWordCharWithInvalidChar() {
    assertFalse(CodeConsumer.isWordChar('.'));
  }
  
  @Test
  public void testShouldPreserveExtraBlocks() {
    assertFalse(codeConsumer.shouldPreserveExtraBlocks());
  }
  
  @Test
  public void testBreakAfterBlockFor() {
    Node node = new Node();
    assertFalse(codeConsumer.breakAfterBlockFor(node, false));
  }
  
  @Test
  public void testEndFile() {
    codeConsumer.endFile();
  }
}