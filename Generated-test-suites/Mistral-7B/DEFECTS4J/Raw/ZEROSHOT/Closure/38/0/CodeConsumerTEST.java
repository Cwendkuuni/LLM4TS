import static org.junit.Assert.*;
import org.junit.Test;
import static com.google.javascript.jscomp.CodeConsumer.*;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if startSourceMapping was called
    assertTrue(consumer.sourceMappedNodes.contains(node));
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Check if endSourceMapping was called and source mapping was removed
    assertFalse(consumer.sourceMappedNodes.contains(node));
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingWhenFalse() {
    CodeConsumer consumer = new CodeConsumerWithContinueProcessingFalse();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    String expected = "test";
    StringBuilder actual = new StringBuilder();
    consumer.getCode().append(actual);
    assertEquals(expected, actual.toString());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("test");
    String expected = "test";
    StringBuilder actual = new StringBuilder();
    consumer.getCode().append(actual);
    assertEquals(expected, actual.toString());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.getCode().toString());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getCode().toString());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode().toString());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode().toString());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    assertEquals("", consumer.getCode().toString());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode().toString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode().toString());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode().toString());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode().toString());
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode().toString());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode().toString());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.getCode().toString());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    assertEquals("test;\n", consumer.getCode().toString());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    assertEquals("test", consumer.getCode().toString());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function test() {");
    consumer.endFunction();
    assertEquals("function test() {\n}", consumer.getCode().toString());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function test() {");
    consumer.endFunction(true);
    assertEquals("function test() {\n}", consumer.getCode().toString());
    consumer.append("return 5;");
    consumer.maybeEndStatement();
    assertEquals("function test() {\n  return 5;\n}", consumer.getCode().toString());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode().toString());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    consumer.endCaseBody();
    assertEquals(":\n", consumer.getCode().toString());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.add("test");
    String expected = "test";
    StringBuilder actual = new StringBuilder();
    consumer.getCode().append(actual);
    assertEquals(expected, actual.toString());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addOp("+", false);
    assertEquals(" +", consumer.getCode().toString());
  }

  @Test
  public void testAddOpBinary() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addOp("+", true);
    assertEquals(" +", consumer.getCode().toString());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(5.0);
    assertEquals("5.0", consumer.getCode().toString());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, isNegativeZero(5.0));
    assertEquals(true, isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('1'));
    assertFalse(isWordChar('@'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode().toString());
  }
}

class StubCodeConsumer extends CodeConsumer {
  private StringBuilder code = new StringBuilder();
  private List<Node> sourceMappedNodes = new ArrayList<>();

  @Override
  public void startSourceMapping(Node node) {
    sourceMappedNodes.add(node);
  }

  @Override
  public void endSourceMapping(Node node) {}

  @Override
  public char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  @Override
  public void add(String newcode) {
    super.add(newcode);
  }

  @Override
  public StringBuilder getCode() {
    return code;
  }
}

class CodeConsumerWithContinueProcessingFalse extends CodeConsumer {
  @Override
  public boolean continueProcessing() {
    return false;
  }
}