package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CodeConsumerTest {
  abstract class TestCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      throw new UnsupportedOperationException();
    }

    @Override
    void append(String str) {
      // no-op
    }

    @Override
    void add(String newcode) {
      // no-op
    }

    @Override
    void addNumber(double x) {
      // no-op
    }

    @Override
    void addOp(String op, boolean binOp) {
      // no-op
    }

    @Override
    void startSourceMapping(Node node) {
      // no-op
    }

    @Override
    void endSourceMapping(Node node) {
      // no-op
    }

    @Override
    boolean continueProcessing() {
      return true;
    }

    @Override
    void beginBlock() {
      // no-op
    }

    @Override
    void endBlock(boolean shouldEndLine) {
      // no-op
    }

    @Override
    void listSeparator() {
      // no-op
    }

    @Override
    void maybeLineBreak() {
      // no-op
    }

    @Override
    void maybeCutLine() {
      // no-op
    }

    @Override
    void endLine() {
      // no-op
    }

    @Override
    void notePreferredLineBreak() {
      // no-op
    }

    @Override
    void beginCaseBody() {
      // no-op
    }

    @Override
    void endCaseBody() {
      // no-op
    }

    @Override
    void endFunction() {
      // no-op
    }

    @Override
    void endFunction(boolean statementContext) {
      // no-op
    }

    @Override
    void beginCaseBody() {
      // no-op
    }

    @Override
    void shouldPreserveExtraBlocks() {
      return false;
    }

    @Override
    boolean breakAfterBlockFor(Node n, boolean statementContext) {
      return statementContext;
    }

    @Override
    void endFile() {
      // no-op
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node_();
    consumer.startSourceMapping(node);
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node_();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    throw new TestCaseNotFoundException();
  }

  @Test
  public void testAppend() {
    throw new TestCaseNotFoundException();
  }

  @Test
  public void testAdd() {
    throw new TestCaseNotFoundException();
  }

  @Test
  public void testAddNumber() {
    throw new TestCaseNotFoundException();
  }

  @Test
  public void testAddOp() {
    throw new TestCaseNotFoundException();
  }

  @Test
  public void testStartSourceMappingWithNullNode() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertNull(consumer.statementNeedsEnded);
    consumer.startSourceMapping(null);
    assertNull(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndSourceMappingWithNullNode() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.startSourceMapping(new Node_());
    consumer.endSourceMapping(null);
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testContinueProcessingWhenFalse() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }
    };
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testAppendBlockStart() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.appendBlockStart();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.appendBlockEnd();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.startNewLine();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.maybeLineBreak();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.maybeCutLine();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.endLine();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.notePreferredLineBreak();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void appendBlockStart() {
        assertEquals("", consumer.getLastChar());
      }
    };
    consumer.beginBlock();
    assertEquals("{", consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void appendBlockEnd() {
        assertEquals("}", consumer.getLastChar());
      }
    };
    consumer.beginBlock();
    consumer.append(";");
    consumer.endBlock();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testEndBlockWithStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void appendBlockEnd() {
        assertEquals("}", consumer.getLastChar());
      }
    };
    consumer.beginBlock();
    consumer.append(";");
    consumer.endBlock(true);
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.listSeparator();
    assertEquals(",", consumer.getLastChar());
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.append("foo");
    consumer.endStatement();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndStatementNoSemiColon() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.append("foo");
    consumer.endStatement(false);
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.append("foo");
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void appendBlockEnd() {
        assertEquals("}", consumer.getLastChar());
      }
    };
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.endFunction();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testEndFunctionWithStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void appendBlockEnd() {
        assertEquals("}", consumer.getLastChar());
      }
    };
    consumer.beginBlock();
    consumer.append("function foo() { foo(); }");
    consumer.endFunction();
    assertEquals("}", consumer.getLastChar());
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.beginCaseBody();
    assertEquals(":", consumer.getLastChar());
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.beginCaseBody();
    consumer.append(";");
    consumer.endCaseBody();
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.add("foo");
    assertEquals("foo", consumer.getLastCode());
  }

  @Test
  public void testAppendOp() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.appendOp("+", false);
    assertEquals("+", consumer.getLastCode());
  }

  @Test
  public void testAddOpBinary() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.appendOp("+", true);
    assertEquals(" + ", consumer.getLastCode());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    consumer.addNumber(1);
    assertEquals("1", consumer.getLastCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return true;
      }
    };
    assertTrue(consumer.breakAfterBlockFor(new Node_(), true));
  }

  @Test
  public void testBreakAfterBlockForIfStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return false;
      }
    };
    assertFalse(consumer.breakAfterBlockFor(new Node_(), false));
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endFile() {
        assertEquals("", consumer.getLastCode());
      }
    };
    consumer.endFile();
    assertEquals("", consumer.getLastCode());
  }
}