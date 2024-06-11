import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  private static class TestConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      return '\u0000';
    }

    @Override
    void append(String str) {
      System.out.print(str);
    }

    @Override
    void add(String newcode) {
      append(newcode);
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestConsumer consumer = new TestConsumer();

    Node node = new Node();
    consumer.startSourceMapping(node);
    // verify no output
  }

  @Test
  public void testEndSourceMapping() {
    TestConsumer consumer = new TestConsumer();

    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.addIdentifier("testFunction");
    consumer.endSourceMapping(node);

    String expected = "testFunction//"; // source mapping comment
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @Test
  public void testContinueProcessing() {
    TestConsumer consumer = new TestConsumer();

    consumer.setContinueProcessing(false);

    consumer.addIdentifier("testIdentifier");

    assertTrue(consumer.continueProcessing());

    consumer.addIdentifier("testIdentifier2");

    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestConsumer consumer = new TestConsumer();

    consumer.addIdentifier("testIdentifier");

    char expected = 't';
    char actual = consumer.getLastChar();

    assertEquals(expected, actual);
  }

  @Test
  public void testAppend() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testString");

    String expected = "testString";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockStart() {
    TestConsumer consumer = new TestConsumer();

    consumer.appendBlockStart();

    String expected = "{";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockEnd() {
    TestConsumer consumer = new TestConsumer();

    consumer.appendBlockStart();
    consumer.append("testBlock");
    consumer.appendBlockEnd();

    String expected = "{testBlock}";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testStartNewLine() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testString1");
    consumer.startNewLine();
    consumer.append("testString2");

    String expected = "testString1\ntestString2";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeLineBreak() {
    TestConsumer consumer = new TestConsumer();

    consumer.addIdentifier("testIdentifier1");
    consumer.maybeLineBreak();
    consumer.addIdentifier("testIdentifier2");

    String expected = "testIdentifier1 \n testIdentifier2";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeCutLine() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testString1");
    consumer.maybeCutLine();
    consumer.addIdentifier("testIdentifier");
    consumer.append("testString2");

    String expected = "testString1\ntestIdentifiertestString2";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndLine() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testString1");
    consumer.endLine();
    consumer.append("testString2");

    String expected = "testString1\n testString2";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testString1");
    consumer.notePreferredLineBreak();
    consumer.append("testString2");

    String expected = "testString1\n testString2";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testBeginBlock() {
    TestConsumer consumer = new TestConsumer();

    consumer.beginBlock();
    consumer.append("testBlockContent");
    consumer.endBlock();

    String expected = "{testBlockContent}";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlock() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testString1");
    consumer.beginBlock();
    consumer.append("testBlockContent");
    consumer.endBlock();
    consumer.append("testString2");

    String expected = "testString1\n{\ntestBlockContent\n}\n testString2";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testListSeparator() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testString1");
    consumer.listSeparator();
    consumer.append("testString2");

    String expected = "testString1,\n testString2";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatement() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testStatement1");
    consumer.endStatement();
    consumer.append("testStatement2");

    String expected = "testStatement1;\n testStatement2";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testStatement1");
    consumer.endStatement(true);
    consumer.append("testStatement2");

    String expected = "testStatement1;\n testStatement2";
    String actual = consumer.getCode();

    assertEquals(expecte, actual);
  }

  @Test
  public void testMaybeEndStatement() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testStatement1");
    consumer.maybeEndStatement();
    consumer.append("testStatement2");

    String expected = "testStatement1;\n testStatement2";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunction() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("function testFunction() {");
    consumer.endFunction();
    consumer.append("}");

    String expected = "function testFunction() {\n}\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testBeginCaseBody() {
    TestConsumer consumer = new TestConsumer();

    consumer.beginCaseBody();
    consumer.append("testCaseBody");
    consumer.endCaseBody();

    String expected = ": \ntestCaseBody\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAdd() {
    TestConsumer consumer = new TestConsumer();

    consumer.add("testString");

    String expected = "testString";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddIdentifier() {
    TestConsumer consumer = new TestConsumer();

    consumer.addIdentifier("testIdentifier");

    String expected = "testIdentifier";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddOp() {
    TestConsumer consumer = new TestConsumer();

    consumer.addOp("+", false);

    String expected = "+";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddNumber() {
    TestConsumer consumer = new TestConsumer();

    consumer.addNumber(42);

    String expected = "42";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestConsumer consumer = new TestConsumer();

    Node node = new Node();
    node.setType(Node.FOR_STMT);
    node.setStart(0);
    node.setEnd(10);
    node.setCond(new Node());
    node.setBody(new Node());

    consumer.add(node.toString());
    consumer.add("for (int i = 0; i < 10; i++) { i++; }");

    String expected = "for (int i = 0; i < 10; i++) {\ni++;\n}";
    String actual = consumer.getCode();

    assertEquals(expected, actual);

    consumer.setShouldPreserveExtraBlocks(true);

    expected = "for (int i = 0; i < 10; i++) {\n i++;\n}";
    actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestConsumer consumer = new TestConsumer();

    Node node = new Node();
    node.setType(Node.IF_STMT);
    node.setStart(0);
    node.setEnd(10);
    node.setCond(new Node());
    node.setBody(new Node());

    consumer.setBreakAfterBlockFor(node, true);

    consumer.add(node.toString());
    consumer.add("if (condition) { statement; }");

    String expected = "if (condition) {\n statement\n}";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndFile() {
    TestConsumer consumer = new TestConsumer();

    consumer.append("testString1");
    consumer.endFile();

    String expected = "testString1";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }
}