
import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.STRING);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit_ThisOnLeftSideOfAssignment() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        assertTrue(checkGlobalThis.shouldReportThis(n, parent));
    }

    @Test
    public void testVisit_GetProperty() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        assertTrue(checkGlobalThis.shouldReportThis(n, parent));
    }

    @Test
    public void testVisit_ThisInSafeContext() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.FUNCTION);
        assertFalse(checkGlobalThis.shouldReportThis(n, parent));
    }

    @Test
    public void testVisitor_ThisEncountered_LevelWarning_GenerateError() {
        AbstractCompiler compiler = new AbstractCompiler();
        compiler.reportCodeChange();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.STRING);
        checkGlobalThis.visit(t, n, parent);
        assertTrue(compiler.hasHaltingErrors());
    }

    @Test
    public void testVisitor_ThisEncountered_LevelOff_NoError() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.OFF);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.STRING);
        checkGlobalThis.visit(t, n, parent);
        assertFalse(compiler.hasHaltingErrors());
    }

}