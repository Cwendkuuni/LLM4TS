import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if startSourceMapping method does not throw any exception
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Check if endSourceMapping method does not throw any exception
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    // Test continueProcessing method with default implementation
    assertTrue(consumer.continueProcessing());

    // Test derive class and override continueProcessing method to return false
    class TestDerivedConsumer extends CodeConsumer {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    }
    TestDerivedConsumer testDerivedConsumer = new TestDerivedConsumer();
    assertFalse(testDerivedConsumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    char lastChar = consumer.getLastChar();
    assertEquals('c', lastChar);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    String expectedCode = "identifier";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("stringToAppend");
    String expectedCode = "stringToAppend";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String expectedCode = "{\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("codeBlock");
    consumer.appendBlockEnd();
    String expectedCode = "{\ncodeBlock\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    String expectedCode = "\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    // Test empty code case
    consumer.maybeLineBreak();
    String expectedCode = "";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);

    // Test code with a single character
    consumer.append("a");
    consumer.maybeLineBreak();
    expectedCode = "\na";
    actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);

    // Test code with multiple characters
    consumer.append("abcd");
    consumer.maybeLineBreak();
    expectedCode = "\nabcd";
    actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    // Test empty code case
    consumer.maybeCutLine();
    String expectedCode = "";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);

    // Test code with a single character
    consumer.append("a");
    consumer.maybeCutLine();
    expectedCode = "a";
    actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);

    // Test code with multiple characters
    consumer.append("abcd");
    consumer.maybeCutLine();
    expectedCode = "abcd";
    actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    String expectedCode = "\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    String expectedCode = "";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String expectedCode = "{\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("codeBlock");
    consumer.endBlock();
    String expectedCode = "{\ncodeBlock\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    String expectedCode = ", ";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("statement");
    consumer.endStatement();
    String expectedCode = "statement;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("statement");
    consumer.endStatement(true);
    String expectedCode = "statement;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("statement1");
    consumer.append("statement2");
    consumer.maybeEndStatement();
    String expectedCode = "statement1;\nstatement2";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);

    consumer.append("statement1");
    consumer.append("statement2");
    consumer.maybeEndStatement(true);
    expectedCode = "statement1;\nstatement2;\n";
    actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("functionName(");
    consumer.append("arguments");
    consumer.append(") {\ncodeBlock\n}");
    consumer.endFunction();
    String expectedCode = "function functionName(" +
        "arguments) {\ncodeBlock\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("functionName(");
    consumer.append("arguments");
    consumer.append(") {\ncodeBlock}");
    consumer.endFunction(true);
    String expectedCode = "function functionName(" +
        "arguments) {\ncodeBlock\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("case label:");
    consumer.beginCaseBody();
    consumer.append("codeBlock");
    consumer.endCaseBody();
    String expectedCode = "case label:\n{\ncodeBlock\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("case label:");
    consumer.beginCaseBody();
    consumer.append("codeBlock");
    String expectedCode = "case label:\n{\ncodeBlock\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);

    consumer.endCaseBody();
    String expectedCodeAfterEndCaseBody = "case label:\n{\ncodeBlock\n}";
    String actualCodeAfterEndCaseBody = consumer.getCode();
    assertEquals(expectedCodeAfterEndCaseBody, actualCodeAfterEndCaseBody);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("string1");
    consumer.add("string2");
    String expectedCode = "string1 string2";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("x");
    consumer.addOp("+", false);
    consumer.addNumber(1);
    String expectedCode = "x + 1";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);

    consumer.append("x");
    consumer.addOp("+", true);
    consumer.addNumber(1);
    expectedCode = "x +\n1";
    actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("x");
    consumer.addNumber(1);
    String expectedCode = "x + 1";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    // Test default implementation
    assertFalse(consumer.shouldPreserveExtraBlocks());

    // Test derived class overriding shouldPreserveExtraBlocks method to return true
    class TestDerivedConsumer extends CodeConsumer {
      @Override
      public boolean shouldPreserveExtraBlocks() {
        return true;
      }
    }
    TestDerivedConsumer testDerivedConsumer = new TestDerivedConsumer();
    assertTrue(testDerivedConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    // Test empty code case
    assertFalse(consumer.breakAfterBlockFor(node, false));

    // Test code with a single statement
    consumer.beginBlock();
    consumer.append("statement");
    boolean result = consumer.breakAfterBlockFor(node, false);
    assertFalse(result);

    // Test code with multiple statements
    consumer.beginBlock();
    consumer.append("statement1");
    consumer.append("statement2");
    result = consumer.breakAfterBlockFor(node, false);
    assertFalse(result);

    // Test code with a single statement and statement context
    consumer.beginBlock();
    consumer.append("statement");
    consumer.endStatement();
    result = consumer.breakAfterBlockFor(node, true);
    assertTrue(result);

    // Test code with multiple statements and statement context
    consumer.beginBlock();
    consumer.append("statement1");
    consumer.append("statement2");
    consumer.endStatement();
    result = consumer.breakAfterBlockFor(node, true);
    assertTrue(result);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    String expectedCode = "";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  private static class TestCodeConsumer extends CodeConsumer {
    @Override
    public char getLastChar() {
      return '\0';
    }

    @Override
    public String getCode() {
      return "";
    }
  }
}