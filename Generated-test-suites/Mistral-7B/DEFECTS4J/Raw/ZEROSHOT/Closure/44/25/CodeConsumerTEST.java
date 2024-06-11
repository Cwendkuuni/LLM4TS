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
    assertEquals('\u0000', consumer.getLastChar());

    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer() {
      @Override
      void add(String newcode) {
        super.add(newcode);
        assertEquals(newcode, getLastCode());
      }
    };

    consumer.addIdentifier("identifier");
    assertEquals("identifier", getLastCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer() {
      @Override
      void add(String newcode) {
        super.add(newcode);
        assertEquals(newcode, getLastCode());
      }
    };

    consumer.append("test");
    assertEquals("test", getLastCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", getLastCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", getLastCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", getLastCode());
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
    assertEquals("\n", getLastCode());
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
    assertEquals("{\n", getLastCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", getLastCode());
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", getLastCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", getLastCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement();
    assertEquals(";\n", getLastCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement(true);
    assertEquals(";\n", getLastCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeEndStatement();
    // assert nothing
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", getLastCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction(true);
    assertEquals("\n}", getLastCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", getLastCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    // assert nothing
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer() {
      @Override
      void add(String newcode) {
        super.add(newcode);
        assertEquals(newcode, getLastCode());
      }
    };

    consumer.add("ab");
    assertEquals("ab", getLastCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new StubCodeConsumer() {
      @Override
      void add(String newcode) {
        super.add(newcode);
        assertEquals(newcode, getLastCode());
      }
    };

    consumer.appendOp("+", false);
    assertEquals("+", getLastCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer() {
      @Override
      void add(String newcode) {
        super.add(newcode);
        assertEquals(newcode, getLastCode());
      }
    };

    consumer.addOp("+", false);
    assertEquals("", getLastCode());

    consumer.append("a");
    consumer.addOp("+", false);
    assertEquals("a+", getLastCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer() {
      @Override
      void add(String newcode) {
        super.add(newcode);
        assertEquals(newcode, getLastCode());
      }
    };

    consumer.addNumber(123);
    assertEquals("123", getLastCode());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.isNegativeZero(0));
    assertFalse(consumer.isNegativeZero(-0));
    assertFalse(consumer.isNegativeZero(-1));
    assertFalse(consumer.isNegativeZero(Double.NEGATIVE_INFINITY));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('z'));
    assertTrue(consumer.isWordChar('0'));
    assertTrue(consumer.isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    // assert nothing
  }

  private String getLastCode() {
    return consumer.getLastCode();
  }

  private static class StubCodeConsumer extends CodeConsumer {
    private StringBuilder lastCode = new StringBuilder();

    @Override
    void add(String newcode) {
      lastCode.append(newcode);
    }

    @Override
    String getLastCode() {
      return lastCode.toString();
    }
  }
}