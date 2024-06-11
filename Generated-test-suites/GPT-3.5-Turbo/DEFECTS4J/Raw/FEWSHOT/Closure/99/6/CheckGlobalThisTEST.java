import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    }

    @Test
    public void testShouldTraverse_functionConstructor() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordConstructor();
        functionNode.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, null);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_functionThisType() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        functionNode.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, null);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_functionOverride() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordOverride();
        functionNode.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, null);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_functionBlockParent() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        functionNode.setParent(parent);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_functionScriptParent() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        functionNode.setParent(parent);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_functionNameParent() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        functionNode.setParent(parent);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_functionAssignParent() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        functionNode.setParent(parent);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit_thisOnLeftSideOfAssignment() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.visit(t, n, parent);
        // Assert that an error is reported using the compiler object
    }

    @Test
    public void testVisit_thisWithPropertyAccess() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.visit(t, n, parent);
        // Assert that an error is reported using the compiler object
    }

    @Test
    public void testGetFunctionJsDocInfo_jsDocNotNull() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_jsDocParentName() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        n.setParent(parent);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_jsDocParentAssign() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        Node grandParent = new Node(Token.VAR);
        n.setParent(parent);
        parent.setParent(grandParent);
        JSDocInfo jsDoc = new JSDocInfo();
        grandParent.setJSDocInfo(jsDoc);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_nullJsDocParent() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        n.setParent(parent);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(null);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(result);
    }
}