package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  abstract class TestCodeConsumer extends CodeConsumer {
    @Override
    abstract char getLastChar();

    @Override
    void append(String str) {
      // Implement this in subclasses
    }

    @Override
    void add(String newcode) {
      super.add(newcode);
    }

    @Override
    void addNumber(double x) {
      super.addNumber(x);
    }
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implement this in subclasses
      }
    };
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implement this in subclasses
      }

      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return statementContext;
      }
    };
    assertFalse(consumer.breakAfterBlockFor(null, false));
  }

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        // Implement this in subclasses
      }

      @Override
      void append(String str) {
        // Implement this in subclasses
      }
    };
    consumer.startSourceMapping(null);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endSourceMapping(Node node) {
        // Implement this in subclasses
      }

      @Override
      void append(String str) {
        // Implement this in subclasses
      }
    };
    consumer.endSourceMapping(null);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }

      @Override
      void append(String str) {
        // Implement this in subclasses
      }
    };
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        // Implement this in subclasses
      }
    };
    char lastChar = consumer.getLastChar();
    assertEquals('a', lastChar);
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void append(String str) {
        super.add(str);
      }
    };
    consumer.append("hello");
    assertEquals("hello", consumer.getCode());
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }
    };
    consumer.add("hello");
    assertEquals("hello", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }
    };
    consumer.addNumber(1.2);
    assertEquals("1.2", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void appendBlockStart() {
        super.append("{");
      }
    };
    consumer.beginBlock();
    assertEquals("{", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @override
      char getLastChar() {
        return 'a';
      }

      @Override
      void appendBlockEnd() {
        super.append("}");
      }

      @Override
      void endLine() {
        super.append("\n");
      }
    };
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("{\n}", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @override
      char getLastChar() {
        return 'a';
      }

      @Override
      void startNewLine() {
        super.append("\n");
      }
    };
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @override
      char getLastChar() {
        return 'a';
      }

      @Override
      void maybeLineBreak() {
        super.maybeCutLine();
        super.endLine();
      }
    };
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @override
      char getLastChar() {
        return 'a';
      }

      @Override
      void maybeCutLine() {
        super.append("\n");
      }
    };
    consumer.maybeCutLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @override
      char getLastChar() {
        return 'a';
      }

      @Override
      void endLine() {
        super.append("\n");
      }
    };
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @override
      char getLastChar() {
        return 'a';
      }

      @Override
      void notePreferredLineBreak() {
        super.endLine();
      }
    };
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @override
      char getLastChar() {
        return 'a';
      }

      @Override
      void beginCaseBody() {
        super.append(":");
      }
    };
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @override
      char getLastChar() {
        return 'a';
      }
    };
    consumer.endCaseBody();
    assertEquals("", consumer.getCode());
  }
}