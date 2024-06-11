import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @Before
  public void setup() {
    consumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    assertFalse(consumer.continueProcessing());
    consumer = new ContinueProcessingCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("abcd");
    assertEquals('d', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    String identifier = "identifier";
    consumer.addIdentifier(identifier);
    assertEquals(identifier, consumer.getCode());
  }

  @Test
  public void testAppend() {
    consumer.append("Hello, World!");
    assertEquals("Hello, World!", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.beginBlock();
    consumer.append("{");
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    consumer.beginBlock();
    consumer.append("{");
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer.append("a, b, c");
    assertEquals("a, b, c\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("if (x > 5) {");
    consumer.endStatement();
    assertEquals("if (x > 5) {\n", consumer.getCode());
    consumer.append("}");
    consumer.endStatement(true);
    assertEquals("if (x > 5) {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.appendBlockStart();
    consumer.append("return 42;");
    consumer.appendBlockEnd();
    consumer.endFunction();
    assertEquals("function foo() {\n  return 42;\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("case 5:");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case 5:\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.append("x = 1 + ");
    consumer.add("2");
    consumer.append(";");
    assertEquals("x = 1 + 2;\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.append("x = 1 + ");
    consumer.addOp("2", true);
    consumer.append(";");
    assertEquals("x = 1 + 2;\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.append("x = ");
    consumer.addNumber(3.14);
    consumer.append(";");
    assertEquals("x = 3.14;\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
    consumer = new NoExtraBlocksCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Node.BLOCK);
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  private String getCode() {
    return consumer.getCode();
  }
}

abstract class TestCodeConsumer extends CodeConsumer {
  @Override
  protected char getLastChar() {
    return '\u0000';
  }

  @Override
  public String getCode() {
    return new String(code);
  }
}

class ContinueProcessingCodeConsumer extends TestCodeConsumer {
  @Override
  public boolean continueProcessing() {
    return false;
  }
}

class NoExtraBlocksCodeConsumer extends TestCodeConsumer {
  @Override
  public boolean shouldPreserveExtraBlocks() {
    return false;
  }
}