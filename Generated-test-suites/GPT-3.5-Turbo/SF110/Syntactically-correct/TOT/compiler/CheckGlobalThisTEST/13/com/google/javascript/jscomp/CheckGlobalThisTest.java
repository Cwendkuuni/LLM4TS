import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionConstructor_ShouldReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType(new String[] { "string" });
        n.putProp(Node.JSDOC_INFO_PROP, jsDocInfo);
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithoutLogicalPlaceForThisAnnotation_ShouldReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NUMBER);
        parent.addChildToFront(n);
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionInsideObjectLiteral_ShouldReturnTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.OBJECTLIT);
        parent.addChildToFront(n);
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionInsideObjectLiteralAssignedToPrototype_ShouldReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.STRING_KEY);
        Node parent = new Node(Token.NAME);
        parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
        parent.addChildToFront(n);
        Node gramps = new Node(Token.NAME);
        gramps.addChildToFront(parent);
        gramps.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
        Node greatGramps = new Node(Token.NAME);
        greatGramps.addChildToFront(gramps);
        greatGramps.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testShouldReportThis_ThisOnLeftSideOfAssignment_ShouldReturnTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.THIS);
        Node assignLhsChild = new Node(Token.NUMBER);
        checkGlobalThis.assignLhsChild = assignLhsChild;
        
        // Act
        boolean result = checkGlobalThis.shouldReportThis(n);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testShouldReportThis_ThisWithPropertyAccess_ShouldReturnTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
        parent.addChildToFront(n);
        
        // Act
        boolean result = checkGlobalThis.shouldReportThis(n);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testShouldReportThis_ThisWithNoPropertyAccess_ShouldReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NUMBER);
        parent.addChildToFront(n);
        
        // Act
        boolean result = checkGlobalThis.shouldReportThis(n);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJsDocInfo_ShouldReturnJsDocInfo() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo expectedJsDocInfo = new JSDocInfo();
        n.putProp(Node.JSDOC_INFO_PROP, expectedJsDocInfo);
        
        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        // Assert
        assertEquals(expectedJsDocInfo, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_FunctionNameWithJsDocInfo_ShouldReturnJsDocInfo() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.NAME);
        JSDocInfo expectedJsDocInfo = new JSDocInfo();
        n.putProp(Node.JSDOC_INFO_PROP, expectedJsDocInfo);
        
        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        // Assert
        assertEquals(expectedJsDocInfo, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_FunctionAssignedToVarWithJsDocInfo_ShouldReturnJsDocInfo() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.putProp(Node.JSDOC_INFO_PROP, null);
        parent.putProp(Node.JSDOC_INFO_PROP, null);
        parent.addChildToFront(functionNode);
        JSDocInfo expectedJsDocInfo = new JSDocInfo();
        parent.putProp(Node.JSDOC_INFO_PROP, expectedJsDocInfo);
        
        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        
        // Assert
        assertEquals(expectedJsDocInfo, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_FunctionAssignedToVarWithoutJsDocInfo_ShouldReturnNull() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.putProp(Node.JSDOC_INFO_PROP, null);
        parent.putProp(Node.JSDOC_INFO_PROP, null);
        parent.addChildToFront(functionNode);
        
        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        
        // Assert
        assertNull(result);
    }
}