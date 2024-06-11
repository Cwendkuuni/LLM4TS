package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  abstract class TestCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      throw new UnsupportedOperationException();
    }

    @Override
    void append(String str) {
      // implementation in subclass
    }

    @Override
    void add(String newcode) {
      append(newcode);
    }

    @Override
    void addOp(String op, boolean binOp) {
      appendOp(op, binOp);
    }

    @Override
    void addNumber(double x) {
      appendNumber(x);
    }

    @Override
    boolean continueProcessing() {
      return true;
    }

    @Override
    void startSourceMapping(Node node) {
      // implementation in subclass
    }

    @Override
    void endSourceMapping(Node node) {
      // implementation in subclass
    }

    @Override
    boolean shouldPreserveExtraBlocks() {
      return false;
    }

    @Override
    boolean breakAfterBlockFor(Node node, boolean statementContext) {
      return false;
    }

    @Override
    void endFile() {
      // implementation in subclass
    }

    void appendOp(String op, boolean binOp) {
      append(op);
    }

    void appendNumber(double x) {
      append(String.valueOf(x));
    }
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        assertEquals(node, getLastNode());
      }

      Node lastNode;

      @Override
      void add(String newcode) {
        lastNode = new Node_test(newcode);
      }
    };

    Node node = new Node_expression("1 + 2");
    consumer.startSourceMapping(node);
    consumer.add("{");
    consumer.add("1 + 2");
    consumer.add("}");

    assertTrue(consumer.continueProcessing());
    consumer.endSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      boolean mappingActive = false;

      @Override
      void startSourceMapping(Node node) {
        mappingActive = true;
      }

      @Override
      void endSourceMapping(Node node) {
        mappingActive = false;
      }

      @Override
      void append(String str) {
        // implementation in superclass
      }

      @Test
      public void testMappingActive() {
        consumer.startSourceMapping(new Node_statement("{});"));
        consumer.add("1 + 2");
        consumer.endSourceMapping(new Node_statement("{});"));

        assertFalse(mappingActive);
      }
    };

    consumer.startSourceMapping(new Node_statement("{});"));
    consumer.add("1 + 2");
    consumer.endSourceMapping(new Node_statement("{});"));

    assertFalse(consumer.mappingActive);
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      boolean processing = false;

      @Override
      boolean continueProcessing() {
        processing = true;
        return true;
      }

      @Test
      public void testProcessing() {
        consumer.add("1 + 2");

        assertTrue(consumer.processing);
        consumer.processing = false;
        consumer.add("3 + 4");

        assertTrue(consumer.processing);
      }
    };

    consumer.add("1 + 2");
    consumer.add("3 + 4");
  }

  @Test
  public void testGetLastChar() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      char lastChar;

      @Override
      char getLastChar() {
        return lastChar;
      }

      @Override
      void add(String newcode) {
        lastChar = newcode.charAt(newcode.length() - 1);
      }
    };

    consumer.add("abc");

    assertEquals('c', consumer.getLastChar());
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
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }
    };

    consumer.append("abc");

    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void add(String newcode) {
        sb.append(newcode);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }
    };

    consumer.add("abc");

    assertEquals("abc", consumer.toString());
  }

  @Test
  public void testAddOp() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void addOp(String op, boolean binOp) {
        sb.append(op);
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }
    };

    consumer.addOp("+", true);

    assertEquals("+", consumer.toString());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      StringBuilder sb = new StringBuilder();

      @Override
      void addNumber(double x) {
        sb.append(String.valueOf(x));
      }

      @Override
      char getLastChar() {
        return sb.charAt(sb.length() - 1);
      }
    };

    consumer.addNumber(1.23);

    assertEquals("1.23", consumer.toString());
  }

  @Test
  public void testStatementNeedsEnded() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      boolean statementNeedsEnded = false;

      @Override
      void endStatement() {
        statementNeedsEnded = true;
      }

      @Override
      void endStatement(boolean needSemiColon) {
        // implementation in superclass
      }

      @Test
      public void testStatementNeedsEnded() {
        consumer.beginBlock();
        consumer.addIdentifier("foo");
        consumer.add("=");
        consumer.addNumber(1.23);
        consumer.endBlock();

        assertFalse(consumer.statementNeedsEnded);
      }

      @Test
      public void testStatementNeedsEndedWithStatement() {
        consumer.beginBlock();
        consumer.addIdentifier("foo");
        consumer.add("=");
        consumer.addNumber(1.23);
        consumer.endStatement();

        assertTrue(consumer.statementNeedsEnded);
      }
    };

    consumer.beginBlock();
    consumer.addIdentifier("foo");
    consumer.add("=");
    consumer.addNumber(1.23);
    consumer.endBlock();

    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testSawFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      boolean sawFunction = false;

      @Override
      void endFunction() {
        sawFunction = true;
      }

      @Test
      public void testSawFunction() {
        consumer.beginBlock();
        consumer.addIdentifier("foo");
        consumer.add("(");
        consumer.addIdentifier("bar");
        consumer.add(")");
        consumer.endFunction();

        assertTrue(consumer.sawFunction);
      }
    };

    consumer.beginBlock();
    consumer.addIdentifier("foo");
    consumer.add("(");
    consumer.addIdentifier("bar");
    consumer.add(")");
    consumer.endFunction();

    assertTrue(consumer.sawFunction);
  }
}