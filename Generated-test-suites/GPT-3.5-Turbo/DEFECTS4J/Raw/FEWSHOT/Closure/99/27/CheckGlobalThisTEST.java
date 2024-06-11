package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        
        // Test function with @this annotation, should not traverse
        Node functionNode1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        jsDoc1.recordThisType();
        functionNode1.setJSDocInfo(jsDoc1);
        assertEquals(false, checkGlobalThis.shouldTraverse(t, functionNode1, null));
        
        // Test function with @constructor annotation, should not traverse
        Node functionNode2 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc2 = new JSDocInfo();
        jsDoc2.recordConstructor();
        functionNode2.setJSDocInfo(jsDoc2);
        assertEquals(false, checkGlobalThis.shouldTraverse(t, functionNode2, null));
        
        // Test function with @override annotation, should not traverse
        Node functionNode3 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc3 = new JSDocInfo();
        jsDoc3.recordOverride();
        functionNode3.setJSDocInfo(jsDoc3);
        assertEquals(false, checkGlobalThis.shouldTraverse(t, functionNode3, null));
        
        // Test function with valid parent nodes, should traverse
        Node functionNode4 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc4 = null;
        functionNode4.setJSDocInfo(jsDoc4);
        
        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToFront(functionNode4);
        assertEquals(true, checkGlobalThis.shouldTraverse(t, functionNode4, blockNode));
        
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(functionNode4);
        assertEquals(true, checkGlobalThis.shouldTraverse(t, functionNode4, scriptNode));
        
        Node nameNode = new Node(Token.NAME);
        nameNode.addChildToFront(functionNode4);
        assertEquals(true, checkGlobalThis.shouldTraverse(t, functionNode4, nameNode));
        
        Node assignNode = new Node(Token.ASSIGN);
        assignNode.addChildToFront(functionNode4);
        assertEquals(true, checkGlobalThis.shouldTraverse(t, functionNode4, assignNode));
    }
    
    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

        // Test THIS node that should be reported
        Node thisNode1 = new Node(Token.THIS);
        Node parent1 = new Node(Token.ASSIGN);
        assertEquals(true, checkGlobalThis.shouldReportThis(thisNode1, parent1));
        // Test THIS node that should not be reported
        Node thisNode2 = new Node(Token.THIS);
        Node parent2 = new Node(Token.GETPROP);
        assertEquals(false, checkGlobalThis.shouldReportThis(thisNode2, parent2));
        
        // Test reporting
        Node thisNode3 = new Node(Token.THIS);
        Node parent3 = new Node(Token.ASSIGN);
        checkGlobalThis.visit(t, thisNode3, parent3);
        DiagnosticType expectedType = CheckGlobalThis.GLOBAL_THIS;
        Diagnostic actual = t.getErrorReporter().getErrors().get(0);
        assertEquals(expectedType, t.getErrorReporter().getErrors().get(0).getType());
    }
    
    @Test
    public void testGetFunctionJsDocInfo() {
        // Test function with JSDoc, expect JSDocInfo
        Node functionNode1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        functionNode1.setJSDocInfo(jsDoc1);
        CheckGlobalThis checkGlobalThis1 = new CheckGlobalThis(null, null);
        assertEquals(jsDoc1, checkGlobalThis1.getFunctionJsDocInfo(functionNode1));
        
        // Test function without JSDoc, expect parent's JSDocInfo
        Node functionNode2 = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.NAME);
        JSDocInfo jsDoc2 = new JSDocInfo();
        parentNode.setJSDocInfo(jsDoc2);
        functionNode2.setParent(parentNode);
        CheckGlobalThis checkGlobalThis2 = new CheckGlobalThis(null, null);
        assertEquals(jsDoc2, checkGlobalThis2.getFunctionJsDocInfo(functionNode2));
    }
}