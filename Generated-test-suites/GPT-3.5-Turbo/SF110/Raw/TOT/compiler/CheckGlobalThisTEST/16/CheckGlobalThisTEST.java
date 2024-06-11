import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_NonGlobalContext() {
        // Test when the node is not a global context
        // The shouldTraverse method should return false
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation() {
        // Test when the function has a constructor annotation
        // The shouldTraverse method should return false
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        n.setJSDocInfo(jsDocInfo);
        Node parent = new Node();
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisTypeAnnotation() {
        // Test when the function has a this type annotation
        // The shouldTraverse method should return false
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setThisType(new JSTypeExpression(new Node(Token.LC, "object"), "object"));
        n.setJSDocInfo(jsDocInfo);
        Node parent = new Node();
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation() {
        // Test when the function has an override annotation
        // The shouldTraverse method should return false
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        n.setJSDocInfo(jsDocInfo);
        Node parent = new Node();
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionNotInBlockOrScript() {
        // Test when the function is not in a block or script
        // The shouldTraverse method should return false
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        Node parent = new Node();
        parent.setType(Token.NAME);
        Node gramps = new Node();
        gramps.setType(Token.VAR);
        parent.setParent(gramps);
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionLentToPrototype() {
        // Test when the function is getting lent to a prototype
        // The shouldTraverse method should return false
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        Node parent = new Node(Token.GETPROP);
        parent.putBooleanProp(NodeUtil.IS_OBJECT_LIT_PROPERTY, true);
        Node gramps = new Node();
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setLendsName("Object.prototype");
        gramps.setJSDocInfo(jsDocInfo);
        parent.setParent(gramps);
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionOnTheLeftSideOfAssignment() {
        // Test when the function is on the left side of an assignment
        // The shouldTraverse method should return true
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(n);
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(shouldTraverse);
    }

    @Test
    public void testShouldTraverse_FunctionWithPropertyAccess() {
        // Test when the function has a property access
        // The shouldTraverse method should return true
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(n);
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(shouldTraverse);
    }

    @Test
    public void testVisit_ThisNodeReported() {
        // Test for the visit method when 'this' node should be reported
        // The visit method should call compiler.report()
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        checkGlobalThis.assignLhsChild = null;
        AbstractCompiler compiler = new AbstractCompiler();
        boolean compilerReportCalled = false;
        compiler.report = message -> compilerReportCalled = true;
        checkGlobalThis.compiler = compiler;
        checkGlobalThis.visit(t, n, parent);
        assertTrue(compilerReportCalled);
    }

    @Test
    public void testVisit_AssignLhsChildReset() {
        // Test for the visit method when assignLhsChild should be reset
        // The visit method should reset assignLhsChild to null
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        checkGlobalThis.assignLhsChild = n;
        checkGlobalThis.visit(t, n, parent);
        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJSDocInfo() {
        // Test for the getFunctionJsDocInfo method when there is no JSDocInfo
        // The getFunctionJsDocInfo method should return null
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node();
        Node parent = new Node();
        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(jsDocInfo);
    }

    @Test
    public void testGetFunctionJsDocInfo_HasJSDocInfo() {
        // Test for the getFunctionJsDocInfo method when there is JSDocInfo
        // The getFunctionJsDocInfo method should return the JSDocInfo
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node();
        n.setJSDocInfo(new JSDocInfo());
        Node parent = new Node();
        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNotNull(jsDocInfo);
    }
}