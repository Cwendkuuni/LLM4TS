import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;

        // When
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION, new Node(Token.NAME, "test"), new Node(Token.BLOCK)), null);

        // Then
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;

        // When
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION, new Node(Token.ASSIGN, new Node(Token.NAME, "test"), new Node(Token.BLOCK))), null);

        // Then
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithContextOtherThanBlock_Script_Name_Assign_ShouldReturnFalse() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;

        // When
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION, new Node(Token.NAME, "test"), new Node(Token.RETURN, new Node(Token.STRING, "SomeValue"))), new Node(Token.RETURN, new Node(Token.STRING, "SomeValue")));

        // Then
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_AssignNodeWithNestedAssignment_LeftSideShouldBeTraversed() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;

        // When
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.ASSIGN, new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.THIS)), new Node(Token.NAME, "b")), null);

        // Then
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_AssignNodeWithNestedAssignment_RightSideNotAssignToPrototypeProperty_ShouldReturnFalse() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;

        // When
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "property"))), null);

        // Then
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_AssignNodeWithNestedAssignment_RightSideAssignToPrototypeProperty_ShouldReturnTrue() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;

        // When
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "prototype"))), null);

        // Then
        assertTrue(result);
    }

    @Test
    public void testVisit_NodeIsThisAndShouldReportThis_ShouldReportError() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        checkGlobalThis.visit(t, new Node(Token.THIS), new Node(Token.GETPROP));

        // Then
        assertEquals(1, compiler.getErrorCount());
    }

    @Test
    public void testVisit_NodeIsThisAndShouldNotReportThis_ShouldNotReportError() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        checkGlobalThis.visit(t, new Node(Token.THIS), new Node(Token.NAME));

        // Then
        assertEquals(0, compiler.getErrorCount());
    }

    @Test
    public void testVisit_NodeIsAssignLhsChild_ShouldSetAssignLhsChildToNull() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        checkGlobalThis.visit(t, new Node(Token.THIS), new Node(Token.GETPROP, new Node(Token.NAME)));

        // Then
        assertNull(checkGlobalThis.getAssignLhsChild());
    }

    @Test
    public void testVisit_NodeIsNotAssignLhsChild_ShouldNotChangeAssignLhsChild() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        Node assignLhsChild = new Node(Token.NAME);

        checkGlobalThis.setAssignLhsChild(assignLhsChild);

        // When
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        checkGlobalThis.visit(t, new Node(Token.THIS), new Node(Token.NAME));

        // Then
        assertEquals(assignLhsChild, checkGlobalThis.getAssignLhsChild());
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJSDocInfo_ShouldReturnJSDocInfo() {
        // Given
        Node n = new Node(Token.FUNCTION);

        JSDocInfo jsDocInfo = new JSDocInfo();
        n.setJSDocInfo(jsDocInfo);

        // When
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Then
        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithNameParentWithJSDocInfo_ShouldReturnJSDocInfo() {
        // Given
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);

        JSDocInfo jsDocInfo = new JSDocInfo();
        parent.setJSDocInfo(jsDocInfo);
        n.setParent(parent);

        // When
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Then
        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithNameAssignParentWithNameParentWithJSDocInfo_ShouldReturnJSDocInfo() {
        // Given
        Node n = new Node(Token.FUNCTION);
        Node assignParent = new Node(Token.ASSIGN);
        Node nameParent = new Node(Token.NAME);

        JSDocInfo jsDocInfo = new JSDocInfo();
        nameParent.setJSDocInfo(jsDocInfo);
        assignParent.setJSDocInfo(null);
        assignParent.setParent(nameParent);
        n.setParent(assignParent);

        // When
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Then
        assertEquals(jsDocInfo, result);
    }
}