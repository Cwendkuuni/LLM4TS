import org.junit.Test;

import static org.junit.Assert.*;

public class CodeConsumerTest {

    @Test
    public void testStartSourceMapping() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.startSourceMapping(null);
    }

    @Test
    public void testEndSourceMapping() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.endSourceMapping(null);
    }

    @Test
    public void testContinueProcessing() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        assertTrue(codeConsumer.continueProcessing());
    }

    @Test
    public void testGetLastChar() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        assertEquals('\0', codeConsumer.getLastChar());
    }

    @Test
    public void testAddIdentifier() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.addIdentifier("identifier");
    }

    @Test
    public void testAppend() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.append("string");
    }

    @Test
    public void testAppendBlockStart() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.appendBlockStart();
    }

    @Test
    public void testAppendBlockEnd() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.appendBlockEnd();
    }

    @Test
    public void testStartNewLine() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.startNewLine();
    }

    @Test
    public void testMaybeLineBreak() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.maybeLineBreak();
    }

    @Test
    public void testMaybeCutLine() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.maybeCutLine();
    }

    @Test
    public void testEndLine() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.endLine();
    }

    @Test
    public void testNotePreferredLineBreak() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.notePreferredLineBreak();
    }

    @Test
    public void testBeginBlock() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.beginBlock();
    }

    @Test
    public void testEndBlock() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.endBlock();
    }

    @Test
    public void testEndBlockWithShouldEndLine() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.endBlock(true);
    }

    @Test
    public void testListSeparator() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.listSeparator();
    }

    @Test
    public void testEndStatement() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.endStatement();
    }

    @Test
    public void testEndStatementWithNeedSemicolon() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.endStatement(true);
    }

    @Test
    public void testMaybeEndStatement() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.maybeEndStatement();
    }

    @Test
    public void testEndFunction() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.endFunction();
    }

    @Test
    public void testEndFunctionWithStatementContext() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.endFunction(true);
    }

    @Test
    public void testBeginCaseBody() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.beginCaseBody();
    }

    @Test
    public void testEndCaseBody() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.endCaseBody();
    }

    @Test
    public void testAdd() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.add("newcode");
    }

    @Test
    public void testAppendOp() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.appendOp("op", true);
    }

    @Test
    public void testAddOp() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.addOp("op", true);
    }

    @Test
    public void testAddNumber() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.addNumber(1.0);
    }

    @Test
    public void testIsWordChar() {
        assertFalse(CodeConsumer.isWordChar('-'));
        assertTrue(CodeConsumer.isWordChar('a'));
    }

    @Test
    public void testShouldPreserveExtraBlocks() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        assertFalse(codeConsumer.shouldPreserveExtraBlocks());
    }

    @Test
    public void testBreakAfterBlockFor() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        assertFalse(codeConsumer.breakAfterBlockFor(null, false));
    }

    @Test
    public void testEndFile() {
        CodeConsumer codeConsumer = new CodeConsumerImpl();
        codeConsumer.endFile();
    }

    private static class CodeConsumerImpl extends CodeConsumer {
        @Override
        char getLastChar() {
            return '\0';
        }

        @Override
        void append(String str) { }
    }
}