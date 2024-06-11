package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CodeConsumerTest {

    @Test
    public void testStartSourceMapping() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testEndSourceMapping() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testContinueProcessing() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testGetLastChar() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testAddIdentifier() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testAppend() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testAppendBlockStart() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testAppendBlockEnd() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testStartNewLine() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testMaybeLineBreak() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testMaybeCutLine() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testEndLine() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testNotePreferredLineBreak() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testBeginBlock() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testEndBlock() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testListSeparator() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testEndStatement() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testMaybeEndStatement() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testEndFunction() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testBeginCaseBody() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testEndCaseBody() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testAdd() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testAppendOp() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testAddOp() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testAddNumber() {
        CodeConsumer consumer = new CodeConsumer() {};

        // Add test case here
    }

    @Test
    public void testIsNegativeZero() {
        boolean negativeZero = CodeConsumer.isNegativeZero(-0.0);

        assertTrue(negativeZero);
    }

    @Test
    public void testIsWordChar() {
        boolean wordChar = CodeConsumer.isWordChar('_');

        assertTrue(wordChar);
    }

    @Test
    public void testShouldPreserveExtraBlocks() {
        CodeConsumer consumer = new CodeConsumer() {};

        boolean preserveExtraBlocks = consumer.shouldPreserveExtraBlocks();

        assertFalse(preserveExtraBlocks);
    }

    @Test
    public void testBreakAfterBlockFor() {
        CodeConsumer consumer = new CodeConsumer() {};

        boolean breakAfterBlock = consumer.breakAfterBlockFor(null, true);

        assertTrue(breakAfterBlock);
    }

    @Test
    public void testEndFile() {
        CodeConsumer consumer = new CodeConsumer() {};

        consumer.endFile();
    }
}