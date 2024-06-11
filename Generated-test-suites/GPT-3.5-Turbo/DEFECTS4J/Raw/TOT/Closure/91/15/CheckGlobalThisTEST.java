import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setup() {
        checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    }

    @Test
    public void testShouldTraverse_FunctionIsConstructor() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        n.putBooleanProp(Node.CONSTRUCTOR, true);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordConstructor();
        parent.setJSDocInfo(jsDoc);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasThisTypeAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        parent.setJSDocInfo(jsDoc);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        Assert.assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionHasOverrideAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordOverride();
        parent.setJSDocInfo(jsDoc);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionIsNotGlobalContext() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionLeftSideOfAssignment() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        Assert.assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionRightSideOfAssignment_PrototypeProperty() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node lhs = new Node(Token.GETPROP, new Node("prototype"));
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(lhs);
        parent.addChildToBack(n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionRightSideOfAssignment_AssignedToPrototypeProperty() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node lhs = new Node(Token.GETPROP, new Node("prototype"));
        Node llhs = new Node(Token.GETPROP, new Node("this"));
        lhs.addChildToBack(llhs);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(lhs);
        parent.addChildToBack(n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        Assert.assertFalse(result);
    }

    @Test
    public void testVisit_ThisOnLeftSideOfAssignment_ReportsError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);

        DiagnosticType expectedDiagnosticType = CheckGlobalThis.GLOBAL_THIS;

        checkGlobalThis.visit(t, n, parent);

        Assert.assertTrue(t.hasError(parent));
        Assert.assertEquals(expectedDiagnosticType, t.getErrorType(parent));
    }

    @Test
    public void testVisit_ThisWithPropertyAccess_ReportsError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);

        DiagnosticType expectedDiagnosticType = CheckGlobalThis.GLOBAL_THIS;

        checkGlobalThis.visit(t, n, parent);

        Assert.assertTrue(t.hasError(parent));
        Assert.assertEquals(expectedDiagnosticType, t.getErrorType(parent));
    }

    @Test
    public void testVisit_ThisNotOnLeftSideOrWithPropertyAccess_DoesNotReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.FOR);
        parent.addChildToFront(n);

        checkGlobalThis.visit(t, n, parent);

        Assert.assertFalse(t.hasError(parent));
    }

    @Test
    public void testVisit_AssignLhsChildExists_UnsetsAssignLhsChild() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.shouldTraverse(t, n, parent);

        checkGlobalThis.visit(t, n, parent);

        Assert.assertNull(checkGlobalThis.getAssignLhsChild());
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionHasJSDocInfo_ReturnsJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo expectedJSDocInfo = new JSDocInfo();
        n.setJSDocInfo(expectedJSDocInfo);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        Assert.assertEquals(expectedJSDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionDoesNotHaveJSDocInfo_ReturnsParentJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo expectedJSDocInfo = new JSDocInfo();
        parent.setJSDocInfo(expectedJSDocInfo);
        n.setParent(parent);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        Assert.assertEquals(expectedJSDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionDoesNotHaveJSDocInfoAndParentIsAssign_ReturnsGrandparentJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        Node grandparent = new Node(Token.VAR);
        JSDocInfo expectedJSDocInfo = new JSDocInfo();
        grandparent.setJSDocInfo(expectedJSDocInfo);
        parent.setParent(grandparent);
        n.setParent(parent);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        Assert.assertEquals(expectedJSDocInfo, result);
    }
}