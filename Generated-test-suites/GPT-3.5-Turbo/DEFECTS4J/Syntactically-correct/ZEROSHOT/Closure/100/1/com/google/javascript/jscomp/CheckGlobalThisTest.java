import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_NonGlobalContext_ReturnsFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, null, null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_ReturnsFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        n.setJSDocInfo(jsDocInfo);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisTypeAnnotation_ReturnsFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        n.setJSDocInfo(jsDocInfo);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithoutThisAnnotation_ReturnsTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        n.setJSDocInfo(jsDocInfo);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, null);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_LeftSideOfAssignment_ReturnsTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(n);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignment_PrototypeProperty_ReturnsFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(n);
        parent.addChildToBack(new Node(Token.STRING, "prototype"));
        Node grandparent = new Node(Token.ASSIGN);
        grandparent.addChildToBack(parent);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, grandparent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignment_PrototypeSubproperty_ReturnsFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(new Node(Token.NAME, "a"));
        parent.addChildToBack(new Node(Token.STRING, "prototype"));
        parent.addChildToBack(new Node(Token.STRING, "b"));
        Node grandparent = new Node(Token.ASSIGN);
        grandparent.addChildToBack(parent);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, grandparent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignment_NotPrototype_ReturnsTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(new Node(Token.STRING, "a"));
        Node grandparent = new Node(Token.ASSIGN);
        grandparent.addChildToBack(parent);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, grandparent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testVisit_ThisNode_ShouldReportError() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal(checkGlobalThis, null);
        DiagnosticType expectedError = CheckGlobalThis.GLOBAL_THIS;

        // Act
        checkGlobalThis.visit(t, n, null);
        ErrorReporter errorReporter = t.getErrorReporter();
        boolean result = errorReporter.hasErrors();
        List<JSError> errors = errorReporter.getErrors();

        // Assert
        assertTrue(result);
        assertEquals(1, errors.size());
        assertEquals(expectedError, errors.get(0).getType());
    }

    @Test
    public void testVisit_AssignLhsChild_ShouldResetAssignLhsChildToNull() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.THIS);
        Node assignLhsChild = n;
        NodeTraversal t = new NodeTraversal(checkGlobalThis, null);
        checkGlobalThis.setAssignLhsChild(assignLhsChild);

        // Act
        checkGlobalThis.visit(t, n, null);
        Node result = checkGlobalThis.getAssignLhsChild();

        // Assert
        assertNull(result);
    }

    @Test
    public void testVisit_NotAssignLhsChild_ShouldNotResetAssignLhsChild() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.THIS);
        Node assignLhsChild = new Node(Token.NAME);
        NodeTraversal t = new NodeTraversal(checkGlobalThis, null);
        checkGlobalThis.setAssignLhsChild(assignLhsChild);

        // Act
        checkGlobalThis.visit(t, n, null);
        Node result = checkGlobalThis.getAssignLhsChild();

        // Assert
        assertEquals(assignLhsChild, result);
    }

    @Test
    public void testShouldReportThis_AssignLhsChild_ShouldReturnTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.setAssignLhsChild(n);

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_NotAssignLhsChild_ShouldReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node assignLhsChild = new Node(Token.NAME);
        checkGlobalThis.setAssignLhsChild(assignLhsChild);

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetFunctionJsDocInfo_HasJsDocInfo_ShouldReturnJsDocInfo() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo expectedJsDocInfo = new JSDocInfo();
        n.setJSDocInfo(expectedJsDocInfo);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        assertEquals(expectedJsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsName_HasJsDocInfo_ShouldReturnJsDocInfo() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo expectedJsDocInfo = new JSDocInfo();
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(expectedJsDocInfo);
        n.setParent(parent);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        assertEquals(expectedJsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsAssign_HasJsDocInfo_ShouldReturnJsDocInfo() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo expectedJsDocInfo = new JSDocInfo();
        Node parent = new Node(Token.ASSIGN);
        parent.setJSDocInfo(expectedJsDocInfo);
        n.setParent(parent);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        assertEquals(expectedJsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsAssign_HasNoJsDocInfo_ShouldReturnJsDocInfoFromGrandparentVar() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo expectedJsDocInfo = new JSDocInfo();
        Node grandparent = new Node(Token.VAR);
        grandparent.setJSDocInfo(expectedJsDocInfo);
        Node parent = new Node(Token.ASSIGN);
        parent.setParent(grandparent);
        n.setParent(parent);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        assertEquals(expectedJsDocInfo, result);
    }
}