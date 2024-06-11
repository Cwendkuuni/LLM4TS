package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse_globalContext_returnTrue() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        boolean result = callback.shouldTraverse(mock(NodeTraversal.class), mock(Node.class), null);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_constructorFunction_returnFalse() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        Node n = mock(Node.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(jsDoc.isConstructor()).thenReturn(true);
        when(callback.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        Node parent = mock(Node.class);
        boolean result = callback.shouldTraverse(mock(NodeTraversal.class), n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_annotatedFunction_returnFalse() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        Node n = mock(Node.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(jsDoc.hasThisType()).thenReturn(true);
        when(callback.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        Node parent = mock(Node.class);
        boolean result = callback.shouldTraverse(mock(NodeTraversal.class), n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_nestedFunctionInBlock_returnFalse() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        Node n = mock(Node.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(callback.getFunctionJsDocInfo(n)).thenReturn(null);
        Node parent = mock(Node.class);
        when(parent.getType()).thenReturn(Token.BLOCK);
        boolean result = callback.shouldTraverse(mock(NodeTraversal.class), n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_nestedFunctionInScript_returnFalse() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        Node n = mock(Node.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(callback.getFunctionJsDocInfo(n)).thenReturn(null);
        Node parent = mock(Node.class);
        when(parent.getType()).thenReturn(Token.SCRIPT);
        boolean result = callback.shouldTraverse(mock(NodeTraversal.class), n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_nestedFunctionInName_returnFalse() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        Node n = mock(Node.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(callback.getFunctionJsDocInfo(n)).thenReturn(null);
        Node parent = mock(Node.class);
        when(parent.getType()).thenReturn(Token.NAME);
        boolean result = callback.shouldTraverse(mock(NodeTraversal.class), n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_nestedFunctionInAssign_returnFalse() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        Node n = mock(Node.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(callback.getFunctionJsDocInfo(n)).thenReturn(null);
        Node parent = mock(Node.class);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        boolean result = callback.shouldTraverse(mock(NodeTraversal.class), n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testVisit_thisKeyword_reportError() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        Node n = mock(Node.class);
        when(n.getType()).thenReturn(Token.THIS);
        NodeTraversal traversal = mock(NodeTraversal.class);
        when(traversal.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS)).thenReturn(mock(ErrorReport.class));
        callback.visit(traversal, n, null);
        
        verify(traversal, times(1)).makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS);
    }
    
    @Test
    public void testVisit_thisKeyword_onLeftSideAssign_reportError() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        Node n = mock(Node.class);
        when(n.getType()).thenReturn(Token.THIS);
        Node parent = mock(Node.class);
        NodeTraversal traversal = mock(NodeTraversal.class);
        callback.shouldReportThis(n, parent);
        when(traversal.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS)).thenReturn(mock(ErrorReport.class));
        callback.visit(traversal, n, parent);
        
        verify(traversal, times(1)).makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS);
    }
    
    @Test
    public void testVisit_thisKeyword_noReport() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        Node n = mock(Node.class);
        when(n.getType()).thenReturn(Token.THIS);
        NodeTraversal traversal = mock(NodeTraversal.class);
        callback.shouldReportThis(n, null);
        callback.visit(traversal, n, null);
        
        verify(traversal, never()).makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_returnJSDocInfo() {
        Callback callback = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
        Node n = mock(Node.class);
        when(n.getJSDocInfo()).thenReturn(mock(JSDocInfo.class));
        Node parent = mock(Node.class);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getJSDocInfo()).thenReturn(mock(JSDocInfo.class));
        Node gramps = mock(Node.class);
        when(gramps.getType()).thenReturn(Token.VAR);
        when(gramps.getJSDocInfo()).thenReturn(mock(JSDocInfo.class));
        when(parent.getParent()).thenReturn(gramps);
        JSDocInfo result = callback.getFunctionJsDocInfo(n);
        
        assertTrue(result != null);
    }
}