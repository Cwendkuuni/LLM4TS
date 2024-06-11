import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
    // Check if no exception is thrown
    verify(consumer, never()).throwException(anyString());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);
    // Check if no exception is thrown
    verify(consumer, never()).throwException(anyString());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }
    };

    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      private char lastChar = '\0';

      @Override
      char getLastChar() {
        return lastChar;
      }

      @Override
      void add(String identifier) {
        lastChar = identifier.charAt(identifier.length() - 1);
      }
    };

    consumer.add("identifier");
    assertEquals('d', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    String string = "string";

    consumer.append(string);
    verify(consumer).add(string);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAppendEmptyString() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.append("");
    fail("An IllegalArgumentException is expected");
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.appendBlockStart();
    verify(consumer).append("{");
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.appendBlockEnd();
    verify(consumer).append("}");
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.startNewLine();
    verify(consumer).endLine();
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.maybeLineBreak();
    // Check if no exception is thrown
    verify(consumer, never()).throwException(anyString());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.maybeCutLine();
    // Check if no exception is thrown
    verify(consumer, never()).throwException(anyString());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.endLine();
    // Check if no exception is thrown
    verify(consumer, never()).throwException(anyString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.notePreferredLineBreak();
    // Check if no exception is thrown
    verify(consumer, never()).throwException(anyString());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.beginBlock();
    verify(consumer).append("{");
    verify(consumer).endLine();
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.endBlock();
    consumer.endBlock(true);

    verify(consumer).append("}");
    verify(consumer).endLine();
    verify(consumer, times(2)).statementNeedsEnded = false;
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.listSeparator();
    verify(consumer).add(",");
    verify(consumer).maybeLineBreak();
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.endStatement();
    consumer.endStatement(true);

    verify(consumer, times(2)).statementNeedsEnded = false;
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.maybeEndStatement();

    verify(consumer).maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.endFunction();
    consumer.endFunction(true);

    verify(consumer).sawFunction = true;
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.beginCaseBody();
    verify(consumer).append(":");
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.endCaseBody();
    // Check if no exception is thrown
    verify(consumer, never()).throwException(anyString());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder stringBuilder = new StringBuilder();

      @Override
      void append(String str) {
        stringBuilder.append(str);
      }

      @Override
      void add(String newcode) {
        super.add(newcode);
      }

      @Override
      char getLastChar() {
        return stringBuilder.charAt(stringBuilder.length() - 1);
      }
    };

    consumer.add("identifier");
    consumer.add("string");

    assertEquals("identifierstring", stringBuilder.toString());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder stringBuilder = new StringBuilder();

      @Override
      void append(String op) {
        stringBuilder.append(op);
      }

      @Override
      void addOp(String op, boolean binOp) {
        super.addOp(op, binOp);
      }

      @Override
      char getLastChar() {
        return stringBuilder.charAt(stringBuilder.length() - 1);
      }
    };

    consumer.addOp("+", true);
    consumer.addOp("+");

    assertEquals("++", stringBuilder.toString());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder stringBuilder = new StringBuilder();

      @Override
      void append(String str) {
        stringBuilder.append(str);
      }

      @Override
      void addNumber(double x) {
        super.addNumber(x);
      }

      @Override
      char getLastChar() {
        return stringBuilder.charAt(stringBuilder.length() - 1);
      }
    };

    consumer.addNumber(1.2);
    consumer.addNumber(3.4);

    assertEquals("1.23.4", stringBuilder.toString());
  }

  @Test
  public void testIsNegativeZero() {
    assertTrue(CodeConsumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('z'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertTrue(CodeConsumer.isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };

    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    Node node = mock(Node.class);
    boolean statementContext = true;

    assertTrue(consumer.breakAfterBlockFor(node, statementContext));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.endFile();
    // Check if no exception is thrown
    verify(consumer, never()).throwException(anyString());
  }
}