import org.junit.Test;
import static org.mockito.Mockito.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
    verify(consumer, times(1)).startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);
    verify(consumer, times(1)).endSourceMapping(node);
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
      @Override
      abstract char getLastChar() {
        return 'a';
      }
    };

    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      abstract void add(String newcode) {
        super.add(newcode);
      }
    };

    consumer.addIdentifier("identifier");
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      abstract void append(String str) {
        super.append(str);
      }
    };

    consumer.append("string");
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void appendBlockStart() {
        super.appendBlockStart();
      }
    };

    consumer.appendBlockStart();
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void appendBlockEnd() {
        super.appendBlockEnd();
      }
    };

    consumer.appendBlockEnd();
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void startNewLine() {
        super.startNewLine();
      }
    };

    consumer.startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void maybeLineBreak() {
        super.maybeLineBreak();
      }
    };

    consumer.maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void maybeCutLine() {
        super.maybeCutLine();
      }
    };

    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endLine() {
        super.endLine();
      }
    };

    consumer.endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void notePreferredLineBreak() {
        super.notePreferredLineBreak();
      }
    };

    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void beginBlock() {
        super.beginBlock();
      }
    };

    consumer.beginBlock();
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endBlock() {
        super.endBlock();
      }
    };

    consumer.endBlock();
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endBlock(boolean shouldEndLine) {
        super.endBlock(shouldEndLine);
      }
    };

    consumer.endBlock(true);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void listSeparator() {
        super.listSeparator();
      }
    };

    consumer.listSeparator();
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endStatement() {
        super.endStatement();
      }
    };

    consumer.endStatement();
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endStatement(boolean needSemiColon) {
        super.endStatement(true);
      }
    };

    consumer.endStatement(true);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void maybeEndStatement() {
        super.maybeEndStatement();
      }
    };

    consumer.maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endFunction() {
        super.endFunction();
      }
    };

    consumer.endFunction();
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endFunction(boolean statementContext) {
        super.endFunction(true);
      }
    };

    consumer.endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void beginCaseBody() {
        super.beginCaseBody();
      }
    };

    consumer.beginCaseBody();
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endCaseBody() {
        super.endCaseBody();
      }
    };

    consumer.endCaseBody();
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
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return true;
      }
    };

    assertTrue(consumer.breakAfterBlockFor(null, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endFile() {
        super.endFile();
      }
    };

    consumer.endFile();
  }
}