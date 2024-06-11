package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CodeConsumer.isWordChar;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('A'));
    assertTrue(isWordChar('Z'));
    assertTrue(isWordChar('0'));
    assertTrue(isWordChar('9'));
    assertFalse(isWordChar(' '));
    assertFalse(isWordChar('-'));
    assertFalse(isWordChar('+'));
  }

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    when(consumer.continueProcessing()).thenReturn(true);

    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }
    };

    assertEquals('a', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void append(String str) {
        append(str.toCharArray());
      }

      @Override
      void append(char[] chars) {
        for (char c : chars) {
          System.out.print(c);
        }
      }
    };

    consumer.append("hello");
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.appendBlockStart();

    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.appendBlockEnd();

    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.startNewLine();

    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.maybeLineBreak();

    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.maybeCutLine();

    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.endLine();

    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.notePreferredLineBreak();

    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.beginBlock();

    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.endBlock();

    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithStatementContext() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.endBlock(true);

    assertEquals("\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.listSeparator();

    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void append(String str) {
        append(str.toCharArray());
      }

      @Override
      void append(char[] chars) {
        for (char c : chars) {
          System.out.print(c);
        }
      }

      @Override
      void endStatement() {
        append(";");
        append("\n");
      }
    };

    consumer.append("print(\"hello\");");

    assertEquals("print(\"hello\");\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.endStatement(true);

    assertEquals(";\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.maybeEndStatement();

    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.endFunction();

    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.endFunction(true);

    assertEquals("\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.beginCaseBody();

    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = mock(CodeConsumer.class);

    consumer.endCaseBody();

    assertEquals("", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void append(String str) {
        append(str.toCharArray());
      }

      @Override
      void append(char[] chars) {
        for (char c : chars) {
          System.out.print(c);
        }
      }

      @Override
      void add(String newcode) {
        append(newcode);
      }
    };

    consumer.add("hello world");

    assertEquals("hello world", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void addNumber(double x) {
        add(String.valueOf(x));
      }
    };

    consumer.addNumber(1.23);

    assertEquals("1.23", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void addOp(String op, boolean binOp) {
        append(op);
      }
    };

    consumer.addOp("+");

    assertEquals("+", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    when(consumer.shouldPreserveExtraBlocks()).thenReturn(true);

    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    when(consumer.breakAfterBlockFor(node, false)).thenReturn(false);

    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    when(consumer.breakAfterBlockFor(node, true)).thenReturn(true);

    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    consumer.endFile();

    assertEquals("", consumer.getCode());
  }
}