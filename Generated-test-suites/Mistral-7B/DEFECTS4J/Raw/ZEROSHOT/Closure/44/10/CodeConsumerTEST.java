import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.Node;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer codeConsumer;

  @Before
  public void setUp() {
    codeConsumer = mock(CodeConsumer.class);
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.startSourceMapping(node);
    verify(node).setSourceMapping(codeConsumer);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.endSourceMapping(node);
    verify(node).clearSourceMapping();
  }

  @Test
  public void testContinueProcessing() {
    codeConsumer.continueProcessing();
  }

  @Test
  public void testGetLastChar() {
    codeConsumer.append("foo");
    assertEquals('f', codeConsumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    codeConsumer.addIdentifier("identifier");
    String expected = "identifier";
    assertEquals(expected, codeConsumer.getLastCode());
  }

  @Test
  public void testAppend() {
    codeConsumer.append("string");
    String expected = "string";
    assertEquals(expected, codeConsumer.getLastCode());
  }

  @Test
  public void testAppendBlockStart() {
    codeConsumer.appendBlockStart();
    assertEquals("{", codeConsumer.getLastCode());
  }

  @Test
  public void testAppendBlockEnd() {
    codeConsumer.appendBlockEnd();
    assertEquals("}", codeConsumer.getLastCode());
  }

  @Test
  public void testStartNewLine() {
    codeConsumer.startNewLine();
    assertEquals("\n", codeConsumer.getLastCode());
  }

  @Test
  public void testMaybeLineBreak() {
    codeConsumer.maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    codeConsumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    codeConsumer.endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    codeConsumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    codeConsumer.beginBlock();
    assertEquals("{\n", codeConsumer.getLastCode());
  }

  @Test
  public void testEndBlock() {
    codeConsumer.endBlock();
    assertEquals("}\n", codeConsumer.getLastCode());
  }

  @Test
  public void testListSeparator() {
    codeConsumer.listSeparator();
    assertEquals(",\n", codeConsumer.getLastCode());
  }

  @Test
  public void testEndStatement() {
    codeConsumer.endStatement();
  }

  @Test
  public void testEndStatementWithSemiColon() {
    codeConsumer.endStatement(true);
    assertEquals(";\n", codeConsumer.getLastCode());
  }

  @Test
  public void testMaybeEndStatement() {
    codeConsumer.maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    codeConsumer.endFunction();
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    codeConsumer.endFunction(true);
    assertEquals("}\n", codeConsumer.getLastCode());
  }

  @Test
  public void testBeginCaseBody() {
    codeConsumer.beginCaseBody();
    assertEquals(":\n", codeConsumer.getLastCode());
  }

  @Test
  public void testEndCaseBody() {
  }

  @Test
  public void testAdd() {
    codeConsumer.add("code");
    String expected = "code";
    assertEquals(expected, codeConsumer.getLastCode());
  }

  @Test
  public void testAppendOp() {
    codeConsumer.appendOp("+", false);
    assertEquals("+", codeConsumer.getLastCode());
  }

  @Test
  public void testAddOpWithBinaryOperator() {
    codeConsumer.addOp("+=", true);
    assertEquals("+=", codeConsumer.getLastCode());
  }

  @Test
  public void testAddNumber() {
    codeConsumer.addNumber(123.456);
    String expected = "123.456";
    assertEquals(expected, codeConsumer.getLastCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, CodeConsumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertEquals(true, CodeConsumer.isWordChar('_'));
    assertEquals(true, CodeConsumer.isWordChar('a'));
    assertEquals(true, CodeConsumer.isWordChar('1'));
    assertEquals(false, CodeConsumer.isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertEquals(false, codeConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    assertEquals(false, codeConsumer.breakAfterBlockFor(mock(Node.class), false));
  }

  @Test
  public void testEndFile() {
    codeConsumer.endFile();
  }
}