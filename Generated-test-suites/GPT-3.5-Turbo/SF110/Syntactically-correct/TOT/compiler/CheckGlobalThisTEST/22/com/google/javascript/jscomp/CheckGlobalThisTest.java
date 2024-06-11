import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_ReturnsTrueForGlobalContext() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node n = new Node(Token.SCRIPT);
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_ReturnsFalseForConstructorFunction() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        n.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ReturnsFalseForFunctionWithThisAnnotation() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        n.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ReturnsFalseForFunctionWithOverrideAnnotation() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        n.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ReturnsFalseForFunctionWithoutPlaceForThisAnnotation() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        Node grandparent = new Node(Token.SCRIPT);
        grandparent.addChildToFront(n);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, grandparent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ReturnsFalseForFunctionLentToPrototype() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node functionNode = new Node(Token.FUNCTION);
        Node propNode = new Node(Token.GETPROP);
        functionNode.addChildToFront(propNode);
        Node parent = null;
        Node grandparent = new Node(Token.SCRIPT);
        grandparent.addChildToFront(functionNode);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setLendsName("prototype");
        grandparent.setJSDocInfo(jsDocInfo);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, propNode, grandparent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ReturnsTrueForLeftSideOfAssignment() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        Node grandparent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        grandparent.addChildToFront(parent);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_ReturnsFalseForAssignmentToPrototypeProperty() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node lhsChild1 = new Node(Token.NAME);
        Node lhsChild2 = new Node(Token.GETPROP);
        lhsChild2.addChildToFront(lhsChild1);
        lhsChild2.addChildToBack(new Node(Token.STRING, "prototype"));
        Node parent = new Node(Token.ASSIGN);
        Node grandparent = new Node(Token.SCRIPT);
        parent.addChildToBack(lhsChild2);
        grandparent.addChildToFront(parent);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, lhsChild1, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ReturnsFalseForAssignmentToNestedPrototypeProperty() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node lhsChild1 = new Node(Token.NAME);
        Node lhsChild2 = new Node(Token.GETPROP);
        lhsChild2.addChildToFront(new Node(Token.NAME, "a"));
        lhsChild2.addChildToBack(lhsChild1);
        Node parent = new Node(Token.ASSIGN);
        Node grandparent = new Node(Token.SCRIPT);
        parent.addChildToBack(lhsChild2);
        grandparent.addChildToFront(parent);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, lhsChild1, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldReportThis_ReturnsTrueForThisOnLeftSideOfAssignment() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        parent.addChildToBack(n);

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ReturnsTrueForThisWithPropertyAccess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ReturnsFalseForThisWithoutPropertyAccess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        parent.addChildToBack(n);

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertFalse(result);
    }

    @Test
    public void testVisit_NoReportedErrorForValidThisUsage() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        parent.addChildToBack(n);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        // No error should be reported
        // assert statements can be added here to verify that an error was not reported
    }

    @Test
    public void testVisit_ErrorReportedForGlobalThisUsage() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        // Error should be reported
        // assert statements can be added here to verify that an error was reported
    }

    @Test
    public void testVisit_AssignLhsChildResetAfterVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node lhsChild = new Node(Token.NAME);
        checkGlobalThis.assignLhsChild = lhsChild;

        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_ReturnsJsDocInfoForFunction() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        n.setJSDocInfo(jsDocInfo);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ReturnsJsDocInfoForFunctionAssignedToVariable() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(functionNode);
        JSDocInfo jsDocInfo = new JSDocInfo();
        parent.setJSDocInfo(jsDocInfo);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ReturnsJsDocInfoForVariableAssignedToFunction() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

        Node variableNode = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(variableNode);
        JSDocInfo jsDocInfo = new JSDocInfo();
        parent.setJSDocInfo(jsDocInfo);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(variableNode);

        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ReturnsJsDocInfoForVariableAssignedToFunctionInVarDeclaration() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

        Node variableNode = new Node(Token.NAME);
        Node declarationNode = new Node(Token.VAR);
        declarationNode.addChildToBack(variableNode);
        JSDocInfo jsDocInfo = new JSDocInfo();
        declarationNode.setJSDocInfo(jsDocInfo);
        Node grandparent = new Node(Token.SCRIPT);
        grandparent.addChildToFront(declarationNode);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(variableNode);

        assertEquals(jsDocInfo, result);
    }
}