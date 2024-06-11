import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.jscomp.AbstractCompiler;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_WithTypeFunction_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithFunctionJsDocInfo_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();

        n.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithFunctionJsDocInfoAndConstructor_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);

        n.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithFunctionJsDocInfoAndInterface_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setInterface(true);

        n.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithFunctionJsDocInfoAndThisType_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();

        n.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithFunctionJsDocInfoAndOverride_ReturnsFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);

        n.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithParentScript_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.SCRIPT);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_WithParentBlock_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.BLOCK);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_WithParentName_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.NAME);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_WithParentAssign_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.ASSIGN);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_WithParentString_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.STRING);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_WithParentNumber_ReturnsTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.NUMBER);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testVisit_WithTokenThisAndParent_Get_ShouldReport() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);

        checkGlobalThis.visit(t, n, parent);
    }

    @Test
    public void testVisit_WithTokenThisAndAssignLhsChild_NotNull_ShouldReport() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.visit(t, n, parent);

        n = new Node(Token.STRING);
        parent = new Node(Token.GETPROP);
        checkGlobalThis.visit(t, n, parent);
    }

    @Test
    public void testGetFunctionJsDocInfo_WithNoJsDocInfo_ReturnsNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertNull(result);
    }

    @Test
    public void testGetFunctionJsDocInfo_WithParentName_ReturnsParentJSDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDocInfo = new JSDocInfo();

        parent.setJSDocInfo(jsDocInfo);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_WithParentAssign_ReturnsParentJSDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDocInfo = new JSDocInfo();

        parent.setJSDocInfo(jsDocInfo);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_WithParentNameAndGrandparentVar_ReturnsGrandparentJSDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        Node grandparent = new Node(Token.VAR);
        JSDocInfo jsDocInfo = new JSDocInfo();

        grandparent.setJSDocInfo(jsDocInfo);
        parent.setParent(grandparent);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertNull(result);
    }
}