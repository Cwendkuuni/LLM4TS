package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @org.junit.Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
    verify(consumer).startSourceMapping(node);
  }

  @org.junit.Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
    verify(consumer).endSourceMapping(node);
  }

  @org.junit.Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };
    assertTrue(consumer.continueProcessing());

    consumer = new CodeConsumer() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };
    assertFalse(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      private char lastChar = '\u0000';

      @Override
      public char getLastChar() {
        return lastChar;
      }

      @Override
      public void append(String str) {
        lastChar = str.charAt(str.length() - 1);
      }
    };
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testGetLastCharAfterEmptyAppend() {
    CodeConsumer consumer = new CodeConsumer() {
      private char lastChar = '\u0000';

      @Override
      public char getLastChar() {
        return lastChar;
      }

      @Override
      public void append(String str) {
        lastChar = str.charAt(str.length() - 1);
      }
    };
    consumer.append("");
  }

  @org.junit.Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello world");
    assertEquals("hello world", consumer.getCode());
  }

  @org.junit.Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @org.junit.Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    assertEquals("", consumer.getCode());
  }

  @org.junit.Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode());
  }

  @org.junit.Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement();
    assertEquals(";\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endStatement(true);
    assertEquals(";\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeEndStatement();
    assertEquals("", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeEndStatement(true);
    assertEquals(";\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction(true);
    assertEquals("\n}", consumer.getCode());
  }

  @org.junit.Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endCaseBody();
    assertEquals("", consumer.getCode());
  }

  @org.junit.Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("test");
    assertEquals("test", consumer.getCode());
  }

  @org.junit.Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    assertEquals("test", consumer.getCode());
  }

  @org.junit.Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", false);
    assertEquals("+", consumer.getCode());
  }

  @org.junit.Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(123.456);
    assertEquals("123.456", consumer.getCode());
  }

  @org.junit.Test
  public void testIsWordChar() {
    assertEquals(true, CodeConsumer.isWordChar('a'));
    assertEquals(true, CodeConsumer.isWordChar('A'));
    assertEquals(true, CodeConsumer.isWordChar('0'));
    assertEquals(true, CodeConsumer.isWordChar('_'));
    assertEquals(true, CodeConsumer.isWordChar('$'));
    assertEquals(true, CodeConsumer.isWordChar(' '));
    assertEquals(false, CodeConsumer.isWordChar('%'));
    assertEquals(false, CodeConsumer.isWordChar('!'));
  }

  @org.junit.Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @org.junit.Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = mock(Node.class);
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @org.junit.Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = mock(Node.class);
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @org.junit.Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @org.junit.Test
  public void testSawFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction();
    assertFalse(consumer.sawFunction);

    consumer.endFunction(true);
    assertTrue(consumer.sawFunction);
  }
}

class TestCodeConsumer extends CodeConsumer {
  private StringBuilder code = new StringBuilder();

  @Override
  public void add(String newcode) {
    code.append(newcode);
  }

  @Override
  public void append(String str) {
    code.append(str);
  }

  @Override
  public void appendBlockStart() {
    code.append("{");
  }

  @Override
  public void appendBlockEnd() {
    code.append("}");
  }

  @Override
  public void startNewLine() {
    code.append("\n");
  }

  @Override
  public void maybeCutLine() {
    code.append(" ");
  }

  @Override
  public void endLine() {
    code.append("\n");
  }

  @Override
  public void notePreferredLineBreak() {
    // no-op
  }

  @Override
  public void beginBlock() {
    code.append("{\n");
  }

  @Override
  public void endBlock() {
    code.append("}\n");
  }

  @Override
  public void endBlock(boolean shouldEndLine) {
    if (shouldEndLine) {
      code.append("}\n");
    } else {
      code.append("}");
    }
  }

  @Override
  public void listSeparator() {
    code.append(",\n");
  }

  @Override
  public char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  public String getCode() {
    String codeString = code.toString();
    code.setLength(0);
    return codeString;
  }
}