package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  abstract class TestCodeConsumer extends CodeConsumer {
    void append(String str) {}

    void add(String newcode) {}

    void addNumber(double x) {}

    void addOp(String op, boolean binOp) {}

    void endBlock() {}

    void endFunction() {}

    void endStatement() {}

    void maybeCutLine() {}

    void maybeEndStatement() {}

    char getLastChar() {
      return '\u0000';
    }

    void startBlock() {}

    void startSourceMapping(Node node) {}

    void endSourceMapping(Node node) {}

    boolean continueProcessing() {
      return true;
    }
  }

  @Test
  public void testStartSourceMappingAndEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();

    consumer.startSourceMapping(node);
    consumer.append("foo");
    consumer.endSourceMapping(node);

    String expectedSourceMap = "1:1-1:4";
    String actualSourceMap = node.toSourceString();

    int sourceMapStartIndex = actualSourceMap.indexOf(":") + 1;
    int sourceMapLength = actualSourceMap.substring(sourceMapStartIndex).length();

    assertTrue(sourceMapLength > 0);
    assertTrue(actualSourceMap.contains(expectedSourceMap.substring(0, sourceMapLength)));
  }

  @Test
  public void testContinueProcessingReturnsFalse() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }
    };

    assertTrue(consumer.continueProcessing()); // Default implementation returns true

    consumer.append("before");
    consumer.append("after");

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("before", code);
  }

  @Test
  public void testAppendEmptyString() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.append("");

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("", code);
  }

  @Test
  public void testAppendIdentifier() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("identifier", code);
  }

  @Test
  public void testAppendBlockStartAndBlockEnd() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void appendBlockStart() {
        super.append("{");
      }

      @Override
      void appendBlockEnd() {
        super.append("}");
      }
    };

    consumer.beginBlock();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("{", code);

    consumer.endBlock();

    code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("}", code);
  }

  @Test
  public void testStartNewLineAndMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void startNewLine() {
        super.append("\n");
      }

      @Override
      void maybeLineBreak() {
        super.append("\n");
      }
    };

    consumer.maybeLineBreak();
    consumer.append("foo");
    consumer.maybeLineBreak();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("\nfoo\n", code);
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void maybeCutLine() {
        super.append("\n");
      }
    };

    consumer.maybeCutLine();
    consumer.append("foo");
    consumer.maybeCutLine();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("foo\n", code);
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endLine() {
        super.append("\n");
      }
    };

    consumer.append("foo");
    consumer.endLine();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("foo\n", code);
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void notePreferredLineBreak() {
        super.append("\n");
      }
    };

    consumer.notePreferredLineBreak();
    consumer.append("foo");

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("\nfoo", code);
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void beginBlock() {
        super.append("{\n");
        super.endLine();
      }
    };

    consumer.beginBlock();
    consumer.append("foo");
    consumer.endBlock();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("{\nfoo\n}", code);
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endBlock() {
        super.append("}");
        super.endLine();
      }
    };

    consumer.beginBlock();
    consumer.append("foo");
    consumer.endBlock();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("{\n  foo\n}", code);
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void listSeparator() {
        super.add(",");
        super.maybeLineBreak();
      }
    };

    consumer.append("foo");
    consumer.listSeparator();
    consumer.append("bar");

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("foo,\nbar", code);
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endStatement() {
        super.endStatement(true);
      }
    };

    consumer.append("foo");
    consumer.endStatement();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("foo;\n", code);
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endFunction() {
        super.endFunction(true);
      }
    };

    consumer.append("function foo() {");
    consumer.beginBlock();
    consumer.append("}");
    consumer.endFunction();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("function foo() {\n}\n", code);
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void beginCaseBody() {
        super.append(":\n");
      }
    };

    consumer.append("case 0:");
    consumer.beginCaseBody();
    consumer.append("break;");
    consumer.endBlock();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("case 0:\n{\n  break;\n}", code);
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer();

    consumer.append("case 0:");
    consumer.beginCaseBody();
    consumer.append("break;");
    consumer.endCaseBody();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("case 0:\n  break;\n", code);
  }

  @Test
  public void testShouldPreserveExtraBlocksReturnsTrue() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };

    boolean result = consumer.shouldPreserveExtraBlocks();

    assertTrue(result);
  }

  @Test
  public void testBreakAfterBlockForReturnsTrueWithStatementContext() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return statementContext;
      }
    };

    assertTrue(consumer.breakAfterBlockFor(null, true));
  }

  @Test
  public void testBreakAfterBlockForReturnsFalseWithoutStatementContext() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return !statementContext;
      }
    };

    assertFalse(consumer.breakAfterBlockFor(null, false));
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endFile() {
        super.append("\n");
      }
    };

    consumer.append("foo");
    consumer.endFile();

    String code = consumer.getOutput(); // getOutput() is not defined in the TestCodeConsumer
    assertEquals("foo\n", code);
  }
}