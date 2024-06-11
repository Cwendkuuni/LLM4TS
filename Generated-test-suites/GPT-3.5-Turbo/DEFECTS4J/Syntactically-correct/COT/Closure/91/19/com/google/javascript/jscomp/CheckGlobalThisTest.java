import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test shouldTraverse method with a valid function node
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
        
        // Test shouldTraverse method with a constructor function node
        n = new Node(Token.FUNCTION);
        parent = new Node(Token.BLOCK);
        parent.setJSDocInfo(new JSDocInfo());
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.markConstructor();
        parent.getParent().setJSDocInfo(jsDoc);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test visit method with THIS node on left side of an assignment
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.visit(t, n, parent);
        // Assert that a warning diagnostic type has been reported
        
        // Test visit method with THIS node on right side of an assignment
        n = new Node(Token.THIS);
        parent = new Node(Token.ASSIGN);
        parent.setFirstChild(new Node(Token.GETPROP));
        checkGlobalThis.visit(t, n, parent);
        // Assert that a warning diagnostic type has not been reported
        
        // Test visit method with non-THIS node
        n = new Node(Token.NAME);
        parent = new Node(Token.ASSIGN);
        parent.setFirstChild(new Node(Token.GETPROP));
        checkGlobalThis.visit(t, n, parent);
        // Assert that a warning diagnostic type has not been reported
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test getFunctionJsDocInfo method with function having JSDoc
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
        
        // Test getFunctionJsDocInfo method with function having parent with JSDoc
        n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(new JSDocInfo());
        n.setParent(parent);
        result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(parent.getJSDocInfo(), result);
        
        // Test getFunctionJsDocInfo method with function having grandparent with JSDoc
        n = new Node(Token.FUNCTION);
        parent = new Node(Token.NAME);
        Node grandparent = new Node(Token.VAR);
        grandparent.setJSDocInfo(new JSDocInfo());
        parent.setParent(grandparent);
        n.setParent(parent);
        result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(grandparent.getJSDocInfo(), result);
        
        // Test getFunctionJsDocInfo method with function having no JSDoc
        n = new Node(Token.FUNCTION);
        parent = new Node(Token.NAME);
        parent.setJSDocInfo(null);
        n.setParent(parent);
        result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(result);
    }
}