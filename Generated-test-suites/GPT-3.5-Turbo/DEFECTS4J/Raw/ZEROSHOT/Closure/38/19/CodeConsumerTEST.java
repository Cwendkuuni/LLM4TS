import org.junit.*;
import static org.junit.Assert.*;

public class CodeConsumerTest {
  
  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new CodeConsumerImpl();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // assert test case here
  }
  
  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new CodeConsumerImpl();
    Node node = new Node();
    consumer.endSourceMapping(node);
    // assert test case here
  }
  
  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new CodeConsumerImpl();
    boolean result = consumer.continueProcessing();
    // assert test case here
  }
  
  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumerImpl();
    char result = consumer.getLastChar();
    // assert test case here
  }
  
  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new CodeConsumerImpl();
    String identifier = "identifier";
    consumer.addIdentifier(identifier);
    // assert test case here
  }
  
  @Test
  public void testAppend() {
    CodeConsumer consumer = new CodeConsumerImpl();
    String str = "string";
    consumer.append(str);
    // assert test case here
  }
  
  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.appendBlockStart();
    // assert test case here
  }
  
  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.appendBlockEnd();
    // assert test case here
  }
  
  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.startNewLine();
    // assert test case here
  }
  
  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.maybeLineBreak();
    // assert test case here
  }
  
  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.maybeCutLine();
    // assert test case here
  }
  
  @Test
  public void testEndLine() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.endLine();
    // assert test case here
  }
  
  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.notePreferredLineBreak();
    // assert test case here
  }
  
  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.beginBlock();
    // assert test case here
  }
  
  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.endBlock();
    // assert test case here
  }
  
  @Test
  public void testEndBlock_shouldEndLine() {
    CodeConsumer consumer = new CodeConsumerImpl();
    boolean shouldEndLine = true;
    consumer.endBlock(shouldEndLine);
    // assert test case here
  }
  
  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.listSeparator();
    // assert test case here
  }
  
  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.endStatement();
    // assert test case here
  }
  
  @Test
  public void testEndStatement_needSemiColon() {
    CodeConsumer consumer = new CodeConsumerImpl();
    boolean needSemiColon = true;
    consumer.endStatement(needSemiColon);
    // assert test case here
  }
  
  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.maybeEndStatement();
    // assert test case here
  }
  
  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.endFunction();
    // assert test case here
  }
  
  @Test
  public void testEndFunction_statementContext() {
    CodeConsumer consumer = new CodeConsumerImpl();
    boolean statementContext = true;
    consumer.endFunction(statementContext);
    // assert test case here
  }
  
  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.beginCaseBody();
    // assert test case here
  }
  
  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.endCaseBody();
    // assert test case here
  }
  
  @Test
  public void testAdd() {
    CodeConsumer consumer = new CodeConsumerImpl();
    String newcode = "newcode";
    consumer.add(newcode);
    // assert test case here
  }
  
  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new CodeConsumerImpl();
    String op = "op";
    boolean binOp = false;
    consumer.appendOp(op, binOp);
    // assert test case here
  }
  
  @Test
  public void testAddOp() {
    CodeConsumer consumer = new CodeConsumerImpl();
    String op = "op";
    boolean binOp = false;
    consumer.addOp(op, binOp);
    // assert test case here
  }
  
  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new CodeConsumerImpl();
    double x = 2.0;
    consumer.addNumber(x);
    // assert test case here
  }
  
  @Test
  public void testIsNegativeZero() {
    double x = -0.0;
    boolean result = CodeConsumer.isNegativeZero(x);
    // assert test case here
  }
  
  @Test
  public void testIsWordChar() {
    char ch = 'a';
    boolean result = CodeConsumer.isWordChar(ch);
    // assert test case here
  }
  
  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new CodeConsumerImpl();
    boolean result = consumer.shouldPreserveExtraBlocks();
    // assert test case here
  }
  
  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new CodeConsumerImpl();
    Node n = new Node();
    boolean statementContext = false;
    boolean result = consumer.breakAfterBlockFor(n, statementContext);
    // assert test case here
  }
  
  @Test
  public void testEndFile() {
    CodeConsumer consumer = new CodeConsumerImpl();
    consumer.endFile();
    // assert test case here
  }
  
  private class CodeConsumerImpl extends CodeConsumer {
    
    @Override
    void startSourceMapping(Node node) {
      // implementation here
    }
    
    @Override
    void endSourceMapping(Node node) {
      // implementation here
    }
    
    @Override
    boolean continueProcessing() {
      // implementation here
      return false;
    }
    
    @Override
    char getLastChar() {
      // implementation here
      return 'a';
    }
    
    @Override
    void append(String str) {
      // implementation here
    }
    
    @Override
    boolean shouldPreserveExtraBlocks() {
      // implementation here
      return true;
    }
    
    @Override
    boolean breakAfterBlockFor(Node n, boolean statementContext) {
      // implementation here
      return true;
    }
    
    @Override
    void endFile() {
      // implementation here
    }
  }
}