package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback;
import com.google.javascript.jscomp.NodeTraversal.FunctionCallback;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnreachableCodeEliminationTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);

        compiler.initOptions(new CompilerOptions());

        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode("function test() {var x = 1; return x;}");
  
        uce.process(externs, root);

        // TODO: Verify changes made by UnreachableCodeElimination
    }

    @Test
    public void testEliminationPass_visit() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(new AbstractCompiler(), true);
        UnreachableCodeElimination.EliminationPass eliminationPass = uce.new EliminationPass(cfg);

        NodeTraversal traversal = new NodeTraversal(new AbstractShallowCallback() {
            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                // test visit method of eliminationPass
            }
        });
        Node node = new Node(Token.RETURN);
        Node parent = new Node(Token.BLOCK);
        
        eliminationPass.visit(traversal, node, parent);

        // TODO: Verify changes made by EliminationPass visit method
    }

    @Test
    public void testEliminationPass_tryRemoveUnconditionalBranching() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(new AbstractCompiler(), true);
        UnreachableCodeElimination.EliminationPass eliminationPass = uce.new EliminationPass(cfg);

        Node node = new Node(Token.RETURN);
        
        eliminationPass.tryRemoveUnconditionalBranching(node);

        // TODO: Verify changes made by EliminationPass tryRemoveUnconditionalBranching method
    }

    @Test
    public void testEliminationPass_computeFollowing() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(new AbstractCompiler(), true);
        UnreachableCodeElimination.EliminationPass eliminationPass = uce.new EliminationPass(cfg);

        Node node = new Node(Token.RETURN);
        
        Node result = eliminationPass.computeFollowing(node);

        // TODO: Verify result of EliminationPass computeFollowing method
    }

    @Test
    public void testEliminationPass_removeDeadExprStatementSafely() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(new AbstractCompiler(), true);
        UnreachableCodeElimination.EliminationPass eliminationPass = uce.new EliminationPass(cfg);

        Node node = new Node(Token.RETURN);
        
        eliminationPass.removeDeadExprStatementSafely(node);

        // TODO: Verify changes made by EliminationPass removeDeadExprStatementSafely method
    }

    @Test
    public void testEliminationPass_removeNode() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(new AbstractCompiler(), true);
        UnreachableCodeElimination.EliminationPass eliminationPass = uce.new EliminationPass(cfg);

        Node node = new Node(Token.RETURN);

        eliminationPass.removeNode(node);

        // TODO: Verify changes made by EliminationPass removeNode method
    }
}