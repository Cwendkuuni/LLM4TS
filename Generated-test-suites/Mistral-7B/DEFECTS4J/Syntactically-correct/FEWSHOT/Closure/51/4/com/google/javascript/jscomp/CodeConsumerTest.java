package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CodeConsumer.isWordChar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
  abstract static class TestCodeConsumer extends CodeConsumer {
    abstract char getLastChar();
  }

  static class TestUtil extends TestCodeConsumer {

    @Override
    char getLastChar() {
      return '\u0000';
    }

    void testIsWordChar() {
      for (int i = 0; i < 128; i++) {
        char c = (char) i;
        assertTrue(isWordChar(c) || c == '_' || c == '$');
      }
      for (int i = 128; i < 256; i++) {
        char c = (char) i;
        assertFalse(isWordChar(c));
      }
    }

    @Test
    public void testIsWordChar_IdentifierStart() {
      for (int i = 0; i < 256; i++) {
        char c = (char) i;
        assertTrue(isWordChar(c) || c == '_');
      }
    }

    @Test
    public void testIsWordChar_IdentifierPart() {
      for (int i = 0; i < 256; i++) {
        char c = (char) i;
        assertTrue(isWordChar(c) || c == '$');
      }
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
    public void testShouldPreserveExtraBlocks_False() {
      TestCodeConsumer consumer = new TestCodeConsumer() {
        @Override
        boolean shouldPreserveExtraBlocks() {
          return false;
        }
      };
      assertFalse(consumer.shouldPreserveExtraBlocks());
    }
  }

  static class TestCodeConsumerWithAppend extends TestCodeConsumer {

    String code = "";

    @Override
    void append(String str) {
      code += str;
    }

    @Test
    public void testAppend() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("hello");
      consumer.append(" world");
      assertEquals("helloworld", consumer.code);
    }

    @Test
    public void testAppendOp() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.appendOp("+", false);
      consumer.append("1");
      consumer.appendOp("+", false);
      consumer.append("2");
      assertEquals("+1+2", consumer.code);
    }

    @Test
    public void testAddNumber() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.addNumber(10);
      assertEquals("10", consumer.code);

      consumer.addNumber(-10);
      assertEquals("-10", consumer.code);

      consumer.addNumber(3.14);
      assertEquals("3.14", consumer.code);

      consumer.addNumber(-3.14);
      assertEquals("-3.14", consumer.code);

      consumer.addNumber(0.123456789);
      assertEquals("0.123456789", consumer.code);

      consumer.addNumber(123456789012345L);
      assertEquals("123456789012345", consumer.code);

      consumer.addNumber(Long.MIN_VALUE);
      assertEquals("-9223372036854775807", consumer.code);

      consumer.addNumber(Long.MAX_VALUE);
      assertEquals("9223372036854775807", consumer.code);
    }

    @Test
    public void testAddIdentifier() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.addIdentifier("foo");
      assertEquals("foo", consumer.code);
    }

    @Test
    public void testAddOp() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.addOp("+", false);
      consumer.addNumber(1);
      consumer.addOp("+", false);
      consumer.addNumber(2);
      assertEquals("+1+2", consumer.code);

      consumer.addOp("*", true);
      consumer.addNumber(1);
      consumer.addOp("*", true);
      consumer.addNumber(2);
      assertEquals("1*2", consumer.code);
    }

    @Test
    public void testMaybeLineBreak() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.maybeLineBreak();
      assertEquals("\n", consumer.code.substring(consumer.code.length() - 1));
    }

    @Test
    public void testMaybeCutLine() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("foo");
      consumer.maybeCutLine();
      consumer.append("bar");
      assertEquals("foo\nbartest", consumer.code);
    }

    @Test
    public void testEndLine() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.endLine();
      assertEquals("\n", consumer.code.substring(consumer.code.length() - 1));
    }

    @Test
    public void testNotePreferredLineBreak() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.notePreferredLineBreak();
      assertEquals("\n", consumer.code.substring(consumer.code.length() - 1));
    }

    @Test
    public void testBeginBlock() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.beginBlock();
      assertEquals("{", consumer.code);
    }

    @Test
    public void testEndBlock() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("{");
      consumer.endBlock();
      assertEquals("}", consumer.code);
    }

    @Test
    public void testEndBlockWithNewLine() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("{");
      consumer.endBlock(true);
      assertEquals("}\n", consumer.code);
    }

    @Test
    public void testListSeparator() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.listSeparator();
      assertEquals(",\n", consumer.code.substring(consumer.code.length() - 2));
    }

    @Test
    public void testMaybeEndStatement() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("if (true) {");
      consumer.maybeEndStatement();
      consumer.append("}");
      assertEquals("if (true){\n}\n", consumer.code);

      consumer.append("if (true) ");
      consumer.maybeEndStatement();
      consumer.append("{");
      consumer.append("}");
      assertEquals("if (true)\n{\n}\n", consumer.code);

      consumer.append("if (true) ");
      consumer.maybeEndStatement();
      consumer.append(";");
      assertEquals("if (true)\n;\n", consumer.code);

      consumer.append("if (true) ");
      consumer.maybeEndStatement();
      consumer.append(";");
      consumer.endLine();
      consumer.append("else");
      assertEquals("if (true)\n;\nelse", consumer.code);
    }

    @Test
    public void testMaybeEndStatementWithNewLine() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("if (true) ");
      consumer.maybeEndStatement();
      consumer.append(";");
      consumer.endLine();
      consumer.append("else");
      assertEquals("if (true)\n;\nelse\n", consumer.code);
    }

    @Test
    public void testEndStatement() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("if (true) {");
      consumer.endStatement();
      consumer.append("}");
      assertEquals("if (true)\n{\n}\n", consumer.code);

      consumer.append("if (true) ");
      consumer.endStatement();
      consumer.append(";");
      assertEquals("if (true)\n;\n", consumer.code);

      consumer.append("if (true) ");
      consumer.endStatement();
      consumer.append(";");
      consumer.endLine();
      consumer.append("else");
      assertEquals("if (true)\n;\nelse\n", consumer.code);
    }

    @Test
    public void testEndStatementWithNewLine() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("if (true) ");
      consumer.endStatement();
      consumer.append(";");
      consumer.endLine();
      consumer.append("else");
      assertEquals("if (true)\n;\nelse\n", consumer.code);
    }

    @Test
    public void testEndFunction() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("function foo() {");
      consumer.endFunction();
      assertEquals("}", consumer.code);
    }

    @Test
    public void testEndFunctionWithNewLine() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("function foo() {");
      consumer.endFunction(true);
      assertEquals("}\n", consumer.code);
    }

    @Test
    public void testBeginCaseBody() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("switch (1) {");
      consumer.beginCaseBody();
      consumer.append("case 1:");
      assertEquals("switch (1) {\n  case 1:\n    ", consumer.code);
    }

    @Test
    public void testEndCaseBody() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("switch (1) {");
      consumer.beginCaseBody();
      consumer.append("case 1:");
      consumer.endCaseBody();
      assertEquals("switch (1) {\n  case 1:\n}", consumer.code);
    }

    @Test
    public void testStartSourceMapping() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      Node node = new Node();
      consumer.startSourceMapping(node);
      assertEquals("// SourceMappingVersion:1.1.{ \"version\": 1,\"file\": \"CodeConsumerTest.java\",\"sourceRoot\": \"/\",\"sources\": [{\"file\": \"CodeConsumer.java\"}],\"names\":[],\"mappings\":[]}", consumer.code);
    }

    @Test
    public void testEndSourceMapping() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      Node node = new Node();
      consumer.startSourceMapping(node);
      consumer.endSourceMapping(node);
      assertEquals("}", consumer.code);
    }

    @Test
    public void testContinueProcessing() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      assertTrue(consumer.continueProcessing());
    }

    @Test
    public void testContinueProcessingFalse() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend() {
        @Override
        boolean continueProcessing() {
          return false;
        }
      };
      assertFalse(consumer.continueProcessing());
    }

    @Test
    public void testGetLastChar() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.append("foo");
      assertEquals('o', consumer.getLastChar());
    }

    @Test
    public void testAddEmptyString() {
      TestCodeConsumerWithAppend consumer = new TestCodeConsumerWithAppend();
      consumer.add("");
      assertEquals("", consumer.code);
    }
  }

  @Test
  public void testTestUtil() {
    new TestUtil().testIsWordChar();
  }

  @Test
  public void testTestCodeConsumerWithAppend() {
    new TestCodeConsumerWithAppend().testAppend();
  }
}