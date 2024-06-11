
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
    private CodeConsumer codeConsumer;

    @Before
    public void setUp() {
        codeConsumer = spy(CodeConsumer.class);
    }

    @Test
    public void testStartSourceMapping() {
        // setUp
        Node node = new Node();
        
        // execute
        codeConsumer.startSourceMapping(node);
        
        // assert
        verify(codeConsumer, times(1)).startSourceMapping(node);
    }

    @Test
    public void testEndSourceMapping() {
        // setUp
        Node node = new Node();
        
        // execute
        codeConsumer.endSourceMapping(node);
        
        // assert
        verify(codeConsumer, times(1)).endSourceMapping(node);
    }

    @Test
    public void testContinueProcessing() {
        // execute
        boolean result = codeConsumer.continueProcessing();
        
        // assert
        assertEquals(true, result);
    }

    @Test
    public void testGetLastChar() {
        // execute
        char result = codeConsumer.getLastChar();
        
        // assert
        assertEquals('\0', result);
    }

    @Test
    public void testAddIdentifier() {
        // setUp
        String identifier = "testIdentifier";
        
        // execute
        codeConsumer.addIdentifier(identifier);
        
        // assert
        verify(codeConsumer, times(1)).add(identifier);
    }

    @Test
    public void testAppend() {
        // setUp
        String str = "testString";
        
        // execute
        codeConsumer.append(str);
        
        // assert
        verify(codeConsumer, times(1)).append(str);
    }

    @Test
    public void testAppendBlockStart() {
        // execute
        codeConsumer.appendBlockStart();
        
        // assert
        verify(codeConsumer, times(1)).append("{");
    }

    @Test
    public void testAppendBlockEnd() {
        // execute
        codeConsumer.appendBlockEnd();
        
        // assert
        verify(codeConsumer, times(1)).append("}");
    }

    @Test
    public void testStartNewLine() {
        // execute
        codeConsumer.startNewLine();
        
        // assert
        verify(codeConsumer, times(1)).startNewLine();
    }

    @Test
    public void testMaybeLineBreak() {
        // execute
        codeConsumer.maybeLineBreak();
        
        // assert
        verify(codeConsumer, times(1)).maybeCutLine();
    }

    @Test
    public void testMaybeCutLine() {
        // execute
        codeConsumer.maybeCutLine();
        
        // assert
        verify(codeConsumer, times(1)).maybeCutLine();
    }

    @Test
    public void testEndLine() {
        // execute
        codeConsumer.endLine();
        
        // assert
        verify(codeConsumer, times(1)).endLine();
    }

    @Test
    public void testNotePreferredLineBreak() {
        // execute
        codeConsumer.notePreferredLineBreak();
        
        // assert
        verify(codeConsumer, times(1)).notePreferredLineBreak();
    }

    @Test
    public void testBeginBlock() {
        // execute
        codeConsumer.beginBlock();
        
        // assert
        verify(codeConsumer, times(1)).beginBlock();
    }

    @Test
    public void testEndBlock() {
        // execute
        codeConsumer.endBlock();
        
        // assert
        verify(codeConsumer, times(1)).endBlock(false);
    }

    @Test
    public void testEndBlock_shouldEndLine() {
        // execute
        codeConsumer.endBlock(true);
        
        // assert
        verify(codeConsumer, times(1)).endBlock(true);
    }

    @Test
    public void testListSeparator() {
        // execute
        codeConsumer.listSeparator();
        
        // assert
        verify(codeConsumer, times(1)).add(",");
        verify(codeConsumer, times(1)).maybeLineBreak();
    }

    @Test
    public void testEndStatement() {
        // execute
        codeConsumer.endStatement();
        
        // assert
        verify(codeConsumer, times(1)).endStatement(false);
    }

    @Test
    public void testEndStatement_needSemiColon() {
        // execute
        codeConsumer.endStatement(true);
        
        // assert
        verify(codeConsumer, times(1)).endStatement(true);
    }

    @Test
    public void testMaybeEndStatement() {
        // execute
        codeConsumer.maybeEndStatement();
        
        // assert
        verify(codeConsumer, times(1)).maybeEndStatement();
    }

    @Test
    public void testEndFunction() {
        // execute
        codeConsumer.endFunction();
        
        // assert
        verify(codeConsumer, times(1)).endFunction(false);
    }

    @Test
    public void testEndFunction_statementContext() {
        // execute
        codeConsumer.endFunction(true);
        
        // assert
        verify(codeConsumer, times(1)).endFunction(true);
        verify(codeConsumer, times(1)).endLine();
    }

    @Test
    public void testBeginCaseBody() {
        // execute
        codeConsumer.beginCaseBody();
        
        // assert
        verify(codeConsumer, times(1)).beginCaseBody();
    }

    @Test
    public void testEndCaseBody() {
        // execute
        codeConsumer.endCaseBody();
        
        // assert
        verify(codeConsumer, times(1)).endCaseBody();
    }

    @Test
    public void testAdd() {
        // setUp
        String newcode = "testCode";
        
        // execute
        codeConsumer.add(newcode);
        
        // assert
        verify(codeConsumer, times(1)).maybeEndStatement();
        verify(codeConsumer, times(1)).append(newcode);
    }

    @Test
    public void testAppendOp() {
        // setUp
        String op = "+";
        boolean binOp = true;
        
        // execute
        codeConsumer.appendOp(op, binOp);
        
        // assert
        verify(codeConsumer, times(1)).append(op);
    }

    @Test
    public void testAddOp() {
        // setUp
        String op = "+";
        boolean binOp = true;
        
        // execute
        codeConsumer.addOp(op, binOp);
        
        // assert
        verify(codeConsumer, times(1)).maybeEndStatement();
        verify(codeConsumer, times(1)).appendOp(op, binOp);
        verify(codeConsumer, times(1)).maybeCutLine();
    }

    @Test
    public void testAddNumber() {
        // setUp
        double x = 100.0;
        
        // execute
        codeConsumer.addNumber(x);
        
        // assert
        verify(codeConsumer, times(1)).maybeEndStatement();
        verify(codeConsumer, times(1)).add(Double.toString(x));
    }

    @Test
    public void testIsNegativeZero() {
        // setUp
        double x = -0.0;
        
        // execute
        boolean result = CodeConsumer.isNegativeZero(x);
        
        // assert
        assertEquals(true, result);
    }

    @Test
    public void testIsWordChar() {
        // execute
        boolean result = CodeConsumer.isWordChar('_');
        
        // assert
        assertEquals(true, result);
    }

    @Test
    public void testShouldPreserveExtraBlocks() {
        // execute
        boolean result = codeConsumer.shouldPreserveExtraBlocks();
        
        // assert
        assertEquals(false, result);
    }

    @Test
    public void testBreakAfterBlockFor() {
        // setUp
        Node n = new Node();
        boolean statementContext = true;
        
        // execute
        boolean result = codeConsumer.breakAfterBlockFor(n, statementContext);
        
        // assert
        assertEquals(statementContext, result);
    }

    @Test
    public void testEndFile() {
        // execute
        codeConsumer.endFile();
        
        // assert
        verify(codeConsumer, times(1)).endFile();
    }
}
