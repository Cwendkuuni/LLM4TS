import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    }
    
    @Test
    public void testShouldTraverse_FunctionWithConstructorJsDoc_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        
        n.setJSDocInfo(jsDoc);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithInterfaceJsDoc_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setInterface(true);
        
        n.setJSDocInfo(jsDoc);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithThisTypeJsDoc_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        
        n.setJSDocInfo(jsDoc);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionWithOverrideJsDoc_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        
        n.setJSDocInfo(jsDoc);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionInsideBlock_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        
        Node parent = new Node(Token.BLOCK);
        
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionInsideScript_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        
        Node parent = new Node(Token.SCRIPT);
        
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionInsideAssignment_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        
        Node parent = new Node(Token.ASSIGN);
        
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionNotInsideValidBlock_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        
        Node parent = new Node(Token.NAME);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionNotInsideValidScript_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        
        Node parent = new Node(Token.NAME);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionNotInsideValidAssignment_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        
        Node parent = new Node(Token.NAME);
        
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_ParentIsNull_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testVisit_NodeIsThisButShouldNotReport_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        
        assertFalse(checkGlobalThis.shouldReportThis(n, parent));
    }
    
    @Test
    public void testVisit_NodeIsThisAndShouldReport_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        
        assertTrue(checkGlobalThis.shouldReportThis(n, parent));
    }
    
    @Test
    public void testVisit_NodeIsAssignLhsChild_ResetsAssignLhsChild() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        
        checkGlobalThis.visit(t, n, parent);
        
        assertNull(checkGlobalThis.assignLhsChild);
    }
    
    @Test
    public void testVisit_NodeIsNotAssignLhsChild_DoesNotResetAssignLhsChild() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.BLOCK);
        
        checkGlobalThis.visit(t, n, parent);
        
        assertNotNull(checkGlobalThis.assignLhsChild);
    }
    
    @Test
    public void testVisit_NodeIsThisWithProperContext_AddsWarningToReport() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        
        checkGlobalThis.visit(t, n, parent);
        
        assertTrue(compiler.reports(n).contains(CheckGlobalThis.GLOBAL_THIS));
    }
    
    @Test
    public void testGetFunctionJsDocInfo_FunctionHasJsDoc_ReturnsJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        
        n.setJSDocInfo(jsDoc);
        
        assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ParentIsNameWithJsDoc_ReturnsJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(jsDoc);
        
        n.setParent(parent);
        
        assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ParentIsNameWithVarDeclarationWithJsDoc_ReturnsJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        
        Node parent = new Node(Token.NAME);
        
        Node varDeclaration = new Node(Token.VAR);
        varDeclaration.setJSDocInfo(jsDoc);
        
        parent.setParent(varDeclaration);
        n.setParent(parent);
        
        assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
    }
    
    @Test
    public void testGetFunctionJsDocInfo_NoJsDoc_ReturnsNull() {
        Node n = new Node(Token.FUNCTION);
        
        assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
    }

}