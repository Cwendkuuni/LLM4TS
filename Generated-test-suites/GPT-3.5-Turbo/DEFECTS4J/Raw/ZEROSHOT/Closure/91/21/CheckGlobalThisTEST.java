import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertTrue(result);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        checkGlobalThis.visit(t, n, parent);
        // Test if the report method was called correctly
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        Node n = new Node();
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        // Test the JSDocInfo object returned
    }
}