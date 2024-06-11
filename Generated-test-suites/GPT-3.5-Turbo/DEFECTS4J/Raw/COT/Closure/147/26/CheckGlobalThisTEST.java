import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test with function that is not a constructor or has annotations
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
        
        // Test with function that is a constructor
        n.setType(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        parent.setJSDocInfo(jsDoc);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
        
        // Test with annotated function
        jsDoc.setThisType(true);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
        
        // Test with function that does not have a logical place for a @this annotation
        jsDoc.setConstructor(false);
        jsDoc.setInterface(true);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
        
        // Test with function that does not belong to a valid context
        Node grandParent = new Node(Token.VAR);
        parent.setParent(grandParent);
        jsDoc.setInterface(false);
        jsDoc.setOverride(true);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test with THIS on the left side of an assignment
        checkGlobalThis.visit(t, n, parent);
        // Check that an error is reported
        
        // Test with THIS with a property access
        Node propertyAccess = new Node(Token.GETPROP);
        parent = propertyAccess;
        checkGlobalThis.visit(t, n, parent);
        // Check that an error is reported
        
        // Test without THIS on the left side of an assignment or property access
        checkGlobalThis.visit(t, n, parent);
        // Check that no error is reported
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        Node n = null;
        Node parent = null;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        // Test with function having JSDocInfo
        JSDocInfo expected = new JSDocInfo();
        n.setJSDocInfo(expected);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
        
        // Test with function having parent with JSDocInfo
        parent.setJSDocInfo(expected);
        result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
        
        // Test with named function in var declaration with parent with JSDocInfo
        Node grandParent = new Node(Token.VAR);
        parent.setParent(grandParent);
        result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
        
        // Test with function without JSDocInfo
        n.setJSDocInfo(null);
        result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(result);
    }
}