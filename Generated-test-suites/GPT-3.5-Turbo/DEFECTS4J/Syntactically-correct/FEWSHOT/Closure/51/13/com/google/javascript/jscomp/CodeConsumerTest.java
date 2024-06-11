package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
    @Test
    public void testStartSourceMapping() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        Node node = new Node();
        codeConsumer.startSourceMapping(node);
        // your assertions here
    }

    @Test
    public void testEndSourceMapping() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        Node node = new Node();
        codeConsumer.endSourceMapping(node);
        // your assertions here
    }

    @Test
    public void testContinueProcessing() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        assertTrue(codeConsumer.continueProcessing());
        // your assertions here
    }

    @Test
    public void testGetLastChar() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        assertEquals('c', codeConsumer.getLastChar());
        // your assertions here
    }

    @Test
    public void testAddIdentifier() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.addIdentifier("identifier");
        // your assertions here
    }

    @Test
    public void testAppend() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.append("test");
        // your assertions here
    }

    @Test
    public void testAppendBlockStart() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.appendBlockStart();
        // your assertions here
    }

    @Test
    public void testAppendBlockEnd() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.appendBlockEnd();
        // your assertions here
    }

    @Test
    public void testStartNewLine() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.startNewLine();
        // your assertions here
    }

    @Test
    public void testMaybeLineBreak() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.maybeLineBreak();
        // your assertions here
    }

    @Test
    public void testMaybeCutLine() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.maybeCutLine();
        // your assertions here
    }

    @Test
    public void testEndLine() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.endLine();
        // your assertions here
    }

    @Test
    public void testNotePreferredLineBreak() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.notePreferredLineBreak();
        // your assertions here
    }

    @Test
    public void testBeginBlock() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.beginBlock();
        // your assertions here
    }

    @Test
    public void testEndBlock() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.endBlock();
        // your assertions here
    }

    @Test
    public void testEndBlockWithShouldEndLine() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.endBlock(true);
        // your assertions here
    }

    @Test
    public void testListSeparator() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.listSeparator();
        // your assertions here
    }

    @Test
    public void testEndStatement() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.endStatement();
        // your assertions here
    }

    @Test
    public void testEndStatementWithNeedSemiColon() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.endStatement(true);
        // your assertions here
    }

    @Test
    public void testMaybeEndStatement() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.maybeEndStatement();
        // your assertions here
    }

    @Test
    public void testEndFunction() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.endFunction();
        // your assertions here
    }

    @Test
    public void testEndFunctionWithStatementContext() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.endFunction(true);
        // your assertions here
    }

    @Test
    public void testBeginCaseBody() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.beginCaseBody();
        // your assertions here
    }

    @Test
    public void testEndCaseBody() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.endCaseBody();
        // your assertions here
    }

    @Test
    public void testAdd() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.add("test");
        // your assertions here
    }

    @Test
    public void testAppendOp() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.appendOp("op", true);
        // your assertions here
    }

    @Test
    public void testAddOp() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.addOp("op", true);
        // your assertions here
    }

    @Test
    public void testAddNumberWithNegativeNumber() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.addNumber(-1.0);
        // your assertions here
    }

    @Test
    public void testAddNumberWithPositiveNumber() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.addNumber(1.0);
        // your assertions here
    }

    @Test
    public void testIsWordCharWithUnderscore() {
        assertTrue(CodeConsumer.isWordChar('_'));
        // your assertions here
    }

    @Test
    public void testIsWordCharWithDollarSign() {
        assertTrue(CodeConsumer.isWordChar('$'));
        // your assertions here
    }

    @Test
    public void testIsWordCharWithLetterOrDigit() {
        assertTrue(CodeConsumer.isWordChar('A'));
        // your assertions here
    }

    @Test
    public void testIsWordCharWithNonWordChar() {
        assertFalse(CodeConsumer.isWordChar('-'));
        // your assertions here
    }

    @Test
    public void testShouldPreserveExtraBlocks() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        assertFalse(codeConsumer.shouldPreserveExtraBlocks());
        // your assertions here
    }

    @Test
    public void testBreakAfterBlockForWithStatementContext() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        Node node = new Node();
        assertTrue(codeConsumer.breakAfterBlockFor(node, true));
        // your assertions here
    }

    @Test
    public void testBreakAfterBlockForWithoutStatementContext() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        Node node = new Node();
        assertFalse(codeConsumer.breakAfterBlockFor(node, false));
        // your assertions here
    }

    @Test
    public void testEndFile() {
        CodeConsumer codeConsumer = new CodeConsumerTestImpl();
        codeConsumer.endFile();
        // your assertions here
    }

    private class CodeConsumerTestImpl extends CodeConsumer {
        @Override
        void startSourceMapping(Node node) {
            // implementation here
        }

        @Override
        void endSourceMapping(Node node) {
            // implementation here
        }

        @Override
        void append(String str) {
            // implementation here
        }

        @Override
        char getLastChar() {
            return 'c';
        }
    }
}