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
            void append(String str) {}

            @Override
            void startNewLine() {}

            @Override
            void maybeCutLine() {}

            @Override
            void endLine() {}
        };
    }

    @Test
    public void testStartSourceMapping() {
        codeConsumer.startSourceMapping(null);
    }

    @Test
    public void testEndSourceMapping() {
        codeConsumer.endSourceMapping(null);
    }

    @Test
    public void testContinueProcessing() {
        assertTrue(codeConsumer.continueProcessing());
    }

    @Test
    public void testAddIdentifier() {
        codeConsumer.addIdentifier("identifier");
    }

    @Test
    public void testAppendBlockStart() {
        codeConsumer.appendBlockStart();
    }

    @Test
    public void testAppendBlockEnd() {
        codeConsumer.appendBlockEnd();
    }

    @Test
    public void testMaybeLineBreak() {
        codeConsumer.maybeLineBreak();
    }

    @Test
    public void testMaybeCutLine() {
        codeConsumer.maybeCutLine();
    }

    @Test
    public void testEndLine() {
        codeConsumer.endLine();
    }

    @Test
    public void testNotePreferredLineBreak() {
        codeConsumer.notePreferredLineBreak();
    }

    @Test
    public void testBeginBlock() {
        codeConsumer.beginBlock();
    }

    @Test
    public void testEndBlock() {
        codeConsumer.endBlock();
    }

    @Test
    public void testEndBlockWithShouldEndLine() {
        codeConsumer.endBlock(true);
    }

    @Test
    public void testListSeparator() {
        codeConsumer.listSeparator();
    }

    @Test
    public void testEndStatement() {
        codeConsumer.endStatement();
    }

    @Test
    public void testEndStatementWithNeedSemiColon() {
        codeConsumer.endStatement(true);
    }

    @Test
    public void testMaybeEndStatement() {
        codeConsumer.maybeEndStatement();
    }

    @Test
    public void testEndFunction() {
        codeConsumer.endFunction();
    }

    @Test
    public void testEndFunctionWithStatementContext() {
        codeConsumer.endFunction(true);
    }

    @Test
    public void testBeginCaseBody() {
        codeConsumer.beginCaseBody();
    }

    @Test
    public void testEndCaseBody() {
        codeConsumer.endCaseBody();
    }

    @Test
    public void testAddOp() {
        codeConsumer.addOp("op", false);
    }

    @Test
    public void testAddNumber() {
        codeConsumer.addNumber(1.0);
    }

    @Test
    public void testIsNegativeZero() {
        assertFalse(CodeConsumer.isNegativeZero(0.0));
    }

    @Test
    public void testIsWordChar() {
        assertFalse(CodeConsumer.isWordChar(' '));
    }

    @Test
    public void testShouldPreserveExtraBlocks() {
        assertFalse(codeConsumer.shouldPreserveExtraBlocks());
    }

    @Test
    public void testBreakAfterBlockFor() {
        assertFalse(codeConsumer.breakAfterBlockFor(null, false));
    }

    @Test
    public void testEndFile() {
        codeConsumer.endFile();
    }
}