package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CodeConsumer.isWordChar;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  abstract static class TestCodeConsumer extends CodeConsumer {
    char lastChar;

    @Override
    char getLastChar() {
      return lastChar;
    }
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('A'));
    assertTrue(isWordChar('0'));
    assertTrue(isWordChar('9'));
    assertFalse(isWordChar('!'));
    assertFalse(isWordChar('('));
    assertFalse(isWordChar(')'));
    assertFalse(isWordChar('+'));
    assertFalse(isWordChar('-'));
    assertFalse(isWordChar('*'));
    assertFalse(isWordChar('/'));
  }

  @Test
  public void testStartSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        lastChar = 'a';
      }
    };
    consumer.startSourceMapping(new Node());
    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testEndSourceMapping() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endSourceMapping(Node node) {
        lastChar = 'b';
      }
    };
    consumer.startSourceMapping(new Node());
    consumer.endSourceMapping(new Node());
    assertEquals('b', consumer.getLastChar());
  }

  @Test
  public void testContinueProcessing() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean continueProcessing() {
        lastChar = 'c';
        return false;
      }
    };
    assertFalse(consumer.continueProcessing());
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testGetLastCharInitialState() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertEquals('\u0000', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void append(String str) {
        lastChar = str.charAt(str.length() - 1);
      }
    };
    consumer.append("foo");
    assertEquals('f', consumer.getLastChar());
  }

  @Test
  public void testAppendOp() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void appendOp(String op, boolean binOp) {
        lastChar = op.charAt(op.length() - 1);
      }
    };
    consumer.addOp("+", false);
    assertEquals('+', consumer.getLastChar());
  }

  @Test
  public void testAddNumber() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void addNumber(double x) {
        lastChar = Character.isDigit((char) x) ? ((char) x) : (char) x;
      }
    };
    consumer.addNumber(123);
    assertEquals('3', consumer.getLastChar());
    consumer.addNumber(-123);
    assertEquals('3', consumer.getLastChar()); // Should have added a space
  }

  @Test
  public void testAdd() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void add(String newcode) {
        lastChar = newcode.charAt(newcode.length() - 1);
      }
    };
    consumer.add("foo");
    assertEquals('o', consumer.getLastChar());
  }

  @Test
  public void testBeginBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void beginBlock() {
        lastChar = '{';
      }
    };
    consumer.beginBlock();
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testBeginCaseBody() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void beginCaseBody() {
        lastChar = ':';
      }
    };
    consumer.beginCaseBody();
    assertEquals(':', consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endBlock() {
        lastChar = '}';
      }
    };
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testEndFunction() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endFunction() {
        lastChar = '}';
      }
    };
    consumer.beginFunction();
    consumer.endFunction();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testMaybeEndStatement() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void maybeEndStatement() {
        lastChar = ';';
      }
    };
    consumer.append("foo");
    consumer.maybeEndStatement();
    assertEquals(';', consumer.getLastChar());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(new TestCodeConsumer() {}.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        lastChar = statementContext ? ';' : '{';
        return statementContext;
      }
    };
    consumer.startNewLine();
    consumer.append("if (true) {");
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
    assertEquals(';', consumer.getLastChar());
  }

  @Test
  public void testEndFile() {
    TestCodeConsumer consumer = new TestCodeConsumer() {
      @Override
      void endFile() {
        lastChar = 'E';
      }
    };
    consumer.endFile();
    assertEquals('E', consumer.getLastChar());
  }
}