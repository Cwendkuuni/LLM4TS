import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node(); // Dummy node
    consumer.startSourceMapping(node);
    consumer.append("test");
    consumer.endSourceMapping(node);

    String expectedSource = "test"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node1 = new Node(); // Dummy node 1
    Node node2 = new Node(); // Dummy node 2

    consumer.startSourceMapping(node1);
    consumer.append("test1");
    consumer.endSourceMapping(node1);

    consumer.startSourceMapping(node2);
    consumer.append("test2");
    consumer.endSourceMapping(node2);

    String expectedSource = "test1\ntest2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertFalse(consumer.continueProcessing());
  }

  @Test(expected = RuntimeException.class)
  public void testGetLastChar_EmptyCode() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("");

    consumer.getLastChar();
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.append("test2");

    String expectedSource = "test1test2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.appendBlockStart();
    consumer.append("test2");
    consumer.appendBlockEnd();

    String expectedSource = "test1{\ntest2\n}"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.appendBlockStart();
    consumer.appendBlockEnd();
    consumer.append("test2");

    String expectedSource = "test1{\n}\ntest2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.startNewLine();
    consumer.append("test2");

    String expectedSource = "test1\ntest2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.maybeLineBreak();
    consumer.append("test2");

    String expectedSource = "test1\ntest2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.append("test2");
    consumer.maybeCutLine();

    String expectedSource = "test1\n test2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.endLine();

    String expectedSource = "test1\n"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.notePreferredLineBreak();
    consumer.append("test2");

    String expectedSource = "test1\ntest2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.beginBlock();
    consumer.append("test2");
    consumer.endBlock();

    String expectedSource = "test1{\ntest2\n}"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.beginBlock();
    consumer.append("test2");
    consumer.endBlock();

    String expectedSource = "test1{\ntest2\n}"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testEndBlock_ShouldEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.beginBlock();
    consumer.append("test2");
    consumer.endBlock(true);

    String expectedSource = "test1{\ntest2\n}"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");

    String expectedSource = "test1,\ntest2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.endStatement();
    consumer.append("test2");

    String expectedSource = "test1;\ntest2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testEndStatement_NoSemiColonNeeded() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.endStatement(false);
    consumer.append("test2");

    String expectedSource = "test1test2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.maybeEndStatement();
    consumer.append("test2");
    consumer.endStatement();

    String expectedSource = "test1;\ntest2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.endFunction();
    consumer.append("test2");

    String expectedSource = "test1{\ntest2\n}"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testEndFunction_StatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.endFunction(true);
    consumer.append("test2");

    String expectedSource = "test1{\ntest2\n}\n"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("switch (expr) {\ncases:");
    consumer.beginCaseBody();
    consumer.append("test1");
    consumer.endCaseBody();
    consumer.append("}");

    String expectedSource = "switch (expr) {\ncases:\n{\n test1\n}\n}"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("switch (expr) {\ncases:\n{\n test1\n}");
    consumer.endCaseBody();

    String expectedSource = "switch (expr) {\ncases:\n{\n test1\n}\n}"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.add("test2");

    String expectedSource = "test1test2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.addIdentifier("identifier");

    String expectedSource = "test1identifier"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.addOp("+", false);
    consumer.append("test2");

    String expectedSource = "test1+test2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testAddOp_BinOp() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.addOp("*", true);
    consumer.append("test2");

    String expectedSource = "test1*test2"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.addNumber(123);

    String expectedSource = "test1123"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer_ShouldPreserveExtraBlocks();
    Node node = new Node(); // Dummy node

    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor_StatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = new Node(); // Dummy node

    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockFor_NotStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = new Node(); // Dummy node

    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("test1");
    consumer.endFile();

    String expectedSource = "test1"; // Expected source code
    String actualSource = consumer.getSource();

    assertEquals(expectedSource, actualSource);
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  String getSource() {
    return super.source;
  }
}

class TestCodeConsumer_ShouldPreserveExtraBlocks extends TestCodeConsumer {
  @Override
  boolean shouldPreserveExtraBlocks() {
    return true;
  }
}