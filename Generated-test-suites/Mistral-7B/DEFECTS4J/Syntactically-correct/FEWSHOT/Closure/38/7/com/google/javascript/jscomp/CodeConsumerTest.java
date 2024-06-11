package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  abstract class TestCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      throw new UnsupportedOperationException();
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        // no implementation needed for testing
      }

      @Override
      void endSourceMapping(Node node) {
        // no implementation needed for testing
      }
    };

    Node node = new Node();
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        // no implementation needed for testing
      }

      @Override
      void endSourceMapping(Node node) {
        // no implementation needed for testing
      }
    };

    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }
    };

    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      String lastCode = "";

      @Override
      void add(String newcode) {
        lastCode += newcode;
      }

      @Override
      char getLastChar() {
        return lastCode.charAt(lastCode.length() - 1);
      }
    };

    consumer.add("foo");
    consumer.add("bar");
    assertEquals('r', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.append("bar");
    assertEquals("foobar", consumer.toString());
  }

  @Test
  public void testAppendBlockStart() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void appendBlockStart() {
        sb.append("{");
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.appendBlockStart();
    assertEquals("foo{\n", consumer.toString());
  }

  @Test
  public void testAppendBlockEnd() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void appendBlockEnd() {
        sb.append("}");
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.appendBlockStart();
    consumer.append("bar");
    consumer.appendBlockEnd();
    assertEquals("foobar}\n", consumer.toString());
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void startNewLine() {
        sb.append("\n");
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.startNewLine();
    assertEquals("fooinput\n", consumer.toString());
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void maybeLineBreak() {
        sb.append("\n");
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.maybeLineBreak();
    assertEquals("foo\n", consumer.toString());
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void maybeCutLine() {
        sb.append("\n");
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.append("bar");
    consumer.maybeCutLine();
    assertEquals("foo\nbar", consumer.toString());
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void endLine() {
        sb.append("\n");
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.endLine();
    assertEquals("fooinput\n", consumer.toString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void notePreferredLineBreak() {
        // no implementation needed for testing
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.notePreferredLineBreak();
    assertEquals("fooinput", consumer.toString());
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void beginBlock() {
        sb.append("{\n");
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.beginBlock();
    assertEquals("foo{\n", consumer.toString());
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void endBlock() {
        sb.append("}");
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.beginBlock();
    consumer.append("bar");
    consumer.endBlock();
    assertEquals("foo{\nbar}\n", consumer.toString());
  }

  @Test
  public void testListSeparator() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void listSeparator() {
        sb.append(",");
        sb.append("\n");
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.listSeparator();
    assertEquals("foo,\n", consumer.toString());
  }

  @Test
  public void testEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void endStatement() {
        sb.append(";");
        sb.append("\n");
      }

      @Override
      void endStatement(boolean needSemiColon) {
        // no implementation needed for testing
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.endStatement();
    assertEquals("foo;\n", consumer.toString());
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void endStatement() {
        // no implementation needed for testing
      }

      @Override
      void maybeEndStatement() {
        sb.append(";");
        sb.append("\n");
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.maybeEndStatement();
    assertEquals("foo;\n", consumer.toString());
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void endFunction() {
        sb.append("}");
        sb.append("\n");
      }

      @Override
      void endFunction(boolean statementContext) {
        // no implementation needed for testing
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.beginBlock();
    consumer.append("bar");
    consumer.endFunction();
    assertEquals("foo{\nbar}\n", consumer.toString());
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void beginCaseBody() {
        sb.append(":\n");
      }

      @Override
      void endCaseBody() {
        // no implementation needed for testing
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.beginCaseBody();
    assertEquals("foo:\n", consumer.toString());
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void endCaseBody() {
        // no implementation needed for testing
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.beginCaseBody();
    consumer.append("bar");
    assertEquals("foo:\nbar", consumer.toString());
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void add(String newcode) {
        sb.append(newcode);
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.add("bar");
    assertEquals("foobar", consumer.toString());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void addOp(String op, boolean binOp) {
        sb.append(op);
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.addOp("+");
    assertEquals("foo+", consumer.toString());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void append(String str) {
        sb.append(str);
      }

      @Override
      abstract char getLastChar();

      @Override
      void addNumber(double x) {
        String numberString = x > 0 ? String.valueOf(x) : "-" + String.valueOf(-x);
        sb.append(numberString);
      }

      @Override
      String toString() {
        return sb.toString();
      }
    };

    consumer.append("foo");
    consumer.addNumber(123);
    assertEquals("foonum123", consumer.toString());
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
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return true;
      }
    };

    assertTrue(consumer.breakAfterBlockFor(null, false));
  }
}