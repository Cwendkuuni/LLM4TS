import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if startSourceMapping method does not throw any exception
    assertTrue(true);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAbstractMethodGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    // Test for abstract method getLastChar which does not have any implementation
    consumer.getLastChar();
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    String str = "testString";
    consumer.append(str);
    // Check if the appended string is correctly added to the code
    assertEquals(str, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    // Check if a newline is correctly added to the code
    assertEquals("\n", consumer.getCode().substring(consumer.getCode().length() - 1));
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    // Check if a line break is added to the code if necessary
    assertTrue(consumer.getCode().contains("\n"));
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    String line = "testLine";
    consumer.append(line);
    consumer.maybeCutLine();
    // Check if the line is correctly split if necessary
    assertEquals(line.substring(0, line.length() / 2), consumer.getCode().substring(0, consumer.getCode().length() / 2));
    assertEquals(line.substring(line.length() / 2), consumer.getCode().substring(consumer.getCode().length() / 2));
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("testString");
    consumer.endLine();
    // Check if a semicolon and newline are correctly added to the code
    assertTrue(consumer.getCode().endsWith(";"));
    assertTrue(consumer.getCode().endsWith("\n"));
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    // Check if a line break is marked as preferred
    assertTrue(consumer.getCode().contains("//line"));
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    // Check if the block is correctly started with {"
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("testCode");
    consumer.endBlock();
    // Check if the block is correctly ended with }
    assertEquals("testCode\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    // Check if a comma and newline are correctly added to the code
    assertTrue(consumer.getCode().endsWith(",\n"));
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("testCode;");
    consumer.endStatement();
    // Check if a semicolon and newline are correctly added to the code if needed
    assertTrue(consumer.getCode().endsWith(";"));
    assertTrue(consumer.getCode().endsWith("\n"));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("testCode");
    consumer.endFunction();
    // Check if the function is correctly ended with }
    assertEquals("testCode\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.beginCaseBody();
    // Check if the case body is correctly started with :
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.beginCaseBody();
    consumer.append("testCode");
    consumer.endCaseBody();
    // Check if the case body is correctly ended
    assertEquals("testCode\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("testPrefix");
    consumer.add("testIdentifier");
    // Check if the identifier is correctly added to the code
    assertEquals("testPrefix testIdentifier\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("testPrefix");
    consumer.addOp("+", false);
    consumer.add(5);
    // Check if the addition operation is correctly added to the code
    assertEquals("testPrefix + 5\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("testPrefix");
    consumer.addNumber(5);
    // Check if the number is correctly added to the code
    assertEquals("testPrefix 5\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('1'));
    assertFalse(CodeConsumer.isWordChar(' '));
    assertFalse(CodeConsumer.isWordChar('('));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    // Test if single-statement blocks should be preserved
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    // Test if a line break can be added after a block based on the statement context
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    // Test if the code generation can be interrupted
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testStatementNeedsEndedAndStatementStarted() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("testCode;");
    consumer.endStatement();
    consumer.append("anotherTestCode;");
    consumer.endStatement();
    // Check if statementNeedsEnded and statementStarted flags are correctly set
    assertFalse(consumer.statementNeedsEnded);
    assertFalse(consumer.statementStarted);
    consumer.append("testCode;");
    consumer.endStatement();
    consumer.append("anotherTestCode;");
    consumer.endStatement();
    assertTrue(consumer.statementNeedsEnded);
    assertTrue(consumer.statementStarted);
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertFalse(consumer.sawFunction);
    consumer.append("function testFunction() {}");
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

}

class StubCodeConsumer extends CodeConsumer {
  private String code = "";

  @Override
  char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  @Override
  void append(String str) {
    code += str;
  }

  @Override
  void add(String str) {
    super.add(str);
  }

  @Override
  void addOp(String op, boolean binOp) {
    super.addOp(op, binOp);
  }

  @Override
  void addNumber(double x) {
    super.addNumber(x);
  }

  @Override
  String getCode() {
    return code;
  }
}