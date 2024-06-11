package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  abstract class TestCodeConsumer extends CodeConsumer {
    abstract char getLastChar();

    void append(String str) {
      // Implement this in subclasses
    }

    void add(String newcode) {
      append(newcode);
    }

    void addOp(String op, boolean binOp) {
      append("op"); // Replace "op" with the actual operator
    }

    void addNumber(double x) {
      append("x"); // Replace "x" with the actual number
    }

    boolean isWordChar(char ch) {
      return true; // Replace with actual implementation
    }

    boolean shouldPreserveExtraBlocks() {
      return true; // Replace with actual implementation
    }

    boolean breakAfterBlockFor(Node n, boolean statementContext) {
      return true; // Replace with actual implementation
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }
    };

    Node node = new Node();
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
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
      char getLastChar() {
        return 'a';
      }

      @Override
      boolean continueProcessing() {
        return false;
      }
    };

    assertTrue(consumer.continueProcessing());
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }
    };

    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
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

    consumer.addIdentifier("identifier");
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
        assertEquals("a" + str, consumer.getLastCharString());
      }

      String getLastCharString() {
        String result = new String(new char[] { getLastChar() });
        return result;
      }
    };

    consumer.append("test");
  }

  @Test
  public void testAppendOp() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      void appendOp(String op, boolean binOp) {
        assertEquals("aop", consumer.getLastCharString());
      }

      @Override
      void addOp(String op, boolean binOp) {
        consumer.appendOp(op, binOp);
      }
    };

    consumer.addOp("+", false);
  }

  @Test
  public void testAddNumber() {
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
      void addNumber(double x) {
        assertEquals("ax", consumer.getLastCharString());
        assertEquals(String.valueOf(x), consumer.getLastCode());
      }

      String getLastCode() {
        return new String(new char[] { getLastChar() });
      }
    };

    consumer.addNumber(1.23);
  }

  @Test
  public void testIsWordChar() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }

      @Override
      boolean isWordChar(char ch) {
        return false;
      }
    };

    assertFalse(consumer.isWordChar('_'));
    assertFalse(consumer.isWordChar('$'));
    assertFalse(consumer.isWordChar('x'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('9'));
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

    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
    assertTrue(consumer.breakAfterBlockFor(new Node(), false));
  }
}