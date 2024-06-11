import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new CodeConsumer() {
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
      private char lastChar = '\u0000';

      @Override
      public char getLastChar() {
        return lastChar;
      }

      @Override
      public void add(String newcode) {
        lastChar = newcode.charAt(newcode.length() - 1);
      }
    };

    consumer.add("test");
    assertEquals('\t', consumer.getLastChar());
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
      public char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }
    };

    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new CodeConsumer() {
      private int lineLength = 0;

      @Override
      public void startNewLine() {
        lineLength = 0;
      }
    };

    consumer.append("test");
    consumer.startNewLine();
    consumer.append("another");

    assertEquals(6, consumer.getLastCharIndexOf("\n"));
    assertEquals(1, consumer.getLastCharIndexOf(" "));
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.maybeLineBreak();
    verify(consumer, times(0)).maybeCutLine();
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.maybeCutLine();
    verify(consumer, times(0)).endLine();
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public void endLine() {
        lineLength = 0;
      }
    };

    consumer.append("test");
    consumer.endLine();

    assertEquals(0, consumer.getLastCharIndexOf("\n"));
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.notePreferredLineBreak();
    verify(consumer, times(0)).maybeCutLine();
    verify(consumer, times(0)).endLine();
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public void beginBlock() {
        assertEquals(0, getLineNumber());
        appendBlockStart();
        assertEquals(1, getLineNumber());
        endLine();
      }

      private int lineNumber = 0;

      @Override
      public int getLineNumber() {
        return lineNumber;
      }
    };

    consumer.beginBlock();
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      private boolean blockEnded = false;

      @Override
      public void endBlock() {
        blockEnded = true;
      }

      @Override
      public void endBlock(boolean shouldEndLine) {
        blockEnded = true;
      }

      @Override
      public void appendBlockEnd() {
        assertEquals(0, getLineNumber());
        appendBlockEnd();
        assertEquals(1, getLineNumber());
        endLine();
        assertEquals(false, blockEnded);
      }

      @Test
      public void testEndBlockWithoutEndingLine() {
        consumer.beginBlock();
        consumer.append("test");
        consumer.endBlock(false);
        consumer.append("another");

        assertEquals(1, getLineNumber());
        assertEquals(true, blockEnded);
      }

      @Test
      public void testEndBlockWithEndingLine() {
        consumer.beginBlock();
        consumer.append("test");
        consumer.endBlock(true);
        consumer.append("another");

        assertEquals(2, getLineNumber());
        assertEquals(true, blockEnded);
      }

      private int lineNumber = 0;

      @Override
      public int getLineNumber() {
        return lineNumber;
      }
    };

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    consumer.append("another");

    assertEquals(2, getLineNumber());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public void listSeparator() {
        assertEquals(0, getLineNumber());
        append(",");
        maybeLineBreak();
      }
    };

    consumer.append("test");
    consumer.listSeparator();
    consumer.append("another");

    assertEquals(1, getLineNumber());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public void endStatement() {
        assertEquals(0, getLineNumber());
        endStatement(true);
      }

      private int lineNumber = 0;

      @Override
      public int getLineNumber() {
        return lineNumber;
      }
    };

    consumer.append("test");
    consumer.endStatement();
    consumer.append("another");

    assertEquals(1, getLineNumber());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new CodeConsumer() {
      private boolean functionEnded = false;

      @Override
      public void endFunction() {
        functionEnded = true;
      }

      @Override
      public void endFunction(boolean statementContext) {
        functionEnded = true;
      }

      @Test
      public void testEndFunctionWithoutStatementContext() {
        consumer.append("function test() {");
        consumer.beginBlock();
        consumer.append("test();");
        consumer.endFunction();
        consumer.append("}");

        assertEquals(1, getLineNumber());
        assertEquals(true, functionEnded);
      }

      @Test
      public void testEndFunctionWithStatementContext() {
        consumer.append("function test() {");
        consumer.beginBlock();
        consumer.append("test();");
        consumer.endFunction(true);
        consumer.append("}");

        assertEquals(2, getLineNumber());
        assertEquals(true, functionEnded);
      }

      private int lineNumber = 0;
    };

    consumer.append("function test() {");
    consumer.beginBlock();
    consumer.append("test();");
    consumer.endFunction();
    consumer.append("}");

    assertEquals(1, getLineNumber());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public void beginCaseBody() {
        assertEquals(0, getLineNumber());
        append(":");
      }
    };

    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("case 0:");
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.append("}");

    assertEquals(1, getLineNumber());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new CodeConsumer() {
      private boolean caseBodyEnded = false;

      @Override
      public void endCaseBody() {
        caseBodyEnded = true;
      }

      @Test
      public void testEndCaseBodyWithoutBlock() {
        consumer.append("switch (x) {");
        consumer.beginBlock();
        consumer.append("case 0:");
        consumer.endCaseBody();
        consumer.append("break;");
        consumer.endBlock();

        assertEquals(1, getLineNumber());
        assertEquals(true, caseBodyEnded);
      }

      @Test
      public void testEndCaseBodyWithBlock() {
        consumer.append("switch (x) {");
        consumer.beginBlock();
        consumer.append("case 0:");
        consumer.beginBlock();
        consumer.append("test();");
        consumer.endBlock();
        consumer.endCaseBody();
        consumer.append("break;");
        consumer.endBlock();

        assertEquals(2, getLineNumber());
        assertEquals(true, caseBodyEnded);
      }

      private int lineNumber = 0;

      @Override
      public int getLineNumber() {
        return lineNumber;
      }
    };

    consumer.append("switch (x) {");
    consumer.beginBlock();
    consumer.append("case 0:");
    consumer.beginBlock();
    consumer.append("test();");
    consumer.endCaseBody();
    consumer.append("break;");
    consumer.endBlock();

    assertEquals(2, getLineNumber());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void add(String newcode) {
        sb.append(newcode);
      }

      @Override
      public char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }
    };

    consumer.append("test");
    consumer.add(" another");

    assertEquals(6, consumer.getLastCharIndexOf("\n"));
    assertEquals(1, consumer.getLastCharIndexOf(" "));
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void addOp(String op, boolean binOp) {
        sb.append(op);
      }

      @Override
      public char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }
    };

    consumer.append("x + y");

    assertEquals(2, consumer.getLastCharIndexOf("+"));
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      public void addNumber(double x) {
        sb.append(String.valueOf(x));
      }

      @Override
      public char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }
    };

    consumer.addNumber(-123.456);

    assertEquals(6, consumer.getLastCharIndexOf("\n"));
    assertEquals(4, consumer.getLastCharIndexOf("."));
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
}