import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        n.setJSDocInfo(jsDocInfo);
        Node parent = null;
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        n.setJSDocInfo(jsDocInfo);
        Node parent = null;
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithNoAnnotation_ShouldReturnTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = null;
        n.setJSDocInfo(jsDocInfo);
        Node parent = null;
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_AssignLhsChildIsNull_ShouldReturnTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node();
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node();
        Node rhs = new Node();
        parent.addChildToFront(lhs);
        lhs.addChildToFront(rhs);
        parent.setFirstChild(lhs);
        lhs.setNext(rhs);
        checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_AssignLhsChildNotNull_ShouldReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node();
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node();
        Node rhs = new Node();
        Node assignLhsChild = new Node();
        parent.addChildToFront(lhs);
        lhs.addChildToFront(rhs);
        parent.setFirstChild(lhs);
        lhs.setNext(rhs);
        checkGlobalThis.shouldTraverse(t, n, parent);
        checkGlobalThis.visit(t, n, parent);
        checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testVisit_NodeIsThisAndShouldReportThis_ShouldReportDiagnostic() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        Node assignLhsChild = new Node();
        checkGlobalThis.visit(t, n, parent);
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        // TODO: Assert that a diagnostic is reported
    }
    
    @Test
    public void testVisit_NodeIsAssignLhsChild_ShouldSetAssignLhsChildToNull() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node();
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node();
        Node rhs = new Node();
        Node assignLhsChild = new Node();
        parent.addChildToFront(lhs);
        lhs.addChildToFront(rhs);
        parent.setFirstChild(lhs);
        lhs.setNext(rhs);
        checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Act
        checkGlobalThis.visit(t, assignLhsChild, parent);
        // TODO: Call a getter method to get the assignLhsChild value
        
        // Assert
        assertNull(checkGlobalThis.getAssignLhsChild());
    }
}