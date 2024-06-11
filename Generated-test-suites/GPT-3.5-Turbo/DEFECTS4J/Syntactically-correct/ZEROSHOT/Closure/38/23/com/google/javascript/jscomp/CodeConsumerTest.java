import static org.junit.Assert.*;
import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer codeConsumer = new CodeConsumer();
    Node node = new Node();
    codeConsumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer codeConsumer = new CodeConsumer();
    Node node = new Node();
    codeConsumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer codeConsumer = new CodeConsumer();
    assertTrue(codeConsumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer codeConsumer = new CodeConsumer();
    assertEquals('\u0000', codeConsumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.addIdentifier("identifier");
  }

  @Test
  public void testAppend() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.append("string");
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.appendBlockStart();
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.appendBlockEnd();
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.beginBlock();
  }

  @Test
  public void testEndBlock() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.endBlock();
  }

  @Test
  public void testEndBlockTrue() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.endBlock(true);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.listSeparator();
  }

  @Test
  public void testEndStatement() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.endStatement();
  }

  @Test
  public void testEndStatementTrue() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.endStatement(true);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.endFunction();
  }

  @Test
  public void testEndFunctionTrue() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.beginCaseBody();
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.endCaseBody();
  }

  @Test
  public void testAdd() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.add("newcode");
  }

  @Test
  public void testAppendOp() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.appendOp("op", true);
  }

  @Test
  public void testAddOp() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.addOp("op", true);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.addNumber(1.0);
  }

  @Test
  public void testIsNegativeZero() {
    assertTrue(CodeConsumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('a'));
    assertFalse(CodeConsumer.isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer codeConsumer = new CodeConsumer();
    assertFalse(codeConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer codeConsumer = new CodeConsumer();
    Node node = new Node();
    assertFalse(codeConsumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer codeConsumer = new CodeConsumer();
    codeConsumer.endFile();
  }
}