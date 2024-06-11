package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {
    
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapseVar = new CollapseVariableDeclarations(compiler);
        
        Node externs = null;
        Node root = null;
        collapseVar.process(externs, root);
        
        // Define the expected collapses
        
        // Test case 1: var a; var b = 1; var c = 2;
        Node startNode1 = new Node(Token.VAR);
        Node bAssignNode = new Node(Token.ASSIGN, new Node(Token.NAME, "b"), new Node(Token.NUMBER, "1"));
        Node cAssignNode = new Node(Token.ASSIGN, new Node(Token.NAME, "c"), new Node(Token.NUMBER, "2"));
        startNode1.addChildToFront(new Node(Token.NAME, "a"));
        startNode1.addChildToBack(bAssignNode);
        startNode1.addChildToBack(cAssignNode);
        
        Node endNode1 = cAssignNode;
        
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(startNode1);
        
        Collapse collapse1 = new Collapse(startNode1, endNode1, parent1);
        
        // Test case 2: a = true; b = true; var c = true;
        Node startNode2 = new Node(Token.VAR);
        Node aAssignNode = new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.TRUE));
        Node bAssignNode2 = new Node(Token.ASSIGN, new Node(Token.NAME, "b"), new Node(Token.TRUE));
        Node cAssignNode2 = new Node(Token.VAR);
        cAssignNode2.addChildToBack(new Node(Token.NAME, "c"));
        cAssignNode2.addChildToBack(new Node(Token.TRUE));

        startNode2.addChildToBack(aAssignNode);
        startNode2.addChildToBack(bAssignNode2);
        startNode2.addChildToBack(cAssignNode2);
        
        Node endNode2 = cAssignNode2;
        
        Node parent2 = new Node(Token.SCRIPT);
        parent2.addChildToBack(startNode2);
        
        Collapse collapse2 = new Collapse(startNode2, endNode2, parent2);
        
        // Add the collapses to the expected list
        List<Collapse> expectedCollapses = new ArrayList<>();
        expectedCollapses.add(collapse1);
        expectedCollapses.add(collapse2);
        
        // Compare the expected collapses to the actual collapses
        assertEquals(expectedCollapses, collapseVar.collapses);
        
        // TODO: Test other aspects of the CollapseVariableDeclarations class
    }
}