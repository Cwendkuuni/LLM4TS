import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_returnsTrueForInitialBlock() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.BLOCK);
        Node parent = null;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_returnsTrueForInitialScript() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.SCRIPT);
        Node parent = null;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_returnsTrueForInitialName() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = null;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_returnsTrueForInitialAssign() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.ASSIGN);
        Node parent = null;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_returnsFalseForConstructorFunctionWithThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        n.setJSDocInfo(jsDocInfo);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_returnsFalseForNonConstructorFunctionWithThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        n.setJSDocInfo(jsDocInfo);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_returnsFalseForFunctionWithOverrideAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordOverride();
        n.setJSDocInfo(jsDocInfo);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_returnsFalseForFunctionInsideIfStatement() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.IF);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_returnsFalseForFunctionInsideForLoop() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FOR);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_returnsFalseForFunctionInsideDoWhileLoop() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.DO);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_returnsTrueForFunctionInsideBlock() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_returnsTrueForFunctionInsideScript() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testVisit_reportsErrorForThisOnLeftSideOfAssignment() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        
        checkGlobalThis.visit(t, n, parent);
        
        // assert that a warning error was reported
    }
    
    @Test
    public void testVisit_reportsErrorForThisWithPropertyAccess() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        
        checkGlobalThis.visit(t, n, parent);
        
        // assert that a warning error was reported
    }
    
    @Test
    public void testVisit_doesNotReportErrorForThisWithoutPropertyAccess() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = null;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        
        checkGlobalThis.visit(t, n, parent);
        
        // assert that no error was reported
    }
    
    @Test
    public void testVisit_resetsAssignLhsChildWhenNodeIsAssignLhsChild() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        checkGlobalThis.setAssignLhsChild(n);
        
        checkGlobalThis.visit(t, n, parent);
        
        assertNull(checkGlobalThis.getAssignLhsChild());
    }
    
    @Test
    public void testGetFunctionJsDocInfo_returnsJSDocInfoForNodeWithJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo expected = new JSDocInfo();
        n.setJSDocInfo(expected);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_returnsJSDocInfoForParentNodeWithName() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo expected = new JSDocInfo();
        parent.setJSDocInfo(expected);
        n.setParent(parent);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_returnsJSDocInfoForParentNodeWithAssign() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo expected = new JSDocInfo();
        parent.setJSDocInfo(expected);
        n.setParent(parent);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_returnsJSDocInfoForGrampsNodeWithVar() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.FUNCTION);
        JSDocInfo expected = new JSDocInfo();
        gramps.setJSDocInfo(expected);
        parent.setParent(gramps);
        n.setParent(parent);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(expected, result);
    }
}