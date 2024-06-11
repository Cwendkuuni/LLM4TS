import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;

public class MinimizeExitPointsTest {

    @Test
    public void testTryMinimizeExits_ExitNodeRemoved() {
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = IR.createBlock();
        n.addChildToFront(IR.createReturn());
        minimizeExitPoints.tryMinimizeExits(n, Token.RETURN, null);
        assertEquals(0, n.getChildCount());
    }
    
    @Test
    public void testTryMinimizeExits_IfNodeWithTrueAndFalseBlock() {
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = IR.createIf(IR.createName("x"), IR.createBlock(IR.createBreak()), IR.createBlock(IR.createCall(IR.createName("blah")), IR.createCall(IR.createName("foo"))));
        minimizeExitPoints.tryMinimizeExits(n, Token.BREAK, null);
        Node ifBlock = n.getFirstChild().getNext();
        Node elseBlock = ifBlock.getNext();
        assertNull(ifBlock.getFirstChild());
        assertEquals(2, elseBlock.getChildCount());
    }
    
    @Test
    public void testTryMinimizeExits_DoNodeWithFalseCondition() {
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = IR.createDo(IR.createBlock(IR.createBreak()), IR.createBoolean(false));
        minimizeExitPoints.tryMinimizeExits(n, Token.BREAK, null);
        assertEquals(0, n.getFirstChild().getChildCount());
    }
    
    @Test
    public void testTryMinimizeExits_FunctionNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = IR.createFunction("foo");
        n.addChildToBack(IR.createReturn());
        minimizeExitPoints.tryMinimizeExits(n, Token.RETURN, null);
        assertEquals(0, n.getLastChild().getChildCount());
    }
    
    @Test
    public void testMatchingExitNode_ReturnNodeNoExpression() {
        Node n = IR.createReturn();
        assertTrue(MinimizeExitPoints.matchingExitNode(n, Token.RETURN, null));
    }
    
    @Test
    public void testMatchingExitNode_BreakNodeWithName() {
        Node n = IR.createBreak("label");
        assertTrue(MinimizeExitPoints.matchingExitNode(n, Token.BREAK, "label"));
    }
    
    @Test
    public void testMatchingExitNode_BreakNodeNoName() {
        Node n = IR.createBreak();
        assertTrue(MinimizeExitPoints.matchingExitNode(n, Token.BREAK, null));
    }
    
    @Test
    public void testMoveAllFollowing_OneNodeOnlyToFront() {
        Node start = IR.createReturn();
        Node srcParent = IR.createBlock(start);
        Node destParent = IR.createBlock(IR.createBreak());
        MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
        assertEquals(1, destParent.getChildCount());
        assertEquals(start, destParent.getFirstChild());
    }
    
    @Test
    public void testMoveAllFollowing_MultipleNodesToBack() {
        Node start = IR.createReturn();
        Node n2 = IR.createBreak();
        Node srcParent = IR.createBlock(start, n2);
        Node destParent = IR.createBlock(IR.createCall(IR.createName("foo")));
        MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
        assertEquals(3, destParent.getChildCount());
        assertEquals(n2, destParent.getLastChild());
    }
}