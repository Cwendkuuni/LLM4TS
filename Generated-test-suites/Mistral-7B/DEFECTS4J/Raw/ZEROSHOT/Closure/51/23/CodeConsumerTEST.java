import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // assert nothing
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
    // assert nothing
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    // assert default char value
    assertEquals('\u0000', consumer.getLastChar());

    consumer.add("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer() {
      @Override
      public void append(String str) {
        super.append(str);
        append(str);
      }
    };
    consumer.append("test");

    String expectedCode = "testtest";
    StringBuilder actualCode = new StringBuilder();
    consumer.getCode().append(actualCode);
    assertEquals(expectedCode, actualCode.toString());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    // assert nothing
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    // assert nothing
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    // assert nothing
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{", consumer.getLastChar());
    consumer.endLine();
    assertEquals("\n{", consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testEndBlockWithEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock(true);
    assertEquals("}", consumer.getLastChar());
    assertEquals("\n}", consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",", consumer.getLastChar());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement();
    assertEquals(";", consumer.getLastChar());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement(true);
    assertEquals(";", consumer.getLastChar());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("int x = 5;");
    consumer.maybeEndStatement();
    assertEquals(";", consumer.getLastChar());
    consumer.endLine();
    assertEquals("\n", consumer.getLastChar());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("void func() {");
    consumer.endFunction();
    assertEquals("}", consumer.getLastChar());
    consumer.endLine();
    assertEquals("\n}", consumer.getLastChar());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("void func() {");
    consumer.append("int x = 5;");
    consumer.endFunction(true);
    assertEquals("}", consumer.getLastChar());
    assertEquals("\n}", consumer.getLastChar());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":", consumer.getLastChar());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    consumer.endCaseBody();
    // assert nothing
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getLastCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test ");
    consumer.add("string");
    assertEquals("test strings", consumer.getLastCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("1 + 2");
    assertEquals("1 + 2", consumer.getLastCode());

    consumer = new StubCodeConsumer();
    consumer.append("1 + 2 ");
    consumer.addOp("+", true);
    assertEquals("1 + 2", consumer.getLastCode());

    consumer = new StubCodeConsumer();
    consumer.append("1 ");
    consumer.addOp("+", true);
    consumer.append("2");
    assertEquals("1 + 2", consumer.getLastCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(1.2);
    assertEquals("1.2", consumer.getLastCode());

    consumer = new StubCodeConsumer();
    consumer.append("1.2 ");
    consumer.addNumber(2.3);
    assertEquals("1.2 2.3", consumer.getLastCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('b'));
    assertFalse(isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer() {
      @Override
      public boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    // assert nothing
  }

  private static class StubCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    public void append(String str) {
      code.append(str);
    }

    @Override
    public String getCode() {
      return code.toString();
    }

    @Override
    public void add(String newcode) {
      super.add(newcode);
      code.append(newcode);
    }
  }
}