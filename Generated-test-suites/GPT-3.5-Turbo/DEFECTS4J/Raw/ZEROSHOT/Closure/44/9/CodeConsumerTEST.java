import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodeConsumerTest {
    private CodeConsumer codeConsumer;

    @Before
    public void setUp() {
        codeConsumer = new CodeConsumer() {
            @Override
            void append(String str) {
                //Do nothing
            }

            @Override
            void appendBlockStart() {
                //Do nothing
            }

            @Override
            void appendBlockEnd() {
                //Do nothing
            }

            @Override
            void startNewLine() {
                //Do nothing
            }

            @Override
            void maybeLineBreak() {
                //Do nothing
            }

            @Override
            void maybeCutLine() {
                //Do nothing
            }

            @Override
            void endLine() {
                //Do nothing
            }

            @Override
            void notePreferredLineBreak() {
                //Do nothing
            }

            @Override
            void beginBlock() {
                //Do nothing
            }

            @Override
            void endBlock() {
                //Do nothing
            }

            @Override
            void endBlock(boolean shouldEndLine) {
                //Do nothing
            }

            @Override
            void listSeparator() {
                //Do nothing
            }

            @Override
            void maybeEndStatement() {
                //Do nothing
            }

            @Override
            void endFunction() {
                //Do nothing
            }

            @Override
            void endFunction(boolean statementContext) {
                //Do nothing
            }

            @Override
            void beginCaseBody() {
                //Do nothing
            }

            @Override
            void endCaseBody() {
                //Do nothing
            }

            @Override
            void add(String newcode) {
                //Do nothing
            }

            @Override
            void appendOp(String op, boolean binOp) {
                //Do nothing
            }

            @Override
            void addOp(String op, boolean binOp) {
                //Do nothing
            }

            @Override
            void addNumber(double x) {
                //Do nothing
            }
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
    public void testGetLastChar() {
        assertEquals('\u0000', codeConsumer.getLastChar());
    }

    @Test
    public void testAddIdentifier() {
        codeConsumer.addIdentifier("identifier");
    }

    @Test
    public void testEndStatement() {
        codeConsumer.endStatement();
    }

    @Test
    public void testEndStatementWithSemiColon() {
        codeConsumer.endStatement(true);
    }

    @Test
    public void testEndStatementWithoutSemiColon() {
        codeConsumer.endStatement(false);
    }

    @Test
    public void testEndBlock() {
        codeConsumer.endBlock();
    }

    @Test
    public void testEndBlockWithEndLine() {
        codeConsumer.endBlock(true);
    }

    @Test
    public void testListSeparator() {
        codeConsumer.listSeparator();
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
    public void testAdd() {
        codeConsumer.add("code");
    }

    @Test
    public void testAppendOp() {
        codeConsumer.appendOp("op", false);
    }

    @Test
    public void testAddOp() {
        codeConsumer.addOp("op", false);
    }

    @Test
    public void testAddNumber() {
        codeConsumer.addNumber(1.0);
    }
}