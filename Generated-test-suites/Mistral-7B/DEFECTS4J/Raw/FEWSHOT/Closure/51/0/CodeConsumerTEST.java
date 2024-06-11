package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CodeConsumer.isWordChar;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  abstract static class TestCodeConsumer extends CodeConsumer {
    abstract void testIsWordChar();

    @Override
    void startSourceMapping(Node node) {}

    @Override
    void endSourceMapping(Node node) {}

    @Override
    boolean continueProcessing() {
      return true;
    }

    @Override
    char getLastChar() {
      throw new UnsupportedOperationException();
    }

    @Override
    void addIdentifier(String identifier) {}

    @Override
    abstract void append(String str);

    @Override
    void appendBlockStart() {}

    @Override
    void appendBlockEnd() {}

    @Override
    void startNewLine() {}

    @Override
    void maybeLineBreak() {}

    @Override
    void endLine() {}

    @Override
    void notePreferredLineBreak() {}

    @Override
    void beginBlock() {}

    @Override
    void endBlock() {}

    @Override
    void listSeparator() {}

    @Override
    void endStatement() {}

    @Override
    void endStatement(boolean needSemiColon) {}

    @Override
    void maybeEndStatement() {}

    @Override
    void endFunction() {}

    @Override
    void endFunction(boolean statementContext) {}

    @Override
    void beginCaseBody() {}

    @Override
    void endCaseBody() {}

    @Override
    void add(String newcode) {}

    @Override
    void appendOp(String op, boolean binOp) {}

    @Override
    void addOp(String op, boolean binOp) {}

    @Override
    void addNumber(double x) {}
  }

  @Test
  public void testIsWordChar() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };

    c.testIsWordChar();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLastChar() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.getLastChar();
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    Node node = new Node();
    c.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    Node node = new Node();
    c.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    assertTrue(c.continueProcessing());
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.endFile();
  }

  @Test
  public void testAddIdentifier() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }
    };
    c.addIdentifier("identifier");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAppend() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.append("string");
  }

  @Test
  public void testAppendBlockStart() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.appendBlockStart();
  }

  @Test
  public void testAppendBlockEnd() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.appendBlockEnd();
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
  //      void append(String str) {}
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return statementContext;
      }
    };
    c.maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.beginBlock();
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.endBlock();
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals(",", str);
      }
    };
    c.listSeparator();
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.endStatement();
  }

  @Test
  public void testEndStatementWithSemiColon() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.endStatement(true);
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.maybeEndStatement();
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.endFunction();
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.beginCaseBody();
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.endCaseBody();
  }

  @Test
  public void testAdd() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {
        assertEquals("", str);
      }

      @Override
      void appendOp(String op, boolean binOp) {
        assertEquals("", op);
      }

      @Override
      void addOp(String op, boolean binOp) {}

      @Override
      void addNumber(double x) {
        assertEquals("", String.valueOf(x));
      }
    };
    c.add("string");
  }

  @Test
  public void testAppendOp() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}

      @Override
      void addOp(String op, boolean binOp) {
        assertEquals("", op);
      }
    };
    c.appendOp("+", false);
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}

      @Override
      void appendOp(String op, boolean binOp) {}

      @Override
      void addNumber(double x) {}
    };
    c.addOp("+", false);
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      void append(String str) {}
    };
    c.addNumber(0.1);
  }

  @Test
  public void testIsWordCharTrue() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('A'));
    assertTrue(isWordChar('1'));
    assertTrue(isWordChar('Z'));
  }

  @Test
  public void testIsWordCharFalse() {
    assertFalse(isWordChar(' '));
    assertFalse(isWordChar(','));
    assertFalse(isWordChar('('));
    assertFalse(isWordChar(')'));
    assertFalse(isWordChar('{'));
    assertFalse(isWordChar('}'));
  }

  @Test
  public void testShouldPreserveExtraBlocksFalse() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      boolean shouldPreserveExtraBlocks() {
        return false;
      }
    };
    assertFalse(c.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return false;
      }
    };
    assertFalse(c.breakAfterBlockFor(new Node(), false));
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    TestCodeConsumer c = new TestCodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return true;
      }
    };
    assertTrue(c.breakAfterBlockFor(new Node(), false));
  }
}