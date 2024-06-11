package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  abstract class MyCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      throw new UnsupportedOperationException();
    }

    @Override
    void append(String str) {
      // Empty implementation
    }
  }

  @Test
  public void testContinueProcessing() {
    MyCodeConsumer consumer = new MyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testStartSourceMapping() {
    MyCodeConsumer consumer = new MyCodeConsumer() {
      @Override
      void startSourceMapping(Node node) {
        // Empty implementation
      }

      @Override
      void endSourceMapping(Node node) {
        // Empty implementation
      }
    };
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testStatementNeedsEndedAndStatementStarted() {
    MyCodeConsumer consumer = new MyCodeConsumer() {
      boolean statementNeedsEnded = false;
      boolean statementStarted = false;

      @Override
      void beginBlock() {
        statementStarted = true;
      }

      @Override
      void endBlock() {
        statementNeedsEnded = false;
        statementStarted = false;
      }

      @Override
      void maybeEndStatement() {
        statementNeedsEnded = true;
      }

      @Override
      void endStatement() {
        statementNeedsEnded = false;
      }

      @Override
      char getLastChar() {
        return '\u0000';
      }

      @Override
      void append(String str) {
        // Empty implementation
      }
    };

    consumer.maybeEndStatement();
    consumer.beginBlock();
    consumer.maybeEndStatement();
    consumer.endBlock();
    consumer.endStatement();

    assertFalse(consumer.statementNeedsEnded);
    assertFalse(consumer.statementStarted);

    consumer.maybeEndStatement();
    consumer.beginBlock();
    consumer.beginBlock();
    consumer.maybeEndStatement();
    consumer.endBlock();
    consumer.endBlock();
    consumer.endStatement();

    assertTrue(consumer.statementNeedsEnded);
    assertTrue(consumer.statementStarted);
  }

  @Test
  public void testSawFunction() {
    MyCodeConsumer consumer = new MyCodeConsumer() {
      boolean sawFunction = false;

      @Override
      void endFunction() {
        sawFunction = true;
      }

      @Override
      char getLastChar() {
        return '\u0000';
      }

      @Override
      void append(String str) {
        // Empty implementation
      }
    };

    consumer.append("function foo() {}", "function bar() {}", "function baz() {}", "foo();", "bar();", "baz();");

    assertTrue(consumer.sawFunction);
  }
}