import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
    private CodeConsumer codeConsumer;

    @Before
    public void setUp() {
        codeConsumer = new CodeConsumer() {
            @Override
            char getLastChar() {
                return ' ';
            }

            @Override
            void append(String str) {
                // do nothing
            }
        };
    }

    @Test
    public void testStartSourceMapping() {
        codeConsumer.startSourceMapping(null);
        // add assertions here
    }

    @Test
    public void testEndSourceMapping() {
        codeConsumer.endSourceMapping(null);
        // add assertions here
    }

    @Test
    public void testContinueProcessing() {
        assertTrue(codeConsumer.continueProcessing());
        // add assertions here
    }

    @Test
    public void testAddIdentifier() {
        codeConsumer.addIdentifier("identifier");
        // add assertions here
    }

    @Test
    public void testAppend() {
        codeConsumer.append("string");
        // add assertions here
    }

    @Test
    public void testAppendBlockStart() {
        codeConsumer.appendBlockStart();
        // add assertions here
    }

    @Test
    public void testAppendBlockEnd() {
        codeConsumer.appendBlockEnd();
        // add assertions here
    }

    @Test
    public void testStartNewLine() {
        codeConsumer.startNewLine();
        // add assertions here
    }

    @Test
    public void testMaybeLineBreak() {
        codeConsumer.maybeLineBreak();
        // add assertions here
    }

    @Test
    public void testMaybeCutLine() {
        codeConsumer.maybeCutLine();
        // add assertions here
    }

    @Test
    public void testEndLine() {
        codeConsumer.endLine();
        // add assertions here
    }

    @Test
    public void testNotePreferredLineBreak() {
        codeConsumer.notePreferredLineBreak();
        // add assertions here
    }

    @Test
    public void testBeginBlock() {
        codeConsumer.beginBlock();
        // add assertions here
    }

    @Test
    public void testEndBlock() {
        codeConsumer.endBlock();
        // add assertions here
    }

    @Test
    public void testEndBlockWithShouldEndLine() {
        codeConsumer.endBlock(true);
        // add assertions here
    }

    @Test
    public void testListSeparator() {
        codeConsumer.listSeparator();
        // add assertions here
    }

    @Test
    public void testEndStatement() {
        codeConsumer.endStatement();
        // add assertions here
    }

    @Test
    public void testEndStatementWithNeedSemiColon() {
        codeConsumer.endStatement(true);
        // add assertions here
    }

    @Test
    public void testMaybeEndStatement() {
        codeConsumer.maybeEndStatement();
        // add assertions here
    }

    @Test
    public void testEndFunction() {
        codeConsumer.endFunction();
        // add assertions here
    }

    @Test
    public void testEndFunctionWithStatementContext() {
        codeConsumer.endFunction(true);
        // add assertions here
    }

    @Test
    public void testBeginCaseBody() {
        codeConsumer.beginCaseBody();
        // add assertions here
    }

    @Test
    public void testEndCaseBody() {
        codeConsumer.endCaseBody();
        // add assertions here
    }

    @Test
    public void testAdd() {
        codeConsumer.add("newcode");
        // add assertions here
    }

    @Test
    public void testAppendOp() {
        codeConsumer.appendOp("op", true);
        // add assertions here
    }

    @Test
    public void testAddOp() {
        codeConsumer.addOp("op", true);
        // add assertions here
    }

    @Test
    public void testAddNumber() {
        codeConsumer.addNumber(2.5);
        // add assertions here
    }

    @Test
    public void testIsWordChar() {
        assertTrue(codeConsumer.isWordChar('a'));
        assertFalse(codeConsumer.isWordChar(' '));
        // add assertions here
    }

    @Test
    public void testShouldPreserveExtraBlocks() {
        assertFalse(codeConsumer.shouldPreserveExtraBlocks());
        // add assertions here
    }

    @Test
    public void testBreakAfterBlockFor() {
        assertTrue(codeConsumer.breakAfterBlockFor(null, true));
        assertFalse(codeConsumer.breakAfterBlockFor(null, false));
        // add assertions here
    }

    @Test
    public void testEndFile() {
        codeConsumer.endFile();
        // add assertions here
    }
}