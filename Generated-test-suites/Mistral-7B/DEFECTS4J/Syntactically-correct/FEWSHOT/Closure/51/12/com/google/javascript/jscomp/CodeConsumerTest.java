package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Test
  public void testStartSourceMapping() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    Node node = new Node();
    consumer.startSourceMapping(node);

    verify(generator).startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    Node node = new Node();
    consumer.endSourceMapping(node);

    verify(generator).endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingWhenFalse() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer = new FakeCodeConsumer(consumer) {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };

    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.append("foo");

    char expected = 'f';
    char actual = consumer.getLastChar();

    assertEquals(expected, actual);
  }

  @Test
  public void testAppend() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.append("foo");

    String expected = "foo";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockStart() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.appendBlockStart();

    String expected = "{\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.appendBlockEnd();

    String expected = "}";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testStartNewLine() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.startNewLine();

    String expected = "\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.maybeLineBreak();

    String expected = "\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeCutLine() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.maybeCutLine();

    String expected = "";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndLine() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.endLine();

    String expected = "\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.notePreferredLineBreak();

    String expected = "\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testBeginBlock() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.beginBlock();

    String expected = "{\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlock() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.endBlock();

    String expected = "}";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.endBlock(true);

    String expected = "}";
    String actual = consumer.getCode();

    String codeBeforeEndBlock = consumer.getCodeBeforeLastChar();

    assertEquals(expected, actual);
    assertEquals("\n", codeBeforeEndBlock);
  }

  @Test
  public void testListSeparator() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.listSeparator();

    String expected = ", ";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatement() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.append("foo");
    consumer.endStatement();

    String expected = "foo;\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.append("foo");
    consumer.endStatement(true);

    String expected = "foo;\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.append("foo");
    consumer.maybeEndStatement();

    String expected = "";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunction() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.append("function foo() {");
    consumer.endFunction();

    String expected = "}";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndFunctionWithNewline() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.append("function foo() {");
    consumer.endFunction(true);

    String expected = "}";
    String actual = consumer.getCode();

    String codeBeforeEndFunction = consumer.getCodeBeforeLastChar();

    assertEquals(expected, actual);
    assertEquals("\n", codeBeforeEndFunction);
  }

  @Test
  public void testBeginCaseBody() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.beginCaseBody();

    String expected = ": ";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testEndCaseBody() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.endCaseBody();

    String expected = "";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAdd() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.add("foo");

    String expected = "foo";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddIdentifier() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.addIdentifier("foo");

    String expected = "foo";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddOp() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.append("foo");
    consumer.appendOp("+", false);
    consumer.append("bar");

    String expected = "foo + bar";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddOpWithNewline() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.append("foo");
    consumer.appendOp("+", false);
    consumer.append("bar");
    consumer.endStatement(true);

    String expected = "foo + bar;\n";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddNumber() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.addNumber(1.5);

    String expected = "1.5";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testAddNumberWithNewline() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.append("foo");
    consumer.addNumber(1.5);
    consumer.endStatement(true);

    String expected = "foo\n1.5;";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  @Test
  public void testIsWordChar() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('1'));

    assertFalse(consumer.isWordChar(' '));
    assertFalse(consumer.isWordChar('?'));
    assertFalse(consumer.isWordChar('['));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    assertFalse(consumer.breakAfterBlockFor(null, false));
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    assertTrue(consumer.breakAfterBlockFor(null, true));
  }

  @Test
  public void testEndFile() {
    CodeGenerator generator = mock(CodeGenerator.class);
    consumer = new TestCodeConsumer(generator);

    consumer.endFile();

    String expected = "";
    String actual = consumer.getCode();

    assertEquals(expected, actual);
  }

  private static class TestCodeConsumer extends CodeConsumer {
    private CodeGenerator generator;

    TestCodeConsumer(CodeGenerator generator) {
      this.generator = generator;
    }

    @Override
    public String getCode() {
      return generator.getCode();
    }

    @Override
    public String getCodeBeforeLastChar() {
      return generator.getCodeBeforeLastChar();
    }
  }

  private static class FakeCodeConsumer extends CodeConsumer {
    private CodeConsumer delegate;

    FakeCodeConsumer(CodeConsumer delegate) {
      this.delegate = delegate;
    }

    @Override
    public boolean continueProcessing() {
      return delegate.continueProcessing();
    }
  }
}