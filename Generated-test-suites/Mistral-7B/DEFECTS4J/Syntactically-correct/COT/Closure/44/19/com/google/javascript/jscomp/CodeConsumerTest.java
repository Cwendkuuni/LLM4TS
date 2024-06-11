import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if startSourceMapping method was called
    assertTrue(consumer.statementStarted);

    consumer.endSourceMapping(node);
    // Check if endSourceMapping method was called
    assertFalse(consumer.statementStarted);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    // Test continueProcessing method returns true by default
    assertTrue(consumer.continueProcessing());

    consumer = new ThrowingCodeConsumer();
    // Test continueProcessing method returns false when an exception is thrown
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());

    consumer = new DummyCodeConsumer();
    consumer.append(" ");
    assertEquals(' ', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("Hello World");
    String expectedOutput = "Hello World";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getLastChar());
  }

  @Test (expected = AssertionError.class)
  public void testAppendBlockEndWithStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    consumer.append(";");
    consumer.appendBlockEnd();
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.startNewLine();
    consumer.append("newline");
    String expectedOutput = "test\nnewline";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.maybeCutLine();
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    consumer.append("newline");
    String expectedOutput = "test\nnewline";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.getLastChar());
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
    consumer.append("test");
    consumer.endBlock();
    String expectedOutput = "test}\n";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndBlockWithStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test;");
    consumer.endBlock();
    String expectedOutput = "test;\n}";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    String expectedOutput = "test1,\n test2";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test;");
    consumer.endStatement();
    String expectedOutput = "test;\n";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test;");
    consumer.endStatement(true);
    String expectedOutput = "test;\n";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    String expectedOutput = "test;\n";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test (expected = AssertionError.class)
  public void testMaybeEndStatementWithEmptyString() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("");
    consumer.maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("function test() {");
    consumer.endFunction();
    String expectedOutput = "function test() {\n}\n";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndFunctionWithStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("function test() { test; }");
    consumer.endFunction();
    String expectedOutput = "function test() {\n  test;\n}\n";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("case label:");
    consumer.beginCaseBody();
    String expectedOutput = "case label:\n{\n";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("case label:");
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    String expectedOutput = "case label:\n{\n  test\n}";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.add(" new code");
    String expectedOutput = "test new code";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("1 + 2");
    consumer.addOp("+", true);
    String expectedOutput = "1 + 2 +";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("3.14159265358979323846");
    consumer.addNumber(1.61803398875);
    String expectedOutput = "3.14159265358979323846 1.61803398875";
    String actualOutput = consumer.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.isNegativeZero(-0));
    assertFalse(consumer.isNegativeZero(0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('9'));
    assertFalse(consumer.isWordChar('?'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    node.setType(BLOCK);
    node.setFirstChild(new Node());
    node.setLastChild(new Node());
    assertTrue(consumer.shouldPreserveExtraBlocks());

    CodeConsumer singleStatementConsumer = new SingleStatementCodeConsumer();
    assertFalse(singleStatementConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    node.setType(IF);
    node.setFirstChild(new Node());
    node.setLastChild(new Node());
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}

abstract class DummyCodeConsumer extends CodeConsumer {
  @Override
  abstract String getCode();
}

class SingleStatementCodeConsumer extends DummyCodeConsumer {
  @Override
  String getCode() {
    return "";
  }

  @Override
  boolean shouldPreserveExtraBlocks() {
    return false;
  }
}

class ThrowingCodeConsumer extends DummyCodeConsumer {
  @Override
  boolean continueProcessing() {
    throw new RuntimeException("Continue processing failed");
  }
}