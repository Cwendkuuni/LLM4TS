package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodeConsumerTest {

    private CodeConsumer codeConsumer;

    @Before
    public void setUp() {
        codeConsumer = new CodeConsumer() {
            StringBuilder code = new StringBuilder();

            @Override
            char getLastChar() {
                return code.length() > 0 ? code.charAt(code.length() - 1) : '\0';
            }

            @Override
            void append(String str) {
                code.append(str);
            }

            @Override
            void startNewLine() {
                code.append("\n");
            }
        };
    }

    @Test
    public void testAppend_appendString_shouldAppendToCode() {
        codeConsumer.append("Hello World");
        assertEquals("Hello World", codeConsumer.code.toString());
    }

    @Test
    public void testAppendBlockStart_shouldAppendOpeningBrace() {
        codeConsumer.appendBlockStart();
        assertEquals("{", codeConsumer.code.toString());
    }

    @Test
    public void testAppendBlockEnd_shouldAppendClosingBrace() {
        codeConsumer.appendBlockEnd();
        assertEquals("}", codeConsumer.code.toString());
    }

    @Test
    public void testMaybeEndStatement_statementNeedsEnded_shouldAppendSemicolon() {
        codeConsumer.statementNeedsEnded = true;
        codeConsumer.maybeEndStatement();
        assertEquals(";\n", codeConsumer.code.toString());
    }

    @Test
    public void testMaybeEndStatement_statementStarted_shouldSetStatementNeedsEnded() {
        codeConsumer.statementStarted = true;
        codeConsumer.maybeEndStatement();
        assertTrue(codeConsumer.statementNeedsEnded);
    }

    @Test
    public void testMaybeEndStatement_noStatementStartedOrNeedsEnded_shouldNotAppendAnything() {
        codeConsumer.maybeEndStatement();
        assertEquals("", codeConsumer.code.toString());
    }

    @Test
    public void testEndStatement_needSemiColon_shouldAppendSemicolon() {
        codeConsumer.endStatement(true);
        assertEquals(";\n", codeConsumer.code.toString());
    }

    @Test
    public void testEndStatement_noNeedSemiColon_shouldNotAppendSemicolon() {
        codeConsumer.endStatement(false);
        assertEquals("", codeConsumer.code.toString());
    }

    @Test
    public void testBeginBlock_statementNeedsEnded_shouldAppendSemicolonAndStartNewLineAndBlockStart() {
        codeConsumer.statementNeedsEnded = true;
        codeConsumer.beginBlock();
        assertEquals(";\n{\n", codeConsumer.code.toString());
    }

    @Test
    public void testBeginBlock_noStatementNeedsEnded_shouldStartNewLineAndBlockStart() {
        codeConsumer.beginBlock();
        assertEquals("{\n", codeConsumer.code.toString());
    }

    @Test
    public void testEndBlock_shouldAppendBlockEndAndEndLine() {
        codeConsumer.endBlock();
        assertEquals("}\n", codeConsumer.code.toString());
    }
    
}