import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new MockCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler);
        NodeTraversal traversal = new NodeTraversal(compiler, checker);
        assertTrue(checker.shouldTraverse(traversal, new Node(Token.SCRIPT), null));
        assertFalse(checker.shouldTraverse(traversal, new Node(Token.FUNCTION), null));
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new MockCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler);
        NodeTraversal traversal = new NodeTraversal(compiler, checker);
        Node thisNode = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        thisNode.setParent(parent);
        checker.visit(traversal, thisNode, parent);
        assertEquals(DiagnosticType.JSC_USED_GLOBAL_THIS, compiler.getErrorLevel());
    }

    @Test
    public void testShouldReportThis() {
        CheckGlobalThis checker = new CheckGlobalThis(new MockCompiler());
        Node thisNode = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        thisNode.setParent(parent);
        assertTrue(checker.shouldReportThis(thisNode));
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        CheckGlobalThis checker = new CheckGlobalThis(new MockCompiler());
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo(false);
        functionNode.setJSDocInfo(jsDocInfo);
        assertEquals(jsDocInfo, checker.getFunctionJsDocInfo(functionNode));
    }

    private static class MockCompiler extends AbstractCompiler {
        private DiagnosticType errorType;

        public void report(DiagnosticType type) {
            errorType = type;
        }

        public DiagnosticType getErrorLevel() {
            return errorType;
        }
    }
}