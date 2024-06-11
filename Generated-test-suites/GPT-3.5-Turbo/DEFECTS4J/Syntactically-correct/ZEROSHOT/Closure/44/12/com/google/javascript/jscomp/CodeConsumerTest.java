import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class CodeConsumerTest {
  
  @Test
  public void testStartSourceMapping() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    Node node = Mockito.mock(Node.class);
    
    codeConsumer.startSourceMapping(node);
    
    // Your assertions here
  }
  
  @Test
  public void testEndSourceMapping() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    Node node = Mockito.mock(Node.class);
    
    codeConsumer.endSourceMapping(node);
    
    // Your assertions here
  }
  
  @Test
  public void testContinueProcessing() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    boolean result = codeConsumer.continueProcessing();
    
    // Your assertions here
  }
  
  @Test
  public void testGetLastChar() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    char result = codeConsumer.getLastChar();
    
    // Your assertions here
  }
  
  @Test
  public void testAddIdentifier() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    String identifier = "identifier";
    
    codeConsumer.addIdentifier(identifier);
    
    // Your assertions here
  }
  
  @Test
  public void testAppend() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    String str = "string";
    
    codeConsumer.append(str);
    
    // Your assertions here
  }
  
  @Test
  public void testAppendBlockStart() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.appendBlockStart();
    
    // Your assertions here
  }
  
  @Test
  public void testAppendBlockEnd() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.appendBlockEnd();
    
    // Your assertions here
  }
  
  @Test
  public void testStartNewLine() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.startNewLine();
    
    // Your assertions here
  }
  
  @Test
  public void testMaybeLineBreak() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.maybeLineBreak();
    
    // Your assertions here
  }
  
  @Test
  public void testMaybeCutLine() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.maybeCutLine();
    
    // Your assertions here
  }
  
  @Test
  public void testEndLine() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.endLine();
    
    // Your assertions here
  }
  
  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.notePreferredLineBreak();
    
    // Your assertions here
  }
  
  @Test
  public void testBeginBlock() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.beginBlock();
    
    // Your assertions here
  }
  
  @Test
  public void testEndBlock() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.endBlock();
    
    // Your assertions here
  }
  
  @Test
  public void testEndBlock_with_shouldEndLine() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    boolean shouldEndLine = true;
    
    codeConsumer.endBlock(shouldEndLine);
    
    // Your assertions here
  }
  
  @Test
  public void testListSeparator() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.listSeparator();
    
    // Your assertions here
  }
  
  @Test
  public void testEndStatement() {
    // Test case for endStatement()
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.endStatement();
    
    // Your assertions here
  }
  
  @Test
  public void testEndStatement_with_needSemiColon() {
    // Test case for endStatement(boolean needSemiColon)
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    boolean needSemiColon = true;
    
    codeConsumer.endStatement(needSemiColon);
    
    // Your assertions here
  }
  
  @Test
  public void testMaybeEndStatement() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.maybeEndStatement();
    
    // Your assertions here
  }
  
  @Test
  public void testEndFunction() {
    // Test case for endFunction()
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.endFunction();
    
    // Your assertions here
  }
  
  @Test
  public void testEndFunction_with_statementContext() {
    // Test case for endFunction(boolean statementContext)
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    boolean statementContext = true;
    
    codeConsumer.endFunction(statementContext);
    
    // Your assertions here
  }
  
  @Test
  public void testBeginCaseBody() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.beginCaseBody();
    
    // Your assertions here
  }
  
  @Test
  public void testEndCaseBody() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.endCaseBody();
    
    // Your assertions here
  }
  
  @Test
  public void testAdd() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    String newcode = "newcode";
    
    codeConsumer.add(newcode);
    
    // Your assertions here
  }
  
  @Test
  public void testAppendOp() {
    // Test case for appendOp(String op, boolean binOp)
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    String op = "op";
    boolean binOp = true;
    
    codeConsumer.appendOp(op, binOp);
    
    // Your assertions here
  }
  
  @Test
  public void testAddOp() {
    // Test case for addOp(String op, boolean binOp)
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    String op = "op";
    boolean binOp = true;
    
    codeConsumer.addOp(op, binOp);
    
    // Your assertions here
  }
  
  @Test
  public void testAddNumber() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    double x = 1.0;
    
    codeConsumer.addNumber(x);
    
    // Your assertions here
  }
  
  @Test
  public void testIsNegativeZero() {
    // Test case for isNegativeZero(double x)
    double x = -0.0;
    
    boolean result = CodeConsumer.isNegativeZero(x);
    
    // Your assertions here
  }
  
  @Test
  public void testIsWordChar() {
    // Test case for isWordChar(char ch)
    char ch = '_';
    
    boolean result = CodeConsumer.isWordChar(ch);
    
    // Your assertions here
  }
  
  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    boolean result = codeConsumer.shouldPreserveExtraBlocks();
    
    // Your assertions here
  }
  
  @Test
  public void testBreakAfterBlockFor() {
    // Test case for breakAfterBlockFor(Node n, boolean statementContext)
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    Node n = Mockito.mock(Node.class);
    boolean statementContext = true;
    
    boolean result = codeConsumer.breakAfterBlockFor(n, statementContext);
    
    // Your assertions here
  }
  
  @Test
  public void testEndFile() {
    CodeConsumer codeConsumer = Mockito.mock(CodeConsumer.class, Mockito.CALLS_REAL_METHODS);
    
    codeConsumer.endFile();
    
    // Your assertions here
  }
}