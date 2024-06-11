import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo().recordConstructor();
        node.setJSDocInfo(jsDoc);
        NodeTraversal t = new NodeTraversal();
        // Act
        boolean result = shouldTraverse(t, node, null);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        node.setJSDocInfo(jsDoc);
        NodeTraversal t = new NodeTraversal();
        // Act
        boolean result = shouldTraverse(t, node, null);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionAssignmentToPrototypeProperty_ShouldReturnFalse() {
        // Arrange
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        lhs.addChildToBack(new Node(Token.STRING, "property"));
        parent.addChildToBack(lhs);
        Node lhsChild = new Node(Token.NAME, "this");
        parent.addChildToBack(lhsChild);
        NodeTraversal t = new NodeTraversal();
        // Act
        boolean result = shouldTraverse(t, lhsChild, parent);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionAssignmentToSubpropertyOfPrototype_ShouldReturnFalse() {
        // Arrange
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "this"));
        lhs.addChildToBack(new Node(Token.STRING, "prototype.subproperty"));
        parent.addChildToBack(lhs);
        Node lhsChild = new Node(Token.NAME, "this");
        parent.addChildToBack(lhsChild);
        NodeTraversal t = new NodeTraversal();
        // Act
        boolean result = shouldTraverse(t, lhsChild, parent);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionAssignmentToNonPrototypeProperty_ShouldReturnTrue() {
        // Arrange
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "property"));
        parent.addChildToBack(lhs);
        Node lhsChild = new Node(Token.NAME, "this");
        parent.addChildToBack(lhsChild);
        NodeTraversal t = new NodeTraversal();
        // Act
        boolean result = shouldTraverse(t, lhsChild, parent);
        // Assert
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionAssignmentToVariable_ShouldReturnTrue() {
        // Arrange
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME, "a");
        parent.addChildToBack(lhs);
        Node rhs = new Node(Token.NAME, "this");
        parent.addChildToBack(rhs);
        Node lhsChild = new Node(Token.NAME, "this");
        parent.addChildToBack(lhsChild);
        NodeTraversal t = new NodeTraversal();
        // Act
        boolean result = shouldTraverse(t, lhsChild, parent);
        // Assert
        assertTrue(result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJSDocInfo_ShouldReturnJSDocInfo() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        node.setJSDocInfo(jsDoc);
        // Act
        JSDocInfo result = getFunctionJsDocInfo(node);
        // Assert
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithNameParentWithJSDocInfo_ShouldReturnParentJSDocInfo() {
        // Arrange
        Node parent = new Node(Token.NAME);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToBack(node);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        // Act
        JSDocInfo result = getFunctionJsDocInfo(node);
        // Assert
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithAssignParentWithJSDocInfo_ShouldReturnParentJSDocInfo() {
        // Arrange
        Node parent = new Node(Token.ASSIGN);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToBack(node);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        // Act
        JSDocInfo result = getFunctionJsDocInfo(node);
        // Assert
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithNameParentInsideVarWithJSDocInfo_ShouldReturnGrandparentJSDocInfo() {
        // Arrange
        Node grandparent = new Node(Token.VAR);
        Node parent = new Node(Token.NAME);
        Node node = new Node(Token.FUNCTION);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(node);
        JSDocInfo jsDoc = new JSDocInfo();
        grandparent.setJSDocInfo(jsDoc);
        // Act
        JSDocInfo result = getFunctionJsDocInfo(node);
        // Assert
        assertEquals(jsDoc, result);
    }
}