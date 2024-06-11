import org.junit.Test;
import static org.junit.Assert.*;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();
    Node node = new Node();

    codeConsumer.startSourceMapping(node);
    // add assertions
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();
    Node node = new Node();

    codeConsumer.endSourceMapping(node);
    // add assertions
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    boolean result = codeConsumer.continueProcessing();
    // add assertions
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    char lastChar = codeConsumer.getLastChar();
    // add assertions
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();
    String identifier = "test";

    codeConsumer.addIdentifier(identifier);
    // add assertions
  }

  @Test
  public void testAppend() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();
    String str = "test";

    codeConsumer.append(str);
    // add assertions
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.appendBlockStart();
    // add assertions
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.appendBlockEnd();
    // add assertions
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.startNewLine();
    // add assertions
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.maybeLineBreak();
    // add assertions
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.maybeCutLine();
    // add assertions
  }

  @Test
  public void testEndLine() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.endLine();
    // add assertions
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.notePreferredLineBreak();
    // add assertions
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.beginBlock();
    // add assertions
  }

  @Test
  public void testEndBlock() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.endBlock();
    // add assertions
  }

  @Test
  public void testListSeparator() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.listSeparator();
    // add assertions
  }

  @Test
  public void testEndStatement() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.endStatement();
    // add assertions
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.maybeEndStatement();
    // add assertions
  }

  @Test
  public void testEndFunction() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.endFunction();
    // add assertions
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.beginCaseBody();
    // add assertions
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.endCaseBody();
    // add assertions
  }

  @Test
  public void testAdd() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();
    String newCode = "test";

    codeConsumer.add(newCode);
    // add assertions
  }

  @Test
  public void testAppendOp() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();
    String op = "+";
    boolean binOp = true;

    codeConsumer.appendOp(op, binOp);
    // add assertions
  }

  @Test
  public void testAddOp() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();
    String op = "+";
    boolean binOp = true;

    codeConsumer.addOp(op, binOp);
    // add assertions
  }

  @Test
  public void testAddNumber() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();
    double x = 5.0;

    codeConsumer.addNumber(x);
    // add assertions
  }

  @Test
  public void testIsNegativeZero() {
    double x = -0.0;

    boolean result = CodeConsumer.isNegativeZero(x);
    // add assertions
  }

  @Test
  public void testIsWordChar() {
    char ch = '_';

    boolean result = CodeConsumer.isWordChar(ch);
    // add assertions
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    boolean result = codeConsumer.shouldPreserveExtraBlocks();
    // add assertions
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();
    Node n = new Node();
    boolean statementContext = true;

    boolean result = codeConsumer.breakAfterBlockFor(n, statementContext);
    // add assertions
  }

  @Test
  public void testEndFile() {
    CodeConsumer codeConsumer = new CodeConsumerImpl();

    codeConsumer.endFile();
    // add assertions
  }
}