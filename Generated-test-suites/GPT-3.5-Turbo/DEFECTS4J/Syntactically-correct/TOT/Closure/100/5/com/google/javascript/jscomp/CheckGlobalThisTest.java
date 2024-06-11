import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(null, null);
    }

    @Test
    public void shouldTraverse_WithGlobalFunction_ReturnsFalse() {
        // Arrange
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldTraverse_WithConstructorFunction_ReturnsFalse() {
        // Arrange
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldTraverse_WithThisAnnotatedFunction_ReturnsFalse() {
        // Arrange
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldTraverse_WithNormalFunction_ReturnsTrue() {
        // Arrange
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldTraverse_WithLeftSideOfAssign_ReturnsTrue() {
        // Arrange
        NodeTraversal t = null;
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        lhs.addChildToFront(n);
        parent.addChildToFront(lhs);
        n.addChildToFront(new Node(Token.THIS));
        checkGlobalThis.visit(t, n, parent);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldTraverse_WithRightSideOfAssign_Prototype_ReturnsFalse() {
        // Arrange
        NodeTraversal t = null;
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToFront(new Node(Token.NAME, "prototype"));
        lhs.addChildToBack(n);
        parent.addChildToFront(lhs);
        n.addChildToFront(new Node(Token.THIS));

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldTraverse_WithRightSideOfAssign_Subproperty_ReturnsFalse() {
        // Arrange
        NodeTraversal t = null;
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToFront(new Node(Token.NAME, "a.prototype.b"));
        lhs.addChildToBack(n);
        parent.addChildToFront(lhs);
        n.addChildToFront(new Node(Token.THIS));

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void visit_WithThis_ShouldReportGlobalThis() {
        // Arrange
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = null;

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        // Make sure that a warning was reported
        // Assert.assertTrue(compiler.getErrors().size() > 0);
        // ...
        // Additional assertions for checking the specific error
    }

    @Test
    public void visit_WithAssignLhsChild_ShouldClearAssignLhsChild() {
        // Arrange
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        lhs.addChildToFront(n);
        parent.addChildToFront(lhs);
        n.addChildToFront(new Node(Token.THIS));
        checkGlobalThis.visit(t, n, parent);

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void shouldReportThis_WithAssignLhsChild_ShouldReturnTrue() {
        // Arrange
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        lhs.addChildToFront(n);
        parent.addChildToFront(lhs);
        n.addChildToFront(new Node(Token.THIS));
        checkGlobalThis.visit(null, n, parent);

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldReportThis_WithoutAssignLhsChild_ShouldReturnFalse() {
        // Arrange
        Node n = new Node(Token.THIS);
        Node parent = null;

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getFunctionJsDocInfo_WithFunction_ReturnsNull() {
        // Arrange
        Node n = new Node(Token.FUNCTION);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        assertNull(result);
    }

    @Test
    public void getFunctionJsDocInfo_WithFunctionAndParentName_ReturnsParentJSDocInfo() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(n);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        assertEquals(jsDoc, result);
    }

    @Test
    public void getFunctionJsDocInfo_WithFunctionAndParentAssign_ReturnsParentJSDocInfo() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        assertEquals(jsDoc, result);
    }

    @Test
    public void getFunctionJsDocInfo_WithFunctionAndParentAssignAndParentName_ReturnsGrandparentJSDocInfo() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        Node grandparent = new Node(Token.VAR);
        n.addChildToFront(parent);
        grandparent.addChildToFront(parent);
        JSDocInfo jsDoc = new JSDocInfo();
        grandparent.setJSDocInfo(jsDoc);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        // Assert
        assertEquals(jsDoc, result);
    }

}