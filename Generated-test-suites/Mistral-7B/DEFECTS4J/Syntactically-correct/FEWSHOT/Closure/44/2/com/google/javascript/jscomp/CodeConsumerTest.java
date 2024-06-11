package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Test
  public void testStartSourceMapping() {
    consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot assert anything on the implementation of startSourceMapping
  }

  @Test
  public void testEndSourceMapping() {
    consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Cannot assert anything on the implementation of endSourceMapping
  }

  @Test
  public void testContinueProcessing() {
    consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());

    CodeConsumer consumer2 = new StopCodeConsumer();
    assertFalse(consumer2.continueProcessing());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLastChar() {
    consumer = new StubCodeConsumer();
    consumer.getLastChar();
  }

  @Test
  public void testAddIdentifier() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("addIdentifier", newcode);
      }
    };
    consumer.addIdentifier("identifier");
  }

  @Test
  public void testAppend() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("append", newcode);
      }
    };
    consumer.append("string");
  }

  @Test
  public void testAppendBlockStart() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("appendBlockStart", "{");
      }
    };
    consumer.appendBlockStart();
  }

  @Test
  public void testAppendBlockEnd() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("appendBlockEnd", "}");
      }
    };
    consumer.appendBlockEnd();
  }

  @Test
  public void testStartNewLine() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("startNewLine", "\n");
      }
    };
    consumer.startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("maybeLineBreak", "\n");
      }
    };
    consumer.maybeLineBreak();
  }

  @Test
  public void testMaybeCutLine() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("maybeCutLine", "");
      }
    };
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("endLine", "");
      }
    };
    consumer.endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("notePreferredLineBreak", "");
      }
    };
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("beginBlock", "{\n");
      }
    };
    consumer.beginBlock();
  }

  @Test
  public void testEndBlock() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("endBlock", "\n}");
      }
    };
    consumer.endBlock();
  }

  @Test
  public void testEndBlockWithLineBreak() {
    consumer = new StubCodeConsumer() {
      @Override
  public void add(String newcode) {
        testAddStrings("endBlockWithLineBreak", "\n}");
      }
    };
    consumer.endBlock(true);
  }

  @Test
  public void testListSeparator() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("listSeparator", ", ");
      }
    };
    consumer.listSeparator();
  }

  @Test
  public void testEndStatement() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("endStatement", ";");
      }
    };
    consumer.endStatement();
  }

  @Test
  public void testEndStatementWithLineBreak() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("endStatementWithLineBreak", ";");
      }
    };
    consumer.endStatement(true);
  }

  @Test
  public void testMaybeEndStatement() {
    consumer = new StubCodeConsumer() {
      @
  override public void add(String newcode) {
        testAddStrings("maybeEndStatement", "");
      }
    };
    consumer.maybeEndStatement();

    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("maybeEndStatementWithStatement", ";");
      }
    };
    consumer.maybeEndStatement();
    assertTrue(consumer.statementStarted);
  }

  @Test
  public void testEndFunction() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("endFunction", "}\n");
      }
    };
    consumer.endFunction();
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("endFunctionWithStatementContext", "\n}");
      }
    };
    consumer.endFunction(true);
  }

  @Test
  public void testBeginCaseBody() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("beginCaseBody", ": ");
      }
    };
    consumer.beginCaseBody();
  }

  @Test
  public void testEndCaseBody() {
    consumer = new StubCodeConsumer();
    consumer.endCaseBody();
  }

  @Test
  public void testAdd() {
    consumer = new StubCodeConsumer() {
      @Override
      public void add(String newcode) {
        testAddStrings("add", newcode);
      }
    };
    consumer.add("string");
  }

  @Test
  public void testAddOp() {
    consumer = new StubCodeConsumer() {
      @Override
      public void addOp(String op, boolean binOp) {
        testAddStrings("addOp", op);
      }
    };
    consumer.addOp("+", false);
    consumer.addOp("+", true);
  }

  @Test
  public void testAddNumber() {
    consumer = new StubCodeConsumer() {
      @Override
      public void addNumber(double x) {
        testAddStrings("addNumber", String.valueOf(x));
      }
    };
    consumer.addNumber(1.23);
    consumer.addNumber(-123);
  }

  @Test
  public void testIsNegativeZero() {
    consumer = new StubCodeConsumer();
    assertTrue(consumer.isNegativeZero(0.0));
    assertFalse(consumer.isNegativeZero(1.0));
  }

  @Test
  public void testIsWordChar() {
    consumer = new StubCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('0'));
    assertFalse(consumer.isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(new StubCodeConsumer().shouldPreserveExtraBlocks());
    assertTrue(new PreserveExtraBlocksCodeConsumer().shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    assertTrue(new StubCodeConsumer().breakAfterBlockFor(null, false));
    assertTrue(new StubCodeConsumer().breakAfterBlockFor(new Node(), false));
    assertTrue(new StubCodeConsumer().breakAfterBlockFor(new Node(), true));
    assertFalse(new PreserveExtraBlocksCodeConsumer().breakAfterBlockFor(null, false));
    assertFalse(new PreserveExtraBlocksCodeConsumer().breakAfterBlockFor(null, true));
    assertFalse(new PreserveExtraBlocksCodeConsumer().breakAfterBlockFor(new Node(), false));
    assertTrue(new PreserveExtraBlocksCodeConsumer().breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    consumer = new StubCodeConsumer();
    consumer.endFile();
  }

  private void testAddStrings(String methodName, String string) {
    consumer.maybeEndStatement();
    consumer.append(string);
  }

  static class StubCodeConsumer extends CodeConsumer {
    // Empty implementation of all methods
  }

  static class PreserveExtraBlocksCodeConsumer extends CodeConsumer {
    @Override
    public boolean shouldPreserveExtraBlocks() {
      return true;
    }
  }

  static class StopCodeConsumer extends CodeConsumer {
    @Override
    public boolean continueProcessing() {
      return false;
    }
  }
}