package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Nodeops;
import org.junit.Test;

/**
 * Unit tests for {@link CodeConsumer}.
 */
public class CodeConsumerTest {

  private CodeConsumer consumer = new TestCodeConsumer();

  @Test
  public void testStartSourceMapping() {
    Node node = Nodeops.newIntLiteral(0);
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("hello");
    assertEquals('o', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    consumer.append("world");
    String expected = "world";
    StringBuilder sb = new StringBuilder();
    consumer.append(sb.toString());
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("foo");
    String expected = "foo";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.addOp("+", false);
    consumer.addOp("*", true);
    String expected = "+*\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.addNumber(123);
    String expected = "123\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("if (true) {\n  foobar;\n}");
    consumer.endStatement();
    String expected = "if (true) {\n  foobar;\n};\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function foo() {\n  return 1;\n}");
    consumer.endFunction();
    String expected = "function foo() {\n  return 1;\n}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    Node ifNode = Nodeops.newIfStatement(null, null, Nodeops.newBlockStatement(Nodeops.newStatement(Nodeops.newPrintStatement("foo"))));
    assertTrue(consumer.shouldPreserveExtraBlocks());
    consumer.beginBlock();
    consumer.add(ifNode);
    consumer.endBlock();
    String expected = "if (condition) {\n  print('foo');\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node forNode = Nodeops.newForStatement(null, null, null, Nodeops.newStatement(Nodeops.newPrintStatement("foo")));
    assertFalse(consumer.breakAfterBlockFor(forNode, false));
    consumer.beginBlock();
    consumer.add(forNode);
    consumer.endBlock();
    String expected = "for (initialization; condition; increment) {\n  print('foo');\n}";
    assertEquals(expected, consumer.getCode());

    // Test with statement context
    assertTrue(consumer.breakAfterBlockFor(forNode, true));
    consumer.beginBlock();
    consumer.appendBlockStart();
    consumer.add(forNode);
    consumer.appendBlockEnd();
    String expectedStatementContext = "{for (initialization; condition; increment) {\n  print('foo');\n}}";
    assertEquals(expectedStatementContext, consumer.getCode());
  }

  @Test
  public void testEndFile() {
    consumer.append("function main() {\n}");
    consumer.endFile();
    String expected = "function main() {\n}\n";
    assertEquals(expected, consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  String getCode() {
    StringBuilder sb = new StringBuilder();
    sb.append(super.getCode());
    while (statementNeedsEnded) {
      sb.append(";");
      maybeLineBreak();
    }
    return sb.toString();
  }
}