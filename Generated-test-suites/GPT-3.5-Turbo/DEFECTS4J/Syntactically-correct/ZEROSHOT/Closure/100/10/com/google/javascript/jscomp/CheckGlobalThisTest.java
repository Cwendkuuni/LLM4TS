import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
    
    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler compiler;
    private CheckLevel level;
    private NodeTraversal.Callback callback;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
        callback = checkGlobalThis;
    }
    
    @Test
    public void shouldTraverse_returnTrue_ifNotFunctionNode() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.ROOT);
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void shouldTraverse_returnFalse_ifFunctionNode_hasConstructorAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ROOT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void shouldTraverse_returnFalse_ifFunctionNode_hasThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ROOT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        
        boolean result = callback.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void visit_shouldReportError_ifNodeIsThis_andShouldReportThisReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ROOT);
        DiagnosticType diagnosticType = DiagnosticType.warning(
            "JSC_USED_GLOBAL_THIS",
            "dangerous use of the global 'this' object"
        );
        checkGlobalThis.compiler = new AbstractCompiler() {
            public void report(Error error) {
                assertEquals(diagnosticType, error.getType());
            }
        };
        
        callback.visit(t, n, parent);
    }
    
    @Test
    public void visit_shouldNotReportError_ifNodeIsThis_andShouldReportThisReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ROOT);
        checkGlobalThis.assignLhsChild = new Node(Token.ASSIGN);
        DiagnosticType diagnosticType = DiagnosticType.warning(
            "JSC_USED_GLOBAL_THIS",
            "dangerous use of the global 'this' object"
        );
        checkGlobalThis.compiler = new AbstractCompiler() {
            public void report(Error error) {
                assertEquals(diagnosticType, error.getType());
            }
        };
        
        callback.visit(t, n, parent);
    }
}