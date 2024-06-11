package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodePrinter;

public class CodeConsumerTest {

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeEndStatement();
    assertTrue(consumer.statementNeedsEnded);

    consumer.endStatement();
    assertFalse(consumer.statementNeedsEnded);

    consumer.endStatement(true);
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertFalse(consumer.statementNeedsEnded);

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());

    CodePrinter printer = new StubCodePrinter() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };
    consumer = new StubCodeConsumer(printer);
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  static class StubCodeConsumer extends CodeConsumer {
    StubCodeConsumer() {
      super();
    }

    StubCodeConsumer(CodePrinter printer) {
      super();
      setPrinter(printer);
    }

    @Override
    char getLastChar() {
      return 'a';
    }
  }

  static class StubCodePrinter implements CodePrinter {
    @Override
    public void print(String code) {
      // DO NOTHING
    }

    @Override
    public void indent() {
      // DO NOTHING
    }

    @Override
    public void dedent() {
      // DO NOTHING
    }

    @Override
    public void println(String code) {
      // DO NOTHING
    }
  }
}