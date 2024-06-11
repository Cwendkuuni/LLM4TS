import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.Node;
import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Verify no exceptions and method call was executed
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
    // Verify no exceptions and method call was executed
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    // Verify method returns true by default
    assertTrue(consumer.continueProcessing());

    // Derived classes can return false to stop further processing
    consumer = new DerivedCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    String code = consumer.getLastChar() + "";
    assertEquals("", code);

    consumer.append("abc");
    code = consumer.getLastChar() + "";
    assertEquals("c", code);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addIdentifier("identifier");
    String code = consumer.getCode();
    assertEquals("identifier", code);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("abc");
    String code = consumer.getCode();
    assertEquals("abc", code);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    String code = consumer.getCode();
    assertEquals("{\n", code);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.startNewLine();
    String code = consumer.getCode();
    assertEquals("\n", code);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeLineBreak();
    String code = consumer.getCode();
    assertEquals("", code);

    consumer.append("abc");
    consumer.maybeLineBreak();
    code = consumer.getCode();
    assertEquals("\n", code);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeCutLine();
    String code = consumer.getCode();
    assertEquals("", code);

    consumer.append("abc");
    consumer.maybeCutLine();
    code = consumer.getCode();
    assertEquals("abc\n", code);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endLine();
    String code = consumer.getCode();
    assertEquals("\n", code);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
    String code = consumer.getCode();
    assertEquals("", code);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    String code = consumer.getCode();
    assertEquals("{\n", code);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endBlock();
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endBlock(true);
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.listSeparator();
    String code = consumer.getCode();
    assertEquals(",\n", code);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endStatement();
    String code = consumer.getCode();
    assertEquals(";\n", code);
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endStatement(true);
    String code = consumer.getCode();
    assertEquals(";\n", code);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeEndStatement();
    String code = consumer.getCode();
    assertEquals("", code);

    consumer.append("abc");
    consumer.maybeEndStatement();
    code = consumer.getCode();
    assertEquals("abc;\n", code);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFunction();
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFunction(true);
    String code = consumer.getCode();
    assertEquals("}\n", code);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginCaseBody();
    String code = consumer.getCode();
    assertEquals(":\n", code);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endCaseBody();
    String code = consumer.getCode();
    assertEquals("", code);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.add("abc");
    String code = consumer.getCode();
    assertEquals("abc", code);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addOp("+", true);
    String code = consumer.getCode();
    assertEquals("+", code);

    consumer.append("abc");
    consumer.addOp("+", true);
    code = consumer.getCode();
    assertEquals("+abc", code);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addNumber(123);
    String code = consumer.getCode();
    assertEquals("123", code);

    consumer.addNumber(-123);
    code = consumer.getCode();
    assertEquals("-123", code);

    consumer.addNumber(1.23);
    code = consumer.getCode();
    assertEquals("1.23", code);

    consumer.addNumber(-1.23);
    code = consumer.getCode();
    assertEquals("-1.23", code);
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertEquals(false, consumer.isNegativeZero(0));

    assertEquals(true, consumer.isNegativeZero(Double.NEGATIVE_ZERO));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('z'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('Z'));
    assertTrue(consumer.isWordChar('0'));
    assertTrue(consumer.isWordChar('9'));

    assertFalse(consumer.isWordChar(' '));
    assertFalse(consumer.isWordChar('<'));
    assertFalse(consumer.isWordChar('>'));
    assertFalse(consumer.isWordChar('('));
    assertFalse(consumer.isWordChar(')'));
    assertFalse(consumer.isWordChar('['));
    assertFalse(consumer.isWordChar(']'));
    assertFalse(consumer.isWordChar('{'));
    assertFalse(consumer.isWordChar('}'));
    assertFalse(consumer.isWordChar(','));
    assertFalse(consumer.isWordChar(';'));
    assertFalse(consumer.isWordChar('.'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());

    consumer = new DerivedCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testBreakAfterBlockForWithoutStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
    String code = consumer.getCode();
    assertEquals("", code);
  }
}

class DummyCodeConsumer extends CodeConsumer {
  @Override
  void getLastChar() {
    // No-op
  }

  @Override
  String getCode() {
    return "";
  }

  @Override
  void add(String newcode) {
    // No-op
  }

  @Override
  void appendOp(String op, boolean binOp) {
    // No-op
  }

  @Override
  void addOp(String op, boolean binOp) {
    // No-op
  }

  @Override
  void addNumber(double x) {
    // No-op
  }
}

class DerivedCodeConsumer extends CodeConsumer {
  // Override methods as needed
}