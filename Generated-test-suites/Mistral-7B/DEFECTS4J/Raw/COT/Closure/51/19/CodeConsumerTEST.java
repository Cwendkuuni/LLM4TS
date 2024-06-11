import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node(); // Dummy Node

    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addIdentifier("identifier");
    // Assuming getLastChar() returns 'd' after appending "identifier"
    assertEquals('d', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("testString");
    assertEquals("testString", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endStatement();
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endStatement(true);
    assertEquals(";\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFunction();
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endCaseBody();
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.add("testString");
    assertEquals("testString", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addOp("+");
    assertEquals("+\n", consumer.getCode());
  }

  @Test
  public void testAddOpWithBooleanBinOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addOp("+", true);
    assertEquals("+\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addNumber(123.456);
    assertEquals("123.456\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertFalse(CodeConsumer.isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node(); // Dummy Node
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
  }

  @Test(expected = AssertionError.class)
  public void testNegativeTestSawFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.sawFunction = false;
    consumer.endFunction();
    consumer.endFunction();
  }
}

class DummyCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return getCode().charAt(getCode().length() - 1);
  }

  @Override
  public String getCode() {
    StringBuilder code = new StringBuilder();
    for (char c : append("").toCharArray()) {
      code.append(c);
    }
    return code.toString();
  }
}