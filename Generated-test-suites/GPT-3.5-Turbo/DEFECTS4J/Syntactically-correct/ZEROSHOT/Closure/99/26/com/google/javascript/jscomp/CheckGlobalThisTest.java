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
    public void testShouldTraverse_ValidFunction_ReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_ConstructorFunctionWithThisAnnotation_ReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType(new Node(Token.NAME, "Object"));
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordOverride(true);
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_AssignFunctionToVariable_ReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_AssignFunctionToProperty_ReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_AssignFunctionToPrototype_ReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        n.setString("this");
        Node parent = new Node(Token.GETPROP);
        Node lhs = new Node(Token.NAME);
        Node rhs = new Node(Token.NAME);
        lhs.setString("a");
        rhs.setString("prototype");
        parent.addChildToFront(lhs);
        parent.addChildToBack(rhs);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testVisit_ThisNodeWithValidParent_CallCompilerReport() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.visit(t, n, parent);
        // Add assert statement here
    }

    @Test
    public void testVisit_AssignLhsChildNotNull_SetAssignLhsChildToNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.assignLhsChild = n;
        checkGlobalThis.visit(t, n, parent);
        // Add assert statement here
    }

    @Test
    public void testVisit_AssignLhsChildNull_AssignLhsChildToNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.assignLhsChild = null;
        checkGlobalThis.visit(t, n, parent);
        // Add assert statement here
    }

    @Test
    public void testShouldReportThis_AssignLhsChildNotNull_ReturnTrue() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.assignLhsChild = n;
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ParentIsGetNode_ReturnTrue() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ParentIsNull_ReturnFalse() {
        Node n = new Node(Token.THIS);
        Node parent = null;
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldReportThis_ParentIsOtherNode_ReturnFalse() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertFalse(result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NodeWithJSDocInfo_ReturnJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo expected = new JSDocInfo();
        n.setJSDocInfo(expected);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NodeWithoutJSDocInfoAndParentIsName_ReturnParentJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo expected = new JSDocInfo();
        parent.setJSDocInfo(expected);
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NodeWithoutJSDocInfoAndParentIsAssign_ReturnParentJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo expected = new JSDocInfo();
        parent.setJSDocInfo(expected);
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NodeWithoutJSDocInfoAndParentIsAssignAndGrampsIsVar_ReturnGrampsJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        Node gramps = new Node(Token.VAR);
        JSDocInfo expected = new JSDocInfo();
        gramps.setJSDocInfo(expected);
        parent.setParent(gramps);
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NodeWithoutJSDocInfoAndParentIsOtherNode_ReturnNull() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(result);
    }
}