import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodeConsumerTest {
  
  private CodeConsumer codeConsumer;
  
  @Before
  public void setUp() {
    this.codeConsumer = new CodeConsumer() {
      
      private StringBuilder code = new StringBuilder();
      
      public char getLastChar() {
        if (code.length() > 0) {
          return code.charAt(code.length() - 1);
        }
        return '\0';
      }
      
      public void append(String str) {
        code.append(str);
      }
      
    };
  }

  @Test
  public void testStartSourceMapping() {
    // Test that startSourceMapping does not throw any exceptions
    codeConsumer.startSourceMapping(null);
  }
  
  @Test
  public void testEndSourceMapping() {
    // Test that endSourceMapping does not throw any exceptions
    codeConsumer.endSourceMapping(null);
  }
  
  @Test
  public void testContinueProcessing() {
    // Test that continueProcessing returns true
    assertTrue(codeConsumer.continueProcessing());
  }
  
  @Test
  public void testAddIdentifier() {
    // Test that addIdentifier appends the identifier to the code
    codeConsumer.addIdentifier("identifier");
    assertEquals("identifier", codeConsumer.code.toString());
  }
  
  @Test
  public void testAppend() {
    // Test that append appends the string to the code
    codeConsumer.append("string");
    assertEquals("string", codeConsumer.code.toString());
  }
  
  @Test
  public void testAppendBlockStart() {
    // Test that appendBlockStart appends "{" to the code
    codeConsumer.appendBlockStart();
    assertEquals("{", codeConsumer.code.toString());
  }
  
  @Test
  public void testAppendBlockEnd() {
    // Test that appendBlockEnd appends "}" to the code
    codeConsumer.appendBlockEnd();
    assertEquals("}", codeConsumer.code.toString());
  }
  
  @Test
  public void testStartNewLine() {
    // Test that startNewLine does not throw any exceptions
    codeConsumer.startNewLine();
  }
  
  @Test
  public void testMaybeLineBreak() {
    // Test that maybeLineBreak does not throw any exceptions
    codeConsumer.maybeLineBreak();
  }
  
  @Test
  public void testMaybeCutLine() {
    // Test that maybeCutLine does not throw any exceptions
    codeConsumer.maybeCutLine();
  }
  
  @Test
  public void testEndLine() {
    // Test that endLine does not throw any exceptions
    codeConsumer.endLine();
  }
  
  @Test
  public void testNotePreferredLineBreak() {
    // Test that notePreferredLineBreak does not throw any exceptions
    codeConsumer.notePreferredLineBreak();
  }
  
  @Test
  public void testBeginBlock() {
    // Test that beginBlock appends ";" and "{" to the code
    codeConsumer.statementNeedsEnded = true;
    codeConsumer.beginBlock();
    assertEquals(";", codeConsumer.code.toString().substring(0,1));
    assertEquals("{", codeConsumer.code.toString().substring(1));
  }
  
  @Test
  public void testEndBlock() {
    // Test that endBlock appends "}" to the code
    codeConsumer.endBlock();
    assertEquals("}", codeConsumer.code.toString());
  }
  
  @Test
  public void testListSeparator() {
    // Test that listSeparator appends "," to the code
    codeConsumer.listSeparator();
    assertEquals(",", codeConsumer.code.toString());
  }
  
  @Test
  public void testEndStatement() {
    // Test that endStatement appends ";" to the code
    codeConsumer.endStatement();
    assertEquals(";", codeConsumer.code.toString());
  }
  
  @Test
  public void testMaybeEndStatement() {
    // Test that maybeEndStatement appends ";" to the code if statementNeedsEnded is true
    codeConsumer.statementNeedsEnded = true;
    codeConsumer.maybeEndStatement();
    assertEquals(";", codeConsumer.code.toString());
    
    // Test that maybeEndStatement does not append ";" to the code if statementNeedsEnded is false
    codeConsumer.statementNeedsEnded = false;
    codeConsumer.maybeEndStatement();
    assertEquals("", codeConsumer.code.toString());
  }
  
  @Test
  public void testEndFunction() {
    // Test that endFunction does not throw any exceptions
    codeConsumer.endFunction();
  }
  
  @Test
  public void testAdd() {
    // Test that add appends the new code to the code
    codeConsumer.code.append("previous");
    codeConsumer.add("new");
    assertEquals("previous new", codeConsumer.code.toString());
  }
  
  @Test
  public void testAppendOp() {
    // Test that appendOp appends the operator to the code
    codeConsumer.appendOp("+", false);
    assertEquals("+", codeConsumer.code.toString());
  }
  
  @Test
  public void testAddOp() {
    // Test that addOp appends the operator to the code
    codeConsumer.addOp("+", false);
    assertEquals("+", codeConsumer.code.toString());
  }
  
  @Test
  public void testAddNumber() {
    // Test that addNumber appends the number to the code
    codeConsumer.addNumber(5.0);
    assertEquals("5.0", codeConsumer.code.toString());
  }
  
}