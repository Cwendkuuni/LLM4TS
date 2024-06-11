package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CodeConsumerTest {

  abstract class TestConsumer extends CodeConsumer {
    abstract char getLastChar();

    @Override
    void append(String str) {
      // Implement in subclass
    }

    @Override
    char getLastChar() {
      throw new UnsupportedOperationException();
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }
    };

    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }
    };

    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      boolean continueProcessing() {
        return false;
      }
    };

    assertTrue(consumer.continueProcessing());
    consumer.statementNeedsEnded = true;
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }
    };

    consumer.append("foo");
    assertEquals('f', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void add(String identifier) {
        // Implementation not needed for this test
      }
    };

    consumer.addIdentifier("foo");
  }

  @Test
  public void testAppend() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        super.append(str);
      }

      @Override
      char getLastChar() {
        return super.getLastChar();
      }
    };

    consumer.append("foo");
    assertEquals('f', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockStart() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void appendBlockStart() {
        super.append("{");
      }
    };

    consumer.appendBlockStart();
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void appendBlockEnd() {
        super.append("}");
      }
    };

    consumer.appendBlockEnd();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void startNewLine() {
        super.endLine();
      }
    };

    consumer.startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void maybeLineBreak() {
        super.maybeCutLine();
      }
    };

    consumer.maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void maybeCutLine() {
        super.maybeCutLine();
      }
    };

    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void endLine() {
        super.append("\n");
      }
    };

    consumer.endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void notePreferredLineBreak() {
        super.maybeCutLine();
      }
    };

    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void appendBlockStart() {
        super.append("{");
      }

      @Override
      void beginBlock() {
        super.appendBlockStart();
        super.endLine();
      }
    };

    consumer.beginBlock();
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void appendBlockEnd() {
        super.append("}");
      }

      @Override
      void endBlock() {
        super.appendBlockEnd();
      }

      @Override
      void endBlock(boolean shouldEndLine) {
        super.appendBlockEnd();
        if (shouldEndLine) {
          super.endLine();
        }
      }
    };

    consumer.endBlock();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void listSeparator() {
        super.add(",");
        super.maybeLineBreak();
      }
    };

    consumer.listSeparator();
  }

  @Test
  public void testEndStatement() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void endStatement() {
        super.endStatement(false);
      }
    };

    consumer.endStatement();
  }

  @Test
  public void testEndStatementWithSemiColon() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void endStatement(boolean needSemiColon) {
        super.endStatement(needSemiColon);
      }
    };

    consumer.endStatement(true);
  }

  @Test
  public void testMaybeEndStatement() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void maybeEndStatement() {
        super.maybeEndStatement();
      }
    };

    consumer.maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void endFunction() {
        super.endFunction(false);
      }
    };

    consumer.endFunction();
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void endFunction(boolean statementContext) {
        super.endFunction(statementContext);
      }
    };

    consumer.endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void beginCaseBody() {
        super.append(":");
      }
    };

    consumer.beginCaseBody();
    assertEquals(':', consumer.getLastChar());
  }

  @Test
  public void testEndCaseBody() {
    TestConsumer consumer = new TestConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implementation not needed for this test
      }

      @Override
      void endCaseBody() {
        // Empty implementation
      }
    };

    consumer.endCaseBody();
  }
}