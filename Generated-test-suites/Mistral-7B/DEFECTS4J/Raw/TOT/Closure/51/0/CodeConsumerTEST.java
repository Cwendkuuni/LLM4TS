import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node("sourceNode");
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);

    // Assert that startSourceMapping and endSourceMapping have no effect on the code
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testContinueProcessing() {
    consumer.setContinueProcessing(false);
    assertTrue(consumer.continueProcessing());

    // Test that setting continueProcessing to false actually stops processing
    consumer.append("test");
    consumer.continueProcessing();
    assertEquals("test", consumer.getCode());

    consumer.setContinueProcessing(false);
    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockStart();
    consumer.append("test");
    consumer.appendBlockEnd();
    assertEquals("{test}", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.append("test");
    consumer.startNewLine();
    consumer.append("anotherTest");
    assertEquals("test\nanotherTest", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.append("test");
    consumer.maybeLineBreak();
    consumer.append("anotherTest");
    assertEquals("test\nanotherTest", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.append("test");
    consumer.maybeCutLine();
    consumer.append("anotherTest");
    assertEquals("test\nanotherTest", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    consumer.append("test");
    consumer.endLine();
    consumer.append("anotherTest");
    assertEquals("test\nanotherTest", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.append("test");
    consumer.notePreferredLineBreak();
    consumer.append("anotherTest");
    assertEquals("test\nanotherTest", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{test}", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{test}", consumer.getCode());

    consumer.beginBlock();
    consumer.append("test");
    consumer.appendBlockEnd();
    consumer.append("anotherTest");
    consumer.endBlock();
    assertEquals("{test}\nanotherTest", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer.append("test");
    consumer.listSeparator();
    consumer.append("anotherTest");
    assertEquals("test,anotherTest", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer.append("test");
    consumer.endStatement(true);
    assertEquals("test;", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("test");
    consumer.maybeEndStatement();
    consumer.append("anotherTest");
    assertEquals("test\nanotherTest", consumer.getCode());

    consumer.append("test;");
    consumer.maybeEndStatement();
    consumer.append("anotherTest");
    assertEquals("test;\nanotherTest", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.append("function test() {");
    consumer.append("test();");
    consumer.endFunction();
    assertEquals("function test() {\n  test();\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.append("function test() {");
    consumer.append("test();");
    consumer.endFunction(true);
    assertEquals("function test() {\n  test();\n}", consumer.getCode());
    consumer.append("}");
    consumer.endFunction();
    assertEquals("function test() {\n  test();\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("test();");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case:\n    test();\n}", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("test();");
    consumer.endCaseBody();
    consumer.append("break;");
    assertEquals("switch (x) {\n  case:\n    test();\n}", consumer.getCode());

    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("test();");
    consumer.endBlock();
    consumer.append("break;");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case:\n    { test(); }\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.append("test");
    consumer.add(" anotherTest");
    assertEquals("test anotherTest", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    consumer.append("test");
    consumer.addIdentifier(" anotherTest");
    assertEquals("test anotherTest", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.append("test");
    consumer.addOp(" + ", true);
    consumer.add(3);
    consumer.addOp(" + ", true);
    consumer.add(4);
    assertEquals("test + 3 + 4", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.append("test");
    consumer.addNumber(3.14);
    assertEquals("test 3.14", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('z'));
    assertTrue(CodeConsumer.isWordChar('1'));
    assertFalse(CodeConsumer.isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    consumer.append("for (int i = 0; i < 10; i++) {");
    consumer.append(" i++;");
    boolean preserveExtraBlocks = consumer.shouldPreserveExtraBlocks();
    consumer.append("}");
    assertTrue(preserveExtraBlocks);
    assertEquals("for (int i = 0; i < 10; i++) {\n  i++;\n}", consumer.getCode());

    consumer.append("if (x > 0) {");
    consumer.append(" x++;");
    preserveExtraBlocks = consumer.shouldPreserveExtraBlocks();
    consumer.append("}");
    assertFalse(preserveExtraBlocks);
    assertEquals("if (x > 0) { x++; }", consumer.getCode());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = new Node("testNode");
    assertTrue(consumer.breakAfterBlockFor(node, false));
    consumer.append("test");
    consumer.appendBlockStart();
    consumer.append("anotherTest");
    consumer.appendBlockEnd();
    assertEquals("test{\nanotherTest}", consumer.getCode());

    assertTrue(consumer.breakAfterBlockFor(node, true));
    consumer.append("test");
    consumer.appendBlockStart();
    consumer.append("anotherTest");
    consumer.appendBlockEnd();
    consumer.append("}");
    assertEquals("test{\nanotherTest}\n", consumer.getCode());
  }

  @Test
  public void testEndFile() {
    consumer.append("test");
    consumer.endFile();
    assertEquals("test", consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return super.getLastChar();
  }

  @Override
  public void append(String str) {
    super.add(str);
  }

  @Override
  public void appendBlockStart() {
    super.beginBlock();
  }

  @Override
  public void appendBlockEnd() {
    super.endBlock();
  }

  @Override
  public void startNewLine() {
    super.startLine();
  }

  @Override
  public void maybeLineBreak() {
    super.maybeCutLine();
  }

  @Override
  public void maybeCutLine() {
    // Empty implementation
  }

  @Override
  public void endLine() {
    super.endLine();
  }

  @Override
  public void notePreferredLineBreak() {
    // Empty implementation
  }

  @Override
  public void beginBlock() {
    super.beginBlock();
    append("{");
  }

  @Override
  public void endBlock() {
    super.endBlock();
    append("}");
  }

  @Override
  public void listSeparator() {
    super.listSeparator();
    append(",");
  }

  @Override
  public void addIdentifier(String identifier) {
    super.addIdentifier(identifier);
  }

  @Override
  public void addOp(String op, boolean binOp) {
    super.addOp(op, binOp);
  }

  @Override
  public void addNumber(double x) {
    super.addNumber(x);
  }

  @Override
  public void endStatement() {
    super.endStatement();
    append(";");
  }

  @Override
  public void endFunction() {
    super.endFunction();
    append("}");
  }

  @Override
  public boolean shouldPreserveExtraBlocks() {
    return true;
  }

  @Override
  public boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return true;
  }
}