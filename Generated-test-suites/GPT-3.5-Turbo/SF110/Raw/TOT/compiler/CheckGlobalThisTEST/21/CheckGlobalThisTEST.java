import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FuncConstructor_ReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        n.setJSDocInfo(jsDocInfo);
        Node parent = new Node();
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, parent);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FuncThisAnnotation_ReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setThisType(new Node(Token.THIS));
        n.setJSDocInfo(jsDocInfo);
        Node parent = new Node();
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, parent);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithName_ReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.FUNCTION);
        n.setFirstChild(new Node(Token.NAME));
        Node parent = new Node();
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, parent);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionInsideBlock_ReturnTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK, new Node(Token.SCRIPT));
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(null, n, parent);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testVisit_ThisOnLeftSideOfAssign_ShouldReportThis() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal(null);
        
        // Act
        checkGlobalThis.visit(t, n, null);
        
        // Assert
        // Assert report
    }
    
    @Test
    public void testVisit_ThisWithPropertyAccess_ShouldReportThis() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP, n);
        NodeTraversal t = new NodeTraversal(null);
        
        // Act
        checkGlobalThis.visit(t, n, parent);
        
        // Assert
        // Assert report
    }
    
    @Test
    public void testShouldReportThis_ThisOnLeftSideOfAssign_ReturnTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.THIS);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        
        // Act
        boolean result = checkGlobalThis.shouldReportThis(n);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testShouldReportThis_ThisWithPropertyAccess_ReturnTrue() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP, n);
        
        // Act
        boolean result = checkGlobalThis.shouldReportThis(n);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testShouldReportThis_ThisNotOnLeftSideOfAssignOrWithPropertyAccess_ReturnFalse() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        
        // Act
        boolean result = checkGlobalThis.shouldReportThis(n);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJsDocInfo_ReturnJsDocInfo() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.STRING);
        JSDocInfo jsDocInfo = new JSDocInfo();
        n.setParent(new Node(Token.STRING, null, new Node(Token.FUNCTION, n)));
        n.getParent().setJSDocInfo(jsDocInfo);
        
        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        // Assert
        Assert.assertEquals(jsDocInfo, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ParentNodeIsName_ReturnParentJsDocInfo() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.STRING);
        JSDocInfo parentJsDocInfo = new JSDocInfo();
        Node parent = new Node(Token.NAME, n);
        parent.setJSDocInfo(parentJsDocInfo);
        n.setParent(parent);
        
        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        // Assert
        Assert.assertEquals(parentJsDocInfo, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_GrampsIsVar_ReturnGrampsJsDocInfo() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = new Node(Token.STRING);
        JSDocInfo grampsJsDocInfo = new JSDocInfo();
        Node parent = new Node(Token.NAME);
        Node gramps = new Node(Token.VAR, parent);
        gramps.setJSDocInfo(grampsJsDocInfo);
        parent.setParent(gramps);
        n.setParent(parent);
        
        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        // Assert
        Assert.assertEquals(grampsJsDocInfo, result);
    }

}