package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

    @Test
    public void testStartSourceMapping() {
        CodeConsumer consumer = mock(CodeConsumer.class);
        Node node = mock(Node.class);

        consumer.startSourceMapping(node);

        verify(consumer).startSourceMapping(node);
    }

    @Test
    public void testEndSourceMapping() {
        CodeConsumer consumer = mock(CodeConsumer.class);
        Node node = mock(Node.class);

        consumer.endSourceMapping(node);

        verify(consumer).endSourceMapping(node);
    }

    @Test
    public void testContinueProcessing() {
        CodeConsumer consumer = mock(CodeConsumer.class);

        assertTrue(consumer.continueProcessing());

        verify(consumer).continueProcessing();
    }

    @Test
    public void testContinueProcessing_whenFalse() {
        CodeConsumer consumer = mock(CodeConsumer.class);

        consumer.continueProcessing = () -> false;

        assertFalse(consumer.continueProcessing());

        verify(consumer).continueProcessing();
    }

    @Test
    public void testGetLastChar() {
        CodeConsumer consumer = new TestCodeConsumer();

        assertSame("x", consumer.getLastChar());

        consumer.append("y");

        assertSame("y", consumer.getLastChar());
    }

    @Test
    public void testAppend() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");

        assertEquals("hello", consumer.getCode());
    }

    @Test
    public void testAppendBlockStart() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.appendBlockStart();

        assertEquals("{\n", consumer.getCode());
    }

    @Test
    public void testAppendBlockEnd() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.appendBlockEnd();

        assertEquals("hello}\n", consumer.getCode());
    }

    @Test
    public void testStartNewLine() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.startNewLine();

        assertEquals("\n", consumer.getCode());
    }

    @Test
    public void testMaybeLineBreak() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.maybeLineBreak();

        assertEquals("hello\n", consumer.getCode());
    }

    @Test
    public void testMaybeCutLine() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.maybeCutLine();

        assertEquals("hello\n", consumer.getCode());
    }

    @Test
    public void testEndLine() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.endLine();

        assertEquals("hello\n", consumer.getCode());
    }

    @Test
    public void testNotePreferredLineBreak() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.notePreferredLineBreak();

        assertEquals("\n", consumer.getCode());
    }

    @Test
    public void testBeginBlock() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.beginBlock();

        assertEquals("{\n", consumer.getCode());
    }

    @Test
    public void testEndBlock() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.appendBlockEnd();

        assertEquals("hello}\n", consumer.getCode());
    }

    @Test
    public void testEndBlock_shouldEndLine() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.endBlock(true);

        assertEquals("hello}\n", consumer.getCode());
    }

    @Test
    public void testListSeparator() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.listSeparator();

        assertEquals("hello,\n", consumer.getCode());
    }

    @Test
    public void testEndStatement() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.endStatement();

        assertEquals("hello;\n", consumer.getCode());
    }

    @Test
    public void testEndStatement_needSemiColon() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.endStatement(true);

        assertEquals("hello;\n", consumer.getCode());
    }

    @Test
    public void testMaybeEndStatement() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.maybeEndStatement();

        assertEquals("hello;\n", consumer.getCode());
    }

    @Test
    public void testEndFunction() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("function hello() {}");
        consumer.endFunction();

        assertEquals("function hello() {\n}\n", consumer.getCode());
    }

    @Test
    public void testEndFunction_statementContext() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("function hello() {\n}");
        consumer.endFunction(true);

        assertEquals("function hello() {\n}\n", consumer.getCode());
    }

    @Test
    public void testBeginCaseBody() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.beginCaseBody();

        assertEquals(":\n", consumer.getCode());
    }

    @Test
    public void testEndCaseBody() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("case 0:\n");
        consumer.endCaseBody();

        assertEquals("case 0:\n}", consumer.getCode());
    }

    @Test
    public void testAdd() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.add(" world");

        assertEquals("hello world", consumer.getCode());
    }

    @Test
    public void testAddOp() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.addOp("+ 1", true);

        assertEquals("hello + 1", consumer.getCode());
    }

    @Test
    public void testAddNumber() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.append("hello");
        consumer.addNumber(1.23);

        assertEquals("hello 1.23", consumer.getCode());
    }

    @Test
    public void testIsWordChar() {
        CodeConsumer consumer = new TestCodeConsumer();

        assertTrue(consumer.isWordChar('a'));
        assertTrue(consumer.isWordChar('_'));
        assertTrue(consumer.isWordChar('$'));
        assertTrue(consumer.isWordChar('1'));
        assertFalse(consumer.isWordChar(' '));
        assertFalse(consumer.isWordChar('('));
    }

    @Test
    public void testShouldPreserveExtraBlocks() {
        CodeConsumer consumer = new TestCodeConsumer();

        assertFalse(consumer.shouldPreserveExtraBlocks());

        verify(consumer).shouldPreserveExtraBlocks();
    }

    @Test
    public void testBreakAfterBlockFor() {
        CodeConsumer consumer = new TestCodeConsumer();

        Node node = mock(Node.class);

        assertTrue(consumer.breakAfterBlockFor(node, false));

        verify(consumer).breakAfterBlockFor(node, false);
    }

    @Test
    public void testBreakAfterBlockFor_statementContext() {
        CodeConsumer consumer = new TestCodeConsumer();

        Node node = mock(Node.class);

        assertTrue(consumer.breakAfterBlockFor(node, true));

        verify(consumer).breakAfterBlockFor(node, true);
    }

    @Test
    public void testEndFile() {
        CodeConsumer consumer = new TestCodeConsumer();

        consumer.endFile();

        assertEquals("", consumer.getCode());
    }

    private static class TestCodeConsumer extends CodeConsumer {
        private StringBuilder code = new StringBuilder();

        @Override
        void add(String newcode) {
            super.add(newcode);
            code.append(newcode);
        }

        @Override
        void append(String str) {
            super.append(str);
            code.append(str);
        }

        @Override
        char getLastChar() {
            return code.charAt(code.length() - 1);
        }

        @Override
        String getCode() {
            return code.toString();
        }
    }
}