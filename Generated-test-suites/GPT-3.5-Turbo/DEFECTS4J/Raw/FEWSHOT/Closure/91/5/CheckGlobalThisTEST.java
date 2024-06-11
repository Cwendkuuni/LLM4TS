package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private AbstractCompiler compiler;
    private CheckLevel level;
    private CheckGlobalThis checker;
    private NodeTraversal t;
    
    @Before
    public void setup() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checker = new CheckGlobalThis(compiler, level);
    }
    
    @Test
    public void testShouldTraverseWithFunctionAndConstructorAnnotation() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, null);
        boolean result = checker.shouldTraverse(t, n, nodeWithParent);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverseWithFunctionAndThisAnnotation(){
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setThisType(new JSTypeExpression());
        n.setJSDocInfo(jsDoc);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, null);
        boolean result = checker.shouldTraverse(t, n, nodeWithParent);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverseWithFunctionAndOverrideAnnotation(){
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, null);
        boolean result = checker.shouldTraverse(t, n, nodeWithParent);
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverseWithFunctionAndBlockParent(){
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        boolean result = checker.shouldTraverse(t, n, nodeWithParent);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverseWithFunctionAndScriptParent(){
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        boolean result = checker.shouldTraverse(t, n, nodeWithParent);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverseWithFunctionAndNameParent(){
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        boolean result = checker.shouldTraverse(t, n, nodeWithParent);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverseWithFunctionAndAssignParent(){
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        boolean result = checker.shouldTraverse(t, n, nodeWithParent);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverseWithFunctionAndStringParent(){
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        boolean result = checker.shouldTraverse(t, n, nodeWithParent);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverseWithFunctionAndNumberParent(){
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NUMBER);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        boolean result = checker.shouldTraverse(t, n, nodeWithParent);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverseWithFunctionAndNestedAssignParent(){
        Node n = new Node(Token.FUNCTION);
        Node grandParent = new Node(Token.ASSIGN);
        Node parent = new Node(Token.ASSIGN);
        grandParent.addChildToBack(parent);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        boolean result = checker.shouldTraverse(t, n, nodeWithParent);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverseWithAssignNodeAndThisOnLeftSide(){
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        checker.visit(t, n, parent);
        boolean result = (checker.assignLhsChild == null);
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverseWithAssignNodeNotOnLeftSideAndNotAnAssignmentToPrototype(){
        Node n = new Node(Token.THIS);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.THIS));
        lhs.addChildToBack(new Node(Token.STRING, "prototype"));
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(lhs);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        checker.visit(t, n, parent);
        boolean result = (checker.assignLhsChild == null);
        assertEquals(false, result);
    }
    
    @Test
    public void testVisitWithThisAndShouldReportThis(){
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        boolean result = checker.shouldReportThis(n, parent);
        assertEquals(true, result);
    }
    
    @Test
    public void testVisitWithThisAndNotShouldReportThis(){
        Node n = new Node(Token.THIS);
        Node parent = null;
        boolean result = checker.shouldReportThis(n, parent);
        assertEquals(false, result);
    }
    
    @Test
    public void testVisitWithNotAssignLhsChild(){
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        checker.assignLhsChild = null;
        checker.visit(t, n, parent);
        boolean result = (checker.assignLhsChild == null);
        assertEquals(true, result);
    }
    
    @Test
    public void testVisitWithAssignLhsChild(){
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        NodeTraversal.NodeWithParent nodeWithParent = new NodeTraversal.NodeWithParent(n, parent);
        checker.visit(t, n, parent);
        boolean result = (checker.assignLhsChild == null);
        assertEquals(false, result);
    }
    
}