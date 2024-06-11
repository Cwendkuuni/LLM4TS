import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if startSourceMapping method was called
    assertTrue(consumer.getLastChar() == '{' || consumer.getLastChar() == '(');
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Check if endSourceMapping method was called and source mapping was reset
    assertEquals(Character.MIN_VALUE, consumer.getLastChar());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    assertEquals('l', consumer.getLastChar());
  }

  @Test(expected = AssertionError.class)
  public void testGetLastCharAfterEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    consumer.getLastChar();
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    assertEquals("hello", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    consumer.append("world");
    assertEquals("\nworld", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    consumer.append("world");
    consumer.append("hello");
    assertEquals("world\nhello", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    consumer.append("world");
    assertEquals("world\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    consumer.append("world");
    assertEquals("world\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("world");
    consumer.endBlock();
    assertEquals("{\nworld}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("world");
    consumer.endBlock(true);
    assertEquals("{\nworld\n}", consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testEndBlockWithoutStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock();
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    consumer.append("world");
    assertEquals("world,\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("world");
    consumer.endStatement();
    consumer.append("hello");
    assertEquals("world;\nhello", consumer.getCode());
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("world");
    consumer.endStatement(true);
    consumer.append("hello");
    assertEquals("world;\nhello", consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testEndStatementWithoutStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement();
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("world");
    consumer.maybeEndStatement();
    consumer.append("hello");
    assertEquals("world;\nhello", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("\treturn 1;");
    consumer.endFunction();
    assertEquals("function test() {\n\treturn 1;\n}\n", consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testEndFunctionWithoutStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.endFunction();
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("case 1:");
    consumer.beginCaseBody();
    consumer.append("return 1;");
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endBlock();
    assertEquals("case 1:\n{\n\treturn 1;\n}\nbreak;\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("case 1:");
    consumer.beginCaseBody();
    consumer.append("return 1;");
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endBlock();
    assertEquals("case 1:\n{\n\treturn 1;\n}\nbreak;\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("world");
    consumer.add("hello");
    assertEquals("worldhello", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("world");
    consumer.addOp("+", true);
    consumer.append("hello");
    assertEquals("world+hello", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("world");
    consumer.addNumber(1.5);
    assertEquals("world1.5", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(new TestCodeConsumer().shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndStatementWithEmptyStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("world");
    consumer.endStatement();
    consumer.append("hello");
    assertEquals("world;\nhello", consumer.getCode());
  }

  public static class TestCodeConsumer extends CodeConsumer {
    private String code = "";

    @Override
    void add(String newcode) {
      code += newcode;
    }

    @Override
    void append(String str) {
      code += str;
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    String getCode() {
      return code;
    }

    @Override
    void endFile() {
      super.endFile();
    }

  }

}