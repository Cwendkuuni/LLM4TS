import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new MyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertNull(consumer.getLastChar());
    consumer.add("test");
    assertEquals('t', consumer.getLastChar());
    consumer.endSourceMapping(node);
    assertEquals(-1, consumer.getLastChar());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new MyCodeConsumer();
    assertFalse(consumer.continueProcessing());
    consumer.append("test");
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("test");
    consumer.startNewLine();
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("test");
    consumer.maybeCutLine();
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{\ntest\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    assertEquals("test1,\ntest2\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endFunction();
    assertEquals("{\ntest\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("test:");
    consumer.beginCaseBody();
    consumer.append("test2");
    consumer.endCaseBody();
    assertEquals("test:\n{\ntest2\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("test");
    consumer.add(" test2");
    assertEquals("test test2\n", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.addIdentifier("test");
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.shouldPreserveExtraBlocks();
    assertEquals(false, consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new MyCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  private String getCode() {
    return consumer.getCode();
  }

  private class MyCodeConsumer extends CodeConsumer {
    @Override
    public char getLastChar() {
      return super.getLastChar();
    }

    @Override
    public String getCode() {
      return super.getCode();
    }
  }
}