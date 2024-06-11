import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CodeConsumerTest {
 
    @Mock
    private CodeConsumer codeConsumer;
 
    private Node node;
 
    @Before
    public void setUp() {
        node = new Node();
    }
 
    @Test
    public void testStartSourceMapping() {
        codeConsumer.startSourceMapping(node);
        // Verify that the method was called
        verify(codeConsumer).startSourceMapping(node);
    }
 
    @Test
    public void testEndSourceMapping() {
        codeConsumer.endSourceMapping(node);
        // Verify that the method was called
        verify(codeConsumer).endSourceMapping(node);
    }
 
    @Test
    public void testContinueProcessing() {
        assertTrue(codeConsumer.continueProcessing());
        // Verify that the method was called
        verify(codeConsumer).continueProcessing();
    }
 
    @Test
    public void testGetLastChar() {
        char lastChar = codeConsumer.getLastChar();
        // Verify that the method was called
        verify(codeConsumer).getLastChar();
        assertNotNull(lastChar);
    }
 
    @Test
    public void testAddIdentifier() {
        String identifier = "test";
        codeConsumer.addIdentifier(identifier);
        // Verify that the method was called
        verify(codeConsumer).addIdentifier(identifier);
    }
 
    @Test
    public void testAppend() {
        String str = "test";
        codeConsumer.append(str);
        // Verify that the method was called
        verify(codeConsumer).append(str);
    }
 
    @Test
    public void testAppendBlockStart() {
        codeConsumer.appendBlockStart();
        // Verify that the method was called
        verify(codeConsumer).appendBlockStart();
    }
 
    @Test
    public void testAppendBlockEnd() {
        codeConsumer.appendBlockEnd();
        // Verify that the method was called
        verify(codeConsumer).appendBlockEnd();
    }
 
    @Test
    public void testStartNewLine() {
        codeConsumer.startNewLine();
        // Verify that the method was called
        verify(codeConsumer).startNewLine();
    }
 
    @Test
    public void testMaybeLineBreak() {
        codeConsumer.maybeLineBreak();
        // Verify that the method was called
        verify(codeConsumer).maybeLineBreak();
    }
 
    @Test
    public void testMaybeCutLine() {
        codeConsumer.maybeCutLine();
        // Verify that the method was called
        verify(codeConsumer).maybeCutLine();
    }
 
    @Test
    public void testEndLine() {
        codeConsumer.endLine();
        // Verify that the method was called
        verify(codeConsumer).endLine();
    }
 
    @Test
    public void testNotePreferredLineBreak() {
        codeConsumer.notePreferredLineBreak();
        // Verify that the method was called
        verify(codeConsumer).notePreferredLineBreak();
    }
 
    @Test
    public void testBeginBlock() {
        codeConsumer.beginBlock();
        // Verify that the method was called
        verify(codeConsumer).beginBlock();
    }
 
    @Test
    public void testEndBlock() {
        codeConsumer.endBlock();
        // Verify that the method was called
        verify(codeConsumer).endBlock();
    }
 
    @Test
    public void testListSeparator() {
        codeConsumer.listSeparator();
        // Verify that the method was called
        verify(codeConsumer).listSeparator();
    }
 
    @Test
    public void testEndStatement() {
        codeConsumer.endStatement();
        // Verify that the method was called
        verify(codeConsumer).endStatement();
    }
 
    @Test
    public void testEndFunction() {
        codeConsumer.endFunction();
        // Verify that the method was called
        verify(codeConsumer).endFunction();
    }
 
    @Test
    public void testBeginCaseBody() {
        codeConsumer.beginCaseBody();
        // Verify that the method was called
        verify(codeConsumer).beginCaseBody();
    }
 
    @Test
    public void testEndCaseBody() {
        codeConsumer.endCaseBody();
        // Verify that the method was called
        verify(codeConsumer).endCaseBody();
    }
 
    @Test
    public void testAdd() {
        String newcode = "test";
        codeConsumer.add(newcode);
        // Verify that the method was called
        verify(codeConsumer).add(newcode);
    }
 
    @Test
    public void testAppendOp() {
        String op = "+";
        boolean binOp = true;
        codeConsumer.appendOp(op, binOp);
        // Verify that the method was called
        verify(codeConsumer).appendOp(op, binOp);
    }
 
    @Test
    public void testAddOp() {
        String op = "+";
        boolean binOp = true;
        codeConsumer.addOp(op, binOp);
        // Verify that the method was called
        verify(codeConsumer).addOp(op, binOp);
    }
 
    @Test
    public void testAddNumber() {
        double x = 10.5;
        codeConsumer.addNumber(x);
        // Verify that the method was called
        verify(codeConsumer).addNumber(x);
    }
 
    @Test
    public void testIsWordChar() {
        char ch = 'a';
        boolean isWordChar = codeConsumer.isWordChar(ch);
        // Verify that the method was called
        verify(codeConsumer).isWordChar(ch);
        assertTrue(isWordChar);
    }
 
    @Test
    public void testShouldPreserveExtraBlocks() {
        boolean shouldPreserveExtraBlocks = codeConsumer.shouldPreserveExtraBlocks();
        // Verify that the method was called
        verify(codeConsumer).shouldPreserveExtraBlocks();
        assertFalse(shouldPreserveExtraBlocks);
    }
 
    @Test
    public void testBreakAfterBlockFor() {
        boolean statementContext = true;
        boolean breakAfterBlockFor = codeConsumer.breakAfterBlockFor(node, statementContext);
        // Verify that the method was called
        verify(codeConsumer).breakAfterBlockFor(node, statementContext);
        assertTrue(breakAfterBlockFor);
    }
 
    @Test
    public void testEndFile() {
        codeConsumer.endFile();
        // Verify that the method was called
        verify(codeConsumer).endFile();
    }
 
}