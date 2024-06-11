package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  abstract class TestCodeConsumer extends CodeConsumer {
    abstract char getLastChar();
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return ' ';
      }

      @Override
      void startSourceMapping(Node node) {
        // Test empty
        startSourceMapping(null);
        endSourceMapping(null);

        // Test non-null
        String nodeText = "nodeText";
        startSourceMapping(new Node() {
          @Override
          public String toString() {
            return nodeText;
          }
        });
        endSourceMapping(new Node() {
          @Override
          public String toString() {
            return nodeText;
          }
        });
      }

      @Override
      void endSourceMapping(Node node) {}
    };

    // Test empty
    consumer.startSourceMapping(null);
    consumer.endSourceMapping(null);

    // Test non-null
    String nodeText = "nodeText";
    consumer.startSourceMapping(new Node() {
      @Override
      public String toString() {
        return nodeText;
      }
    });
    consumer.endSourceMapping(new Node() {
      @Override
      public String toString() {
        return nodeText;
      }
    });
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean continueProcessing() {
        return false;
      }

      @Override
      char getLastChar() {
        return ' ';
      }
    };

    // Test continueProcessing returns false
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
      void append(String str) {}
    };

    consumer.append("abc");

    char lastChar = consumer.getLastChar();

    assertEquals('c', lastChar);
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return ' ';
      }

      @Override
      void append(String str) {
        add(str);
      }
    };

    consumer.append("abc");

    assertEquals("abc", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return ' ';
      }

      @Override
      void appendOp(String op, boolean binOp) {
        addOp(op, binOp);
      }
    };

   consumer.appendOp("+", false);
    consumer.appendOp("++", false);

    assertEquals(" +++", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return ' ';
      }

      @Override
      void addOp(String op, boolean binOp) {
        add(op);
      }
    };

    consumer.addOp("+", false);
    consumer.addOp("++", false);

    assertEquals(" +++", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return ' ';
      }

      @Override
      void addNumber(double x) {
        add(String.valueOf(x));
      }
    };

    consumer.addNumber(123.456);

    assertEquals("123.456", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('A'));
    assertTrue(CodeConsumer.isWordChar('B'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertTrue(CodeConsumer.isWordChar('9'));
    assertFalse(CodeConsumer.isWordChar('!'));
    assertFalse(CodeConsumer.isWordChar('%'));
    assertFalse(CodeConsumer.isWordChar('&'));
    assertFalse(CodeConsumer.isWordChar('*'));
    assertFalse(CodeConsumer.isWordChar('('));
    assertFalse(CodeConsumer.isWordChar(')'));
    assertFalse(CodeConsumer.isWordChar('+'));
    assertFalse(CodeConsumer.isWordChar('-'));
    assertFalse(CodeConsumer.isWordChar('.'));
    assertFalse(CodeConsumer.isWordChar('/'));
    assertFalse(CodeConsumer.isWordChar('>'));
    assertFalse(CodeConsumer.isWordChar('<'));
    assertFalse(CodeConsumer.isWordChar('='));
    assertFalse(CodeConsumer.isWordChar('['));
    assertFalse(CodeConsumer.isWordChar(']'));
    assertFalse(CodeConsumer.isWordChar('{'));
    assertFalse(CodeConsumer.isWordChar('}'));
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

      @Override
      char getLastChar() {
        return ' ';
      }
    };

    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endFile() {
        assertEquals("", consumer.getCode());
      }
    };

    consumer.endFile();

    assertEquals("", consumer.getCode());
  }
}