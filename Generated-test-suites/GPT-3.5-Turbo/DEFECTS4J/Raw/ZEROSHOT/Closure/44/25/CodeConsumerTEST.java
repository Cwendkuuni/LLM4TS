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
        return 0;
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
    // assertion here
  }
  
  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    codeConsumer.endSourceMapping(node);
    // assertion here
  }
  
  @Test
  public void testContinueProcessing() {
    boolean result = codeConsumer.continueProcessing();
    // assertion here
  }
  
  @Test
  public void testAddIdentifier() {
    String identifier = "identifier";
    codeConsumer.addIdentifier(identifier);
    // assertion here
  }
  
  @Test
  public void testAppend() {
    String str = "string";
    codeConsumer.append(str);
    // assertion here
  }
  
  @Test
  public void testAppendBlockStart() {
    codeConsumer.appendBlockStart();
    // assertion here
  }
  
  @Test
  public void testAppendBlockEnd() {
    codeConsumer.appendBlockEnd();
    // assertion here
  }
  
  @Test
  public void testStartNewLine() {
    codeConsumer.startNewLine();
    // assertion here
  }
  
  @Test
  public void testMaybeLineBreak() {
    codeConsumer.maybeLineBreak();
    // assertion here
  }
  
  @Test
  public void testMaybeCutLine() {
    codeConsumer.maybeCutLine();
    // assertion here
  }
  
  @Test
  public void testEndLine() {
    codeConsumer.endLine();
    // assertion here
  }
  
  @Test
  public void testNotePreferredLineBreak() {
    codeConsumer.notePreferredLineBreak();
    // assertion here
  }
  
  @Test
  public void testBeginBlock() {
    codeConsumer.beginBlock();
    // assertion here
  }
  
  @Test
  public void testEndBlock() {
    codeConsumer.endBlock();
    // assertion here
  }
  
  @Test
  public void testEndBlockWithEndLine() {
    codeConsumer.endBlock(true);
    // assertion here
  }
  
  @Test
  public void testListSeparator() {
    codeConsumer.listSeparator();
    // assertion here
  }
  
  @Test
  public void testEndStatement() {
    codeConsumer.endStatement();
    // assertion here
  }
  
  @Test
  public void testEndStatementWithSemiColon() {
    boolean needSemiColon = true;
    codeConsumer.endStatement(needSemiColon);
    // assertion here
  }
  
  @Test
  public void testMaybeEndStatement() {
    codeConsumer.maybeEndStatement();
    // assertion here
  }
  
  @Test
  public void testEndFunction() {
    codeConsumer.endFunction();
    // assertion here
  }
  
  @Test
  public void testEndFunctionWithStatementContext() {
    boolean statementContext = true;
    codeConsumer.endFunction(statementContext);
    // assertion here
  }
  
  @Test
  public void testBeginCaseBody() {
    codeConsumer.beginCaseBody();
    // assertion here
  }
  
  @Test
  public void testEndCaseBody() {
    codeConsumer.endCaseBody();
    // assertion here
  }
  
  @Test
  public void testAddOp() {
    String op = "op";
    boolean binOp = true;
    codeConsumer.addOp(op, binOp);
    // assertion here
  }
  
  @Test
  public void testAddNumberForLongValue() {
    double x = 12345;
    codeConsumer.addNumber(x);
    // assertion here
  }
  
  @Test
  public void testAddNumberForDoubleValue() {
    double x = 12.34;
    codeConsumer.addNumber(x);
    // assertion here
  }
  
  @Test
  public void testIsNegativeZeroForNegativeZero() {
    double x = -0.0;
    boolean result = CodeConsumer.isNegativeZero(x);
    // assertion here
  }
  
  @Test
  public void testIsNegativeZeroForPositiveZero() {
    double x = 0.0;
    boolean result = CodeConsumer.isNegativeZero(x);
    // assertion here
  }
  
  @Test
  public void testIsWordCharForUnderscore() {
    char ch = '_';
    boolean result = CodeConsumer.isWordChar(ch);
    // assertion here
  }
  
  @Test
  public void testIsWordCharForDollarSign() {
    char ch = '$';
    boolean result = CodeConsumer.isWordChar(ch);
    // assertion here
  }
  
  @Test
  public void testIsWordCharForLetter() {
    char ch = 'a';
    boolean result = CodeConsumer.isWordChar(ch);
    // assertion here
  }
  
  @Test
  public void testIsWordCharForDigit() {
    char ch = '7';
    boolean result = CodeConsumer.isWordChar(ch);
    // assertion here
  }
  
  @Test
  public void testShouldPreserveExtraBlocks() {
    boolean result = codeConsumer.shouldPreserveExtraBlocks();
    // assertion here
  }
  
  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    Node n = new Node();
    boolean statementContext = true;
    boolean result = codeConsumer.breakAfterBlockFor(n, statementContext);
    // assertion here
  }
  
  @Test
  public void testBreakAfterBlockForWithoutStatementContext() {
    Node n = new Node();
    boolean statementContext = false;
    boolean result = codeConsumer.breakAfterBlockFor(n, statementContext);
    // assertion here
  }
  
  @Test
  public void testEndFile() {
    codeConsumer.endFile();
    // assertion here
  }
}