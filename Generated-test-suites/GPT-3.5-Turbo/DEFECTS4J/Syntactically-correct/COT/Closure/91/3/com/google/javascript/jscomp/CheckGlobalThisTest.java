
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.JSError;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        
        checkGlobalThis.visit(t, n, parent);
        
        assertEquals(1, t.getErrorCount());
        JSError error = t.getErrors().get(0);
        assertEquals("JSC_USED_GLOBAL_THIS", error.getType().key);
    }

    @Test
    public void testShouldReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        assertTrue(checkGlobalThis.shouldReportThis(n, parent));
        
        n = new Node(Token.THIS);
        parent = new Node(Token.NAME);
        assertFalse(checkGlobalThis.shouldReportThis(n, parent));
        
        n = new Node(Token.THIS);
        parent = new Node();
        assertFalse(checkGlobalThis.shouldReportThis(n, parent));
        
        n = new Node(Token.THIS);
        parent = null;
        assertFalse(checkGlobalThis.shouldReportThis(n, parent));
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        Node n = new Node();
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        
        JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(jsDoc);
        
        n = new Node();
        parent = new Node(Token.NAME);
        jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(jsDoc);
        
        n = new Node();
        parent = new Node(Token.ASSIGN);
        parent.setJSDocInfo(new JSDocInfo());
        jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNotNull(jsDoc);
        
        n = new Node();
        Node gramps = new Node(Token.VAR);
        gramps.setJSDocInfo(new JSDocInfo());
        parent = new Node(Token.NAME);
        parent.setParent(gramps);
        jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNotNull(jsDoc);
    }
}
