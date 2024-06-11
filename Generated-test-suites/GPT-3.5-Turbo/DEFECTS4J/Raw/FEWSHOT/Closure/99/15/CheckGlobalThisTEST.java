package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import org.mockito.Mockito;

public class CheckGlobalThisTest {
    
    private AbstractCompiler mockCompiler = Mockito.mock(AbstractCompiler.class);
    private CheckLevel mockLevel = CheckLevel.WARNING;
    
    @Test
    public void testShouldTraverse_noTraverseFunctionWithConstructorAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.FUNCTION, "");
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        Node parent = null;
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_noTraverseFunctionWithThisAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.FUNCTION, "");
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setThisType("Object");
        n.setJSDocInfo(jsDoc);
        Node parent = null;
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_noTraverseFunctionWithOverrideAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.FUNCTION, "");
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        Node parent = null;
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_noTraverseFunctionWithInvalidParentType() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.FUNCTION, "");
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = Node.newString(Token.SUB, "");
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_traverseNormalFunction() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.FUNCTION, "");
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = Node.newString(Token.SCRIPT, "");
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_traverseNamedFunction() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.FUNCTION, "foo");
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = Node.newString(Token.SCRIPT, "");

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_traverseFunctionAssignment() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.ASSIGN, "");
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = Node.newString(Token.NAME, "");

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_noTraverseAssignmentToPrototypeProperty() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.NAME, "a.prototype.property");
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = Node.newString(Token.ASSIGN, "a.prototype.property = c");

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_noTraverseAssignmentToPrototypeSubproperty() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.NAME, "a.prototype.subproperty.property");
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = Node.newString(Token.ASSIGN, "a.prototype.subproperty.property = c");

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }
    
    @Test
    public void testVisit_reportGlobalThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.THIS, "this");
        Node parent = Node.newString(Token.GETPROP, "");
        parent.addChildToBack(n);
        
        checkGlobalThis.visit(t, n, parent);
        
        Mockito.verify(mockCompiler).report(Mockito.any());
    }
    
    @Test
    public void testVisit_noReportGlobalThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(mockCompiler, mockLevel);
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Node.newString(Token.THIS, "this");
        Node parent = Node.newString(Token.ASSIGN, "");
        parent.addChildToBack(n);

        checkGlobalThis.visit(t, n, parent);

        Mockito.verify(mockCompiler, Mockito.never()).report(Mockito.any());
    }
}