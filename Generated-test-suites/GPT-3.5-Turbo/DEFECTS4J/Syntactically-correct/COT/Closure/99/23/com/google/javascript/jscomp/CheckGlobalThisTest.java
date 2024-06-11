
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.DiagnosticsReporter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.DiagnosticType;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;

public class CheckGlobalThisTest {
    
    private AbstractCompiler compiler;
    private CheckGlobalThis checkGlobalThis;
    
    @Test
    public void testShouldTraverse_returnsFalseForConstructorFunction() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_returnsFalseForFunctionWithThisTypeAnnotation() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_returnsFalseForFunctionWithOverrideAnnotation() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordOverride();
        n.setJSDocInfo(jsDoc);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_returnsFalseForNonTraversableFunction() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ADD);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_returnsTrueForValidFunction() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testVisit_reportsGlobalThisError() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        checkGlobalThis.visit(t, n, parent);
        assertTrue(t.getErrors().contains(new DiagnosticType("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")));
    }

}

