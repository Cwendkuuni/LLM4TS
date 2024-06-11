import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {

    private CodeConsumer codeConsumer;

    @Before
    public void setUp() {
        codeConsumer = new CodeConsumerImpl();
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
        boolean result = codeConsumer.continueProcessing();
        assert result;
    }

    @Test
    public void testGetLastChar() {
        char result = codeConsumer.getLastChar();
        assert result == '\u0000';
    }

    @Test
    public void testAddIdentifier() {
        codeConsumer.addIdentifier("identifier");
    }

    @Test
    public void testAppend() {
        codeConsumer.append("str");
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
    public void testStartNewLine() {
        codeConsumer.startNewLine();
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
    public void testListSeparator() {
        codeConsumer.listSeparator();
    }

    @Test
    public void testEndStatement() {
        codeConsumer.endStatement();
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
    public void testBeginCaseBody() {
        codeConsumer.beginCaseBody();
    }

    @Test
    public void testEndCaseBody() {
        codeConsumer.endCaseBody();
    }

    @Test
    public void testAdd() {
        codeConsumer.add("newcode");
    }

    @Test
    public void testAppendOp() {
        codeConsumer.appendOp("+", true);
    }

    @Test
    public void testAddOp() {
        codeConsumer.addOp("+", true);
    }

    @Test
    public void testAddNumber() {
        codeConsumer.addNumber(0.5);
    }

    @Test
    public void testIsWordChar() {
        boolean result = CodeConsumer.isWordChar('_');
        assert result;
    }

    @Test
    public void testShouldPreserveExtraBlocks() {
        boolean result = codeConsumer.shouldPreserveExtraBlocks();
        assert !result;
    }

    @Test
    public void testBreakAfterBlockFor() {
        boolean result = codeConsumer.breakAfterBlockFor(null, false);
        assert !result;
    }

    @Test
    public void testEndFile() {
        codeConsumer.endFile();
    }

    private class CodeConsumerImpl extends CodeConsumer {
        @Override
        char getLastChar() {
            return '\u0000';
        }

        @Override
        void append(String str) {

        }
    }
}