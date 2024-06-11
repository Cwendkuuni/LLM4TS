import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
    // Add assertions here if needed
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
    // Add assertions here if needed
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };
    assertTrue(consumer.continueProcessing());

    consumer = new CodeConsumer() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      private char lastChar = 0;

      @Override
      char getLastChar() {
        return lastChar;
      }

      @Override
      void add(String newcode) {
        lastChar = newcode.charAt(newcode.length() - 1);
      }
    };
    consumer.add("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }
    };
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void appendBlockStart() {
        sb.append("{");
      }
    };
    consumer.appendBlockStart();
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void appendBlockEnd() {
        sb.append("}");
      }
    };
    consumer.appendBlockEnd();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void startNewLine() {
        sb.append("\n");
      }
    };
    consumer.startNewLine();
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void maybeLineBreak() {
        sb.append("\n");
      }
    };
    consumer.maybeLineBreak();
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void maybeCutLine() {
        sb.append("\n");
      }
    };
    consumer.maybeCutLine();
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void endLine() {
        sb.append("\n");
      }
    };
    consumer.endLine();
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void notePreferredLineBreak() {
        sb.append("\n");
      }
    };
    consumer.notePreferredLineBreak();
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void beginBlock() {
        sb.append("{");
      }
    };
    consumer.beginBlock();
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void endBlock() {
        sb.append("}");
      }
    };
    consumer.endBlock();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testEndBlockWithEndLine() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void endBlock(boolean shouldEndLine) {
        if (shouldEndLine) {
          sb.append("\n");
        }
        sb.append("}");
      }
    };
    consumer.endBlock(true);
    assertEquals('\n', consumer.getLastChar());
    consumer.endBlock(false);
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void listSeparator() {
        sb.append(",");
      }
    };
    consumer.listSeparator();
    assertEquals(',', consumer.getLastChar());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();
      private boolean statementEnded = false;

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void endStatement() {
        endStatement(false);
      }

      @Override
      void endStatement(boolean needSemiColon) {
        if (needSemiColon) {
          sb.append(";");
          maybeLineBreak();
          statementEnded = true;
        } else if (statementStarted) {
          statementEnded = true;
        }
      }

      @Override
      void maybeEndStatement() {
        if (statementEnded) {
          sb.append(";");
          maybeLineBreak();
          statementEnded = false;
        } else {
          statementStarted = true;
        }
      }
    };
    consumer.append("test");
    consumer.maybeEndStatement();
    consumer.append(";");
    consumer.maybeLineBreak();
    assertEquals(';', consumer.getLastChar());
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();
      private boolean sawFunction = false;

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void endFunction() {
        endFunction(false);
      }

      @Override
      void endFunction(boolean statementContext) {
        if (statementContext) {
          sb.append(";");
          maybeLineBreak();
        }
        sawFunction = true;
      }

      @Override
      boolean shouldPreserveExtraBlocks() {
        return false;
      }
    };
    consumer.append("function test() {}");
    consumer.endFunction();
    assertTrue(sawFunction);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void beginCaseBody() {
        sb.append(":");
      }
    };
    consumer.beginCaseBody();
    assertEquals(':', consumer.getLastChar());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void endCaseBody() {
      }
    };
    consumer.endCaseBody();
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void add(String newcode) {
        sb.append(newcode);
      }
    };
    consumer.add("test");
    assertEquals("test", sb.toString());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void addOp(String op, boolean binOp) {
        sb.append(op);
      }
    };
    consumer.addOp("+", false);
    assertEquals("+", consumer.getLastChar() + "");
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void append(String str) {
        sb.append(str);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }

      @Override
      void addNumber(double x) {
        sb.append(String.valueOf(x));
      }
    };
    consumer.addNumber(123.456);
    assertEquals("123.456", sb.toString());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('1'));
    assertFalse(CodeConsumer.isWordChar('+'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return statementContext;
      }
    };
    assertTrue(consumer.breakAfterBlockFor(mock(Node.class), true));
    assertFalse(consumer.breakAfterBlockFor(mock(Node.class), false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endFile() {
      }
    };
    consumer.endFile();
  }
}