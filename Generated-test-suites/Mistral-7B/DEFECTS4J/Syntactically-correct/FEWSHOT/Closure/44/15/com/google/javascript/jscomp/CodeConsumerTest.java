package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer consumer;
  private Node node;

  @Test
  public void testStartSourceMapping() {
    consumer = mock(CodeConsumer.class);
    node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    consumer = mock(CodeConsumer.class);
    node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessing_false() {
    consumer = new StubCodeConsumer() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer = new StubCodeConsumer();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.toString());
  }

  @Test
  public void testAppend() {
    consumer = new StubCodeConsumer();
    consumer.append("abc");
    assertEquals("abc", consumer.toString());
  }

  @Test
  public void testAppendBlockStart() {
    consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.toString());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.toString());
  }

  @Test
  public void testStartNewLine() {
    consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testMaybeCutLine() {
    consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    consumer = new StubCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.toString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.toString());
  }

  @Test
  public void testEndBlock() {
    consumer = new StubCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.toString());
  }

  @Test
  public void testEndBlock_shouldEndLine() {
    consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.toString());
  }

  @Test
  public void testListSeparator() {
    consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.toString());
  }

  @Test
  public void testEndStatement() {
    consumer = new StubCodeConsumer();
    consumer.endStatement();
    assertEquals(";\n", consumer.toString());
  }

  @Test
  public void testEndStatement_needSemiColon() {
    consumer = new StubCodeConsumer();
    consumer.endStatement(true);
    assertEquals(";\n", consumer.toString());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer = new StubCodeConsumer();
    consumer.maybeEndStatement();
    assertEquals(";\n", consumer.toString());
  }

  @Test
  public void testMaybeEndStatement_noStatementToEnd() {
    consumer = new StubCodeConsumer();
    consumer.maybeEndStatement();
    assertEquals("", consumer.toString());
  }

  @Test
  public void testEndFunction() {
    consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", consumer.toString());
  }

  @Test
  public void testEndFunction_statementContext() {
    consumer = new StubCodeConsumer();
    consumer.endFunction(true);
    assertEquals("}\n", consumer.toString());
  }

  @Test
  public void testBeginCaseBody() {
    consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.toString());
  }

  @Test
  public void testEndCaseBody() {
    consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    assertEquals("", consumer.toString());
  }

  @Test
  public void testAdd() {
    consumer = new StubCodeConsumer();
    consumer.add("abc");
    assertEquals("abc", consumer.toString());
  }

  @Test
  public void testAdd_needSpace() {
    consumer = new StubCodeConsumer();
    consumer.append("return ");
    consumer.add("foo");
    assertEquals("return foo\n", consumer.toString());
  }

  @Test
  public void testAddOp() {
    consumer = new StubCodeConsumer();
    consumer.append("x");
    consumer.addOp("+", false);
    consumer.append("y");
    assertEquals("x + y\n", consumer.toString());
  }

  @Test
  public void testAddOp_binop() {
    consumer = new StubCodeConsumer();
    consumer.append("x");
    consumer.addOp("+", true);
    consumer.append("y");
    assertEquals("(x + y)\n", consumer.toString());
  }

  @Test
  public void testAddNumber() {
    consumer = new StubCodeConsumer();
    consumer.addNumber(1.23);
    assertEquals("1.23\n", consumer.toString());
  }

  @Test
  public void testAddNumber_negative() {
    consumer = new StubCodeConsumer();
    consumer.append("-");
    consumer.addNumber(-1.23);
    assertEquals("-1.23\n", consumer.toString());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor_false() {
    consumer = new StubCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(null, false));
  }

  @Test
  public void testBreakAfterBlockFor_true() {
    consumer = new StubCodeConsumer();
    assertTrue(consumer.breakAfterBlockFor(null, true));
  }

  @Test
  public void testEndFile() {
    consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.toString());
  }

  private static class StubCodeConsumer extends CodeConsumer {
    @Override
    public char getLastChar() {
      return '\0';
    }

    @Override
    public void append(String str) {
      super.append(str);
    }

    @Override
    public void appendBlockStart() {
      super.appendBlockStart();
    }

    @Override
    public void appendBlockEnd() {
      super.appendBlockEnd();
    }

    @Override
    public void startNewLine() {
      super.startNewLine();
    }

    @Override
    public void maybeLineBreak() {
      super.maybeLineBreak();
    }

    @Override
    public void maybeCutLine() {
      super.maybeCutLine();
    }

    @Override
    public void endLine() {
      super.endLine();
    }

    @Override
    public void notePreferredLineBreak() {
      super.notePreferredLineBreak();
    }

    @Override
    public void beginBlock() {
      super.beginBlock();
    }

    @Override
    public void endBlock() {
      super.endBlock();
    }

    @Override
    public void endBlock(boolean shouldEndLine) {
      super.endBlock(shouldEndLine);
    }

    @Override
    public void listSeparator() {
      super.listSeparator();
    }

    @Override
    public void endStatement() {
      super.endStatement();
    }

    @Override
    public void endStatement(boolean needSemiColon) {
      super.endStatement(needSemiColon);
    }

    @Override
    public void maybeEndStatement() {
      super.maybeEndStatement();
    }

    @Override
    public void endFunction() {
      super.endFunction();
    }

    @Override
    public void endFunction(boolean statementContext) {
      super.endFunction(statementContext);
    }

    @Override
    public void beginCaseBody() {
      super.beginCaseBody();
    }

    @Override
    public void endCaseBody() {
      super.endCaseBody();
    }

    @Override
    public void add(String newcode) {
      super.add(newcode);
    }

    @Override
    public void addOp(String op, boolean binOp) {
      super.addOp(op, binOp);
    }

    @Override
    public void addNumber(double x) {
      super.addNumber(x);
    }

    @Override
    public boolean shouldPreserveExtraBlocks() {
      return false;
    }

    @Override
    public boolean breakAfterBlockFor(Node n, boolean statementContext) {
      return false;
    }

    @Override
    public void endFile() {
      super.endFile();
    }

    @Override
    public void startSourceMapping(Node node) {
      super.startSourceMapping(node);
    }

    @Override
    public void endSourceMapping(Node node) {
      super.endSourceMapping(node);
    }

    @Override
    public boolean continueProcessing() {
      return true;
    }
  }
}