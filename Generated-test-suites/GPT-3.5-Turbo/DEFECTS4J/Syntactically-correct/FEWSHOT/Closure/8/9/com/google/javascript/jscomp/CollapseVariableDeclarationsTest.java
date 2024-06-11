
package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        Node externs = new Node();
        Node root = new Node();
        
        // Create sample variable declarations
        Node var1 = createVarNode("var a;", null);
        Node var2 = createVarNode("var b = 1;", var1);
        Node var3 = createVarNode("var c = 2;", var2);
        
        // Set up the node traversal
        NodeTraversal.Callback cb = cvd.new GatherCollapses();
        NodeTraversal t = new NodeTraversal(compiler, cb);
        
        // Traverse the nodes
        t.traverseRoots(externs, root);
        
        // Get the collapses list
        List<CollapseVariableDeclarations.Collapse> collapses = cvd.collapses;
        
        // Assert that we have one collapse
        assertEquals(1, collapses.size());
        
        // Assert the collapse details
        CollapseVariableDeclarations.Collapse collapse = collapses.get(0);
        assertEquals(var1, collapse.startNode);
        assertEquals(var3.getNext(), collapse.endNode);
        assertEquals(root, collapse.parent);
    }
    
    @Test
    public void testGatherCollapses() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        // Create sample variable nodes
        Node var1 = createVarNode("var a;", null);
        Node var2 = createVarNode("var b = 1;", var1);
        Node var3 = createVarNode("var c = 2;", var2);
        
        // Set up the collapse list
        List<CollapseVariableDeclarations.Collapse> collapses = cvd.collapses;
        
        // Set up the nodes to collapse set
        Set<Node> nodesToCollapse = cvd.nodesToCollapse;
        
        // Create sample parent node
        Node parent = new Node();
        
        // Create sample child node
        Node child = new Node();
        
        // Create sample if node
        Node ifNode = new Node(Token.IF);
        
        // Create sample variable assignment node
        Node assignNode = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.NAME, "a");
        Node rhsNode = new Node(Token.TRUE);
        assignNode.addChildToBack(lhsNode);
        assignNode.addChildToBack(rhsNode);
        
        // Create sample non-var node
        Node nonVarNode = new Node(Token.EXPR_RESULT, assignNode);
        
        // Set up the node traversal
        NodeTraversal.Callback cb = cvd.new GatherCollapses();
        NodeTraversal t = new NodeTraversal(compiler, cb);
        
        // Traverse the variable nodes
        t.traverseNode(var1);
        t.traverseNode(var2);
        t.traverseNode(var3);
        
        // Traverse the non-variable nodes
        t.traverseNode(parent);
        t.traverseNode(child);
        t.traverseNode(ifNode);
        t.traverseNode(assignNode);
        t.traverseNode(lhsNode);
        t.traverseNode(rhsNode);
        t.traverseNode(nonVarNode);
        
        // Assert the collapses list
        assertEquals(1, collapses.size());
        
        // Assert the nodes to collapse set
        assertEquals(4, nodesToCollapse.size());
        assertTrue(nodesToCollapse.contains(var1));
        assertTrue(nodesToCollapse.contains(var2));
        assertTrue(nodesToCollapse.contains(var3));
        assertTrue(nodesToCollapse.contains(nonVarNode));
    }
    
    @Test
    public void testApplyCollapses() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        // Create sample variable declarations
        Node var1 = createVarNode("var a;", null);
        Node var2 = createVarNode("var b = 1;", var1);
        Node var3 = createVarNode("var c = 2;", var2);
        
        // Set up the collapse list
        List<CollapseVariableDeclarations.Collapse> collapses = cvd.collapses;
        
        // Create sample parent node
        Node parent = new Node();
        
        // Add the variable declarations to the parent node
        addChildrenToNode(parent, var1, var2, var3);
        
        // Set up the variable node
        Node var = new Node(Token.VAR);
        
        // Set up the expected node structure
        Node expectedParent = new Node();
        Node expectedVar = createVarNode("", expectedParent);
        Node expectedAssign1 = createAssignmentNode("a", null, expectedVar);
        Node expectedAssign2 = createAssignmentNode("b", "1", expectedVar);
        Node expectedAssign3 = createAssignmentNode("c", "2", expectedVar);
        addChildrenToNode(expectedVar, expectedAssign1, expectedAssign2, expectedAssign3);
        
        // Apply the collapses
        cvd.applyCollapses();
        
        // Assert the parent node
        assertNodeStructure(expectedParent, parent);
        
        // Assert the variable node
        assertNodeStructure(expectedVar, var);
    }
    
    private Node createVarNode(String code, Node previous) {
        Node var = new Node(Token.VAR);
        Node child = new Node(Token.VAR, new Node(Token.NAME, "a"));
        var.addChildToBack(child);
        
        if (previous != null) {
            previous.addChildAfter(var, previous.getLastChild());
        }
        
        return var;
    }
    
    private Node createAssignmentNode(String lhs, String rhs, Node parent) {
        Node assign = new Node(Token.ASSIGN);
        Node lhsNode = new Node(Token.NAME, lhs);
        assign.addChildToBack(lhsNode);
        
        if (rhs != null) {
            Node rhsNode = new Node(Token.STRING, rhs);
            assign.addChildToBack(rhsNode);
        }
        
        if (parent != null) {
            parent.addChildToBack(assign);
        }
        
        return assign;
    }
    
    private void addChildrenToNode(Node parent, Node... children) {
        for (Node child : children) {
            parent.addChildToBack(child);
        }
    }
    
    private void assertNodeStructure(Node expected, Node actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getFirstChild(), actual.getFirstChild());
        assertEquals(expected.getLastChild(), actual.getLastChild());
        assertEquals(expected.getParent(), actual.getParent());
    }

}
