package com.google.javascript.jscomp;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodeConsumerTest {

    private CodeConsumer codeConsumer;

    @Before
    public void setUp() {
        codeConsumer = new CodeConsumer() {
            @Override
            char getLastChar() {
                return 0;
            }

            @Override
            void append(String str) {

            }
        };
    }

    @Test
    public void testStartSourceMapping() {
        // Test code goes here
    }

    @Test
    public void testEndSourceMapping() {
        // Test code goes here
    }

    @Test
    public void testContinueProcessing() {
        // Test code goes here
    }

    @Test
    public void testAddIdentifier() {
        // Test code goes here
    }

    @Test
    public void testAppend() {
        // Test code goes here
    }

    @Test
    public void testAppendBlockStart() {
        // Test code goes here
    }

    @Test
    public void testAppendBlockEnd() {
        // Test code goes here
    }

    @Test
    public void testStartNewLine() {
        // Test code goes here
    }

    @Test
    public void testMaybeLineBreak() {
        // Test code goes here
    }

    @Test
    public void testMaybeCutLine() {
        // Test code goes here
    }

    @Test
    public void testEndLine() {
        // Test code goes here
    }

    @Test
    public void testNotePreferredLineBreak() {
        // Test code goes here
    }

    @Test
    public void testBeginBlock() {
        // Test code goes here
    }

    @Test
    public void testEndBlock() {
        // Test code goes here
    }

    @Test
    public void testEndBlockWithShouldEndLine() {
        // Test code goes here
    }

    @Test
    public void testListSeparator() {
        // Test code goes here
    }

    @Test
    public void testEndStatement() {
        // Test code goes here
    }

    @Test
    public void testEndStatementWithNeedSemiColon() {
        // Test code goes here
    }

    @Test
    public void testMaybeEndStatement() {
        // Test code goes here
    }

    @Test
    public void testEndFunction() {
        // Test code goes here
    }

    @Test
    public void testEndFunctionWithStatementContext() {
        // Test code goes here
    }

    @Test
    public void testBeginCaseBody() {
        // Test code goes here
    }

    @Test
    public void testEndCaseBody() {
        // Test code goes here
    }

    @Test
    public void testAdd() {
        // Test code goes here
    }

    @Test
    public void testAppendOp() {
        // Test code goes here
    }

    @Test
    public void testAddOp() {
        // Test code goes here
    }

    @Test
    public void testAddNumber() {
        // Test code goes here
    }

    @Test
    public void testIsWordChar() {
        // Test code goes here
    }

    @Test
    public void testShouldPreserveExtraBlocks() {
        // Test code goes here
    }

    @Test
    public void testBreakAfterBlockFor() {
        // Test code goes here
    }

    @Test
    public void testEndFile() {
        // Test code goes here
    }
}